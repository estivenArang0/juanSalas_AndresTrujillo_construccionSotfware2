package com.banco.application.dto.response;

public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;

    public ApiResponse() {}

    public ApiResponse(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public static <T> ApiResponseBuilder<T> builder() { return new ApiResponseBuilder<>(); }

    public static class ApiResponseBuilder<T> {
        private ApiResponse<T> r = new ApiResponse<>();
        public ApiResponseBuilder<T> success(boolean s) { r.success = s; return this; }
        public ApiResponseBuilder<T> message(String m) { r.message = m; return this; }
        public ApiResponseBuilder<T> data(T d) { r.data = d; return this; }
        public ApiResponse<T> build() { return r; }
    }

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public T getData() { return data; }
    public void setData(T data) { this.data = data; }

    public static <T> ApiResponse<T> ok(T data) {
        return ApiResponse.<T>builder().success(true).message("OK").data(data).build();
    }
    public static <T> ApiResponse<T> ok(String message, T data) {
        return ApiResponse.<T>builder().success(true).message(message).data(data).build();
    }
    public static <T> ApiResponse<T> error(String message) {
        return ApiResponse.<T>builder().success(false).message(message).build();
    }
}
