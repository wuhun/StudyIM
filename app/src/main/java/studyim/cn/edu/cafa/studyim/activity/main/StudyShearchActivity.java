package studyim.cn.edu.cafa.studyim.activity.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import studyim.cn.edu.cafa.studyim.R;
import studyim.cn.edu.cafa.studyim.activity.other.DetailUserInfoActivity;
import studyim.cn.edu.cafa.studyim.base.BaseActivity;
import studyim.cn.edu.cafa.studyim.model.BaseModel;
import studyim.cn.edu.cafa.studyim.model.UserInfo;
import studyim.cn.edu.cafa.studyim.util.HttpUtil;
import studyim.cn.edu.cafa.studyim.util.UserAvatarUtil;
import tools.com.lvliangliang.wuhuntools.adapter.LQRAdapterForRecyclerView;
import tools.com.lvliangliang.wuhuntools.adapter.LQRViewHolder;
import tools.com.lvliangliang.wuhuntools.adapter.LQRViewHolderForRecyclerView;
import tools.com.lvliangliang.wuhuntools.adapter.OnItemClickListener;
import tools.com.lvliangliang.wuhuntools.exception.TestLog;
import tools.com.lvliangliang.wuhuntools.exception.WuhunDebug;
import tools.com.lvliangliang.wuhuntools.util.WuhunDataTool;
import tools.com.lvliangliang.wuhuntools.widget.WuhunToast;
import tools.com.lvliangliang.wuhuntools.widget.recyclerview.WuhunRecyclerView;

import static studyim.cn.edu.cafa.studyim.app.MyApplication.getGson;

public class StudyShearchActivity extends BaseActivity {

    @BindView(R.id.btn_search)
    TextView btnSearch;
    @BindView(R.id.ll_default_content)
    RelativeLayout llDefaultContent;
    @BindView(R.id.et_search_content)
    EditText etSearchContent;
    @BindView(R.id.spinner_style)
    Spinner spinnerStyle;
    @BindView(R.id.icon_img_search)
    ImageView iconImgSearch;
    @BindView(R.id.tv_user_empty)
    TextView tvUserEmpty;
    @BindView(R.id.rv_user_info_list)
    WuhunRecyclerView rvUserInfoList;

    private Context mContext;
    ArrayAdapter<String> spinner_adapter;
    List<String> search_style;
    private String[] spinners = new String[]{"学号","电话","昵称"};
    private int currentPosition;



    public static final int REQUEST_SUCCESS = 0x01;
    public static final int REQUEST_FAIL = 0x02;
    public static final int REQUEST_ERROR = 0x03;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_shearch);
        ButterKnife.bind(this);

        initView();
        initAdapter();
        initListener();
    }

    private void initView() {
        mContext = this;
    }

    private void initAdapter() {
        search_style = new ArrayList<>();
        for (int i = 0; i < spinners.length; i++){
            search_style.add(spinners[i]);
        }
//        search_style.add("学号");
//        search_style.add("电话");
//        search_style.add("昵称");

        spinner_adapter = new ArrayAdapter<String>(this, R.layout.shearch_spinner_item, search_style);
        spinnerStyle.setAdapter(spinner_adapter);
    }

    private void initListener() {
        btnSearch.setOnClickListener(mOnClick);
        etSearchContent.setOnFocusChangeListener(mOnFocusChangeListener);
        spinnerStyle.setOnItemSelectedListener(mOnItemSelectedListener);
    }

    private AdapterView.OnItemSelectedListener mOnItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            currentPosition = position;
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {}
    };

    private View.OnFocusChangeListener mOnFocusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus && v.getId() == R.id.et_search_content) {
                spinnerStyle.setVisibility(View.VISIBLE);
                iconImgSearch.setVisibility(View.GONE);
            } else {
                spinnerStyle.setVisibility(View.GONE);
                iconImgSearch.setVisibility(View.VISIBLE);
            }
        }
    };

    private View.OnClickListener mOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_search:
                    WuhunToast.info("您搜索的内容为：" + etSearchContent.getText().toString()).show();
    //                    HttpUtil.userInfoSearch();
    //                    llDefaultContent.setVisibility(View.GONE);
                    String content = etSearchContent.getText().toString();
                    if(WuhunDataTool.isNullString(content)) {
                        WuhunToast.info("请输入" + spinners[currentPosition]).show();
                        return;
                    }
                    if (spinners[currentPosition].equals("学号")) {
                        HttpUtil.userInfoSearch_studentId("studentID", content, mHttpCallback);
                    } else if (spinners[currentPosition].equals("电话")) {
                        HttpUtil.userInfoSearch_studentId("telephone", content, mHttpCallback);
                    } else if (spinners[currentPosition].equals("昵称")) {
                        HttpUtil.userInfoSearch_studentId("nickName", content, mHttpCallback);
                    }
                    break;
            }
        }
    };

    private Callback mHttpCallback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            handler.sendEmptyMessage(REQUEST_ERROR);
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            String json = response.body().string();
            WuhunDebug.debug(json);
            BaseModel<UserInfo> model = getGson().fromJson(json, new TypeToken<BaseModel<UserInfo>>(){}.getType());
