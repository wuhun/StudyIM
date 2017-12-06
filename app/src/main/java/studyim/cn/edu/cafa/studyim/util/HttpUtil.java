package studyim.cn.edu.cafa.studyim.util;

import android.support.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import studyim.cn.edu.cafa.studyim.common.Constant;

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

    /** 发送好友请求 */
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

    /** 搜索用户 */
    public static void userInfoSearch_studentId(String type, String content, Callback callback) {
        String uri = Constant.indexUrl + "CAFA/user/info/search";
        Map<String,String> map = new HashMap<>();
        map.put("type", type);
        map.put("content", content);
        Request.Builder reqBuilder= getLoginReqBuilder(uri, map);
        OkHttpUtil.enqueue(reqBuilder.build(), callback);
    }

    /** 获取好友列表 */
    public static void getFriendList(Callback callback) {
        String uri = Constant.indexUrl + "CAFA/friend/list";
        Request.Builder reqBuilder = getLoginReqBuilder(uri, null);
        OkHttpUtil.enqueue(reqBuilder.build(), callback);
    }

    /** 登录  */
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
    /** 正常request */
    private static Request getRequest(String uri, Map<String, String> map) {
        return new Request.Builder().url(uri)
                .header("content-type", "text/html;charset:utf-8")
                .post(getRequestBody(map))
                .build();
    }

    /** 带token的request */
    private static Request.Builder getLoginReqBuilder(String uri, Map<String, String> map) {
        Map<String, String> param = map;
        if (null == param)
            param = new HashMap<>();
        param.put("userId", getSPUtil().getUSERID());
        Request.Builder reqBuilder = new Request.Builder().url(uri)
                .addHeader("tokens", getSPUtil().getTokens())
                .post(getRequestBody(param));
        return reqBuilder;
    }

    /** 添加参数 */
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
