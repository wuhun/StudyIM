package tools.com.lvliangliang.wuhuntools.util;

import android.content.Context;
import android.content.res.Configuration;

import java.util.Locale;

/**
 * ================================================
 * 作    者：悟魂(了解自己，感悟灵魂，做最好的自己)
 * 创建日期：2018/3/15 0015
 * 版    本：1.0
 * 描    述：
 * 修订历史：
 * ================================================
 */
public class WuhunLanguageTool {

    public static String CHINESE = "zh";
    public static String ENGLISE = "en";

    /**
     * 设置本地语言
     * @param baseContext   全局的base上下文
     * @param language
     */
    public static void shiftLanguage(Context baseContext, String language){
        if(language.equals(CHINESE)){
//            Locale.setDefault(Locale.CHINESE);
//            Configuration config = baseContext.getResources().getConfiguration();
//            config.locale = Locale.CHINESE;
//            baseContext.getResources().updateConfiguration(config, baseContext.getResources().getDisplayMetrics());
            setLocationLanguage(baseContext, Locale.CHINESE);
        } else if (language.equals(ENGLISE)) {
//            Locale.setDefault(Locale.ENGLISH);
//            Configuration config = baseContext.getResources().getConfiguration();
//            config.locale = Locale.ENGLISH;
//            baseContext.getResources().updateConfiguration(config, baseContext.getResources().getDisplayMetrics());
            setLocationLanguage(baseContext, Locale.ENGLISH);
        } else {
            setLocationLanguage(baseContext, Locale.getDefault());
        }
    }

    public static void setLocationLanguage(Context baseContext,Locale locale){
        Locale.setDefault(locale);
        Configuration config = baseContext.getResources().getConfiguration();
        config.locale = locale;
        baseContext.getResources()
                .updateConfiguration(config, baseContext.getResources().getDisplayMetrics());
    }


}
