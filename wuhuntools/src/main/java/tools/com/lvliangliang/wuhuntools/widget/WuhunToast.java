package tools.com.lvliangliang.wuhuntools.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.os.Build;
import android.support.annotation.CheckResult;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import tools.com.lvliangliang.wuhuntools.R;

import static tools.com.lvliangliang.wuhuntools.WuhunTools.getContext;


/**
 * 作者：悟魂 ————2017/11/3 0003.
 * 版本：1.0
 * 说明：
 */

public class WuhunToast {

    //颜色状态
    @ColorInt
    private static int DEFAULT_TEXT_COLOR = Color.parseColor("#FFFFFF");
    @ColorInt
    private static int ERROR_COLOR = Color.parseColor("#FD4C5B"); //红
    @ColorInt
    private static int INFO_COLOR = Color.parseColor("#3F51B5"); // 蓝色
    @ColorInt
    private static int SUCCESS_COLOR = Color.parseColor("#388E3C");//绿
    @ColorInt
    private static int WARNING_COLOR = Color.parseColor("#FFA900");//黄
    @ColorInt
    private static int NORMAL_COLOR = Color.parseColor("#353A3E");//灰

    private static Toast mToast; //重写系统Toast,无延迟
    private static long mExitTime;

    private static Toast currentToast; //自定义封装Toast
    private static final String TOAST_TYPEFACE = "sans-serif-condensed";//字体类型

    /////////////////////////////////////////////////////////////////////////////////
    //// 下面是封装的常用显示方式
    /////////////////////////////////////////////////////////////////////////////////

    // 常规显示-String
    public static Toast normal(@NonNull String message) {
        return normal(message, false, null);
    }

    public static Toast normal(@NonNull String message, Drawable icon) {
        return normal(message, true, icon);
    }

    public static Toast normal(@StringRes int strRes) {
        return normal(strRes, false, -1);
    }

    public static Toast normal(@StringRes int strRes, @DrawableRes int iconRes) {
        return normal(strRes, true, iconRes);
    }

    //string
    @CheckResult
    public static Toast normal(@NonNull String message, boolean withIcon, Drawable icon) {
        return custom(getContext(), message, DEFAULT_TEXT_COLOR, withIcon, icon, false, -1);
    }

    //resId
    @CheckResult
    public static Toast normal(@StringRes int resId, boolean withIcon, @DrawableRes int iconRes) {
        return custom(getContext(), resId, DEFAULT_TEXT_COLOR, withIcon, iconRes, false, -1);
    }

    // 信息显示_string
    public static Toast info(@NonNull String message) {
        return info(message, false, null);
    }

    public static Toast info(@NonNull String message, Drawable icon) {
        return info(message, true, icon);
    }

    public static Toast info(@NonNull String message, boolean withIcon) {
        return info(message, withIcon, getDrawable(getContext(), R.drawable.ic_info_outline_white_48dp));
    }

    public static Toast info(@StringRes int strRes) {
        return info(strRes, false, -1);
    }

    public static Toast info(@StringRes int strRes, @DrawableRes int iconRes) {
        return info(strRes, true, iconRes);
    }

    public static Toast info(@StringRes int strRes, boolean withIcon) {
//        return custom(getContext(), getContext().getString(strRes), DEFAULT_TEXT_COLOR, withIcon, getDrawable(getContext(), R.drawable.ic_info_outline_white_48dp), true, INFO_COLOR);
        return info(strRes, withIcon, R.drawable.ic_info_outline_white_48dp);
    }

    //String
    @CheckResult
    public static Toast info(@NonNull String message, boolean withIcon, Drawable icon) {
        return custom(getContext(), message, DEFAULT_TEXT_COLOR, withIcon, icon, true, INFO_COLOR);
    }

    //resId
    @CheckResult
    public static Toast info(@StringRes int resId, boolean withIcon, @DrawableRes int iconRes) {
        return custom(getContext(), resId, DEFAULT_TEXT_COLOR, withIcon, iconRes, true, INFO_COLOR);
    }

    // 警告显示-string
    public static Toast warning(@NonNull String message) {
        return warning(message, false, null);
    }

    public static Toast warning(@NonNull String message, Drawable icon) {
        return warning(message, true, icon);
    }

    public static Toast warning(@NonNull String message, boolean withIcon) {
        return warning(message, withIcon, getDrawable(getContext(), R.drawable.ic_warning_outline_white));
    }

    public static Toast warning(@StringRes int strRes) {
        return warning(strRes, false, -1);
    }

    public static Toast warning(@StringRes int strRes, @DrawableRes int iconRes) {
        return warning(strRes, true, iconRes);
    }

