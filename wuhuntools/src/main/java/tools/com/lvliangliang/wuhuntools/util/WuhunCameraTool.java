package tools.com.lvliangliang.wuhuntools.util;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;

import java.io.File;

import tools.com.lvliangliang.wuhuntools.permission.PermissionListener;
import tools.com.lvliangliang.wuhuntools.permission.PermissionsUtil;
import tools.com.lvliangliang.wuhuntools.widget.WuhunToast;

/**
 * ================================================
 * 作    者：悟魂(了解自己，感悟灵魂，做最好的自己)
 * 创建日期：2018/1/8 0008
 * 版    本：1.0
 * 描    述：相机相关
 * 修订历史：
 * ================================================
 */
public class WuhunCameraTool {

    private static boolean isPermissation = false; //权限是否成功

    public static final int CODE_CAMERA_RESULT_SUCCESS = 0x01;
    public static final int CODE_CROP_RESULT_REQUEST = 0x02;
//    private static final int CODE_GALLERY_REQUEST = 0x00;
//    private static final int CODE_CAMERA_REQUEST = 0x01;
//    private static final int CODE_RESULT_REQUEST = 0x02;
//    private static final int CAMERA_PERMISSIONS_REQUEST_CODE = 0x03;
//    private static final int STORAGE_PERMISSIONS_REQUEST_CODE = 0x04;

    /** 申请权限拍照 */
    public static boolean takePicturePermisstion(Context mContext) {
        boolean takePicPermission = PermissionsUtil.hasPermission(mContext, Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (takePicPermission) {
            isPermissation = true;
        } else {
            PermissionsUtil.requestPermission(mContext, new PermissionListener() {
                @Override
                public void permissionGranted(@NonNull String[] permission) {
                    isPermissation = true;
                }

                @Override
                public void permissionDenied(@NonNull String[] permission) {
                    WuhunToast.info("您拒绝了拍照权限").show();
                    isPermissation = false;
                }
            }, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE});
        }
        return isPermissation;
    }

    /**
     * 拍照
     * @param activity      回调界面
     * @param imageUri      保存uri
     * @param requestCode   返回结果码
     */
    public static void toCamera(Activity activity, Uri imageUri, int requestCode) {
        Intent intentCamera = new Intent();//调用系统相机
        intentCamera.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);//将拍照结果保存至photo_file的Uri中，不保留在相册中
        activity.startActivityForResult(intentCamera, requestCode);
    }
    /**
     * 拍照
     * @param fragment  回调界面
     * @param imageUri    保存uri
     * @param requestCode   返回结果码
     */
    public static void toCamera(Fragment fragment, Uri imageUri, int requestCode) {
        Intent intentCamera = new Intent();//调用系统相机
        intentCamera.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);//将拍照结果保存至photo_file的Uri中，不保留在相册中
        fragment.startActivityForResult(intentCamera, requestCode);
    }

    /** 获取拍照目录uri */
    public static Uri getUri(Context context, File file) {
        Uri uri = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) { // >24  7.0
            uri = FileProvider.getUriForFile(context, "com.wuhuntools.fileprovider", file);
        } else {
            uri = Uri.fromFile(file);
        }
        return uri;
    }


    /**
     * @param activity    当前activity
     * @param orgUri      剪裁原图的Uri
     * @param desUri      剪裁后的图片的Uri
     * @param aspectX     X方向的比例
     * @param aspectY     Y方向的比例
     * @param width       剪裁图片的宽度
     * @param height      剪裁图片高度
     * @param requestCode 剪裁图片的请求码
     */
    public static void cropImageUri(Activity activity, Uri orgUri, Uri desUri, int aspectX, int aspectY, int width, int height, int requestCode) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        intent.setDataAndType(orgUri, "image/*");
        //发送裁剪信号
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", aspectX);
        intent.putExtra("aspectY", aspectY);
        intent.putExtra("outputX", width);
        intent.putExtra("outputY", height);
        intent.putExtra("scale", true);
        //将剪切的图片保存到目标Uri中
        intent.putExtra(MediaStore.EXTRA_OUTPUT, desUri);
        //1-false用uri返回图片
        //2-true直接用bitmap返回图片（此种只适用于小图片，返回图片过大会报错）
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 读取uri所在的图片
     *
     * @param uri      图片对应的Uri
     * @param mContext 上下文对象
     * @return 获取图像的Bitmap
     */
    public static Bitmap getBitmapFromUri(Uri uri, Context mContext) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), uri);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 删除文件
     * @param uri
     * @return
     */
//    public boolean clearCropFile(Uri uri) {
//        if (uri == null) {
//            return false;
//        }
//
//        File file = new File(uri.getPath());
//        if (file.exists()) {
//            boolean result = file.delete();
//            if (result) {
//
//            } else {
//                android.util.Log.e(tag, "Failed to clear cached crop file.");
//            }
//            return result;
//        } else {
//            android.util.Log.w(tag, "Trying to clear cached crop file but it does not exist.");
//        }
//
//        return false;
//    }

}
