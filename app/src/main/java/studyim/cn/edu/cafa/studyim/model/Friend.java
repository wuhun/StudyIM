package studyim.cn.edu.cafa.studyim.model;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

import tools.com.lvliangliang.wuhuntools.util.WuhunDataTool;
import tools.com.lvliangliang.wuhuntools.util.WuhunPingyinTool;

/**
 * @创建者 CSDN_LQR
 * @描述 朋友表(用户信息表)
 */
public class Friend extends DataSupport implements Comparable<Friend>, Serializable {
/////////////////////////////////////////////////////////////////////////////////
    private String USERBUDDYID;//好友id
    private String NICKNAME;//昵称 - 默认
    private String STUDENTID;//学号
    private String RCID;//融云id
    private String AVATAR;//头像
    private String NAME; //姓名
    private String REMARKNAME;//注备
    private String REMARKMSG;//注备信息

    public Friend(String USERBUDDYID, String NICKNAME, String STUDENTID, String RCID, String AVATAR, String NAME, String REMARKNAME, String REMARKMSG) {
        this.USERBUDDYID = USERBUDDYID;
        this.NICKNAME = NICKNAME;
        this.STUDENTID = STUDENTID;
        this.RCID = RCID;
        this.AVATAR = AVATAR;
        this.NAME = NAME;
        this.REMARKNAME = REMARKNAME;
        this.REMARKMSG = REMARKMSG;
    }

    public Friend(String USERBUDDYID, String AVATAR, String NAME, String NICKNAME, String REMARKNAME) {
        this.USERBUDDYID = USERBUDDYID;
        this.NICKNAME = NICKNAME;
        this.AVATAR = AVATAR;
        this.NAME = NAME;
        this.REMARKNAME = REMARKNAME;
    }

    public String getUSERBUDDYID() {
        return USERBUDDYID;
    }

    public void setUSERBUDDYID(String USERBUDDYID) {
        this.USERBUDDYID = USERBUDDYID;
    }

    public String getNICKNAME() {
        return NICKNAME;
    }

    public void setNICKNAME(String NICKNAME) {
        this.NICKNAME = NICKNAME;
    }

    public String getSTUDENTID() {
        return STUDENTID;
    }

    public void setSTUDENTID(String STUDENTID) {
        this.STUDENTID = STUDENTID;
    }

    public String getRCID() {
        return RCID;
    }

    public void setRCID(String RCID) {
        this.RCID = RCID;
    }

    public String getAVATAR() {
        return AVATAR;
    }

    public void setAVATAR(String AVATAR) {
        this.AVATAR = AVATAR;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public String getREMARKNAME() {
        if(WuhunDataTool.isNullString(this.REMARKNAME)) {
            REMARKNAME = getNICKNAME();
        }
        return REMARKNAME;
    }

    public void setREMARKNAME(String REMARKNAME) {
        this.REMARKNAME = REMARKNAME;
    }

    public String getREMARKMSG() {
        return REMARKMSG;
    }

    public void setREMARKMSG(String REMARKMSG) {
        this.REMARKMSG = REMARKMSG;
    }

    @Override
    public String toString() {
        return "Friend{" +
                "USERBUDDYID='" + USERBUDDYID + '\'' +
                ", NICKNAME='" + NICKNAME + '\'' +
                ", STUDENTID='" + STUDENTID + '\'' +
                ", RCID='" + RCID + '\'' +
                ", AVATAR='" + AVATAR + '\'' +
                ", NAME='" + NAME + '\'' +
                ", REMARKNAME='" + REMARKNAME + '\'' +
                ", REMARKMSG='" + REMARKMSG + '\'' +
                '}';
    }

    /** 注备名称： */
    public boolean isEnptyNoteName() {
        return !TextUtils.isEmpty(REMARKNAME);
    }
//
    @Override
    public boolean equals(Object o) {
        if (o != null) {
            Friend friendInfo = (Friend) o;
            return (getUSERBUDDYID() != null && getUSERBUDDYID().equals(friendInfo.getUSERBUDDYID()));
        } else {
            return false;
        }
    }

    //    @Override
    public int compareTo(@NonNull Friend friend) {
        if(null == getREMARKNAME()) {
            setREMARKNAME(getNICKNAME());
        }
        if(null == friend.getREMARKNAME()) {
            friend.setREMARKNAME(getNICKNAME());
        }
        return WuhunPingyinTool.getPinYinFirstCharIsLetter(this.getREMARKNAME())
                .compareTo(WuhunPingyinTool.getPinYinFirstCharIsLetter(friend.getREMARKNAME()));
//        return WuhunPingyinTool.getPinYinFirstCharIsLetter(this.getNoteName())
//                .compareTo(WuhunPingyinTool.getPinYinFirstCharIsLetter(friend.getNoteName()));
    }
}