    public static Toast warning(@StringRes int strRes, boolean withIcon) {
        return warning(strRes, withIcon, R.drawable.ic_warning_outline_white);
    }

    //string
    @CheckResult
    public static Toast warning(@NonNull String message, boolean withIcon, Drawable icon) {
        return custom(getContext(), message, DEFAULT_TEXT_COLOR, withIcon, icon, true, WARNING_COLOR);
    }

    //resId
    public static Toast warning(@StringRes int resId, boolean withIcon, @DrawableRes int iconRes) {
        return custom(getContext(), resId, DEFAULT_TEXT_COLOR, withIcon, iconRes, true, WARNING_COLOR);
    }

    // 成功显示-string
    public static Toast success(@NonNull String message) {
        return success(message, false, null);
    }

    public static Toast success(@NonNull String message, Drawable icon) {
        return success(message, true, icon);
    }

    public static Toast success(@NonNull String message, boolean withIcon) {
        return success(message, withIcon, getDrawable(getContext(), R.drawable.ic_check_white_48dp));
    }

    public static Toast success(@StringRes int strRes) {
        return success(strRes, false, -1);
    }

    public static Toast success(@StringRes int strRes, @DrawableRes int iconRes) {
        return success(strRes, true, iconRes);
    }

    public static Toast success(@StringRes int strRes, boolean withIcon) {
        return success(strRes, withIcon, R.drawable.ic_check_white_48dp);
    }

    //string
    @CheckResult
    public static Toast success(@NonNull String message, boolean withIcon, Drawable icon) {
        return custom(getContext(), message, DEFAULT_TEXT_COLOR, withIcon, icon, true, SUCCESS_COLOR);
    }

    //resId
    @CheckResult
    public static Toast success(@StringRes int resId, boolean withIcon, @DrawableRes int iconRes) {
        return custom(getContext(), resId, DEFAULT_TEXT_COLOR, withIcon, iconRes, true, SUCCESS_COLOR);
    }

    // 错误显示-string
    public static Toast error(@NonNull String message) {
        return error(message, false, null);
    }

    public static Toast error(@NonNull String message, Drawable icon) {
        return error(message, true, icon);
    }

    public static Toast error(@NonNull String message, boolean withIcon) {
        return error(message, withIcon, getDrawable(getContext(), R.drawable.ic_error_outline_white_48dp));
    }

    public static Toast error(@StringRes int strRes) {
        return error(strRes, false, -1);
    }

    public static Toast error(@StringRes int strRes, @DrawableRes int iconRes) {
        return error(strRes, true, iconRes);
    }

    public static Toast error(@StringRes int strRes, boolean withIcon) {
        return error(strRes, withIcon, R.drawable.ic_error_outline_white_48dp);
    }

    //string
    @CheckResult
    public static Toast error(@NonNull String message, boolean withIcon, Drawable icon) {
        return custom(getContext(), message, DEFAULT_TEXT_COLOR, withIcon, icon, true, ERROR_COLOR);
    }

    //resId
    @CheckResult
    public static Toast error(@StringRes int resId, boolean withIcon, @DrawableRes int iconRes) {
        return custom(getContext(), resId, DEFAULT_TEXT_COLOR, withIcon, iconRes, true, ERROR_COLOR);
    }


    /////////////////////////////////////////////////////////////////////////////////
    //// 封装无需等待的Toast封装
    /////////////////////////////////////////////////////////////////////////////////

    @CheckResult
    public static Toast custom(@NonNull Context context, @StringRes int strRes, @ColorInt int textColor, boolean withIcon, @DrawableRes int iconRes, boolean shouldTint, @ColorInt int tintColor) {
        Drawable drawable = null;
        if (withIcon)
            drawable = getDrawable(context, iconRes);
        return custom(context, context.getResources().getString(strRes), textColor, withIcon, drawable, shouldTint, tintColor);
    }

    /**
     * 给Toast设置布局，背景色，icon图标，文字颜色，提示内容
     *
     * @param context    上下文
     * @param message    提示消息
     * @param textColor  文本颜色
     * @param withIcon   是否设置icon图标
     * @param icon       icon图标 withIcon为true时不能为null
     * @param shouldTint 是否设置背景颜色
     * @param tintColor  要设置的背景颜色
     * @return Toast对象信息
     */
    public static Toast custom(@NonNull Context context, @NonNull String message, @ColorInt int textColor, boolean withIcon, Drawable icon, boolean shouldTint, @ColorInt int tintColor) {
        if (currentToast == null) {
            currentToast = new Toast(context); //单例模式，所以实现没有延迟显示效果
        }
        //toast布局
        final View toastLayout = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.toast_layout, null);
        final ImageView toastIcon = (ImageView) toastLayout.findViewById(R.id.toast_icon);
        final TextView toastTextView = (TextView) toastLayout.findViewById(R.id.toast_text);
        //设置默认显示icon图片
        Drawable drawableFrame;
        if (shouldTint) {
            drawableFrame = tint9PatchDrawableFrame(context, tintColor);
        } else {
            drawableFrame = getDrawable(context, R.drawable.toast_frame);
        }
        setBackground(toastLayout, drawableFrame);

