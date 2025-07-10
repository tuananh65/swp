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

    @Override
    public String toString() {
        // Kiểm tra user có null không trước khi truy cập
        String userDetails = (user != null) ?
                             "userId=" + user.getUserId() +
                             ", userName='" + user.getUserName() + '\'' +
                             ", fullName='" + user.getFullName() + '\'' +
                             ", email='" + user.getEmail() + '\'' +
                             ", phone='" + user.getPhone() + '\'' +
                             ", address='" + user.getAddress() + '\'' +
                             ", gender='" + user.getGender() + '\'' +
                             ", status='" + user.getStatus() + '\'' +
                             ", avatarUrl='" + user.getAvatarUrl() + '\''
                             : "null";

        return "UserDetailDTO{" +
                "user=[" + userDetails +
                "], roleName='" + roleName + '\'' +
                '}';
    }
}