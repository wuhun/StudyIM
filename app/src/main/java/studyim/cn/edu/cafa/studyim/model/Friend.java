package studyim.cn.edu.cafa.studyim.model;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import tools.com.lvliangliang.wuhuntools.util.WuhunPingyinTool;

/**
 * @创建者 CSDN_LQR
 * @描述 朋友表(用户信息表)
 */
public class Friend  implements Comparable<Friend> {
/////////////////////////////////////////////////////////////////////////////////
//        userId:用户id(string)，
//        RCID：融云id(string)，
//        avatar：头像(string)，
//        name：名字(string)，
//        nickname：昵称(string),
//        noteName：对应的备注(string),
/////////////////////////////////////////////////////////////////////////////////

    private String userId;
    private String RCID;
    private String avatar;
    private String name;
    private String nickname;
    private String noteName;

    public Friend(String userId, String avatar, String name, String nickname, String noteName) {
        this.userId = userId;
        this.avatar = avatar;
        this.name = name;
        this.nickname = nickname;
        this.noteName = noteName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRCID() {
        return RCID;
    }

    public void setRCID(String RCID) {
        this.RCID = RCID;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getNoteName() {
        return noteName;
    }

    public void setNoteName(String noteName) {
        this.noteName = noteName;
    }

    public boolean isEnptyNoteName() {
        return !TextUtils.isEmpty(noteName);
    }

    @Override
    public boolean equals(Object o) {
        if (o != null) {
            Friend friendInfo = (Friend) o;
            return (getUserId() != null && getUserId().equals(friendInfo.getUserId()));
//            return (getUserId() != null && getUserId().equals(friendInfo.getUserId()))
//                    && (getName() != null && getName().equals(friendInfo.getName()))
//                    && (getPortraitUri() != null && getPortraitUri().equals(friendInfo.getPortraitUri()))
//                    && (phoneNumber != null && phoneNumber.equals(friendInfo.getPhoneNumber()))
//                    && (displayName != null && displayName.equals(friendInfo.getDisplayName()));
        } else {
            return false;
        }
    }

//    @Override
//    public int compareTo(@NonNull Friend friend) {
//        return this.getNoteName().compareTo(friend.getNoteName());
//    }
    @Override
    public int compareTo(@NonNull Friend friend) {
//        return this.getDisplayName().compareTo(friend.getDisplayName());
        return WuhunPingyinTool.getPinYinFirstCharIsLetter(this.getNoteName())
                .compareTo(WuhunPingyinTool.getPinYinFirstCharIsLetter(friend.getNoteName()));
    }
}