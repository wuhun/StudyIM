package studyim.cn.edu.cafa.studyim.util;

import android.support.annotation.NonNull;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import studyim.cn.edu.cafa.studyim.common.Constant;
import tools.com.lvliangliang.wuhuntools.util.WuhunDataTool;

import static studyim.cn.edu.cafa.studyim.app.MyApplication.getSPUtil;

/**
 * ================================================
 * 作    者：悟魂(了解自己，感悟灵魂，做最好的自己)
 * 创建日期：2017/11/9 0009
 * 版    本：1.0
 * 描    述：网络请求工具类
 * 修订历史：
 * ================================================
 */
public class HttpUtil {

    /** 检查版本 */
    public static void checkServiceVersion(Callback callback){
        String uri = Constant.CHECK_SERVICE_VERSION;
        Request request = getRequest(uri, null);
        OkHttpUtil.enqueue(request, callback);
    }

    /** 忘记密码 */
    public static void forgetpassword(String userId, String telephone,String password,Callback callback){
        String uri = Constant.indexUrl + "CAFA/user/forgetpassword";
        Map<String, String> map = new HashMap<>();
        map.put("userId", userId);
        map.put("telephone", telephone);
        map.put("msgCode", "8888");
        map.put("password", password);
        Request request = getRequest(uri, map);
        OkHttpUtil.enqueue(request, callback);
    }

    /** 删除群文件 */
    public static void groupfiledelete(String type, String groupFileId,String IsGroupType,Callback callback) {
        String uri = Constant.indexUrl + "CAFA/file/groupfiledelete";
        Map<String, String> map = new HashMap<>();
        map.put("type", type);
        map.put("groupFileId", groupFileId);
        map.put("IsGroupType", IsGroupType);
        Request.Builder reqBuilder = getLoginReqBuilder(uri, map);
        OkHttpUtil.enqueue(reqBuilder.build(), callback);
    }

    /** 群主修改群信息 */
    public static void groupChangeMessage(String groupId, File headImage,String groupName,String groupIntroduce, Callback callback){
        String uri = Constant.indexUrl + "CAFA/group/changeMessage";

        MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
        requestBody.addFormDataPart("userId", getSPUtil().getUSERID());
        requestBody.addFormDataPart("groupId", groupId);
        if(!WuhunDataTool.isNullString(groupName)) {
            requestBody.addFormDataPart("groupName", groupName);
        }
        if(!WuhunDataTool.isNullString(groupIntroduce)) {
            requestBody.addFormDataPart("groupIntroduce", groupIntroduce);
        }
//        requestBody.addFormDataPart("groupType", groupType);
        // 参数分别为， 请求key ，文件名称 ， RequestBody
        if(headImage != null && headImage.isFile()) {
            requestBody.addFormDataPart("file", "group_"+ System.currentTimeMillis() + ".jpg",
                    RequestBody.create(MediaType.parse("image/*") , headImage));
        }

        Request.Builder reqBuilder = new Request.Builder().url(uri)
                .addHeader("tokens", getSPUtil().getTokens())
                .post(requestBody.build());

        OkHttpUtil.enqueue(reqBuilder.build(), callback);
    }

    /** 群主设置管理员 */
    public static void GroupAppointManager(String groupId, String managerId, Callback callback) {
        String uri = Constant.indexUrl + "CAFA/group/appointManager";
        Map<String, String> map = new HashMap<>();
        map.put("groupId",groupId);
        map.put("managerId", managerId);
        Request.Builder reqBuilder = getLoginReqBuilder(uri, map);
        OkHttpUtil.enqueue(reqBuilder.build(), callback);
    }

