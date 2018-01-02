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
public class RolesModel implements Serializable {

    /**
     * roleName : 未知
     * roleType : 2
     */

    private String roleName;
    private int roleType;

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
}