//            List<UserInfo> result = model.getResult();
//            TestLog.i("********debug↓********");
//            for (int i=0;i<result.size();i++) {
//                TestLog.i(result.get(i).toString());
//            }
//            TestLog.i("********debug↑********");

            if (response.isSuccessful()) {
                Message msg = handler.obtainMessage(REQUEST_SUCCESS, model);
                handler.sendMessage(msg);
            } else {
                handler.sendEmptyMessage(REQUEST_FAIL);
            }
        }
    };

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == REQUEST_SUCCESS) {
                BaseModel<UserInfo> model = (BaseModel<UserInfo>)msg.obj;
                if (model.getCode() == 1) {
                    WuhunDebug.debug("result:" + (model.getResult() != null && model.getResult().size() <= 0));
                    if (model.getResult() != null && model.getResult().size() <= 0) {
                        tvUserEmpty.setVisibility(View.VISIBLE);
                    } else {
                        tvUserEmpty.setVisibility(View.GONE);
                    }
                    llDefaultContent.setVisibility(View.GONE);
                    // TODO: 2017/12/1 适配数据
                    TestLog.i("获取成功");
                    showUserInfoList(model);
                } else {
                    TestLog.i("获取失败");
                }
            }else if(msg.what == REQUEST_FAIL) {
                WuhunToast.normal(getResources().getString(R.string.request_fail)).show();
            }else if(msg.what == REQUEST_ERROR) {
                WuhunToast.normal(getResources().getString(R.string.request_error)).show();
            }
            super.handleMessage(msg);
        }
    };

    private void showUserInfoList(final BaseModel<UserInfo> model) {
        final List<UserInfo> result = model.getResult();
        LQRAdapterForRecyclerView adapter = new LQRAdapterForRecyclerView<UserInfo>(mContext, result, R.layout.search_user_info_item) {
            @Override
            public void convert(LQRViewHolderForRecyclerView helper, UserInfo item, int position) {
                helper.setText(R.id.tvName, result.get(position).getNICKNAME());
                ImageView imgAvatar = helper.getView(R.id.img_avatar);
                UserAvatarUtil.showAvatar(mContext, item, model.getBefore(), imgAvatar);
            }
        };
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(LQRViewHolder helper, ViewGroup parent, View itemView, int position) {
                UserInfo userInfo = result.get(position);
                Intent intent = new Intent();
                intent.putExtra(DetailUserInfoActivity.PUT_USER_INFO, userInfo);
                intent.putExtra(DetailUserInfoActivity.BEFORE, model.getBefore());
                intent.setClass(mContext, DetailUserInfoActivity.class);
                jumpToActivity(intent);
            }
        });
        rvUserInfoList.setAdapter(adapter);
    }
//    ll_default_content
}
