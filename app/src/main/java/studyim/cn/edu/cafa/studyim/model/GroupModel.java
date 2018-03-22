package studyim.cn.edu.cafa.studyim.model;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * ================================================
 * 作    者：悟魂(了解自己，感悟灵魂，做最好的自己)
 * 创建日期：2018/1/10 0010
 * 版    本：1.0
 * 描    述：
 * 修订历史：
 * ================================================
 */
public class GroupModel extends DataSupport implements Serializable {
    /**
     * GROUPID : 213
     * GROUPIMAGE : uploadFiles/uploadImgs//IOS20171219/cd08f4c55ef943998d7b1be224b40caf.png
     * GROUPRCID : group213
     * GROUPTYPE : G
     * NAME : 12-19新创建
     */
    private int GROUPID;
    private String GROUPIMAGE;
    private String GROUPRCID;
    private String GROUPTYPE;
    private String NAME;
    private String before;

    public int getGROUPID() {
        return GROUPID;
    }

    public void setGROUPID(int GROUPID) {
        this.GROUPID = GROUPID;
    }

    public String getGROUPIMAGE() {
        return GROUPIMAGE;
    }

    public void setGROUPIMAGE(String GROUPIMAGE) {
        this.GROUPIMAGE = GROUPIMAGE;
    }

    public String getGROUPRCID() {
        return GROUPRCID;
    }

    public void setGROUPRCID(String GROUPRCID) {
        this.GROUPRCID = GROUPRCID;
    }

    public String getGROUPTYPE() {
        return GROUPTYPE;
    }

    public void setGROUPTYPE(String GROUPTYPE) {
        this.GROUPTYPE = GROUPTYPE;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public String getBefore() {
        return before;
    }

    public void setBefore(String before) {
        this.before = before;
    }

    @Override
    public String toString() {
        return "GroupModel{" +
                "GROUPID=" + GROUPID +
                ", GROUPIMAGE='" + GROUPIMAGE + '\'' +
                ", GROUPRCID='" + GROUPRCID + '\'' +
                ", GROUPTYPE='" + GROUPTYPE + '\'' +
                ", NAME='" + NAME + '\'' +
                ", before='" + before + '\'' +
                '}';
    }
}
