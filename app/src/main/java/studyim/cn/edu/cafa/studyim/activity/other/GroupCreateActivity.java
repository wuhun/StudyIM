package studyim.cn.edu.cafa.studyim.activity.other;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.rong.imkit.widget.AsyncImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import studyim.cn.edu.cafa.studyim.R;
import studyim.cn.edu.cafa.studyim.app.MyApplication;
import studyim.cn.edu.cafa.studyim.base.BaseActivity;
import studyim.cn.edu.cafa.studyim.common.Constant;
import studyim.cn.edu.cafa.studyim.model.Friend;
import studyim.cn.edu.cafa.studyim.ui.BottomMenuDialog;
import studyim.cn.edu.cafa.studyim.ui.GlideRoundTransform;
import studyim.cn.edu.cafa.studyim.util.HttpUtil;
import studyim.cn.edu.cafa.studyim.util.PhotoUtils;
import tools.com.lvliangliang.wuhuntools.exception.TestLog;
import tools.com.lvliangliang.wuhuntools.util.WuhunCameraTool;
import tools.com.lvliangliang.wuhuntools.util.WuhunState;
import tools.com.lvliangliang.wuhuntools.widget.WuhunToast;

public class GroupCreateActivity extends BaseActivity {

    @BindView(R.id.head_bg)
    ImageView headBg;
    @BindView(R.id.body_img_menu)
    ImageView bodyLeft;
    @BindView(R.id.body_tv_title)
    TextView bodyTvTitle;
    @BindView(R.id.body_search)
    ImageView bodyRight;
    @BindView(R.id.body_ok)
    TextView bodyOk;

    @BindView(R.id.img_Group_portrait)
    AsyncImageView imgGroupPortrait;
    @BindView(R.id.create_groupname)
    EditText createGroupname;
    @BindView(R.id.img_clear_groupname)
    ImageView imgClearGroupname;
    @BindView(R.id.create_ok)
    Button createOk;

    @BindView(R.id.spinner_style)
    Spinner spinnerStyle;
    private List<String> groupTypeList = new ArrayList<>();
    ArrayAdapter<String> spinner_adapter;

    private String groupType = "group"; //创建的类型

    public static final String GROUP_FRIENDS = "groupFriends";
    private List<Friend> groupList = new ArrayList<>();
    private Context mContext;

