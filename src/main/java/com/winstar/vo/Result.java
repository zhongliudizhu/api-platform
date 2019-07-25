package com.winstar.vo;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Result {
    public static String SUCCESS = "success";
    public static String FAIL = "fail";
    private String code;
    private String errorCode;
    private String message;
    private Object data;

    public boolean isSuccess() {
        return SUCCESS.equals(this.code);
    }

    public static Result success(Object data) {
        Result result = new Result();
        result.setCode(SUCCESS);
        result.setData(data);
        return result;
    }

    public static Result success(String code, Object data) {
        Result result = new Result();
        result.setCode(code);
        result.setData(data);
        return result;
    }

    public static Result fail(String errorCode, String message) {
        Result result = new Result();
        result.setCode(FAIL);
        result.setErrorCode(errorCode);
        result.setMessage(message);
        return result;
    }

    public Result(String code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
}
