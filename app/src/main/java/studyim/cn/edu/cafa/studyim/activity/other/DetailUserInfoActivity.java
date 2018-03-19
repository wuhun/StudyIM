package studyim.cn.edu.cafa.studyim.activity.other;

import android.app.AlertDialog;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import io.rong.imkit.RongIM;
import studyim.cn.edu.cafa.studyim.R;
import studyim.cn.edu.cafa.studyim.app.MyApplication;
import studyim.cn.edu.cafa.studyim.base.BaseActivity;
import studyim.cn.edu.cafa.studyim.model.UserInfo;
import studyim.cn.edu.cafa.studyim.ui.OptionItemView;
import studyim.cn.edu.cafa.studyim.util.UserAvatarUtil;
import tools.com.lvliangliang.wuhuntools.widget.WuhunToast;

public class DetailUserInfoActivity extends BaseActivity {

    @BindView(R.id.head_bg)
    ImageView headBg;
    @BindView(R.id.body_img_menu)
    ImageView bodyImgMenu;
    @BindView(R.id.body_tv_title)
    TextView bodyTvTitle;
    @BindView(R.id.body_search)
    ImageView bodySearch;

//    @BindView(R.id.rl_head)
//    RelativeLayout rlHead;
    @BindView(R.id.tv_nickname)
    TextView tvNickname;
    @BindView(R.id.ll_click_set_tag)
    LinearLayout llClickSetTag;
    @BindView(R.id.tv_school)
    TextView tvSchool;
    @BindView(R.id.tv_class)
    TextView tvClass;
    @BindView(R.id.tv_majop)
    TextView tvMajop;
    @BindView(R.id.btn_add_constact)
    Button btnAddConstact;
    @BindView(R.id.btn_send_msg)
    Button btnSendMsg;
    @BindView(R.id.img_avatar)
    ImageView imgAvatar;
    @BindView(R.id.img_sex)
    ImageView img_sex;
    @BindView(R.id.rlMenu)
    RelativeLayout rlMenu;
    @BindView(R.id.svMenu)
    LinearLayout svMenu;

    @BindView(R.id.oiv_alias)
    OptionItemView oivAlias;
    @BindView(R.id.oiv_delete)
    OptionItemView oivDelete;
//    @BindView(R.id.btn_detail_me)
//    Button btnDetailMe;

    private Context mContext;
    private UserInfo userInfo;

    public static final String PUT_USER_INFO = "put_user_info";
    public static final String BEFORE = "before";

    public static final int USER_REMARK_NAME = 0x01;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detaul_user_info);
        ButterKnife.bind(this);

        initView();
        getIntentData();
        initeListener();
    }

    private void initeListener() {
        bodyImgMenu.setOnClickListener(mOnClickListener);//返回
        btnAddConstact.setOnClickListener(mOnClickListener);//添加好友
        llClickSetTag.setOnClickListener(mOnClickListener);//设置注备
        bodySearch.setOnClickListener(mOnClickListener);//
        bodySearch.setVisibility(View.GONE);
        rlMenu.setOnClickListener(mOnClickListener);
        btnSendMsg.setOnClickListener(mOnClickListener);
        
        oivAlias.setOnClickListener(mOnClickListener);
        oivDelete.setOnClickListener(mOnClickListener);
//        btnDetailMe.setOnClickListener(mOnClickListener);
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.body_img_menu) {
                DetailUserInfoActivity.this.finish();
            }else if(v.getId() == R.id.btn_add_constact) { //添加好友
                sendAddFriendRequset();
            }else if(v.getId() == R.id.body_search) {
                showMenu();
            }else if(v.getId() == R.id.rlMenu) {
                hideMenu();
            }else if(v.getId() == R.id.btn_send_msg) {
                /** 发起会话 */
                String displayName = null;
                displayName = userInfo.getNICKNAME();
                if (RongIM.getInstance() != null)
                    RongIM.getInstance().startPrivateChat(mContext, userInfo.getRCID(), displayName);
                finish();
            }
