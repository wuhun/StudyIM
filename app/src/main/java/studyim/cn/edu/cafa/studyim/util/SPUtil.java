package studyim.cn.edu.cafa.studyim.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * ================================================
 * 作    者：悟魂(了解自己，感悟灵魂，做最好的自己)
 * 创建日期：2017/11/28 0028
 * 版    本：1.0
 * 描    述：数据缓存类
 * 修订历史：
 * ================================================
 */
public class SPUtil {
    private static Context mContext;
    private static SPUtil utils = null;
    private static SharedPreferences settings = null;

    public static SPUtil getInstance(Context c) {
        if (utils == null) {
            utils = new SPUtil();
            mContext = c;
            if (settings == null) {
//                settings=PreferenceManager.getDefaultSharedPreferences(mContext);
//                settings= PreferenceManager.getDefaultSharedPreferences(mContext);//preferences.xml
                settings = mContext.getSharedPreferences("settings", Context.MODE_PRIVATE);//settings.xml
            }
        }
        return utils;
    }

    /////////////////////////////////////////////////////////////////////////////////
    //// 我是华丽丽的分割线~
    /////////////////////////////////////////////////////////////////////////////////
//    /** 服务器地址 */
//    public static String SERVER_URL = "http://10.10.0.105:80/dlapi/";
//
//    /** 服务器 */
//    public static final String SERVER = "server_url";
//    public String getServerUrl(){ return settings.getString(SERVER, SERVER_URL); }
//    public void setServerUrl(String url){
//        settings.edit().putString(SERVER, url).commit();
//    }

    /** 登录用户名 */
    public static final String USERNAME = "username";
    /** 用户id */
    public static final String USERID = "userId";
    /** 用户登录tokens */
    public static final String TOKENS = "tokens";
    /** 融云token */
    public static final String RCTOKEN = "rctoken";
    /** 设置更新的版本号-用于一键更新 */
    public static final String SETTING_VERSION = "settingVersion";

    public static final String ROLE_TYPE = "roleType";//角色类型

    public static int getRoletype() {
        return settings.getInt(ROLE_TYPE, 4);
    }

    public void setRoletype(int roletype) {
        settings.edit().putInt(ROLE_TYPE, roletype).commit();
    }

    public int getSettingVersion() {
        return settings.getInt(SETTING_VERSION, 1);
    }

    public void setSettingVersion(int version) {
        settings.edit().putInt(SETTING_VERSION, version).commit();
    }

    public String getUSERID() {return settings.getString(USERID, "");}
    public void setUSERID(String userId) {
        settings.edit().putString(USERID, userId).commit();
    }

    public String getTokens(){return settings.getString(TOKENS, "");}
    public void setTokens(String tokens) {
        settings.edit().putString(TOKENS, tokens).commit();
    }

    public String getRctoken(){return settings.getString(RCTOKEN, "");}
    public void setRctoken(String rctoken){
        settings.edit().putString(RCTOKEN, rctoken).commit();
    }

    public String getUsername(){return settings.getString(USERNAME, "");}
    public void setUsername(String uname){
        settings.edit().putString(USERNAME, uname).commit();
    }

}
