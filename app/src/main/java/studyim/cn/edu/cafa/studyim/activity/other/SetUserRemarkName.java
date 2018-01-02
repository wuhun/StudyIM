package studyim.cn.edu.cafa.studyim.activity.other;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.UserInfo;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import studyim.cn.edu.cafa.studyim.R;
import studyim.cn.edu.cafa.studyim.app.MyApplication;
import studyim.cn.edu.cafa.studyim.base.BaseActivity;
import studyim.cn.edu.cafa.studyim.common.Constant;
import studyim.cn.edu.cafa.studyim.model.FriendUserInfo;
import studyim.cn.edu.cafa.studyim.model.ResultModel;
import studyim.cn.edu.cafa.studyim.util.HttpUtil;
import tools.com.lvliangliang.wuhuntools.exception.TestLog;
import tools.com.lvliangliang.wuhuntools.manager.BroadcastManager;
import tools.com.lvliangliang.wuhuntools.util.WuhunDataTool;
import tools.com.lvliangliang.wuhuntools.widget.WuhunToast;

public class SetUserRemarkName extends BaseActivity {

    @BindView(R.id.head_bg)
    ImageView headBg;
    @BindView(R.id.body_img_menu)
    ImageView bodyImgMenu;
    @BindView(R.id.body_tv_title)
    TextView bodyTvTitle;
    @BindView(R.id.body_search)
    ImageView bodySearch;
    @BindView(R.id.body_ok)
    TextView bodyOk;

    @BindView(R.id.etAlias)
    EditText etAlias;
    @BindView(R.id.ibClearAlias)
    ImageButton ibClearAlias;
    @BindView(R.id.etPhone)
    EditText etPhone;
    @BindView(R.id.ibClearPhone)
    ImageButton ibClearPhone;
    @BindView(R.id.ibClearDesc)
    ImageButton ibClearDesc;
    @BindView(R.id.etDesc)
    EditText etDesc;
//    @BindView(R.id.etPicture)
//    EditText etPicture;
//    @BindView(R.id.ibClearPicture)
//    ImageButton ibClearPicture;

    private Context mContext;
    public static final String FRIEND_INFO = "FriendUserInfo";
    private FriendUserInfo friendinfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_user_remark_name);
        ButterKnife.bind(this);

        initView();
        initIntentData();
        initListener();
    }

    private void initIntentData() {
        Intent intent = getIntent();
        friendinfo = (FriendUserInfo) intent.getSerializableExtra(FRIEND_INFO);
        TestLog.i("SetUserRemarkName: 收到的消息：" + friendinfo );
    }

    private void initListener() {
        bodyImgMenu.setOnClickListener(mClick);
        ibClearAlias.setOnClickListener(mClick);
        bodyOk.setOnClickListener(mClick);

        etAlias.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() >= 10)
                    WuhunToast.normal("超过最大字符10").show();
                if (!WuhunDataTool.isNullString(s.toString())) {
                    ibClearAlias.setVisibility(View.VISIBLE);
                } else {
                    ibClearAlias.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        etPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!WuhunDataTool.isNullString(s.toString())) {
                    ibClearPhone.setVisibility(View.VISIBLE);
                } else {
                    ibClearPhone.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        etDesc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!WuhunDataTool.isNullString(s.toString())) {
                    ibClearDesc.setVisibility(View.VISIBLE);
                } else {
                    ibClearDesc.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private View.OnClickListener mClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.body_img_menu) {//返回
                SetUserRemarkName.this.finish();
            }
//            else if (v.getId() == R.id.body_ok) {
//                Intent intent = new Intent();
//                intent.putExtra("", "");
//                setResult(FriendGetinfoActivity.USER_REMARK_NAME, intent);
//                finish();
//            }
            else if (v.getId() == R.id.ibClearAlias) {
                etAlias.setText("");
            } else if (v.getId() == R.id.body_ok) {
                String alias = etAlias.getText().toString();
                String phone = etPhone.getText().toString();
                String desc = etDesc.getText().toString();
                if (WuhunDataTool.isNullString(alias) && WuhunDataTool.isNullString(phone) && WuhunDataTool.isNullString(desc)) {
                    WuhunToast.normal("没有修改的内容").show();
                } else {
                    if (friendinfo == null)
                        WuhunToast.normal("获取好友信息失败").show();
                    String userId = friendinfo.getUserId() + "";
                    HttpUtil.setNote(userId, alias, phone, desc, null, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            WuhunToast.normal("修改备注失败").show();
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String result = response.body().string();
                            TestLog.i("修改注备==>" + result);
                            ResultModel resultModel = MyApplication.getGson().fromJson(result, ResultModel.class);
                            if (resultModel.getCode() == 1) {
                                WuhunToast.normal("修改备注成功").show();
                                UserInfo info = new UserInfo(friendinfo.getUserId()+"", etAlias.getText().toString(), Uri.parse(friendinfo.getAvatar()));
                                BroadcastManager.getInstance(mContext).sendBroadcast(Constant.UPDATA_CONSTACT_LIST);
                                RongIM.getInstance().refreshUserInfoCache(info);
                                SetUserRemarkName.this.finish();
                            } else {
                                WuhunToast.normal("修改备注失败").show();
                            }
                        }
                    });
                }
            }
        }
    };

    private void initView() {
        mContext = this;
        headBg.setImageResource(R.drawable.main_bg);
        bodyImgMenu.setImageResource(R.drawable.icon_back);
        bodyTvTitle.setText("注备信息");
        bodySearch.setVisibility(View.GONE);
        bodyOk.setVisibility(View.VISIBLE);
    }

}
