package model;

public class Setting {
    private int settingID;
    private int userID;
    private String type;
    private String key; // Lưu ý: "[Key]" có thể không phải là tên biến hợp lệ trong Java
    private String value;
    private Integer order; // "[Order]" cũng có thể cần được đổi tên và có thể là null
    private String status;

    // Constructor mặc định (không tham số)
    public Setting() {
    }

    // Constructor nhận tất cả các thuộc tính
    public Setting(int settingID, int userID, String type, String key, String value, Integer order, String status) {
        this.settingID = settingID;
        this.userID = userID;
        this.type = type;
        this.key = key;
        this.value = value;
        this.order = order;
        this.status = status;
    }

    // Getter cho settingID
    public int getSettingID() {
        return settingID;
    }

    // Setter cho settingID
    public void setSettingID(int settingID) {
        this.settingID = settingID;
    }

    // Getter cho userID
    public int getUserID() {
        return userID;
    }

    // Setter cho userID
    public void setUserID(int userID) {
        this.userID = userID;
    }

    // Getter cho type
    public String getType() {
        return type;
    }

    // Setter cho type
    public void setType(String type) {
        this.type = type;
    }

    // Getter cho key (đã đổi tên từ "[Key]")
    public String getKey() {
        return key;
    }

    // Setter cho key (đã đổi tên từ "[Key]")
    public void setKey(String key) {
        this.key = key;
    }

    // Getter cho value
    public String getValue() {
        return value;
    }

    // Setter cho value
    public void setValue(String value) {
        this.value = value;
    }

    // Getter cho order (đã đổi tên từ "[Order]")
    public Integer getOrder() {
        return order;
    }

    // Setter cho order (đã đổi tên từ "[Order]")
    public void setOrder(Integer order) {
        this.order = order;
    }

    // Getter cho status
    public String getStatus() {
        return status;
    }

    // Setter cho status
    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Setting{" +
                "settingID=" + settingID +
                ", userID=" + userID +
                ", type='" + type + '\'' +
                ", key='" + key + '\'' +
                ", value='" + value + '\'' +
                ", order=" + order +
                ", status='" + status + '\'' +
                '}';
    }
}