package studyim.cn.edu.cafa.studyim.db;

import android.net.Uri;

import com.google.gson.reflect.TypeToken;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.Group;
import io.rong.imlib.model.UserInfo;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import studyim.cn.edu.cafa.studyim.common.Constant;
import studyim.cn.edu.cafa.studyim.model.BaseModel;
import studyim.cn.edu.cafa.studyim.model.Friend;
import studyim.cn.edu.cafa.studyim.model.FriendUserInfo;
import studyim.cn.edu.cafa.studyim.model.GroupMemeberModel;
import studyim.cn.edu.cafa.studyim.model.GroupModel;
import studyim.cn.edu.cafa.studyim.model.SettingModel;
import studyim.cn.edu.cafa.studyim.util.HttpUtil;
import studyim.cn.edu.cafa.studyim.util.UserAvatarUtil;
import tools.com.lvliangliang.wuhuntools.exception.TestLog;
import tools.com.lvliangliang.wuhuntools.util.WuhunDataTool;

import static studyim.cn.edu.cafa.studyim.app.MyApplication.getGson;

/**
 * ================================================
 * 作    者：悟魂(了解自己，感悟灵魂，做最好的自己)
 * 创建日期：2017/12/25 0025
 * 版    本：1.0
 * 描    述：
 * 修订历史：
 * ================================================
 */
public class DBManager {

    private static DBManager mInstance;

    private DBManager(){}

    public static DBManager getmInstance(){
        if(mInstance == null) {
            synchronized (DBManager.class){
                if(mInstance == null) {
                    mInstance = new DBManager();
                }
            }
        }
        return mInstance;
    }

    public void saveFriendUserInfo(FriendUserInfo friendInfo){
        TestLog.i("saveFriendUserInfo:" + friendInfo);
        FriendUserInfo friendUserInfo = DataSupport.find(FriendUserInfo.class, friendInfo.getUserId());
        if(friendUserInfo != null) {
            DataSupport.delete(FriendUserInfo.class, friendInfo.getUserId());
            friendInfo.save();
        }
    }

    /** 同步好友详情信息 */
    public void setAllUserInfo(List<Friend> friends){
        deleteFriends();
        saveFriends(friends);
    }

    /** 查询数据库中好友详情信息 */
    public FriendUserInfo getFriendUserInfo(int userId){
        return DataSupport.find(FriendUserInfo.class, userId);
    }

    /** 保存获取的好友信息 */
    private void saveFriends(List<Friend> friends) {
        List<Friend> list = new ArrayList<>();
        for (Friend entity : friends) {
            String name = WuhunDataTool.isNullString(entity.getREMARKNAME()) ? entity.getNICKNAME() : entity.getREMARKNAME();
            String uri = UserAvatarUtil.initUri(Constant.HOME_URL, entity.getAVATAR());
            String avatarUri = UserAvatarUtil.getAvatarUri(
                    entity.getUSERBUDDYID(),
                    name,
                    uri);
            UserInfo userinfo = new UserInfo(entity.getRCID(), name, Uri.parse(avatarUri));
            RongIM.getInstance().refreshUserInfoCache(userinfo);
            list.add(entity);
        }
        TestLog.i("DBManager - saveFriends: 保存好友到本地数据库列表");
        DataSupport.saveAll(list);
    }

    private void deleteFriends() {
        List<Friend> friends = getFriends();
        for (Friend friend : friends) {
            friend.delete();
        }
    }

    /** 查询好友列表，不包括自己 */
    public synchronized List<Friend> getFriends() {
        //return DataSupport.where("userid != ?", SPUtil.getInstance(getContext()).getUSERID()).find(Friend.class);
        return DataSupport.findAll(Friend.class);
    }

    public void saveSettingsList(List<SettingModel> settingModels){
        DataSupport.saveAll(settingModels);
    }

    public List<SettingModel> getSettings(){
        return DataSupport.findAll(SettingModel.class);
    }

    public void clearSettins(){
        List<SettingModel> settings = getSettings();
        for(SettingModel m : settings){
            m.delete();
        }
    }

    /** 保存群组信息 */
    public void saveGroups(List<GroupModel> groups, String beforeUri){
        if(groups == null) return;
        deleteGroups();
        for(GroupModel model : groups){
            if(model == null) return;
            String uri = UserAvatarUtil.initUri(beforeUri, model.getGROUPIMAGE());
            String portrait = UserAvatarUtil.getAvatarUri(model.getGROUPRCID(), model.getNAME(), uri);
            Group group = new Group(model.getGROUPRCID(), model.getNAME(), Uri.parse(portrait));
            RongIM.getInstance().refreshGroupInfoCache(group);

            refreshGroupMemeber(model.getGROUPID() + "");
        }
        DataSupport.saveAll(groups);
    }

    /** 更新群成员信息 */
    private void refreshGroupMemeber(String groupId) {
        HttpUtil.getGroupMemeberlist(String.valueOf(groupId), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {}

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
//                    TestLog.i("查询群成员result: " + result);
                BaseModel<GroupMemeberModel> model = getGson().fromJson(result, new TypeToken<BaseModel<GroupMemeberModel>>(){}.getType());
                if(response.isSuccessful() && model != null && model.getCode() == 1){
                    String before = WuhunDataTool.isNullString(model.getBefore()) ? Constant.HOME_URL : model.getBefore();
                    List<GroupMemeberModel> memeber = model.getResult();
//                        TestLog.i("循环" + memeber);
                    for(GroupMemeberModel groupitem : memeber){
//                            TestLog.i("成员: " + groupitem);
                        String name = WuhunDataTool.isNullString(groupitem.getREMARKNAME()) ? groupitem.getNICKNAME() : groupitem.getREMARKNAME();
                        String uri = UserAvatarUtil.initUri(before, groupitem.getAVATAR());
                        String avatarUri = UserAvatarUtil.getAvatarUri(
                                groupitem.getUSERID() + "", name, uri);
                        UserInfo userInfo = new UserInfo(groupitem.getRCID(), name, Uri.parse(avatarUri));
                        RongIM.getInstance().refreshUserInfoCache(userInfo);
                    }
                }
            }
        });
    }

    public List<GroupModel> getGroupByClass(){
        return DataSupport.where("GROUPTYPE = ?", "G").find(GroupModel.class);
    }

    public List<GroupModel> getGroupByCommon(){
        return DataSupport.where("GROUPTYPE = ?", "P").find(GroupModel.class);
    }

    public List<GroupModel> getAllGroup(){
        return DataSupport.findAll(GroupModel.class);
    }

    public void deleteGroups(){
        List<GroupModel> groups = getAllGroup();
        for(GroupModel  n : groups){
            n.delete();
        }
    }
}
