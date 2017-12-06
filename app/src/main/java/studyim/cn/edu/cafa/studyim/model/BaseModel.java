package studyim.cn.edu.cafa.studyim.model;

import java.util.List;

/**
 * ================================================
 * 作    者：悟魂(了解自己，感悟灵魂，做最好的自己)
 * 创建日期：2017/12/1 0001
 * 版    本：1.0
 * 描    述：
 *          1、基本数据返回的格式；
 * 修订历史：
 * ================================================
 */
public class BaseModel<T> {

    /**
     * msg :
     * result : [{"SCHOOL":"中央美术学院","NICKNAME":"小黑","MAJOR":"计算机","CODE":2,"SEX":"M","USERID":11,"RCID":"cafa11","CLASS":"白玫瑰","AVATAR":"uploadFiles/uploadImgs//IOS20171116/4b7e3fb2ca7f40d18711f27c7dc7aa80.png","IRNAME":"未知","BIRTHDAY":1484409600000,"ISBUDDY":"N"},{"SCHOOL":"中央美术学院","NICKNAME":"测试账号","MAJOR":"美术鉴赏","CODE":5,"IUNAME":"李明珠","SEX":"M","USERID":21,"RCID":"cafa21","CLASS":"33","IRNAME":"学生","BIRTHDAY":1484409600000,"ISBUDDY":"N"}]
     * code : 1
     * before : http://124.127.38.214/
     */

    private String msg;
    private int code;
    private String before;
    private List<T> result;

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

    public List<T> getResult() {
        return result;
    }

    public void setResult(List<T> result) {
        this.result = result;
    }

}
