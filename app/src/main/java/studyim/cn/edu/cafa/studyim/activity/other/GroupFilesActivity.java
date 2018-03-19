package studyim.cn.edu.cafa.studyim.activity.other;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import studyim.cn.edu.cafa.studyim.R;
import studyim.cn.edu.cafa.studyim.app.MyApplication;
import studyim.cn.edu.cafa.studyim.base.BaseActivity;
import studyim.cn.edu.cafa.studyim.common.Constant;
import studyim.cn.edu.cafa.studyim.model.BaseModel;
import studyim.cn.edu.cafa.studyim.model.GroupFile;
import studyim.cn.edu.cafa.studyim.model.ResultModel;
import studyim.cn.edu.cafa.studyim.util.DownloadUtil;
import studyim.cn.edu.cafa.studyim.util.HttpUtil;
import studyim.cn.edu.cafa.studyim.util.UserAvatarUtil;
import tools.com.lvliangliang.wuhuntools.adapter.LQRAdapterForRecyclerView;
import tools.com.lvliangliang.wuhuntools.adapter.LQRViewHolder;
import tools.com.lvliangliang.wuhuntools.adapter.LQRViewHolderForRecyclerView;
import tools.com.lvliangliang.wuhuntools.adapter.OnItemClickListener;
import tools.com.lvliangliang.wuhuntools.adapter.OnItemLongClickListener;
import tools.com.lvliangliang.wuhuntools.exception.TestLog;
import tools.com.lvliangliang.wuhuntools.net.WuhunNetTools;
import tools.com.lvliangliang.wuhuntools.permission.PermissionListener;
import tools.com.lvliangliang.wuhuntools.permission.PermissionsUtil;
import tools.com.lvliangliang.wuhuntools.util.WuhunDataTool;
import tools.com.lvliangliang.wuhuntools.util.WuhunFileTool;
import tools.com.lvliangliang.wuhuntools.util.WuhunImgTool;
import tools.com.lvliangliang.wuhuntools.util.WuhunOpenFileTool;
import tools.com.lvliangliang.wuhuntools.util.WuhunSizeTool;
import tools.com.lvliangliang.wuhuntools.util.WuhunThread;
import tools.com.lvliangliang.wuhuntools.widget.WuhunToast;
import tools.com.lvliangliang.wuhuntools.widget.recyclerview.WuhunRecyclerView;

public class GroupFilesActivity extends BaseActivity {

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
    @BindView(R.id.rl_head)
    RelativeLayout rlHead;
    @BindView(R.id.ll_sub_title)
    LinearLayout llSubTitle;
    @BindView(R.id.rv_group_file)
    WuhunRecyclerView rvGroupFile;
    @BindView(R.id.rl_tab_photo)
    RelativeLayout rlTabPhoto;
    @BindView(R.id.rl_tab_video)
    RelativeLayout rlTabVideo;
    @BindView(R.id.rl_tab_document)
    RelativeLayout rlTabDocument;
    @BindView(R.id.rl_tab_other)
    RelativeLayout rlTabOther;
    @BindView(R.id.tv_enpty_hint)
    TextView tvEnptyHint;

    private Context mContext;
    public static final String GROUP_ID = "groupId";
    public static final String GROUP_MASTER_ID = "group_master_id";
    public static final String GROUP_MANAGER_ID = "group_manager_id";
    private String groupid;
    private String groupMasterId;
    private String groupManagerId;

