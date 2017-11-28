package studyim.cn.edu.cafa.studyim.util;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.Callback;
import com.lzy.okgo.request.PostRequest;

import studyim.cn.edu.cafa.studyim.common.Constant;
import tools.com.lvliangliang.wuhuntools.util.WuhunDataTool;

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

//    public static void testBaidu(){
//        String uri = "http://www.baidu.com/";
//        OkGo.<String>post(uri)
//                .tag("")
//    }

    public static void login(String account, String password, String channelId, Callback callback) {
        String uri = Constant.indexUrl+"CAFA/user/login";
        PostRequest<String> request = OkGo.<String>post(uri)
                .tag("login")
                .params("account", account)
                .params("password", password);
        if(!WuhunDataTool.isNullString(channelId)) {
            request.params("channelId", channelId);
        }
        request.execute(callback);
    }

}
