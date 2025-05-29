package dto;
import model.Users;

public class UserWithRoleDTO {
    private model.Users user;
    private String roleName;

    public UserWithRoleDTO(model.Users user, String roleName) {
        this.user = user;
        this.roleName = roleName;
    }

    public model.Users getUser() {
        return user;
    }

    public String getRoleName() {
        return roleName;
    }
}
