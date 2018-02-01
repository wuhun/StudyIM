package studyim.cn.edu.cafa.studyim.util;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;

import studyim.cn.edu.cafa.studyim.R;
import studyim.cn.edu.cafa.studyim.app.MyApplication;
import studyim.cn.edu.cafa.studyim.common.Constant;
import studyim.cn.edu.cafa.studyim.model.Friend;
import studyim.cn.edu.cafa.studyim.model.FriendGetAddList;
import studyim.cn.edu.cafa.studyim.model.FriendUserInfo;
import studyim.cn.edu.cafa.studyim.model.GroupModel;
import studyim.cn.edu.cafa.studyim.model.UserInfo;
import studyim.cn.edu.cafa.studyim.ui.GlideRoundTransform;
import tools.com.lvliangliang.wuhuntools.util.WuhunDataTool;
import tools.com.lvliangliang.wuhuntools.util.WuhunImgTool;

/**
 * ================================================
 * 作    者：悟魂(了解自己，感悟灵魂，做最好的自己)
 * 创建日期：2017/12/4 0004
 * 版    本：1.0
 * 描    述：获取用户头像
 * 修订历史：
 * ================================================
 */
public class UserAvatarUtil {

    public static void showAvatar(Context context, UserInfo userInfo, String beforeUrl, ImageView imgAvatar) {
        if (userInfo != null && userInfo.getAVATAR() != null) {
//            WuhunDebug.debug(beforeUrl + userInfo.getAVATAR());
            Glide.with(context).load(beforeUrl + userInfo.getAVATAR())
                    .error(R.mipmap.default_useravatar)
                    .placeholder(R.mipmap.default_useravatar)
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .skipMemoryCache(true)
                    .dontAnimate()
                    .centerCrop().into(imgAvatar);
        } else {
            if (userInfo.getNICKNAME() != null && userInfo.getUSERID() + "" != null) {
                Glide.with(context).load(getAvatarUri(userInfo))
                        .error(R.mipmap.default_useravatar)
                        .placeholder(R.mipmap.default_useravatar)
                        .diskCacheStrategy(DiskCacheStrategy.RESULT)
                        .skipMemoryCache(true)
                        .dontAnimate()
                        .centerCrop().into(imgAvatar);
            } else {
                Glide.with(context).load(R.mipmap.default_useravatar)
                        .placeholder(R.mipmap.default_useravatar)
                        .diskCacheStrategy(DiskCacheStrategy.RESULT)
                        .skipMemoryCache(true)
                        .dontAnimate()
                        .centerCrop().into(imgAvatar);
            }
        }
    }

    public static String getAvatarUri(UserInfo userInfo) {
        String uri;
        if(userInfo == null) return null;
        if (WuhunDataTool.isNullString(userInfo.getAVATAR())) {
            uri =  RongGenerate.generateDefaultAvatar(userInfo.getNICKNAME(), userInfo.getUSERID()+"");
        } else {
            uri = userInfo.getAVATAR();
        }
        return uri;
    }

    public static void showAvatar(Context context, Friend friend, String beforeUrl, ImageView imgAvatar) {
        String before = WuhunDataTool.isNullString(beforeUrl) ? Constant.HOME_URL : beforeUrl;
        String uri = initUri(before, friend.getAVATAR());
        //wuhuntest
        String avatarUri = getAvatarUri(
                friend.getUSERBUDDYID(),
                WuhunDataTool.isNullString(friend.getREMARKNAME()) ? friend.getNICKNAME() : friend.getREMARKNAME(),
                uri);
        Glide.with(context).load(avatarUri)
                .error(R.mipmap.default_useravatar)
                .placeholder(R.mipmap.default_useravatar)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .skipMemoryCache(true)
                .dontAnimate()
                .transform(new CenterCrop(context), new GlideRoundTransform(context))
                .into(imgAvatar);
    }

    public static String getAvatarUri(Friend friend) {
        String uri;
        if(friend == null) return null;
        if (WuhunDataTool.isNullString(friend.getAVATAR())) {
            uri =  RongGenerate.generateDefaultAvatar(friend.getNICKNAME(), friend.getUSERBUDDYID()+"");
        } else {
            uri = friend.getAVATAR();
        }
        return uri;
    }

    public static void showAvatar(Context context, FriendGetAddList model, String beforeUrl, ImageView imgAvatar) {
        String before = WuhunDataTool.isNullString(beforeUrl) ? Constant.HOME_URL : beforeUrl;
        String uri = initUri(before, model.getAVATAR());
        //wuhuntest
        String avatarUri = getAvatarUri(
                model.getUSERID()+"",
                WuhunDataTool.isNullString(model.getREMARKNAME()) ? model.getNICKNAME() : model.getREMARKNAME(),
                uri);
        Glide.with(context).load(avatarUri)
                .error(R.mipmap.default_useravatar)
                .placeholder(R.mipmap.default_useravatar)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .skipMemoryCache(true)
                .dontAnimate()
                .centerCrop()
                .into(imgAvatar);
    }


    public static void showAvatar(Context context, FriendUserInfo model, String beforeUrl, ImageView imgAvatar) {
        String uri = initUri(beforeUrl,model.getAvatar());
        String avatarUri = getAvatarUri(
                model.getUserId() + "",
                WuhunDataTool.isNullString(model.getRemarkName()) ? model.getNickName() : model.getRemarkName(),
                uri);
        Glide.with(context).load(avatarUri)
                .error(R.mipmap.default_useravatar)
                .placeholder(R.mipmap.default_useravatar)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .skipMemoryCache(true)
                .dontAnimate()
                .transform(new CenterCrop(context), new GlideRoundTransform(context))
                .into(imgAvatar);
    }

    public static void showAvatar(Context context, GroupModel model, String beforeUrl, ImageView imgAvatar) {
        String uri = initUri(beforeUrl,model.getGROUPIMAGE());
        String avatarUri = getAvatarUri(
                model.getGROUPRCID() + "",
                model.getNAME(),
                uri);
        Glide.with(context).load(avatarUri)
                .error(R.mipmap.default_useravatar)
                .placeholder(R.mipmap.default_useravatar)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .skipMemoryCache(true)
                .dontAnimate()
                .transform(new CenterCrop(context), new GlideRoundTransform(context))
                .into(imgAvatar);
    }

    public static String initUri(String beforeUrl, String uri){
        String head = WuhunDataTool.isNullString(beforeUrl) ? Constant.HOME_URL : beforeUrl;
        if (!WuhunDataTool.isNullString(uri)) {
            if (WuhunImgTool.isImage(uri) && uri.startsWith("http://")) {
                return uri;
            } else {
                return head + uri;
            }
        }else{
            return Constant.HOME_URL + "null";
        }
    }

    public static String getAvatarUri(String userid, String name,String avaterUri) {
        String uri = null;
        if (WuhunDataTool.isNullString(avaterUri) || !WuhunImgTool.isImage(avaterUri)) {
            uri =  RongGenerate.generateDefaultAvatar(name, userid);
        } else {
            uri = avaterUri;
        }
        return uri;
    }

    public static void showImage(Context context, String uri, ImageView imageView){
        Glide.with(MyApplication.getContext()).load(uri)
                .error(R.mipmap.default_useravatar)
                .placeholder(R.mipmap.default_useravatar)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .skipMemoryCache(true)
                .dontAnimate()
                .centerCrop()
                .into(imageView);
    }
}
