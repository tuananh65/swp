
package model;

public class Role {

    private int roleID;
    private String roleName;

    // Constructor mặc định (không tham số)
    public Role() {
    }

    // Constructor nhận cả RoleID và RoleName
    public Role(int roleID, String roleName) {
        this.roleID = roleID;
        this.roleName = roleName;
    }

    // Getter cho roleID
    public int getRoleID() {
        return roleID;
    }

    // Setter cho roleID
    public void setRoleID(int roleID) {
        this.roleID = roleID;
    }

    // Getter cho roleName
    public String getRoleName() {
        return roleName;
    }

    // Setter cho roleName
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}