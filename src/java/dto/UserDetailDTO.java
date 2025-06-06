package dto;

import model.User;

public class UserDetailDTO {
    private User user;
    private String roleName;

    public UserDetailDTO(User user, String roleName) {
        this.user = user;
        this.roleName = roleName;
    }

    public User getUser() {
        return user;
    }

    public String getRoleName() {
        return roleName;
    }
}