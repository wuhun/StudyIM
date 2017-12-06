package studyim.cn.edu.cafa.studyim.model;

import java.io.Serializable;
import java.util.List;

/**
 * ================================================
 * 作    者：悟魂(了解自己，感悟灵魂，做最好的自己)
 * 创建日期：2017/11/30 0030
 * 版    本：1.0
 * 描    述：
 * 修订历史：
 * ================================================
 */
public class FriendListModel implements Serializable {

    /**
     * result : [{"USERBUDDYID":14,"NICKNAME":"2","STUDENTID":"4","RCID":"cafa14"},{"USERBUDDYID":14,"NICKNAME":"2","STUDENTID":"4","RCID":"cafa14"},{"USERBUDDYID":15,"REMARKNAME":"小明（注备）","NICKNAME":"小明","STUDENTID":"5","RCID":"cafa15","AVATAR":"uploadFiles/uploadImgs//IOS20171129/f7721ffb582b4b86996ac7b63d2dd654.png","NAME":"小丽","REMARKMSG":"常用信息"}]
     * msg :
     * code : 1
     * before : http://124.127.38.214/
     */

    private String msg;
    private int code;
    private String before;
    private List<Friend> result;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getBefore() {
        return before;
    }

    public void setBefore(String before) {
        this.before = before;
    }

    public List<Friend> getResult() {
        return result;
    }

    public void setResult(List<Friend> result) {
        this.result = result;
    }

//    public static class ResultBean {
//        private int USERBUDDYID;
//        private String NICKNAME;
//        private String STUDENTID;
//        private String RCID;
//        private String REMARKNAME;
//        private String AVATAR;
//        private String NAME;
//        private String REMARKMSG;
//
//        public int getUSERBUDDYID() {
//            return USERBUDDYID;
//        }
//
//        public void setUSERBUDDYID(int USERBUDDYID) {
//            this.USERBUDDYID = USERBUDDYID;
//        }
//
//        public String getNICKNAME() {
//            return NICKNAME;
//        }
//
//        public void setNICKNAME(String NICKNAME) {
//            this.NICKNAME = NICKNAME;
//        }
//
//        public String getSTUDENTID() {
//            return STUDENTID;
//        }
//
//        public void setSTUDENTID(String STUDENTID) {
//            this.STUDENTID = STUDENTID;
//        }
//
//        public String getRCID() {
//            return RCID;
//        }
//
//        public void setRCID(String RCID) {
//            this.RCID = RCID;
//        }
//
//        public String getREMARKNAME() {
//            return REMARKNAME;
//        }
//
//        public void setREMARKNAME(String REMARKNAME) {
//            this.REMARKNAME = REMARKNAME;
//        }
//
//        public String getAVATAR() {
//            return AVATAR;
//        }
//
//        public void setAVATAR(String AVATAR) {
//            this.AVATAR = AVATAR;
//        }
//
//        public String getNAME() {
//            return NAME;
//        }
//
//        public void setNAME(String NAME) {
//            this.NAME = NAME;
//        }
//
//        public String getREMARKMSG() {
//            return REMARKMSG;
//        }
//
//        public void setREMARKMSG(String REMARKMSG) {
//            this.REMARKMSG = REMARKMSG;
//        }
//
//        @Override
//        public String toString() {
//            return "ResultBean{" +
//                    "USERBUDDYID=" + USERBUDDYID +
//                    ", NICKNAME='" + NICKNAME + '\'' +
//                    ", STUDENTID='" + STUDENTID + '\'' +
//                    ", RCID='" + RCID + '\'' +
//                    ", REMARKNAME='" + REMARKNAME + '\'' +
//                    ", AVATAR='" + AVATAR + '\'' +
//                    ", NAME='" + NAME + '\'' +
//                    ", REMARKMSG='" + REMARKMSG + '\'' +
//                    '}';
//        }
//    }
}
