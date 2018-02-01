package studyim.cn.edu.cafa.studyim.activity.other;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.rong.imkit.RongContext;
import io.rong.imkit.widget.AsyncImageView;
import io.rong.imlib.model.Conversation;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import studyim.cn.edu.cafa.studyim.R;
import studyim.cn.edu.cafa.studyim.app.MyApplication;
import studyim.cn.edu.cafa.studyim.base.BaseActivity;
import studyim.cn.edu.cafa.studyim.common.Constant;
import studyim.cn.edu.cafa.studyim.model.GroupCreateModel;
import studyim.cn.edu.cafa.studyim.ui.BottomMenuDialog;
import studyim.cn.edu.cafa.studyim.ui.GlideRoundTransform;
import studyim.cn.edu.cafa.studyim.util.HttpUtil;
import tools.com.lvliangliang.wuhuntools.app.WuhunAppTool;
import tools.com.lvliangliang.wuhuntools.exception.TestLog;
import tools.com.lvliangliang.wuhuntools.manager.BroadcastManager;
import tools.com.lvliangliang.wuhuntools.net.WuhunNetTools;
import tools.com.lvliangliang.wuhuntools.permission.PermissionListener;
import tools.com.lvliangliang.wuhuntools.permission.PermissionsUtil;
import tools.com.lvliangliang.wuhuntools.util.WuhunFileTool;
import tools.com.lvliangliang.wuhuntools.util.WuhunImgTool;
import tools.com.lvliangliang.wuhuntools.util.WuhunState;
import tools.com.lvliangliang.wuhuntools.widget.WuhunToast;

import static studyim.cn.edu.cafa.studyim.app.MyApplication.getContext;

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

    private String groupType = "P"; //创建的类型

//    public static final String GROUP_FRIENDS = "groupFriends";
//    private List<Friend> groupList = new ArrayList<>();
    private Context mContext;

    private Uri picUri;//拍照地址
    private File fileUri = new File(Environment.getExternalStorageDirectory().getPath() + File.separator + WuhunAppTool.getAppName(getContext()) + File.separator + "picture" + File.separator + "/photo.jpg");
    private Uri cropUri;//剪裁地址
    private File cropFile = new File(Environment.getExternalStorageDirectory().getPath() + File.separator + WuhunAppTool.getAppName(getContext()) + File.separator + "picture" + File.separator + "/crop_photo.jpg");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_create);
        ButterKnife.bind(this);

//        getSaveData();
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
                    groupType = "P";
                } else if (groupTypeList.get(position).equals("班级群")) {
                    groupType = "G";
                } else {
                    groupType = "P";
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
                        createGroup(groupName);//创建群组
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
        if (cropUri == null || cropUri.getPath() == null) {
            WuhunToast.info("请上传群头像").show();
            return;
        }
//        TestLog.i("群名称：" + groupname + " - 头像：" + picUri.getPath() + " - 类型：" + groupType);
        if(!WuhunNetTools.isAvailable(mContext)) {
            new AlertDialog.Builder(mContext)
                    .setMessage("当前无网络连接，请检查网络状态")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            GroupCreateActivity.this.finish();
                        }
                    }).show();
            return;
        }

        HttpUtil.CreateGroup(groupname, cropFile, groupType, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.sendEmptyMessage(WuhunState.REQUEST_ERROR);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                TestLog.i("result: " + result);
                if (result != null && response.isSuccessful()) {
                    GroupCreateModel model = MyApplication.getGson().fromJson(result, GroupCreateModel.class);
                    if(model.getCode() == 1) {
                        if(RongContext.getInstance() != null && model != null) {
                            // TODO: 2018/1/9 更新群列表信息
                            BroadcastManager.getInstance(getContext()).sendBroadcast(Constant.UPDATE_GROUP_LIST);
                            // 跳转到群聊界面
                            String rcid = model.getResult().getGroupRCID();
                            Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                                    .appendPath("conversation")
                                    .appendPath(Conversation.ConversationType.GROUP.getName().toLowerCase())
                                    .appendQueryParameter("targetId", rcid)
                                    .appendQueryParameter("title", model.getResult().getGroupName())
                                    .appendQueryParameter("groupId", rcid.substring(rcid.indexOf("group") + 5))
                                    .build();
                            startActivity(new Intent("android.intent.action.VIEW", uri));
                            GroupCreateActivity.this.finish();
                        }
                    }
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
//        Intent intent = getIntent();
//        groupList = (List<Friend>) intent.getSerializableExtra(GROUP_FRIENDS);
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
                if (PermissionsUtil.hasPermission(mContext, Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    picUri = buildUri();
                    takePicture(GroupCreateActivity.this, picUri, CODE_CAMERA_REQUEST);
                } else {
                    PermissionsUtil.requestPermission(mContext, new PermissionListener() {
                        @Override
                        public void permissionGranted(@NonNull String[] permission) {
                            //调用系统相机
                            picUri = buildUri();
                            takePicture(GroupCreateActivity.this, picUri, CODE_CAMERA_REQUEST);
                        }

                        @Override
                        public void permissionDenied(@NonNull String[] permission) {
                            WuhunToast.info("您拒绝了拍照权限").show();
                        }
                    }, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, false, null);
                }
            }
        });
        dialog.setMiddleListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                selectPicture();
            }
        });
        dialog.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        TestLog.i("数据回调:" + requestCode);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CODE_CAMERA_REQUEST://相机回调
                    if (new File(picUri.getPath()).exists()) {
                        cropUri = buildCropUri();
                        corp(picUri, cropUri);
                    }
                    break;
                case CODE_CROP_RESULT://剪裁
