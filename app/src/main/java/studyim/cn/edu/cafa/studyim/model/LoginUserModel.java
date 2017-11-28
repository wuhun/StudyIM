package studyim.cn.edu.cafa.studyim.model;

import java.util.List;

/**
 * ================================================
 * 作    者：悟魂(了解自己，感悟灵魂，做最好的自己)
 * 创建日期：2017/11/20 0020
 * 版    本：1.0
 * 描    述：  登录返回的实体类
 * 修订历史：
 * ================================================
 */
public class LoginUserModel {

    /** 登录失败描述信息 */
    private String msg;
    /** 返回的用户数据 */
    private ResultBean result;
    /** 登录成功：1，登录失败：0 */
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
        /**  */
        private String studentID;
        /**  */
        private String RCID;
        /**  */
        private String tokens;
        /** 真实姓名 */
        private Object name;
        /** 头像 */
        private String avatar;
        /** 用户id */
        private String userId;
        /** 是否绑定手机 "N", */
        private String bindtelephone;
        /** 手机号码 */
        private String telephone;
        private String token;
        /** 角色 */
        private List<RolesBean> roles;

        public String getStudentID() {
            return studentID;
        }

        public void setStudentID(String studentID) {
            this.studentID = studentID;
        }

        public Object getName() {
            return name;
        }

        public void setName(Object name) {
            this.name = name;
        }

        public String getRCID() {
            return RCID;
        }

        public void setRCID(String RCID) {
            this.RCID = RCID;
        }

        public String getTokens() {
            return tokens;
        }

        public void setTokens(String tokens) {
            this.tokens = tokens;
        }

        public String getTelephone() {
            return telephone;
        }

        public void setTelephone(String telephone) {
            this.telephone = telephone;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getBindtelephone() {
            return bindtelephone;
        }

        public void setBindtelephone(String bindtelephone) {
            this.bindtelephone = bindtelephone;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public List<RolesBean> getRoles() {
            return roles;
        }

        public void setRoles(List<RolesBean> roles) {
            this.roles = roles;
        }

        public static class RolesBean {
            /** 角色类型 */
            private int roleType;
            /** 角色名称 */
            private String roleName;

            public String getRoleName() {
                return roleName;
            }

            public void setRoleName(String roleName) {
                this.roleName = roleName;
            }

            public int getRoleType() {
                return roleType;
            }

            public void setRoleType(int roleType) {
                this.roleType = roleType;
            }

            @Override
            public String toString() {
                return "RolesBean{" +
                        "角色类型roleType=" + roleType +
                        ", 角色名称roleName='" + roleName + '\'' +
                        '}';
            }
        }
    }
}
