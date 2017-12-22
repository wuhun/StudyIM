package studyim.cn.edu.cafa.studyim.activity.other;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.rong.imkit.RongIM;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import studyim.cn.edu.cafa.studyim.R;
import studyim.cn.edu.cafa.studyim.base.BaseActivity;
import studyim.cn.edu.cafa.studyim.common.Constant;
import studyim.cn.edu.cafa.studyim.model.FriendGetInfoModel;
import studyim.cn.edu.cafa.studyim.ui.OptionItemView;
import studyim.cn.edu.cafa.studyim.util.HttpUtil;
import studyim.cn.edu.cafa.studyim.util.UserAvatarUtil;
import tools.com.lvliangliang.wuhuntools.exception.WuhunDebug;
import tools.com.lvliangliang.wuhuntools.util.WuhunDataTool;
import tools.com.lvliangliang.wuhuntools.widget.WuhunToast;

import static studyim.cn.edu.cafa.studyim.app.MyApplication.getGson;

public class FriendGetinfoActivity extends BaseActivity {

    FriendGetInfoModel friendInfo;

    @BindView(R.id.tv_username)
    TextView tvUsername;
    @BindView(R.id.tv_nickname)
    TextView tvNickname;
    @BindView(R.id.ll_remark_msg)
    LinearLayout llRemarkMsg;
    @BindView(R.id.ll_remark_phone)
    LinearLayout llRemarkPhone;
    @BindView(R.id.tv_remark_msg)
    TextView tvRemarkMsg;
    @BindView(R.id.tv_remark_phone)
    TextView tvRemarkPhone;
    @BindView(R.id.tv_school)
    TextView tvSchool;
    @BindView(R.id.img_avatar)
    ImageView imgAvatar;
    @BindView(R.id.img_sex)
    ImageView imgSex;
    @BindView(R.id.tv_major)
    TextView tvMajor;
    @BindView(R.id.tv_class)
    TextView tvClass;

//    @BindView(R.id.ll_click_set_tag)
//    LinearLayout llClickSetTag;
    @BindView(R.id.btn_add_constact)
    Button btnAddConstact;
    @BindView(R.id.btn_send_msg)
    Button btnSendMsg;

//    @BindView(R.id.btn_detail_me)
//    Button btnDetailMe;
    @BindView(R.id.ll_content_user_info)
    LinearLayout llContentUserInfo;
    @BindView(R.id.vMask)
    View vMask;
    @BindView(R.id.oiv_alias)
    OptionItemView oivAlias;
    @BindView(R.id.oiv_delete)
    OptionItemView oivDelete;
    @BindView(R.id.svMenu)
    LinearLayout svMenu;
    @BindView(R.id.rlMenu)
    RelativeLayout rlMenu;

    @BindView(R.id.head_bg)
    ImageView headBg;
    @BindView(R.id.body_img_menu)
    ImageView bodyImgMenu;
    @BindView(R.id.body_tv_title)
    TextView bodyTvTitle;
    @BindView(R.id.body_search)
    ImageView bodySearch;

    private Context mContext;
    private String extraUserId; //用户id

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_getinfo);
        ButterKnife.bind(this);

        getIntentData();
        initView();
        initeListener();
    }

    private void initeListener() {
        bodyImgMenu.setOnClickListener(mOnClickListener);//返回
        btnAddConstact.setOnClickListener(mOnClickListener);//添加好友
//        llClickSetTag.setOnClickListener(mOnClickListener);//设置注备
//        bodySearch.setOnClickListener(mOnClickListener);//右边菜单
//        rlMenu.setOnClickListener(mOnClickListener);
        // TODO: 2017/12/8 添加好友 发送消息
//        btnAddConstact
        //发消息
        btnSendMsg.setOnClickListener(mOnClickListener);

        oivAlias.setOnClickListener(mOnClickListener);
        oivDelete.setOnClickListener(mOnClickListener);
//        btnDetailMe.setOnClickListener(mOnClickListener);
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.body_img_menu) {
                FriendGetinfoActivity.this.finish();
            }else if(v.getId() == R.id.btn_add_constact) { //添加好友
                sendAddFriendRequset();
            }
