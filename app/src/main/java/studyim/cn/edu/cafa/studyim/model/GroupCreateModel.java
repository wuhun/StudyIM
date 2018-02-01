package studyim.cn.edu.cafa.studyim.model;

/**
 * ================================================
 * 作    者：悟魂(了解自己，感悟灵魂，做最好的自己)
 * 创建日期：2018/1/24 0024
 * 版    本：1.0
 * 描    述：
 * 修订历史：
 * ================================================
 */
public class GroupCreateModel {

    /**
     * msg :
     * result : {"groupName":"ajsjjd","groupImage":"uploadFiles/uploadImgs//IOS20180124/a339901ca0364b6497faa79ab3e91a3e.jpg","groupRCID":"group260"}
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

    public static class ResultBean {
        /**
         * groupName : ajsjjd
         * groupImage : uploadFiles/uploadImgs//IOS20180124/a339901ca0364b6497faa79ab3e91a3e.jpg
         * groupRCID : group260
         */

        private String groupName;
        private String groupImage;
        private String groupRCID;

        public String getGroupName() {
            return groupName;
        }

        public void setGroupName(String groupName) {
            this.groupName = groupName;
        }

        public String getGroupImage() {
            return groupImage;
        }

        public void setGroupImage(String groupImage) {
            this.groupImage = groupImage;
        }

        public String getGroupRCID() {
            return groupRCID;
        }

        public void setGroupRCID(String groupRCID) {
            this.groupRCID = groupRCID;
        }
    }
}