        if (withIcon) {
            if (icon == null)
                throw new IllegalArgumentException("Avoid passing 'icon' as null if 'withIcon' is set to true");
            setBackground(toastIcon, icon);
        } else {
            toastIcon.setVisibility(View.GONE);
        }

        toastTextView.setTextColor(textColor);
        toastTextView.setText(message);
        toastTextView.setTypeface(Typeface.create(TOAST_TYPEFACE, Typeface.NORMAL));//设置字体

        currentToast.setView(toastLayout);
        currentToast.setDuration(Toast.LENGTH_SHORT);
        return currentToast;
    }

    public static final void setBackground(@NonNull View view, Drawable drawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            view.setBackground(drawable);
        else
            view.setBackgroundDrawable(drawable);
    }

    /**
     * 获取Drawable图片资源
     *
     * @param context
     * @param id
     * @return
     */
    public static final Drawable getDrawable(@NonNull Context context, @DrawableRes int id) {
        Drawable drawable;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            drawable = context.getDrawable(id);
        } else {
            drawable = context.getResources().getDrawable(id);
        }
        return (drawable!=null)?drawable:null;
    }

    /**
     * NinePatchDrawable动态加载 XX.9.png
     *
     * @param context
     * @param tintColor
     * @return
     */
    public static final Drawable tint9PatchDrawableFrame(@NonNull Context context, @ColorInt int tintColor) {
        final NinePatchDrawable toastDrawable = (NinePatchDrawable) getDrawable(context, R.drawable.toast_frame);
        //指定滤镜颜色以及混合模式
        toastDrawable.setColorFilter(new PorterDuffColorFilter(tintColor, PorterDuff.Mode.SRC_IN));
        return toastDrawable;
    }

    /////////////////////////////////////////////////////////////////////////////////
    //// 封住系统的Toast,实现延迟~
    /////////////////////////////////////////////////////////////////////////////////

    /**
     * Toast 替代方法 ：立即显示无需等待
     *
     * @param msg 显示内容
     */
    public static void showMyToast(String msg) {
        showMyToast(getContext(), msg, Toast.LENGTH_LONG);
    }

    /**
     * Toast 替代方法 ：立即显示无需等待
     *
     * @param resId String资源ID
     */
    public static void showMyToast(int resId) {
        showMyToast(getContext(), getContext().getString(resId), Toast.LENGTH_SHORT);
    }

    /**
     * Toast 替代方法 ：立即显示无需等待
     *
     * @param context  实体
     * @param resId    String资源ID
     * @param duration 显示时长
     */
    public static void showMyToast(Context context, int resId, int duration) {
        showMyToast(context, context.getString(resId), duration);
    }

    /**
     * Toast 替代方法 ：立即显示无需等待
     *
     * @param context  实体
     * @param msg      要显示的字符串
     * @param duration 显示时长
     */
    public static void showMyToast(Context context, String msg, int duration) {
        if (mToast == null) {
            mToast = Toast.makeText(context, msg, duration);
        } else {
            mToast.setText(msg);
        }
        mToast.show();
    }

    /////////////////////////////////////////////////////////////////////////////////
    //// 系统Toast需要等待~
    /////////////////////////////////////////////////////////////////////////////////

    /**
     * 封装了Toast的方法 :需要等待
     *
     * @param context Context
     * @param str     要显示的字符串
     * @param isLong  Toast.LENGTH_LONG / Toast.LENGTH_SHORT
     */
    public static void showToast(Context context, String str, boolean isLong) {
        if (isLong) {
            Toast.makeText(context, str, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 封装了Toast的方法 :需要等待
     *
     * @param context context
     * @param strId   文字资源id
     * @param isLong  是否长时间显示
     */
    public static void showToast(Context context, int strId, boolean isLong) {
        if (isLong) {
            Toast.makeText(getContext(), getContext().getString(strId), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getContext(), getContext().getString(strId), Toast.LENGTH_SHORT).show();
        }
    }

    /////////////////////////////////////////////////////////////////////////////////
    //// 封装再按一次退出~
    /////////////////////////////////////////////////////////////////////////////////
    public static boolean doubleClickExit() {
        if ((System.currentTimeMillis() - mExitTime) > 2000) {
            WuhunToast.normal("再按一次退出").show();
            mExitTime = System.currentTimeMillis();
            return false;
        }
        return true;
    }

}
