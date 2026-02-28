package hu.fueltracker.dto.register;

public class RegisterResponse {
    private String message;
    private boolean success;

    public RegisterResponse() {
    }

    public RegisterResponse(String message, boolean success) {
        this.message = message;
        this.success = success;
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

    @Override
    public String toString() {
        return "RegisterResponse{" +
                "message='" + message + '\'' +
                ", success=" + success +
                '}';
    }
}
