package studyim.cn.edu.cafa.studyim.model;

import java.io.Serializable;

/**
 * ================================================
 * 作    者：悟魂(了解自己，感悟灵魂，做最好的自己)
 * 创建日期：2018/2/2 0002
 * 版    本：1.0
 * 描    述：
 * 修订历史：
 * ================================================
 */
public class CheckVersionModel implements Serializable {

    /**
     * msg :
     * result : {"versionCode":"2","versionName":"1.2.1","versionDesc":"\n更新内容： \t\t\t\n1、增加xxxxxxxxx功能 \t\t\t\n2、增加xxxxxxxxx显示！ \t\t\t\n3、用户界面优化！ \t\t\t\n4、处理了xxxxxxxxBUG！","downloadUrl":"","versionSize":"15M"}
     * code : 1
     * before :
     */
    private String msg;
    private ResultBean result;
    private int code;
    private String before;

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

    public String getBefore() {
        return before;
    }

    public void setBefore(String before) {
        this.before = before;
    }

    public static class ResultBean implements Serializable {
        /**
         * versionCode : 2
         * versionName : 1.2.1
         * versionDesc :
         更新内容：
         1、增加xxxxxxxxx功能
         2、增加xxxxxxxxx显示！
         3、用户界面优化！
         4、处理了xxxxxxxxBUG！
         * downloadUrl :
         * versionSize : 15M
         */

        private String versionCode;
        private String versionName;
        private String versionDesc;
        private String downloadUrl;
        private String versionSize;

        public String getVersionCode() {
            return versionCode;
        }

        public void setVersionCode(String versionCode) {
            this.versionCode = versionCode;
        }

        public String getVersionName() {
            return versionName;
        }

        public void setVersionName(String versionName) {
            this.versionName = versionName;
        }

        public String getVersionDesc() {
            return versionDesc;
        }

        public void setVersionDesc(String versionDesc) {
            this.versionDesc = versionDesc;
        }

        public String getDownloadUrl() {
            return downloadUrl;
        }

        public void setDownloadUrl(String downloadUrl) {
            this.downloadUrl = downloadUrl;
        }

        public String getVersionSize() {
            return versionSize;
        }

        public void setVersionSize(String versionSize) {
            this.versionSize = versionSize;
        }

        @Override
        public String toString() {
            return "ResultBean{" +
                    "versionCode='" + versionCode + '\'' +
                    ", versionName='" + versionName + '\'' +
                    ", versionDesc='" + versionDesc + '\'' +
                    ", downloadUrl='" + downloadUrl + '\'' +
                    ", versionSize='" + versionSize + '\'' +
                    '}';
        }
    }
}