//            else if(v.getId() == R.id.body_search) {
//                showMenu();
//            }else if(v.getId() == R.id.rlMenu) {
//                hideMenu();
//            }
            else if(v.getId() == R.id.oiv_alias) {
                // TODO: 2017/12/4 设置注备名称 REMARKNAME
//                Intent intent = new Intent(mContext, SetUserRemarkName.class);
//                startActivityForResult(intent, USER_REMARK_NAME);
            }else if(v.getId() == R.id.oiv_delete) {
                // TODO: 2017/12/4 删除当前用户
            }else if(v.getId() == R.id.btn_send_msg) {
                // TODO: 2017/12/8 发送消息
//                Intent intent = new Intent(mContext, SessionActivity.class);
                /** 发起会话 */
                String displayName = null;
                displayName = friendInfo.getResult().getRemarkName();
                if (WuhunDataTool.isNullString(displayName)) {
                    displayName = friendInfo.getResult().getNickName();
                }
//                RongIM.getInstance().startPrivateChat(mContext, friendInfo.getResult().getRCID(), displayName);
                if (RongIM.getInstance() != null)
                    RongIM.getInstance().startPrivateChat(
                            mContext, friendInfo.getResult().getRCID(), displayName);
//                WuhunDebug.debug("与" + displayName + "聊天：" + friendInfo.getResult().getRCID());
//                RongIM.getInstance().startPrivateChat(mContext, extraUserId, displayName);
                finish();
            }
//            else if(v.getId() == R.id.btn_detail_me) {
//                // TODO: 2017/12/5 查看个人信息
//            }
        }
    };

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode == USER_REMARK_NAME) {
//            //获取全局的注备名称
////            data.getStringExtra("");
//        }
//    }

    private void showMenu() {
        rlMenu.setVisibility(View.VISIBLE);
        TranslateAnimation ta = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 1,
                Animation.RELATIVE_TO_SELF, 0);
        ta.setDuration(200);
        svMenu.startAnimation(ta);
    }

    private void hideMenu() {
        TranslateAnimation ta = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 1);
        ta.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                rlMenu.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
        ta.setDuration(200);
        svMenu.startAnimation(ta);
    }

    public static final String GET_USERID = "userId";
    /** 发送好友请求 */
    private void sendAddFriendRequset() {
        // TODO: 2017/12/3 发送好友请求
        Intent intent = new Intent(mContext, SendAddFriendActivity.class);
        intent.putExtra(SendAddFriendActivity.BUDDYUSERID, extraUserId); //对方用户id - 本人学号 - 发送请求途径
        jumpToActivity(intent);
    }

    private void initView() {
        mContext = this;
        headBg.setImageResource(R.mipmap.main_bg);
        bodyImgMenu.setImageResource(R.drawable.icon_back);
        bodyTvTitle.setText("详细资料");
        bodySearch.setVisibility(View.GONE);
//        bodySearch.setImageResource(R.mipmap.head_menu_more);
    }

    public void getIntentData() {
        Intent intent = getIntent();

        extraUserId = intent.getStringExtra(GET_USERID);
        if (extraUserId != null) {
            HttpUtil.friend_getinfo(extraUserId, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    handler.sendEmptyMessage(REQUEST_ERROR);
                }

                @Override
                public void onResponse(Call call, final Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String result = response.body().string();

                        friendInfo = getGson().fromJson(result, FriendGetInfoModel.class);

                        WuhunDebug.debug("DetailUserInfoActivity:" + friendInfo);

                        Message msg = handler.obtainMessage(REQUEST_SUCCESS, friendInfo);
                        handler.sendMessage(msg);
//                        BaseModel<UserInfo> model = getGson().fromJson(result, new TypeToken<BaseModel<UserInfo>>() {}.getType());
//                        WuhunDebug.debug("DetailUserInfoActivity:" + model);
                    } else {
                        handler.sendEmptyMessage(REQUEST_FAIL);
                    }
                }
            });
        }