    /** 群文件上传 */
    public static void GroupUploadFile(String groupId,File file,String type,Callback callback){
        String uri = Constant.indexUrl + "CAFA/file/upload";

        MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
        requestBody.addFormDataPart("userId", getSPUtil().getUSERID());
        requestBody.addFormDataPart("groupId", groupId);
        requestBody.addFormDataPart("class", type);
        // 参数分别为， 请求key ，文件名称 ， RequestBody
        if(file != null && file.isFile()) {
            requestBody.addFormDataPart("file", file.getName(),
                    RequestBody.create(null , file));
        }
        Request.Builder reqBuilder = new Request.Builder().url(uri)
                .addHeader("tokens", getSPUtil().getTokens())
                .post(requestBody.build());

        OkHttpUtil.enqueue(reqBuilder.build(), callback);
    }

    /** 获取群文件 */
    public static void getGroupFiles(String groupId, String type, Callback callback){
        String uri = Constant.indexUrl + "CAFA/file/goup";
        Map<String,String> map = new HashMap<>();
        map.put("groupId", groupId);
        map.put("type", type);
        Request.Builder reqBuilder = getLoginReqBuilder(uri, map);
        OkHttpUtil.enqueue(reqBuilder.build(), callback);
    }

    /** 添加到群组 */
    public static void groupJoin(String groupId, String userId, Callback callback){
        String uri = Constant.indexUrl + "CAFA/group/join";
        Map<String,String> map = new HashMap<>();
        map.put("groupId", groupId);
        map.put("userId", userId);
        Request.Builder reqBuilder = getLoginReqBuilder(uri, map);
        OkHttpUtil.enqueue(reqBuilder.build(), callback);
    }

    /** 退出群聊 */
    public static void groupQuit(String groupId, Callback callback){
        String uri = Constant.indexUrl + "CAFA/group/quit";
        Map<String,String> map = new HashMap<>();
        map.put("groupId", groupId);
        Request.Builder reqBuilder = getLoginReqBuilder(uri, map);
        OkHttpUtil.enqueue(reqBuilder.build(), callback);
    }

    /** 解散群聊 */
    public static void groupDismiss(String groupId, Callback callback){
        String uri = Constant.indexUrl + "CAFA/group/dismiss";
        Map<String,String> map = new HashMap<>();
        map.put("groupId", groupId);
        Request.Builder reqBuilder = getLoginReqBuilder(uri, map);
        OkHttpUtil.enqueue(reqBuilder.build(), callback);
    }

    /** 获取群详情信息 */
    public static void getGroupInfo(String groupId, Callback callback){
        String uri = Constant.indexUrl + "CAFA/group/getinfo";
        Map<String, String> map = new HashMap<>();
        map.put("groupId", groupId);
        Request.Builder reqBuilder = getLoginReqBuilder(uri, map);
        OkHttpUtil.enqueue(reqBuilder.build(), callback);
    }

    /** 获取群内的活动列表 */
    public static void getActivityList(String groupId, Callback callback){
        String uri = Constant.indexUrl + "CAFA/group/activity/list";
        Map<String, String> map = new HashMap<>();
        map.put("groupId", groupId);
        map.put("dateTime", System.currentTimeMillis() + "");
        Request.Builder reqBuilder = getLoginReqBuilder(uri, map);
        OkHttpUtil.enqueue(reqBuilder.build(), callback);
    }

    /**
     * 创建群组
     * @param groupName     群名称
     * @param groupImage    群图片 MediaType.parse("image/*")  "application/octet-stream"
     * @param groupType     群类型：（group//普通群组 --- class//班级群组）
     * @param callback
     */
    public static void CreateGroup(String groupName, File groupImage, String groupType, Callback callback){
        String uri = Constant.indexUrl + "CAFA/group/create";

        // form 表单形式上传
        MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
        requestBody.addFormDataPart("userId", getSPUtil().getUSERID());
        requestBody.addFormDataPart("groupName", groupName);
        requestBody.addFormDataPart("groupType", groupType);
        // 参数分别为， 请求key ，文件名称 ， RequestBody
        requestBody.addFormDataPart("file", "group_"+ System.currentTimeMillis() + ".jpg",
                RequestBody.create(MediaType.parse("image/*") , groupImage));

        Request.Builder reqBuilder = new Request.Builder().url(uri)
                .addHeader("tokens", getSPUtil().getTokens())
                .post(requestBody.build());

        OkHttpUtil.enqueue(reqBuilder.build(), callback);
    }


