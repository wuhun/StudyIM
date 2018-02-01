package studyim.cn.edu.cafa.studyim.model;

/**
 * ================================================
 * 作    者：悟魂(了解自己，感悟灵魂，做最好的自己)
 * 创建日期：2018/1/31 0031
 * 版    本：1.0
 * 描    述：
 * 修订历史：
 * ================================================
 */
public class PhoneCodeModel {

    /**
     * detail : 模板不存在/未审核/非SDK模板
     * httpStatus : 400
     * error : no valid template exist.
     * status : 206
     * description : 模板无效
     */
    private String detail;
    private int httpStatus;
    private String error;
    private int status;
    private String description;

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(int httpStatus) {
        this.httpStatus = httpStatus;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
