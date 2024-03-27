package com.goorm.tricountapi.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

@NoArgsConstructor
@JsonPropertyOrder({"status", "results"})
public class ApiResponse<T> implements Serializable {
    private static ApiResponseStatus OK = new ApiResponseStatus(2000, "OK");

    // 필드
    // TODO 라이브에서 여기에 Getter를 안붙여서 응답이 빈값처럼 내려왔었네요..ㅠ_ㅠ
    @Getter
    private ApiResponseStatus status;
    @Getter
    private List<T> results;

    // 생성자 역할
    public ApiResponse<T> ok() {
        this.status = OK;
        return this;
    }

    public ApiResponse<T> ok(List<T> results) {
        this.status = OK;
        this.results = results;
        return this;
    }

    public ApiResponse<T> ok(T result) {
        return ok(Collections.singletonList(result));
    }

    public ApiResponse<T> fail(int code, String message) {
        this.status = new ApiResponseStatus(code, message);
        return this;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    public static class ApiResponseStatus implements Serializable {
        @Getter
        private int code;
        @Getter
        private String message;
    }
}
