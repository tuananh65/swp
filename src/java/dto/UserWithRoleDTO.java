package dto;
import model.User;

public class UserWithRoleDTO {
    private model.User user;
    private String roleName;

    public UserWithRoleDTO(model.User user, String roleName) {
        this.user = user;
        this.roleName = roleName;
    }

    public model.User getUser() {
        return user;
    }

    public String getRoleName() {
        return roleName;
    }
    @Override
public String toString() {
    return "UserWithRoleDTO{" +
            "userId=" + user.getUserId() +
            ", fullName='" + user.getFullName() + '\'' +
            ", email='" + user.getEmail() + '\'' +
            ", roleName='" + roleName + '\'' +
            '}';
}

}
