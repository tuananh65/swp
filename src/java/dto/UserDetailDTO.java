package dto;

import model.Users;

public class UserDetailDTO {
    private Users user;
    private String roleName;

    public UserDetailDTO(Users user, String roleName) {
        this.user = user;
        this.roleName = roleName;
    }

    public Users getUser() {
        return user;
    }

    public String getRoleName() {
        return roleName;
    }
}