//                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), cropUri);
                        String pathForUri = WuhunImgTool.getPathForUri(mContext, cropUri);
                        TestLog.i("cropUri: " + pathForUri);
                        showImg(pathForUri);
                    break;
                case CODE_SELECT_IMG://相册
                    cropUri = buildCropUri();
                    if (data != null && data.getData() != null) {
                        String picturePath = WuhunImgTool.getPathForUri(mContext, data.getData());
//                        TestLog.i("picturePath： " + picturePath);
                        Uri path = Uri.parse(picturePath);
                        if (path != null) {
                            cropUri = buildCropUri();
                            corp(path, cropUri);
                        }
                    }
                    break;
            }
        }
    }

    private void showImg(String file) {
        Glide.clear(imgGroupPortrait);
        Glide.with(mContext).load(file)
                .error(R.mipmap.default_useravatar)
                .placeholder(R.mipmap.default_useravatar)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .dontAnimate()
                .transform(new CenterCrop(mContext), new GlideRoundTransform(mContext))
                .into(imgGroupPortrait);
    }

    /**  裁剪图片成功后返回  **/
    public static final int CODE_CROP_RESULT = 2;
    /** 选择图片 */
    public static final int CODE_SELECT_IMG = 3;
    /**  拍照成功后返回 */
    public static final int CODE_CAMERA_REQUEST = 4;

    /**  构建uri */
    private Uri buildUri() {
        picUri = Uri.fromFile(fileUri);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            picUri = FileProvider.getUriForFile(GroupCreateActivity.this, "com.studyim.fileprovider", fileUri);
        }
        return picUri;
    }
    /**  构建uri */
    private Uri buildCropUri() {
        cropUri = Uri.fromFile(cropFile);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            cropUri = FileProvider.getUriForFile(GroupCreateActivity.this, "com.studyim.fileprovider", cropFile);
        }
        return cropUri;
    }
    /**
     * 自动获取相机权限
     */
    public static void takePicture(Activity activity, Uri imageUri, int requestCode) {
        //调用系统相机
        Intent intentCamera = new Intent();
        intentCamera.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intentCamera.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件
        }
        //将拍照结果保存至photo_file的Uri中，不保留在相册中
        intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        activity.startActivityForResult(intentCamera, requestCode);
    }

    private void corp(Uri uri,Uri saveUri) {
        Intent cropIntent = new Intent("com.android.camera.action.CROP");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            cropIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        cropIntent.setDataAndType(uri, "image/*");
        cropIntent.putExtra("crop", "true");//发送裁剪信号
        cropIntent.putExtra("aspectX", 1);
        cropIntent.putExtra("aspectY", 1);
        cropIntent.putExtra("outputX", 200);
        cropIntent.putExtra("outputY", 200);
//        cropIntent.putExtra("scale", true);
        //1-false用uri返回图片
        //2-true直接用bitmap返回图片（此种只适用于小图片，返回图片过大会报错）
        cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, saveUri);//将剪切的图片保存到目标Uri中
        cropIntent.putExtra("return-data", false);
//        cropIntent.putExtra("noFaceDetection", true);
        cropIntent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        GroupCreateActivity.this.startActivityForResult(cropIntent, CODE_CROP_RESULT);
    }

    private void selectPicture() {
        //每次选择图片吧之前的图片删除
        WuhunFileTool.deleteFileForUri(buildCropUri());

        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        GroupCreateActivity.this.startActivityForResult(intent, CODE_SELECT_IMG);
    }
}
