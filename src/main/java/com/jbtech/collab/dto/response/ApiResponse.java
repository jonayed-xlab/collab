package com.jbtech.collab.dto.response;

import com.jbtech.collab.utils.ResponseMessageEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private String statusCode;
    private String message;
    private T data;

    public static <T> ApiResponse<T> of(String statusCode, ResponseMessageEnum message, T data) {
        return new ApiResponse<>(statusCode, message.getMessage(), data);
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>("S200", ResponseMessageEnum.SUCCESS.getMessage(), data);
    }
}
