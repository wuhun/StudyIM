package studyim.cn.edu.cafa.studyim.activity.other;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import studyim.cn.edu.cafa.studyim.R;
import studyim.cn.edu.cafa.studyim.base.BaseActivity;
import studyim.cn.edu.cafa.studyim.model.ResultModel;
import studyim.cn.edu.cafa.studyim.util.HttpUtil;
import tools.com.lvliangliang.wuhuntools.exception.WuhunDebug;
import tools.com.lvliangliang.wuhuntools.widget.WuhunToast;

import static studyim.cn.edu.cafa.studyim.app.MyApplication.getGson;

/**
 * 发送好友请求
 */
public class SendAddFriendActivity extends BaseActivity {

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

    @BindView(R.id.et_applymas)
    EditText etApplyMas;
    @BindView(R.id.et_applyname)
    EditText etApplyName;

    public static final String BUDDYUSERID = "buddyUserId";
    public static final String USERID = "userId";

    private String buddyUserId;
    private String sendType = "userId";

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_add_friend);
        ButterKnife.bind(this);

        getIntentData();
        initView();
        initData();
        initListener();
    }

    private void initListener() {
        bodyImgMenu.setOnClickListener(mOnClickListener);
        bodyOk.setOnClickListener(mOnClickListener);
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.body_img_menu) {
                SendAddFriendActivity.this.finish();
            }else if(v.getId() == R.id.body_ok) {
                // TODO: 2017/12/5 发送好友请求
                String msg = etApplyMas.getText().toString().trim();
                String name = etApplyName.getText().toString().trim();

                HttpUtil.friendAdd(buddyUserId, "userid", msg, name, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        handler.sendEmptyMessage(REQUEST_ERROR);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String json = response.body().string();
                        WuhunDebug.debug("==>" + json);
                        if (response.isSuccessful()) {
                            ResultModel model = getGson().fromJson(json, ResultModel.class);
                            Message msg = handler.obtainMessage(REQUEST_SUCCESS, model);
                            handler.sendMessage(msg);
                        } else {
                            handler.sendEmptyMessage(REQUEST_FAIL);
                        }
                    }
                });
            }
        }
    };

    private static final int REQUEST_SUCCESS = 0x01;
    private static final int REQUEST_FAIL = 0x02;
    private static final int REQUEST_ERROR = 0x03;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == REQUEST_SUCCESS) {
                //请求成功
                ResultModel model = ((ResultModel)msg.obj);
                if (model.getCode() == 1) {
                    WuhunToast.normal("发送成功").show();
                    SendAddFriendActivity.this.finish();
                } else {
                    WuhunToast.normal(model.getMsg()).show();
                }
            }else if(msg.what == REQUEST_FAIL) {
                //请求失败
                WuhunToast.warning(R.string.request_fail).show();
            }else if(msg.what == REQUEST_ERROR) {
                //请求错误
                WuhunToast.warning(R.string.request_error).show();
            }
            super.handleMessage(msg);
        }
    };

    private void initView() {
        mContext = this;
        headBg.setImageResource(R.mipmap.main_bg);
        bodyTvTitle.setText("发送验证请求");
        bodyImgMenu.setImageResource(R.drawable.icon_back);
        bodySearch.setVisibility(View.GONE);
        bodyOk.setVisibility(View.VISIBLE);
    }

    private void initData() {
        
    }

    public void getIntentData() {
        Intent intent = getIntent();
        buddyUserId = intent.getStringExtra(BUDDYUSERID);
    }
}
