package com.demo.im.common;

/**
 * 响应数据
 *
 */
public class Result<T> {
    //    @Schema(description = "编码 0表示成功，其他值表示失败")
    private int code = 0;

    //    @Schema(description = "消息内容")
    private String msg = "success";

    //    @Schema(description = "响应数据")
    private T data;

    public static <T> Result<T> ok() {
        return ok(null);
    }

    public static <T> Result<T> ok(T data) {
        Result<T> result = new Result<>();
        result.setData(data);
        return result;
    }

    public static <T> Result<T> error() {
        return error(500,"未知异常");
    }

    public static <T> Result<T> error(String msg) {
        return error(500, msg);
    }


    public static <T> Result<T> error(int code, String msg) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
