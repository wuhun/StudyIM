package tools.com.lvliangliang.wuhuntools.util;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import tools.com.lvliangliang.wuhuntools.R;

/**
 * ================================================
 * 作    者：悟魂(了解自己，感悟灵魂，做最好的自己)
 * 创建日期：2018/1/2 0002
 * 版    本：1.0
 * 描    述：
 * 修订历史：
 * ================================================
 */
public class WuhunPhoneTool {

    private static Dialog callPhoneDialgo;
    /**
     * 是否拨打电话： 需要验证权限 —— Manifest.permission.CALL_PHONE
     * @param mContext  上下文
     * @param phoneNumber   电话号码
     */
    public static void showCallPhone(final Context mContext, String phoneNumber) {
        if (callPhoneDialgo != null) {
            callPhoneDialgo.show();
        } else {
            callPhoneDialgo = new Dialog(mContext);
            callPhoneDialgo.requestWindowFeature(Window.FEATURE_NO_TITLE);//去除title
            View view = View.inflate(mContext, R.layout.call_phone, (ViewGroup) null);
            callPhoneDialgo.setContentView(view);

            final TextView tv_phone_number = (TextView) view.findViewById(R.id.titleTextView);
            tv_phone_number.setText(phoneNumber);

            View btn_dial_accptel = view.findViewById(R.id.btn_dial_accptel);
            btn_dial_accptel.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("MissingPermission")
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel://" + tv_phone_number.getText().toString()));
                    mContext.startActivity(intent);
                }
            });
            //取消
            View btn_dial_cancel = view.findViewById(R.id.btn_dial_cancel);
            btn_dial_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callPhoneDialgo.dismiss();
                }
            });

            callPhoneDialgo.show();
        }
    }
}
