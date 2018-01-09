package studyim.cn.edu.cafa.studyim.ui;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import studyim.cn.edu.cafa.studyim.R;
import studyim.cn.edu.cafa.studyim.activity.other.FriendGetAddListActivity;
import studyim.cn.edu.cafa.studyim.activity.other.GroupSelectFriendActivity;


public class MorePopWindow extends PopupWindow {

    private int screenWidth = 0;
    private View content;
    private Context context;

    @SuppressLint("InflateParams")
    public MorePopWindow(final Activity context) {
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        content = inflater.inflate(R.layout.popupwindow_add, null);

        // 设置SelectPicPopupWindow的View
        this.setContentView(content);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        // 刷新状态
        this.update();
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0000000000);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        this.setBackgroundDrawable(dw);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.activityAnimation);

//        content.measure(0,0);

        RelativeLayout rl_friend_list = (RelativeLayout) content.findViewById(R.id.rl_friend_list);
        RelativeLayout rl_create_group = (RelativeLayout) content.findViewById(R.id.rl_create_group);
//        RelativeLayout re_chatroom = (RelativeLayout) content.findViewById(R.id.re_chatroom);
//        RelativeLayout re_scanner = (RelativeLayout) content.findViewById(R.id.re_scanner);
        rl_friend_list.setOnClickListener(mClickListener);
        rl_create_group.setOnClickListener(mClickListener);
    }

    private final OnClickListener mClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.rl_friend_list) {
                //  好友请求列表
                Intent intent = new Intent(context, FriendGetAddListActivity.class);
                context.startActivity(intent);
            }else if(v.getId() == R.id.rl_create_group) {
                // TODO: 2018/1/5 创建群组
                Intent intent = new Intent(context, GroupSelectFriendActivity.class);
                context.startActivity(intent);
            }
            MorePopWindow.this.dismiss();
        }
    };


    /**
     * 显示popupWindow
     *
     * @param parent
     */
    public void showPopupWindow(View parent, int x, int y) {
        if (!this.isShowing()) {
            // 以下拉方式显示popupwindow
            this.showAsDropDown(parent, x, -10);
        } else {
            this.dismiss();
        }
    }
}
