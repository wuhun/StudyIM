package studyim.cn.edu.cafa.studyim.base;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.view.View;

import java.io.File;

import studyim.cn.edu.cafa.studyim.ui.BottomMenuDialog;
import tools.com.lvliangliang.wuhuntools.app.WuhunAppTool;
import tools.com.lvliangliang.wuhuntools.permission.PermissionListener;
import tools.com.lvliangliang.wuhuntools.permission.PermissionsUtil;
import tools.com.lvliangliang.wuhuntools.util.WuhunFileTool;
import tools.com.lvliangliang.wuhuntools.widget.WuhunToast;

import static studyim.cn.edu.cafa.studyim.app.MyApplication.getContext;

/**
 * ================================================
 * 作    者：悟魂(了解自己，感悟灵魂，做最好的自己)
 * 创建日期：2018/1/25 0025
 * 版    本：1.0
 * 描    述：
 * 修订历史：
 * ================================================
 */
public class CameraActivity extends BaseActivity {

    private Context mContext;
    private Uri picUri;//拍照地址
    private File fileUri = new File(Environment.getExternalStorageDirectory().getPath() + File.separator + WuhunAppTool.getAppName(getContext()) + File.separator + "picture" + File.separator + "/photo.jpg");
    private Uri cropUri;//剪裁地址
    private File cropFile = new File(Environment.getExternalStorageDirectory().getPath() + File.separator + WuhunAppTool.getAppName(getContext()) + File.separator + "picture" + File.separator + "/crop_photo.jpg");
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
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
                    takePicture(CameraActivity.this, picUri, CODE_CAMERA_REQUEST);
                } else {
                    PermissionsUtil.requestPermission(mContext, new PermissionListener() {
                        @Override
                        public void permissionGranted(@NonNull String[] permission) {
                            //调用系统相机
                            picUri = buildUri();
                            takePicture(CameraActivity.this, picUri, CODE_CAMERA_REQUEST);
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
            picUri = FileProvider.getUriForFile(CameraActivity.this, "com.studyim.fileprovider", fileUri);
        }
        return picUri;
    }
    /**  构建uri */
    private Uri buildCropUri() {
        cropUri = Uri.fromFile(cropFile);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            cropUri = FileProvider.getUriForFile(CameraActivity.this, "com.studyim.fileprovider", cropFile);
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
        CameraActivity.this.startActivityForResult(cropIntent, CODE_CROP_RESULT);
    }

    private void selectPicture() {
        //每次选择图片吧之前的图片删除
        WuhunFileTool.deleteFileForUri(buildCropUri());

        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        CameraActivity.this.startActivityForResult(intent, CODE_SELECT_IMG);
    }
}
