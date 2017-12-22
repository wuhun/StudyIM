package studyim.cn.edu.cafa.studyim.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

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
         * ISTOP : N
         * birthday : 1484323200000
         * ISSHIELD : N
         * nickName : 阿拉
         * sex : M
         * roles : [{"roleName":"未知","roleType":2}]
         * remarkName : null
         * RCID : cafa12
         * avatar : http://124.127.38.214/uploadFiles/uploadImgs//IOS20171206/fbb58a35f4d24e24a4c9093603e0eb19.png
         * userId : 12
         * remarkMSG : null
         * major : null
         * school : 中央美术学院
         * name : null
         * class : 3
         * remarkTelephone : null
         * ISBUDDYS : Y
         */
        private String ISTOP; //是否置顶
        private long birthday; //生日
        private String ISSHIELD; // 是否屏蔽
        private String nickName; // 昵称
        private String sex; //性别
        private String remarkName; //注备名
        private String RCID; //融云id
        private String avatar; //头像
        private int userId;//用户id
        private String remarkMSG;//注备信息
        private String major;//专业
        private String school;//学校
        private String name;//姓名
        @SerializedName("class")
        private String classX;//班级
        private String remarkTelephone;//注备电话
        private String ISBUDDYS;// 是否是好友
        private List<RolesModel> roles;//权限

        public String getISTOP() {
            return ISTOP;
        }

        public void setISTOP(String ISTOP) {
            this.ISTOP = ISTOP;
        }

        public long getBirthday() {
            return birthday;
        }

        public void setBirthday(long birthday) {
            this.birthday = birthday;
        }

        public String getISSHIELD() {
            return ISSHIELD;
        }

        public void setISSHIELD(String ISSHIELD) {
            this.ISSHIELD = ISSHIELD;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getRemarkName() {
            return remarkName;
        }

        public void setRemarkName(String remarkName) {
            this.remarkName = remarkName;
        }

        public String getRCID() {
            return RCID;
        }

        public void setRCID(String RCID) {
            this.RCID = RCID;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getRemarkMSG() {
            return remarkMSG;
        }

        public void setRemarkMSG(String remarkMSG) {
            this.remarkMSG = remarkMSG;
        }

        public String getMajor() {
            return major;
        }

        public void setMajor(String major) {
            this.major = major;
        }

        public String getSchool() {
            return school;
        }

        public void setSchool(String school) {
            this.school = school;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getClassX() {
            return classX;
        }

        public void setClassX(String classX) {
            this.classX = classX;
        }

        public String getRemarkTelephone() {
            return remarkTelephone;
        }

        public void setRemarkTelephone(String remarkTelephone) {
            this.remarkTelephone = remarkTelephone;
        }

        public String getISBUDDYS() {
            return ISBUDDYS;
        }

        public void setISBUDDYS(String ISBUDDYS) {
            this.ISBUDDYS = ISBUDDYS;
        }

        public List<RolesModel> getRoles() {
            return roles;
        }

        public void setRoles(List<RolesModel> roles) {
            this.roles = roles;
        }

        @Override
        public String toString() {
            return "FriendGetInfoModel{" +
                    "ISTOP='" + ISTOP + '\'' +
                    ", birthday=" + birthday +
                    ", ISSHIELD='" + ISSHIELD + '\'' +
                    ", nickName='" + nickName + '\'' +
                    ", sex='" + sex + '\'' +
                    ", remarkName='" + remarkName + '\'' +
                    ", RCID='" + RCID + '\'' +
                    ", avatar='" + avatar + '\'' +
                    ", userId=" + userId +
                    ", remarkMSG='" + remarkMSG + '\'' +
                    ", major='" + major + '\'' +
                    ", school='" + school + '\'' +
                    ", name='" + name + '\'' +
                    ", classX='" + classX + '\'' +
                    ", remarkTelephone='" + remarkTelephone + '\'' +
                    ", ISBUDDYS='" + ISBUDDYS + '\'' +
                    ", roles=" + roles +
                    '}';
        }
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
