package studyim.cn.edu.cafa.studyim.model;

import java.io.Serializable;
import java.util.List;

/**
 * 作者：悟魂 ————2017/11/18.
 * 版本：1.0
 * 说明：
 *      我的界面设置model
 */

public class SettingBaseModel implements Serializable {

    /**
     * version : 1
     * result : [{"img":"http://10.10.10.107:8080/test/sideicon.png","reserceId":0,"title":"我的","url":"http://www.baidu.com"},{"img":"http://10.10.10.107:8080/test/sideicon.png","reserceId":0,"title":"朋友","url":"http://www.baidu.com"},{"img":"http://10.10.10.107:8080/test/sideicon.png","reserceId":0,"title":"修改密码","url":"http://www.baidu.com"},{"img":"http://10.10.10.107:8080/test/sideicon.png","reserceId":0,"title":"设置","url":"http://www.baidu.com"}]
     */

    private int version;
    private List<SettingModel> result;

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public List<SettingModel> getResult() {
        return result;
    }

    public void setResult(List<SettingModel> result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "SettingBaseModel{" +
                "version=" + version +
                ", result=" + result +
                '}';
    }
}
