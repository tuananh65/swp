package model;

public class ImportResult {
    private int rowNumber;
    private String message;
    private boolean success;

    public ImportResult() {}

    public ImportResult(int rowNumber, String message, boolean success) {
        this.rowNumber = rowNumber;
        this.message = message;
        this.success = success;
    }

    public int getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(int rowNumber) {
        this.rowNumber = rowNumber;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
