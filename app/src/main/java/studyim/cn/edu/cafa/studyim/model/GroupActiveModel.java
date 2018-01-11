package studyim.cn.edu.cafa.studyim.model;

import java.io.Serializable;
import java.util.List;

/**
 * ================================================
 * 作    者：悟魂(了解自己，感悟灵魂，做最好的自己)
 * 创建日期：2018/1/11 0011
 * 版    本：1.0
 * 描    述：
 * 修订历史：
 * ================================================
 */
public class GroupActiveModel implements Serializable {

    /**
     * result : [{"ISTOP":"N","GROUPID":216,"CREATE_DATE":1515389167000,"USERID":0,"SOURCE":"22111","TITLE":"1111","ID":51,"CONTENT_URL":"http://www.baidu.com","TYPE":"U"}]
     * msg :
     * code : 1
     * before : http://124.127.38.214/
     */
    private String msg;
    private int code;
    private String before;
    private List<ResultBean> result;

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

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * ISTOP : N
         * GROUPID : 216
         * CREATE_DATE : 1515389167000
         * USERID : 0
         * SOURCE : 22111
         * TITLE : 1111
         * ID : 51
         * CONTENT_URL : http://www.baidu.com
         * TYPE : U
         */

        private String ISTOP;
        private int GROUPID;
        private long CREATE_DATE;
        private int USERID;
        private String SOURCE;//来源
        private String TITLE;//标题
        private int ID;
        private String CONTENT_URL;//内容地址
        private String TYPE;// U  C
        private String IMAGE; //图片
        private String SUMMARY;//概要

        public String getISTOP() {
            return ISTOP;
        }

        public void setISTOP(String ISTOP) {
            this.ISTOP = ISTOP;
        }

        public int getGROUPID() {
            return GROUPID;
        }

        public void setGROUPID(int GROUPID) {
            this.GROUPID = GROUPID;
        }

        public long getCREATE_DATE() {
            return CREATE_DATE;
        }

        public void setCREATE_DATE(long CREATE_DATE) {
            this.CREATE_DATE = CREATE_DATE;
        }

        public int getUSERID() {
            return USERID;
        }

        public void setUSERID(int USERID) {
            this.USERID = USERID;
        }

        public String getSOURCE() {
            return SOURCE;
        }

        public void setSOURCE(String SOURCE) {
            this.SOURCE = SOURCE;
        }

        public String getTITLE() {
            return TITLE;
        }

        public void setTITLE(String TITLE) {
            this.TITLE = TITLE;
        }

        public int getID() {
            return ID;
        }

        public void setID(int ID) {
            this.ID = ID;
        }

        public String getCONTENT_URL() {
            return CONTENT_URL;
        }

        public void setCONTENT_URL(String CONTENT_URL) {
            this.CONTENT_URL = CONTENT_URL;
        }

        public String getTYPE() {
            return TYPE;
        }

        public void setTYPE(String TYPE) {
            this.TYPE = TYPE;
        }

        public String getIMAGE() {
            return IMAGE;
        }

        public void setIMAGE(String IMAGE) {
            this.IMAGE = IMAGE;
        }

        public String getSUMMARY() {
            return SUMMARY;
        }

        public void setSUMMARY(String SUMMARY) {
            this.SUMMARY = SUMMARY;
        }

        @Override
        public String toString() {
            return "ResultBean{" +
                    "ISTOP='" + ISTOP + '\'' +
                    ", GROUPID=" + GROUPID +
                    ", CREATE_DATE=" + CREATE_DATE +
                    ", USERID=" + USERID +
                    ", SOURCE='" + SOURCE + '\'' +
                    ", TITLE='" + TITLE + '\'' +
                    ", ID=" + ID +
                    ", CONTENT_URL='" + CONTENT_URL + '\'' +
                    ", TYPE='" + TYPE + '\'' +
                    ", IMAGE='" + IMAGE + '\'' +
                    ", SUMMARY='" + SUMMARY + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "GroupActiveModel{" +
                "msg='" + msg + '\'' +
                ", code=" + code +
                ", before='" + before + '\'' +
                ", result=" + result +
                '}';
    }
}
