package studyim.cn.edu.cafa.studyim.model;

import java.io.Serializable;

/**
 * ================================================
 * 作    者：悟魂(了解自己，感悟灵魂，做最好的自己)
 * 创建日期：2017/12/8 0008
 * 版    本：1.0
 * 描    述：
 * 修订历史：
 * ================================================
 */
public class FriendGetInfoModel implements Serializable {
    /**
     * msg :
     * result : {"ISTOP":"N","birthday":1484236800000,"ISSHIELD":"N","nickName":"2","sex":"M","roles":[{"roleName":"未知","roleType":2}],"remarkName":null,"RCID":"cafa13","avatar":"http://124.127.38.214/null","userId":13,"remarkMSG":null,"major":null,"school":"中央美术学院","name":null,"class":"2","remarkTelephone":null,"ISBUDDYS":"Y"}
     * code : 1
     */

    private String msg;
    private FriendUserInfo result;
    private int code;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public FriendUserInfo getResult() {
        return result;
    }

    public void setResult(FriendUserInfo result) {
        this.result = result;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }


    @Override
    public String toString() {
        return "FriendGetInfoModel{" +
                "msg='" + msg + '\'' +
                ", result=" + result +
                ", code=" + code +
                '}';
    }
}