    /** 获取群公告 */
    public static void getGroupNoticeList(String groupId, Callback callback){
        String uri = Constant.indexUrl + "CAFA/group/noticeList";
        Map<String, String> map = new HashMap<>();
        map.put("groupId", groupId);//群id
        map.put("dateTime", System.currentTimeMillis()+"");
        Request.Builder reqBuilder = getLoginReqBuilder(uri, map);
        OkHttpUtil.enqueue(reqBuilder.build(), callback);
    }

    /** 更具群id获取群成员列表信息 */
    public static void getGroupMemeberlist(String groupId, Callback callback){
        String uri = Constant.indexUrl + "CAFA/group/memeberlist";
        Map<String, String> map = new HashMap<>();
        map.put("groupId", groupId);
        Request.Builder reqBuilder = getLoginReqBuilder(uri, map);
        OkHttpUtil.enqueue(reqBuilder.build(), callback);
    }

    /** 获取班级或者群组列表 */
    public static void getGroupList(String type, Callback callback){
        String uri = Constant.indexUrl + "CAFA/group/list";
        Map<String, String> map = new HashMap<>();
        if(!WuhunDataTool.isNullString(type)) {
            map.put("type", type);
        }
        Request.Builder reqBuilder = getLoginReqBuilder(uri, map);
        OkHttpUtil.enqueue(reqBuilder.build(), callback);
    }

    /**
     * 获取好友详细信息
     */
    public static void friend_getinfo(String buddyUserId, Callback callback) {
        String uri = Constant.indexUrl + "CAFA/friend/getInfo";
        Map<String, String> map = new HashMap<>();
        map.put("buddyUserId", buddyUserId);
        Request.Builder reqBuilder = getLoginReqBuilder(uri, map);
        OkHttpUtil.enqueue(reqBuilder.build(), callback);
    }

    /**
     * 删除好友
     *
     * @param buddyUserId 要删除的好友id
     * @param callback    回调
     */
    public static void friendDelete(String buddyUserId, Callback callback) {
        String uri = Constant.indexUrl + "CAFA/friend/delete";
        Map<String, String> map = new HashMap<>();
        map.put("buddyUserId", buddyUserId);
        Request.Builder reqBuilder = getLoginReqBuilder(uri, map);
        OkHttpUtil.enqueue(reqBuilder.build(), callback);
    }

    /**
     * 修改注备名称
     *
     * @param buddyUserId   好友id
     * @param noteName      备注名称
     * @param noteTelephone 备注电话
     * @param noteMsg       备注信息
     * @param noteImage     备注头像（未实现）
     * @param callback      回调
     */
    public static void setNote(String buddyUserId, String noteName, String noteTelephone, String noteMsg, String noteImage, Callback callback) {
        String uri = Constant.indexUrl + "CAFA/friend/setnote";
        Map<String, String> map = new HashMap<>();
        map.put("buddyUserId", buddyUserId);
        map.put("noteName", noteName);
        map.put("noteTelephone", noteTelephone);
        map.put("noteMsg", noteMsg);
        if (!WuhunDataTool.isNullString(noteImage)) {
            map.put("noteImage", noteImage);
        }
        Request.Builder reqBuilder = getLoginReqBuilder(uri, map);
        OkHttpUtil.enqueue(reqBuilder.build(), callback);
    }

    /**
     * 确认好友请求
     */
    public static void friendAskaddmsg(String buddyUserId, String isAgree, Callback callback) {
        String uri = Constant.indexUrl + "CAFA/friend/askaddmsg";
        Map<String, String> map = new HashMap<>();
        map.put("buddyUserId", buddyUserId);
        map.put("isAgree", isAgree);
        Request.Builder reqBuilder = getLoginReqBuilder(uri, map);
        OkHttpUtil.enqueue(reqBuilder.build(), callback);
    }

