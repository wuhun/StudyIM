package studyim.cn.edu.cafa.studyim.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * ================================================
 * 作    者：悟魂(了解自己，感悟灵魂，做最好的自己)
 * 创建日期：2018/1/2 0002
 * 版    本：1.0
 * 描    述：获取个人信息实体类
 * 修订历史：
 * ================================================
 */
public class UserGetInfo {

    /**
     * msg :
     * result : {"birthday":1484668800000,"major":"计算机绘画","school":"中央美术学院","nickName":"小明","sex":"W","roles":[{"roleName":"教师","roleType":4}],"name":"冀志英","avatar":"http://124.127.38.214/null","userId":14,"class":"22"}
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
         * birthday : 1484668800000
         * major : 计算机绘画
         * school : 中央美术学院
         * nickName : 小明
         * sex : W
         * roles : [{"roleName":"教师","roleType":4}]
         * name : 冀志英
         * avatar : http://124.127.38.214/null
         * userId : 14
         * class : 22
         */

        private long birthday;
        private String major;
        private String school;
        private String nickName;
        private String sex;
        private String name;
        private String avatar;
        private int userId;
        @SerializedName("class")
        private String classX;
        private List<RolesModel> roles;

        public long getBirthday() {
            return birthday;
        }

        public void setBirthday(long birthday) {
            this.birthday = birthday;
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

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
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

        public String getClassX() {
            return classX;
        }

        public void setClassX(String classX) {
            this.classX = classX;
        }

        public List<RolesModel> getRoles() {
            return roles;
        }

        public void setRoles(List<RolesModel> roles) {
            this.roles = roles;
        }

    }
}