//            else if(v.getId() == R.id.oiv_alias) {
//                // TODO: 2017/12/4 设置注备名称 REMARKNAME
//                Intent intent = new Intent(mContext, SetUserRemarkName.class);
//                startActivityForResult(intent, USER_REMARK_NAME);
//            }else if(v.getId() == R.id.oiv_delete) {
//                // TODO: 2017/12/4 删除当前用户
//            }
//            else if(v.getId() == R.id.btn_detail_me) {
//                // TODO: 2017/12/5 查看个人信息
//            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == USER_REMARK_NAME) {
            //获取全局的注备名称
//            data.getStringExtra("");
        }
    }

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

    private static final String BUDDYUSERID = "buddyUserId";
    private static final String USERID = "userId";

    /** 发送好友请求 */
    private void sendAddFriendRequset() {
        // TODO: 2017/12/3 发送好友请求
        Intent intent = new Intent(mContext, SendAddFriendActivity.class);
        intent.putExtra(SendAddFriendActivity.BUDDYUSERID, userInfo.getUSERID()); //对方用户id - 本人学号 - 发送请求途径
        jumpToActivity(intent);
    }

    private void initView() {
        mContext = this;
        headBg.setImageResource(R.mipmap.main_bg);
        bodyImgMenu.setImageResource(R.drawable.icon_back);
        bodyTvTitle.setText(R.string.detail_info);
//        bodySearch.setVisibility(View.GONE);
        bodySearch.setImageResource(R.mipmap.head_menu_more);
    }

    public void getIntentData() {
        Intent intent = getIntent();
        userInfo = (UserInfo) intent.getSerializableExtra(PUT_USER_INFO);
        String url = intent.getStringExtra(BEFORE);

        if(userInfo == null)  return;
        tvNickname.setText(userInfo.getNICKNAME());
        tvSchool.setText(userInfo.getSCHOOL());
        tvMajop.setText(userInfo.getMAJOR());
        tvClass.setText(userInfo.getCLASS());
        UserAvatarUtil.showAvatar(this, userInfo, url, imgAvatar);

        String sex = userInfo.getSEX();
        if (sex.equals("W")) {
            img_sex.setImageResource(R.mipmap.ic_sex_female);
        } else {
            img_sex.setImageResource(R.mipmap.ic_sex_male);
        }

        String isbuddy = userInfo.getISBUDDY();

//        if(userInfo.getUSERID() >=0 && String.valueOf(userInfo.getUSERID()).equals(getSPUtil().getUSERID())) {
//            return;
//        }
//        TestLog.i("当前用户id：" + MyApplication.getSPUtil().getUSERID() + " = " + userInfo.getUSERID());

        if ( MyApplication.getSPUtil().getUSERID().equals(userInfo.getUSERID()) ) {
            btnSendMsg.setVisibility(View.GONE);
            btnAddConstact.setVisibility(View.GONE);
        } else {
            if (isbuddy.equals("N")) {
                btnAddConstact.setVisibility(View.VISIBLE);
                btnSendMsg.setVisibility(View.GONE);
            } else {
                btnSendMsg.setVisibility(View.VISIBLE);
                btnAddConstact.setVisibility(View.GONE);
            }
        }
    }

    private static final int REQUEST_ERROR = 0x01;
    private static final int REQUEST_FAIL = 0x02;
    private static final int REQUEST_SUCCESS = 0x03;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == REQUEST_ERROR) {
                WuhunToast.normal(R.string.get_fail).show();
            }else if(msg.what == REQUEST_FAIL) {
                WuhunToast.normal(getResources().getString(R.string.request_fail)).show();
            }
            super.handleMessage(msg);
        }
    };

//    private String getAvaterUri(UserInfo userInfo) {
//        String uri;
//        if(userInfo == null) return null;
//        if (WuhunDataTool.isNullString(userInfo.getAVATAR())) {
//            uri =  RongGenerate.generateDefaultAvatar(userInfo.getNICKNAME(), userInfo.getUSERID()+"");
//        } else {
//            uri = userInfo.getAVATAR();
//        }
//        return uri;
//    }

    private void showAddFriendDialog(){
        View dialogView = this.getLayoutInflater().inflate(R.layout.add_friend_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext)
                .setView(dialogView)
                .setPositiveButton(R.string.positive, null)
                .setNegativeButton(R.string.negative, null);
        builder.show();
    }
}
