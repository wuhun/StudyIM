package studyim.cn.edu.cafa.studyim.model;

import java.io.Serializable;

/**
 * ================================================
 * 作    者：悟魂(了解自己，感悟灵魂，做最好的自己)
 * 创建日期：2017/12/6 0006
 * 版    本：1.0
 * 描    述：
 * 修订历史：
 * ================================================
 */
public class FriendGetAddList implements Serializable {

    /**
     * REMARKNAME : 啦啦
     * NICKNAME : 小黑
     * SEX : M
     * BUDDYTYPE : A
     * USERID : 11
     * STUDENTID : 1
     * RCID : cafa11
     * AVATAR : uploadFiles/uploadImgs//IOS20171116/4b7e3fb2ca7f40d18711f27c7dc7aa80.png
     * BUDDYSTS : P
     * SENDTYPE : 小红
     * NAME : 小丽
     */

    private String REMARKNAME; //注备名
    private String NICKNAME;//昵称
    private String SEX;//性别
    private String BUDDYTYPE;//好友类型
    private int USERID;//用户id
    private String STUDENTID;//学号
    private String RCID;//融云id
    private String AVATAR;//头像
    private String BUDDYSTS;//
    private String SENDTYPE;
    private String NAME;

    public String getREMARKNAME() {
        return REMARKNAME;
    }

    public void setREMARKNAME(String REMARKNAME) {
        this.REMARKNAME = REMARKNAME;
    }

    public String getNICKNAME() {
        return NICKNAME;
    }

    public void setNICKNAME(String NICKNAME) {
        this.NICKNAME = NICKNAME;
    }

    public String getSEX() {
        return SEX;
    }

    public void setSEX(String SEX) {
        this.SEX = SEX;
    }

    public String getBUDDYTYPE() {
        return BUDDYTYPE;
    }

    public void setBUDDYTYPE(String BUDDYTYPE) {
        this.BUDDYTYPE = BUDDYTYPE;
    }

    public int getUSERID() {
        return USERID;
    }

    public void setUSERID(int USERID) {
        this.USERID = USERID;
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

    public String getBUDDYSTS() {
        return BUDDYSTS;
    }

    public void setBUDDYSTS(String BUDDYSTS) {
        this.BUDDYSTS = BUDDYSTS;
    }

    public String getSENDTYPE() {
        return SENDTYPE;
    }

    public void setSENDTYPE(String SENDTYPE) {
        this.SENDTYPE = SENDTYPE;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    @Override
    public String toString() {
        return "FriendGetAddList{" +
                "REMARKNAME='" + REMARKNAME + '\'' +
                ", NICKNAME='" + NICKNAME + '\'' +
                ", SEX='" + SEX + '\'' +
                ", BUDDYTYPE='" + BUDDYTYPE + '\'' +
                ", USERID=" + USERID +
                ", STUDENTID='" + STUDENTID + '\'' +
                ", RCID='" + RCID + '\'' +
                ", AVATAR='" + AVATAR + '\'' +
                ", BUDDYSTS='" + BUDDYSTS + '\'' +
                ", SENDTYPE='" + SENDTYPE + '\'' +
                ", NAME='" + NAME + '\'' +
                '}';
    }
}
