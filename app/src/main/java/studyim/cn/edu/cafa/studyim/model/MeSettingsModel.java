package studyim.cn.edu.cafa.studyim.model;

import android.support.annotation.DrawableRes;

/**
 * 作者：悟魂 ————2017/11/18.
 * 版本：1.0
 * 说明：
 *      我的界面设置model
 */

public class MeSettingsModel {

    private String img;
    @DrawableRes
    private int reserceId;
    private String title;
    private String url;

    public MeSettingsModel() {
    }

    public MeSettingsModel(String img, String title, String url) {
        this.img = img;
        this.title = title;
        this.url = url;
    }

    public MeSettingsModel(int reserceId, String title, String url) {
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

    public int getReserceId() {
        return reserceId;
    }

    public void setReserceId(int reserceId) {
        this.reserceId = reserceId;
    }

    @Override
    public String toString() {
        return "MeSettingsModel{" +
                "img='" + img + '\'' +
                ", reserceId=" + reserceId +
                ", title='" + title + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
