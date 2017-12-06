package tools.com.lvliangliang.wuhuntools.exception;

/**
 * ================================================
 * 作    者：悟魂(了解自己，感悟灵魂，做最好的自己)
 * 创建日期：2017/11/20 0020
 * 版    本：1.0
 * 描    述：调试工具类
 * 修订历史：
 * ================================================
 */
public class WuhunDebug {

    public static boolean isDebug;

    private static WuhunDebug instence;
    private WuhunDebug(boolean isDebug) {
        this.isDebug = isDebug;
    }

    public static WuhunDebug getInstence(boolean isDebug){
        if(instence == null) {
            synchronized (WuhunDebug.class) {
                if(instence == null)
                    instence = new WuhunDebug(isDebug);
            }
        }
        return instence;
    }

    public static void debug(String str){
        if(isDebug) {
//            WuhunToast.normal(str).show();
            TestLog.i("********debug↓********\n"+str+"\n********debug↑********");
        }
    }

}