//        if(userInfo == null)  return;
//        tvNickname.setText(userInfo.getNICKNAME());
//        tvSchool.setText(userInfo.getSCHOOL());
//        tvMajop.setText(userInfo.getMAJOR());
//        tvClass.setText(userInfo.getCLASS());
//        UserAvatarUtil.showAvatar(this, userInfo, url, imgAvatar);
//
//        String sex = userInfo.getSEX();
//        if (sex.equals("W")) {
//            img_sex.setImageResource(R.mipmap.ic_sex_female);
//        } else {
//            img_sex.setImageResource(R.mipmap.ic_sex_male);
//        }
//
//        String isbuddy = userInfo.getISBUDDY();
//
//        if (isbuddy.equals("N")) {
//            btnAddConstact.setVisibility(View.VISIBLE);
//            btnSendMsg.setVisibility(View.GONE);
//        } else {
//            btnSendMsg.setVisibility(View.VISIBLE);
//            btnAddConstact.setVisibility(View.GONE);
//        }
//
////        tv.setText(userInfo.toString());
//        WuhunDebug.debug("DetailUserInfoActivity:" + userInfo.toString());
    }

    private static final int REQUEST_ERROR = 0x01;
    private static final int REQUEST_FAIL = 0x02;
    private static final int REQUEST_SUCCESS = 0x03;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == REQUEST_ERROR) {
                WuhunToast.normal("获取失败").show();
            }else if(msg.what == REQUEST_FAIL) {
                WuhunToast.normal(getResources().getString(R.string.request_fail)).show();
            }else if(msg.what == REQUEST_SUCCESS) {
                FriendGetInfoModel model = ((FriendGetInfoModel)msg.obj);
                if (model.getCode() == 1) {
                    FriendGetInfoModel.ResultBean friendInfo = model.getResult();
                    showFriendInfo(friendInfo);
                } else {
                    WuhunToast.normal("获取失败").show();
                }
            }
            super.handleMessage(msg);
        }
    };

    private void showFriendInfo(FriendGetInfoModel.ResultBean friendInfo) {
        if(friendInfo == null) return;
        String nickname = null;
        if (!WuhunDataTool.isNullString(friendInfo.getRemarkName())) {
            nickname = friendInfo.getRemarkName();
        } else {
            nickname = friendInfo.getNickName();
        }
        tvNickname.setText("昵称：" + nickname);
        tvUsername.setText(friendInfo.getRemarkName());
        if (!WuhunDataTool.isNullString(friendInfo.getRemarkMSG())) {
            tvRemarkMsg.setText("注备信息：" + friendInfo.getRemarkMSG());
        } else {
            llRemarkMsg.setVisibility(View.GONE);
        }
        if (!WuhunDataTool.isNullString(friendInfo.getRemarkTelephone())) {
            tvRemarkPhone.setText("注备电话" + friendInfo.getRemarkTelephone());
        } else {
            llRemarkPhone.setVisibility(View.GONE);
        }
        String sex = friendInfo.getSex();
        if (sex.equals("W")) {
            imgSex.setImageResource(R.mipmap.ic_sex_female);
        } else {
            imgSex.setImageResource(R.mipmap.ic_sex_male);
        }

        String isbuddy = friendInfo.getISBUDDYS();
        if (isbuddy.equals("N")) {
            btnAddConstact.setVisibility(View.VISIBLE);
            btnSendMsg.setVisibility(View.GONE);
        } else {
            btnSendMsg.setVisibility(View.VISIBLE);
            btnAddConstact.setVisibility(View.GONE);
        }

        tvSchool.setText(friendInfo.getSchool());
        tvMajor.setText(friendInfo.getMajor());
        tvClass.setText(friendInfo.getClassX());
//        RongGenerate.generateDefaultAvatar(nickname, friendInfo.getUserId()+"");
        UserAvatarUtil.showAvatar(this, friendInfo, Constant.HOME_URL, imgAvatar);
    }



//    private void showAddFriendDialog(){
//        View dialogView = this.getLayoutInflater().inflate(R.layout.add_friend_dialog, null);
//        AlertDialog.Builder builder = new AlertDialog.Builder(mContext)
//                .setView(dialogView)
//                .setPositiveButton(R.string.positive, null)
//                .setNegativeButton(R.string.negative, null);
//        builder.show();
//    }
}