    private String[] fileTypes = {"photo", "video", "document", "other"};
    private int position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_files);
        ButterKnife.bind(this);

        getIntentData();
        initView();
        initListener();

        loadData();
    }

    private void initListener() {
        bodyImgMenu.setOnClickListener(mClickListener);
        rlTabPhoto.setOnClickListener(mClickListener);
        rlTabVideo.setOnClickListener(mClickListener);
        rlTabDocument.setOnClickListener(mClickListener);
        rlTabOther.setOnClickListener(mClickListener);
        bodySearch.setOnClickListener(mClickListener);
    }

    private View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.rl_tab_photo:
                    position = 0;
                    loadData();
                    break;
                case R.id.rl_tab_video:
                    position = 1;
                    loadData();
                    break;
                case R.id.rl_tab_document:
                    position = 2;
                    loadData();
                    break;
                case R.id.rl_tab_other:
                    position = 3;
                    loadData();
                    break;
                case R.id.body_img_menu:
                    GroupFilesActivity.this.finish();
                    break;
                case R.id.body_search:
                    showSelectDailog();
                    break;
            }
        }
    };

    private AlertDialog alertDialog1;
    final String[] uploadItem = {"上传图片", "上传视频", "上传文档", "上传其他"};
    final String[] uploadType = {"photo", "video", "document", "other"};
    String type = "other";

    private void showSelectDailog() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(mContext);
        alertBuilder.setItems(uploadItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int index) {
                type = uploadType[index];
                if (PermissionsUtil.hasPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("*/*");//设置类型，我这里是任意类型，任意后缀的可以这样写。
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    startActivityForResult(intent, 1);
                } else {
                    PermissionsUtil.requestPermission(mContext, new PermissionListener() {
                        @Override
                        public void permissionGranted(@NonNull String[] permission) {
                            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                            intent.setType("*/*");//设置类型，我这里是任意类型，任意后缀的可以这样写。
                            intent.addCategory(Intent.CATEGORY_OPENABLE);
                            startActivityForResult(intent, 1);
                        }

                        @Override
                        public void permissionDenied(@NonNull String[] permission) {
                            WuhunThread.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    WuhunToast.info("您拒绝了访问权限").show();
                                }
                            });
                        }
                    }, Manifest.permission.READ_EXTERNAL_STORAGE);
                }
                alertDialog1.dismiss();
            }
        });
        alertDialog1 = alertBuilder.create();
        alertDialog1.show();
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {
                Uri uri = data.getData();
                String path = WuhunFileTool.getPath2uri(GroupFilesActivity.this, uri);
//                byte[] bytes = WuhunFileTool.File2byte(path);
//                String fileStr = WuhunEncodeTool.base64Encode2String(bytes);
                final File file = new File(path);
                TestLog.i("==>" + path + " - " + file.getAbsolutePath());


                HttpUtil.GroupUploadFile(groupid, file, type, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        WuhunThread.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                WuhunToast.info(R.string.request_error_net).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        final String result = response.body().string();
                        if(response.isSuccessful() ) {
                            ResultModel model = MyApplication.getGson().fromJson(result, ResultModel.class);
                            if (model != null && model.getCode() == 1) {
                                WuhunThread.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        WuhunToast.info(file.getName() + "上传成功").show();
                                        loadData();
                                    }
                                });
                            }
                        }
                    }
                });
            }
        }
    }

    private void loadData() {
        if (!WuhunNetTools.isAvailable(mContext)) {
            showNoNetDialog(mContext);
            return;
        }
        setTabBackColor();
        if (groupid != null) {
            HttpUtil.getGroupFiles(groupid, fileTypes[position], new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    WuhunThread.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            WuhunToast.info(R.string.request_error_net).show();
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String result = response.body().string();
                    if(response.isSuccessful()) {
                        final BaseModel<GroupFile> model = MyApplication.getGson().fromJson(result, new TypeToken<BaseModel<GroupFile>>() {
                        }.getType());
                        before = model.getBefore();
                        WuhunThread.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (model != null) {
                                    mdata.clear();
                                    mdata = model.getResult();
                                    adapter.setData(mdata);
                                    if (mdata.size() > 0) {
                                        rvGroupFile.setVisibility(View.VISIBLE);
                                        tvEnptyHint.setVisibility(View.GONE);
                                    } else {
                                        rvGroupFile.setVisibility(View.GONE);
                                        tvEnptyHint.setVisibility(View.VISIBLE);
                                    }
                                }
                            }
                        });
                    }
                }
            });
        }
    }

    private void setTabBackColor() {
        rlTabPhoto.setBackgroundResource(R.drawable.study_tab_bottom_redline_selector);
        rlTabVideo.setBackgroundResource(R.drawable.study_tab_bottom_redline_selector);
        rlTabDocument.setBackgroundResource(R.drawable.study_tab_bottom_redline_selector);
        rlTabOther.setBackgroundResource(R.drawable.study_tab_bottom_redline_selector);
        if (position == 0) {
            rlTabPhoto.setBackgroundResource(R.drawable.study_tab_hover);
        } else if (position == 1) {
            rlTabVideo.setBackgroundResource(R.drawable.study_tab_hover);
        } else if (position == 2) {
            rlTabDocument.setBackgroundResource(R.drawable.study_tab_hover);
        } else if (position == 3) {
            rlTabOther.setBackgroundResource(R.drawable.study_tab_hover);
        }
    }

    private void getIntentData() {
        Intent intent = getIntent();
        groupid = intent.getStringExtra(GROUP_ID);
        groupMasterId = intent.getStringExtra(GROUP_MASTER_ID);
        groupManagerId = intent.getStringExtra(GROUP_MANAGER_ID);
    }

    LQRAdapterForRecyclerView<GroupFile> adapter;
    List<GroupFile> mdata = new ArrayList<>();
    String before;

    private void initView() {
        mContext = this;
        headBg.setImageResource(R.drawable.main_bg);
        bodyImgMenu.setImageResource(R.drawable.icon_back);
        bodyTvTitle.setText("群文件列表");
//        bodySearch.setVisibility(View.GONE);
        bodySearch.setImageResource(R.drawable.icon_upload);
        bodyOk.setVisibility(View.GONE);

        adapter = new LQRAdapterForRecyclerView<GroupFile>(mContext, mdata, R.layout.group_file_item) {
            @Override
            public void convert(LQRViewHolderForRecyclerView helper, GroupFile item, int position) {
                final GroupFile groupFile = mdata.get(position);
                ImageView imgThumb = (ImageView) helper.getView(R.id.img_thumb);
                final Button btnDownload = (Button) helper.getView(R.id.btn_download_file);
                final ProgressBar pb_download = (ProgressBar) helper.getView(R.id.pb_download);

                if (!WuhunDataTool.isNullString(groupFile.getTHUMBNAILPATH())) {
                    String uri = UserAvatarUtil.initUri(before, groupFile.getTHUMBNAILPATH());
                    if (WuhunImgTool.isImage(uri))
                        UserAvatarUtil.showImage(mContext, uri, imgThumb);
                    imgThumb.setVisibility(View.VISIBLE);
                } else {
                    imgThumb.setVisibility(View.GONE);
                }

                helper.setText(R.id.tv_file_name, groupFile.getFILENAME());
                helper.setText(R.id.tv_filesize, WuhunSizeTool.bit2Gb(groupFile.getFILESIZE()));

                final File downloadFile = MyApplication.getDownloadFile(groupFile.getFILENAME());

                if (downloadFile.exists()) {
                    btnDownload.setText("成功");
                } else {
                    btnDownload.setText("下载");
                }

                btnDownload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO: 2018/1/21 0021
                        String uri = initDownUri(before, groupFile.getFILEPATH());
//                        String uri = "http://192.168.1.24:8080/test/568d3921eeb149f39bbd901d5d07e6dc.docx";
//                        TestLog.i("下载地址：" + uri);
//                        TestLog.i("下载地址：" + downloadFile.toString());
                        if (!WuhunDataTool.isNullString(groupFile.getFILENAME())) {
                            DownloadUtil.getInstence().download(
                                    uri,
                                    downloadFile.toString(),
                                    new DownloadUtil.OnDownloadListener() {
                                        @Override
                                        public void onDownloadSuccess() {
                                            WuhunThread.runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    pb_download.setVisibility(View.INVISIBLE);
                                                    btnDownload.setText("成功");
                                                }
                                            });
                                        }

                                        @Override
                                        public void onDownloading(final int progress) {
                                            WuhunThread.runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    pb_download.setVisibility(View.VISIBLE);
                                                    pb_download.setProgress(progress);
                                                    btnDownload.setClickable(false);
                                                    btnDownload.setText("下载中...");
                                                }
                                            });
                                        }

                                        @Override
                                        public void onDownloadFailed() {
                                            WuhunThread.runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    btnDownload.setText("失败");
                                                    if (downloadFile.exists()) {
                                                        downloadFile.delete();
                                                    }
                                                }
                                            });
                                        }
                                    });
                        } else {
                            WuhunToast.info("文件名错误").show();
                        }
                    }
                });
            }
        };
        rvGroupFile.setAdapter(adapter);

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(LQRViewHolder helper, ViewGroup parent, View itemView, int position) {
                GroupFile groupFile = mdata.get(position);
                final File downloadFile = MyApplication.getDownloadFile(groupFile.getFILENAME());

                if (downloadFile.exists() && downloadFile.isFile()) {
                    WuhunOpenFileTool.getInstence(mContext).openFiles(downloadFile.getAbsolutePath());
                }
            }
        });

        TestLog.i("userid: " + MyApplication.getSPUtil().getUSERID()
                + "\n 群主:" + groupMasterId
                + "\n 管理员:" + groupManagerId);

        if (MyApplication.getSPUtil().getUSERID().equals(groupMasterId)
                || MyApplication.getSPUtil().getUSERID().equals(groupManagerId)) {
            adapter.setOnItemLongClickListener(new OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(LQRViewHolder helper, ViewGroup parent, View itemView, int position) {
                    final GroupFile groupFile = mdata.get(position);
                    new AlertDialog.Builder(mContext)
                            .setMessage("是否删除当前文件？")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    HttpUtil.groupfiledelete(groupFile.getCLASS(), String.valueOf(groupFile.getFILEID()), "true", new Callback() {
                                        @Override
                                        public void onFailure(Call call, IOException e) {
                                            WuhunThread.runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    WuhunToast.info(R.string.request_error).show();
                                                }
                                            });
                                        }

                                        @Override
                                        public void onResponse(Call call, final Response response) throws IOException {
                                            String result = response.body().string();
                                            if (response.isSuccessful()) {
                                                final ResultModel resultModel = MyApplication.getGson().fromJson(result, ResultModel.class);
                                                WuhunThread.runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        if (response.isSuccessful() && resultModel.getCode() == 1) {
                                                            WuhunToast.info("删除成功").show();
                                                            loadData();
                                                        } else {
                                                            if (!WuhunDataTool.isNullString(resultModel.getMsg())) {
                                                                WuhunToast.info(resultModel.getMsg()).show();
                                                            }
                                                        }
                                                    }
                                                });
                                            }
                                        }
                                    });
                                }
                            }).setNegativeButton("取消", null).show();
                    return false;
                }
            });
        }
    }

    public static String initDownUri(String beforeUrl, String uri) {
        String head = WuhunDataTool.isNullString(beforeUrl) ? Constant.HOME_URL : beforeUrl;
        if (!WuhunDataTool.isNullString(uri)) {
            if (uri.startsWith("http://")) {
                return uri;
            } else {
                return head + uri;
            }
        } else {
            return "null";
        }
    }

}