    /**
     * 获取申请列表
     */
    public static void friend_GetAddList(Callback callback) {
        String uri = Constant.indexUrl + "CAFA/friend/getaddlist";
//        WuhunDebug.debug("uri:" + uri + "\n token:" + getSPUtil().getTokens() + "\n userid:" + getSPUtil().getUSERID());
        Request.Builder reqBuilder = getLoginReqBuilder(uri, null);
        OkHttpUtil.enqueue(reqBuilder.build(), callback);
    }

    /**
     * 发送好友请求
     */
    public static void friendAdd(String buddyUserId, String userid, String msg, String name, Callback callback) {
        String uri = Constant.indexUrl + "CAFA/friend/add";
        Map<String, String> map = new HashMap<>();
        map.put("buddyUserId", buddyUserId);
        map.put("sendType", userid);
        map.put("applymas", msg);
        map.put("applyname", name);
        Request.Builder reqBuilder = getLoginReqBuilder(uri, map);
        OkHttpUtil.enqueue(reqBuilder.build(), callback);
    }

    /**
     * 搜索用户
     */
    public static void userInfoSearch_studentId(String type, String content, Callback callback) {
        String uri = Constant.indexUrl + "CAFA/user/info/search";
        Map<String, String> map = new HashMap<>();
        map.put("type", type);
        map.put("content", content);
        Request.Builder reqBuilder = getLoginReqBuilder(uri, map);
        OkHttpUtil.enqueue(reqBuilder.build(), callback);
    }

    /**
     * 获取好友列表
     */
    public static void getFriendList(Callback callback) {
        String uri = Constant.indexUrl + "CAFA/friend/list";
        Request.Builder reqBuilder = getLoginReqBuilder(uri, null);
        OkHttpUtil.enqueue(reqBuilder.build(), callback);
    }

    public static void query_setting_list(Callback callback) {
        String uri = Constant.ME_FRAGMENT_UPDATE_SETTINGS;
        Map map = null;
        OkHttpUtil.post(uri, map, callback);
    }

    /**
     * 获取个人资料
     */
    public static void userGetInfo(Callback callback) {
        String uri = Constant.indexUrl + "CAFA/user/getInfo";
        Request.Builder reqBuilder = getLoginReqBuilder(uri, null);
        OkHttpUtil.enqueue(reqBuilder.build(), callback);
    }

    /**
     * 登录
     */
    public static void login(String account, String password, String channelId, Callback callback) {
        String uri = Constant.indexUrl + "CAFA/user/login";

        Map<String, String> map = new HashMap<>();
        map.put("account", account);
        map.put("password", password);
        if (channelId != null)
            map.put("channelId", channelId);
        Request request = getRequest(uri, map);
        OkHttpUtil.enqueue(request, callback);
    }

    /////////////////////////////////////////////////////////////////////////////////
    //// 我是华丽丽的分割线~
    /////////////////////////////////////////////////////////////////////////////////

    /**
     * 正常request
     */
    private static Request getRequest(String uri, Map<String, String> map) {
        return new Request.Builder().url(uri)
                .header("content-type", "text/html;charset:utf-8")
                .post(getRequestBody(map))
                .build();
    }

    /**
     * 带token的request
     */
    private static Request.Builder getLoginReqBuilder(String uri, Map<String, String> map) {
        Map<String, String> param = map;
        if (null == param)
            param = new HashMap<>();
        if(!param.containsKey("userId")){
            param.put("userId", getSPUtil().getUSERID());
        }
        Request.Builder reqBuilder = new Request.Builder().url(uri)
                .addHeader("tokens", getSPUtil().getTokens())
                .post(getRequestBody(param));
        return reqBuilder;
    }

    /**
     * 添加参数
     */
    @NonNull
    private static RequestBody getRequestBody(Map<String, String> map) {
        FormBody.Builder builder = new FormBody.Builder();
        if (map != null) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                builder.add(entry.getKey(), entry.getValue().toString());
            }
        }
        return builder.build();
    }

}
