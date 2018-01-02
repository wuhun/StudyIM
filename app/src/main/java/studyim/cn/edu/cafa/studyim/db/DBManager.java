package studyim.cn.edu.cafa.studyim.db;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import studyim.cn.edu.cafa.studyim.model.Friend;
import studyim.cn.edu.cafa.studyim.model.FriendUserInfo;
import tools.com.lvliangliang.wuhuntools.exception.TestLog;

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
            list.add(entity);
        }
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


}
