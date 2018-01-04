package studyim.cn.edu.cafa.studyim.model;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * ================================================
 * 作    者：悟魂(了解自己，感悟灵魂，做最好的自己)
 * 创建日期：2018/1/4 0004
 * 版    本：1.0
 * 描    述：设置界面的数据备份
 * 修订历史：
 * ================================================
 */
public class SettingModel extends DataSupport implements Serializable {

    private String img;
    private int reserceId;
    private String title;
    private String url;

    public SettingModel(int reserceId, String title, String url) {
        this.reserceId = reserceId;
        this.title = title;
        this.url = url;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getReserceId() {
        return reserceId;
    }

    public void setReserceId(int reserceId) {
        this.reserceId = reserceId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "ResultBean{" +
                "img='" + img + '\'' +
                ", reserceId=" + reserceId +
                ", title='" + title + '\'' +
                ", url='" + url + '\'' +
                '}';
    }

}
