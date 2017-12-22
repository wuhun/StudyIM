package studyim.cn.edu.cafa.studyim.util;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import studyim.cn.edu.cafa.studyim.R;
import studyim.cn.edu.cafa.studyim.model.Friend;
import studyim.cn.edu.cafa.studyim.model.FriendGetAddList;
import studyim.cn.edu.cafa.studyim.model.FriendGetInfoModel;
import studyim.cn.edu.cafa.studyim.model.UserInfo;
import tools.com.lvliangliang.wuhuntools.util.WuhunDataTool;

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
        if (userInfo.getAVATAR() != null) {
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
                Glide.with(context).load(context.getResources().getDrawable(R.mipmap.default_useravatar))
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
        if (friend.getAVATAR() != null) {
//            WuhunDebug.debug(beforeUrl + friend.getAVATAR());
            Glide.with(context).load(beforeUrl + friend.getAVATAR())
                    .error(R.mipmap.default_useravatar)
                    .placeholder(R.mipmap.default_useravatar)
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .skipMemoryCache(true)
                    .dontAnimate()
                    .centerCrop().into(imgAvatar);
        } else {
            if (friend.getNICKNAME() != null && friend.getUSERBUDDYID() + "" != null) {
                Glide.with(context).load(getAvatarUri(friend))
                        .error(R.mipmap.default_useravatar)
                        .placeholder(R.mipmap.default_useravatar)
                        .diskCacheStrategy(DiskCacheStrategy.RESULT)
                        .skipMemoryCache(true)
                        .dontAnimate()
                        .centerCrop().into(imgAvatar);
            } else {
                Glide.with(context).load(context.getResources().getDrawable(R.mipmap.default_useravatar))
                        .placeholder(R.mipmap.default_useravatar)
                        .diskCacheStrategy(DiskCacheStrategy.RESULT)
                        .skipMemoryCache(true)
                        .dontAnimate()
                        .centerCrop().into(imgAvatar);
            }
        }
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
        if (model.getAVATAR() != null) {
//            WuhunDebug.debug(beforeUrl + model.getAVATAR());
            Glide.with(context).load(beforeUrl + model.getAVATAR())
                    .error(R.mipmap.default_useravatar)
                    .placeholder(R.mipmap.default_useravatar)
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .skipMemoryCache(true)
                    .dontAnimate()
                    .centerCrop().into(imgAvatar);
        } else {
            if (model.getNICKNAME() != null && model.getUSERID() + "" != null) {
                Glide.with(context).load(getAvatarUri(model))
                        .error(R.mipmap.default_useravatar)
                        .placeholder(R.mipmap.default_useravatar)
                        .diskCacheStrategy(DiskCacheStrategy.RESULT)
                        .skipMemoryCache(true)
                        .dontAnimate()
                        .centerCrop().into(imgAvatar);
            } else {
                Glide.with(context).load(context.getResources().getDrawable(R.mipmap.default_useravatar))
                        .placeholder(R.mipmap.default_useravatar)
                        .diskCacheStrategy(DiskCacheStrategy.RESULT)
                        .skipMemoryCache(true)
                        .dontAnimate()
                        .centerCrop().into(imgAvatar);
            }
        }
    }

    public static String getAvatarUri(FriendGetAddList model) {
        String uri;
        if(model == null) return null;
        if (WuhunDataTool.isNullString(model.getAVATAR())) {
            uri =  RongGenerate.generateDefaultAvatar(model.getNICKNAME(), model.getUSERID()+"");
        } else {
            uri = model.getAVATAR();
        }
        return uri;
    }

    public static void showAvatar(Context context, FriendGetInfoModel.ResultBean model, String beforeUrl, ImageView imgAvatar) {
        if (model.getAvatar() != null) {
            String uri = null;
            if (model.getAvatar().startsWith("http://")) {
                uri = model.getAvatar();
            } else {
                uri = beforeUrl + model.getAvatar();
            }
            Glide.with(context).load(uri)
                    .error(R.mipmap.default_useravatar)
                    .placeholder(R.mipmap.default_useravatar)
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .skipMemoryCache(true)
                    .dontAnimate()
                    .centerCrop().into(imgAvatar);
        } else {
            if (model.getNickName() != null && model.getUserId() + "" != null) {
                Glide.with(context).load(getAvatarUri(model))
                        .error(R.mipmap.default_useravatar)
                        .placeholder(R.mipmap.default_useravatar)
                        .diskCacheStrategy(DiskCacheStrategy.RESULT)
                        .skipMemoryCache(true)
                        .dontAnimate()
                        .centerCrop().into(imgAvatar);
            } else {
                Glide.with(context).load(context.getResources().getDrawable(R.mipmap.default_useravatar))
                        .placeholder(R.mipmap.default_useravatar)
                        .diskCacheStrategy(DiskCacheStrategy.RESULT)
                        .skipMemoryCache(true)
                        .dontAnimate()
                        .centerCrop().into(imgAvatar);
            }
        }
    }

    public static String getAvatarUri(FriendGetInfoModel.ResultBean model) {
        String uri;
        if(model == null) return null;
        if (WuhunDataTool.isNullString(model.getAvatar())) {
            uri =  RongGenerate.generateDefaultAvatar(model.getNickName(), model.getUserId()+"");
        } else {
            uri = model.getAvatar();
        }
        return uri;
    }
}