    private PhotoUtils photoUtils;
    private Uri picUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_create);
        ButterKnife.bind(this);

        getSaveData();
        initView();
        initListener();
    }

    private void initListener() {
        bodyLeft.setOnClickListener(mClickListener);
        imgGroupPortrait.setOnClickListener(mClickListener);
        imgClearGroupname.setOnClickListener(mClickListener);
        createOk.setOnClickListener(mClickListener);
        createGroupname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    imgClearGroupname.setVisibility(View.VISIBLE);
                } else {
                    imgClearGroupname.setVisibility(View.GONE);
                }
                if(s.length()>=10) {
                    WuhunToast.info("最多输入10个字符").show();
                } else if (s.length() < 2) {
                    WuhunToast.info("最少输入2个字符").show();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
        spinnerStyle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(groupTypeList.get(position).equals("普通群")) {
                    groupType = "group";
                } else if (groupTypeList.get(position).equals("班级群")) {
                    groupType = "class";
                } else {
                    groupType = "group";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.body_img_menu:
                    GroupCreateActivity.this.finish();
                    break;
                case R.id.img_Group_portrait:
                    showPhotoDialog();//拍照 - 本地 - 取消 菜单
                    break;
                case R.id.create_ok:
                    // 1、创建群 2、添加好友
                    String groupName = createGroupname.getText().toString();
                    TestLog.i("输入的内容：" + groupName);
                    if (groupName.length() >= 2 && groupName.length() <= 10) {
                        createGroup(groupName);
                    } else {
                        WuhunToast.info("群名称不符合要求").show();
                    }
                    break;
                case R.id.img_clear_groupname:
                    createGroupname.setText("");
                    break;
            }
        }
    };



    /** 创建 */
    private void createGroup(String groupname) {
        if (picUri == null || picUri.getPath() == null) {
            WuhunToast.info("请上传群头像").show();
            return;
        }
//        TestLog.i("群名称：" + groupname + " - 头像：" + picUri.getPath() + " - 类型：" + groupType);
        HttpUtil.CreateGroup(groupname, picUri.getPath(), groupType, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.sendEmptyMessage(WuhunState.REQUEST_ERROR);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                TestLog.i("result: " + result);
                if (result != null && response.isSuccessful()) {
                    // TODO: 2018/1/9 跳转到群聊界面
                } else {
                    WuhunToast.info(R.string.request_error_net).show();
                }
            }
        });
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case WuhunState.REQUEST_ERROR:
                    WuhunToast.error(R.string.request_error).show();
                    break;
            }
        }
    };

    private void initView() {
        mContext = this;
        headBg.setImageResource(R.drawable.main_bg);
        bodyLeft.setImageResource(R.drawable.icon_back);
        bodyTvTitle.setText("创建群组");
        bodyRight.setVisibility(View.GONE);
        bodyOk.setVisibility(View.GONE);

//        cameraImageUri = WuhunCameraTool.getUri(mContext, cameraFile);
//        cropImageUri = WuhunCameraTool.getUri(mContext, CropFile);

        photoUtils = new PhotoUtils(new PhotoUtils.OnPhotoResultListener() {
            @Override
            public void onPhotoResult(Uri uri) {
                //成功监听
                picUri = uri;
                Glide.with(mContext).load(uri.getPath().toString())
                        .error(R.mipmap.default_useravatar)
                        .placeholder(R.mipmap.default_useravatar)
                        .diskCacheStrategy(DiskCacheStrategy.RESULT)
                        .skipMemoryCache(true)
                        .dontAnimate()
                        .transform(new CenterCrop(mContext), new GlideRoundTransform(mContext))
                        .into(imgGroupPortrait);
            }

            @Override
            public void onPhotoCancel() {}
        });
        //imgGroupPortrait   图片
        //createGroupname     名称
        //createOk      创建并跳转到对话界面
//        if(MyApplication.getSPUtil().get) {
//
//        }
        groupTypeList.add("普通群");
        groupTypeList.add("班级群");
        spinner_adapter = new ArrayAdapter<String>(this, R.layout.shearch_spinner_item, groupTypeList);
        spinnerStyle.setAdapter(spinner_adapter);

        int roletype = MyApplication.getSPUtil().getRoletype();
        TestLog.i("权限管理：" + roletype + "  =  " + (roletype == 5));
        if(roletype == Constant.TEACHER) {
            spinnerStyle.setVisibility(View.VISIBLE);
        }else if(roletype == Constant.STUDENT) {
            spinnerStyle.setVisibility(View.GONE);
        }
    }

    public void getSaveData() {
        Intent intent = getIntent();
        groupList = (List<Friend>) intent.getSerializableExtra(GROUP_FRIENDS);
// Friend{USERBUDDYID='21', NICKNAME='测试账号', STUDENTID='10', RCID='cafa21', AVATAR='null', NAME='李明珠', REMARKNAME='测试账号备注', REMARKMSG='钢甲卡卡龙'}
// Friend{USERBUDDYID='14', NICKNAME='小明', STUDENTID='4', RCID='cafa14', AVATAR='null', NAME='冀志英', REMARKNAME='小明', REMARKMSG='吼吼吼吼'}
    }

    private BottomMenuDialog dialog;
    /**
     * 弹出底部框
     */
    private void showPhotoDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }

        dialog = new BottomMenuDialog(mContext);
        dialog.setConfirmListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                if(WuhunCameraTool.takePicturePermisstion(mContext)) {
                    photoUtils.takePicture(GroupCreateActivity.this);
                }
            }
        });
        dialog.setMiddleListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                photoUtils.selectPicture(GroupCreateActivity.this);
            }
        });
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PhotoUtils.INTENT_CROP:
            case PhotoUtils.INTENT_TAKE:
            case PhotoUtils.INTENT_SELECT:
                photoUtils.onActivityResult(GroupCreateActivity.this, requestCode, resultCode, data);
                break;
        }
    }





}
