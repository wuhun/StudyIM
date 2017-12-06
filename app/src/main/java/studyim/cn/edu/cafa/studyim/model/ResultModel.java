package studyim.cn.edu.cafa.studyim.model;

/**
 * ================================================
 * 作    者：悟魂(了解自己，感悟灵魂，做最好的自己)
 * 创建日期：2017/12/5 0005
 * 版    本：1.0
 * 描    述：
 * 修订历史：
 * ================================================
 */
public class ResultModel {

    private String msg;
    private int code;

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

    @Override
    public String toString() {
        return "ResultModel{" +
                "msg='" + msg + '\'' +
                ", code=" + code +
                '}';
    }
}
