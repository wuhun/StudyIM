package studyim.cn.edu.cafa.studyim.model;

import java.io.Serializable;

/**
 * ================================================
 * 作    者：悟魂(了解自己，感悟灵魂，做最好的自己)
 * 创建日期：2018/1/12 0012
 * 版    本：1.0
 * 描    述：
 * 修订历史：
 * ================================================
 */
public class GroupInfoModel implements Serializable {
    /**
     * msg :
     * result : {"GROUPID":205,"GROUPMANAGERID":11,"GROUPMASTERID":0,"GROUPNAME":"一个群","GROUPMANAGER":"小黑","GROUPMSG":"时所得到的","ISSHIELDED":"N","GROUPIMAGE":"http://124.127.38.214/-1","GROUPMASTER":"超级管理员"}
     * code : 1
     */

    private String msg;
    private ResultBean result;
    private int code;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public static class ResultBean implements Serializable {
        /**
         * GROUPID : 205
         * GROUPMANAGERID : 11
         * GROUPMASTERID : 0
         * GROUPNAME : 一个群
         * GROUPMANAGER : 小黑
         * GROUPMSG : 时所得到的
         * ISSHIELDED : N
         * GROUPIMAGE : http://124.127.38.214/-1
         * GROUPMASTER : 超级管理员
         */

        private int GROUPID;
        private int GROUPMANAGERID; //管理员id
        private int GROUPMASTERID; // 群主id
        private String GROUPNAME;
        private String GROUPMANAGER;
        private String GROUPMSG;
        private String ISSHIELDED;
        private String GROUPIMAGE;
        private String GROUPMASTER;

        public int getGROUPID() {
            return GROUPID;
        }

        public void setGROUPID(int GROUPID) {
            this.GROUPID = GROUPID;
        }

        public int getGROUPMANAGERID() {
            return GROUPMANAGERID;
        }

        public void setGROUPMANAGERID(int GROUPMANAGERID) {
            this.GROUPMANAGERID = GROUPMANAGERID;
        }

        public int getGROUPMASTERID() {
            return GROUPMASTERID;
        }

        public void setGROUPMASTERID(int GROUPMASTERID) {
            this.GROUPMASTERID = GROUPMASTERID;
        }

        public String getGROUPNAME() {
            return GROUPNAME;
        }

        public void setGROUPNAME(String GROUPNAME) {
            this.GROUPNAME = GROUPNAME;
        }

        public String getGROUPMANAGER() {
            return GROUPMANAGER;
        }

        public void setGROUPMANAGER(String GROUPMANAGER) {
            this.GROUPMANAGER = GROUPMANAGER;
        }

        public String getGROUPMSG() {
            return GROUPMSG;
        }

        public void setGROUPMSG(String GROUPMSG) {
            this.GROUPMSG = GROUPMSG;
        }

        public String getISSHIELDED() {
            return ISSHIELDED;
        }

        public void setISSHIELDED(String ISSHIELDED) {
            this.ISSHIELDED = ISSHIELDED;
        }

        public String getGROUPIMAGE() {
            return GROUPIMAGE;
        }

        public void setGROUPIMAGE(String GROUPIMAGE) {
            this.GROUPIMAGE = GROUPIMAGE;
        }

        public String getGROUPMASTER() {
            return GROUPMASTER;
        }

        public void setGROUPMASTER(String GROUPMASTER) {
            this.GROUPMASTER = GROUPMASTER;
        }
    }
}
