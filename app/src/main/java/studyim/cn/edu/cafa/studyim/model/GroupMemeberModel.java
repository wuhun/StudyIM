package studyim.cn.edu.cafa.studyim.model;

import java.io.Serializable;

/**
 * ================================================
 * 作    者：悟魂(了解自己，感悟灵魂，做最好的自己)
 * 创建日期：2018/1/11 0011
 * 版    本：1.0
 * 描    述：
 * 修订历史：
 * ================================================
 */
public class GroupMemeberModel implements Serializable{

    /**
     * REMARKNAME : null
     * NICKNAME : 哈哈
     * GROUPID : 213
     * STS_TYPE : O
     * USERID : 16
     * STUDENTID : 7
     * USER_ID : 16
     * RCID : cafa16
     * AVATAR : uploadFiles/uploadImgs//IOS20171121/09711bddceb143ac883c785627922c4c.png
     * ISFRIEND : N
     * NAME : null
     * USER_BUDDY_ID : 14
     */

    private String REMARKNAME;
    private String NICKNAME;
    private int GROUPID;
    private String STS_TYPE;
    private int USERID;
    private String STUDENTID;
    private int USER_ID;
    private String RCID;
    private String AVATAR;
    private String ISFRIEND;
    private String NAME;
    private int USER_BUDDY_ID;

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

    public int getGROUPID() {
        return GROUPID;
    }

    public void setGROUPID(int GROUPID) {
        this.GROUPID = GROUPID;
    }

    public String getSTS_TYPE() {
        return STS_TYPE;
    }

    public void setSTS_TYPE(String STS_TYPE) {
        this.STS_TYPE = STS_TYPE;
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

    public int getUSER_ID() {
        return USER_ID;
    }

    public void setUSER_ID(int USER_ID) {
        this.USER_ID = USER_ID;
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

    public String getISFRIEND() {
        return ISFRIEND;
    }

    public void setISFRIEND(String ISFRIEND) {
        this.ISFRIEND = ISFRIEND;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public int getUSER_BUDDY_ID() {
        return USER_BUDDY_ID;
    }

    public void setUSER_BUDDY_ID(int USER_BUDDY_ID) {
        this.USER_BUDDY_ID = USER_BUDDY_ID;
    }

    @Override
    public String toString() {
        return "GroupMemeberModel{" +
                "REMARKNAME='" + REMARKNAME + '\'' +
                ", NICKNAME='" + NICKNAME + '\'' +
                ", GROUPID=" + GROUPID +
                ", STS_TYPE='" + STS_TYPE + '\'' +
                ", USERID=" + USERID +
                ", STUDENTID='" + STUDENTID + '\'' +
                ", USER_ID=" + USER_ID +
                ", RCID='" + RCID + '\'' +
                ", AVATAR='" + AVATAR + '\'' +
                ", ISFRIEND='" + ISFRIEND + '\'' +
                ", NAME='" + NAME + '\'' +
                ", USER_BUDDY_ID=" + USER_BUDDY_ID +
                '}';
    }
}
