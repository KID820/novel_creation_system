package com.gdut.aigc.POJO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {
    private Integer code; // 状态码：200成功，其他为失败
    private String message; // 提示信息
    private Object data; // 业务数据

    // 成功静态方法
    public static Result success(Object data) {
        return new Result(200, "success", data);
    }

    public static Result success(String message) {
        return new Result(200, message, null);
    }

    public static Result success(String message, Object data) {
        return new Result(200, message, data);
    }

    // 失败静态方法
    public static Result error(String message) {
        return new Result(400, message, null);
    }

    public static Result error(Integer code, String message) {
        return new Result(code, message, null);
    }
}