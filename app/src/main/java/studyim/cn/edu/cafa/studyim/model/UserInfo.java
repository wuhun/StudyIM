package studyim.cn.edu.cafa.studyim.model;

import java.io.Serializable;

/**
 * ================================================
 * 作    者：悟魂(了解自己，感悟灵魂，做最好的自己)
 * 创建日期：2017/12/1 0001
 * 版    本：1.0
 * 描    述：
 * 修订历史：
 * ================================================
 */
public class UserInfo implements Serializable {
    /**
     * SCHOOL : 中央美术学院
     * NICKNAME : 小黑
     * MAJOR : 计算机
     * CODE : 2
     * SEX : M
     * USERID : 11
     * RCID : cafa11
     * CLASS : 白玫瑰
     * AVATAR : uploadFiles/uploadImgs//IOS20171116/4b7e3fb2ca7f40d18711f27c7dc7aa80.png
     * IRNAME : 未知
     * BIRTHDAY : 1484409600000
     * ISBUDDY : N
     * IUNAME : 李明珠
     *
     */

    private String SCHOOL; //学校
    private String NICKNAME;//昵称
    private String MAJOR;//专业
    private int CODE;//
    private String SEX;//性别
    private String USERID;//用户id
    private String RCID;//融云token
    private String CLASS;//班级
    private String AVATAR;//头像
    private String IRNAME;//
    private long BIRTHDAY;//
    private String ISBUDDY;//是否是好友
    private String IUNAME;//

    public String getSCHOOL() {
        return SCHOOL;
    }

    public void setSCHOOL(String SCHOOL) {
        this.SCHOOL = SCHOOL;
    }

    public String getNICKNAME() {
        return NICKNAME;
    }

    public void setNICKNAME(String NICKNAME) {
        this.NICKNAME = NICKNAME;
    }

    public String getMAJOR() {
        return MAJOR;
    }

    public void setMAJOR(String MAJOR) {
        this.MAJOR = MAJOR;
    }

    public int getCODE() {
        return CODE;
    }

    public void setCODE(int CODE) {
        this.CODE = CODE;
    }

    public String getSEX() {
        return SEX;
    }

    public void setSEX(String SEX) {
        this.SEX = SEX;
    }

    public String getUSERID() {
        return USERID;
    }

    public void setUSERID(String USERID) {
        this.USERID = USERID;
    }

    public String getRCID() {
        return RCID;
    }

    public void setRCID(String RCID) {
        this.RCID = RCID;
    }

    public String getCLASS() {
        return CLASS;
    }

    public void setCLASS(String CLASS) {
        this.CLASS = CLASS;
    }

    public String getAVATAR() {
        return AVATAR;
    }

    public void setAVATAR(String AVATAR) {
        this.AVATAR = AVATAR;
    }

    public String getIRNAME() {
        return IRNAME;
    }

    public void setIRNAME(String IRNAME) {
        this.IRNAME = IRNAME;
    }

    public long getBIRTHDAY() {
        return BIRTHDAY;
    }

    public void setBIRTHDAY(long BIRTHDAY) {
        this.BIRTHDAY = BIRTHDAY;
    }

    public String getISBUDDY() {
        return ISBUDDY;
    }

    public void setISBUDDY(String ISBUDDY) {
        this.ISBUDDY = ISBUDDY;
    }

    public String getIUNAME() {
        return IUNAME;
    }

    public void setIUNAME(String IUNAME) {
        this.IUNAME = IUNAME;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "SCHOOL='" + SCHOOL + '\'' +
                ", NICKNAME='" + NICKNAME + '\'' +
                ", MAJOR='" + MAJOR + '\'' +
                ", CODE=" + CODE +
                ", SEX='" + SEX + '\'' +
                ", USERID=" + USERID +
                ", RCID='" + RCID + '\'' +
                ", CLASS='" + CLASS + '\'' +
                ", AVATAR='" + AVATAR + '\'' +
                ", IRNAME='" + IRNAME + '\'' +
                ", BIRTHDAY=" + BIRTHDAY +
                ", ISBUDDY='" + ISBUDDY + '\'' +
                ", IUNAME='" + IUNAME + '\'' +
                '}';
    }
}
