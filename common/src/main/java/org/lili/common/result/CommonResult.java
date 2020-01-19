package org.lili.common.result;

import java.io.Serializable;


public class CommonResult<T> implements Serializable {
    private static final long serialVersionUID = 7917345507074842804L;
    private String code;
    private String msg;
    private T data;
    private String message;

    public static class Builder {
        public static CommonResult SUCC() {
            CommonResult vo = new CommonResult();
            vo.setCode("0");
            vo.setmsg("suc");
            return vo;
        }

        public static CommonResult FAIL() {
            CommonResult vo = new CommonResult();
            return vo;
        }
    }

    public CommonResult initErrCodeAndMsg(String code, String msg) {
        this.code = code;
        this.msg = msg;
        return this;
    }
    
    public CommonResult initErrCodeAndMsg(int code, String msg) {
        this.code = Integer.toString(code);
        this.msg = msg;
        return this;
    }

    public CommonResult initSuccCodeAndMsg(String code, String msg) {
        this.code = code;
        this.msg = msg;
        return this;
    }

    public CommonResult initSuccCodeAndMsg(int code, String msg) {
        this.code = Integer.toString(code);
        this.msg = msg;
        return this;
    }

    public CommonResult initSuccData(T data) {
        this.data = data;
        return this;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getmsg() {
        return msg;
    }

    public void setmsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * 使用message存储国际化key
     *
     * @param code
     * @param msg
     * @param message
     * @return
     */
    public CommonResult setCodeAndMsgAndMessage(String code, String msg, String message) {
        this.code = code;
        this.msg = msg;
        this.message = message;
        return this;
    }

    @Override
    public String toString() {
        return "CommonResult{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                ", message='" + message + '\'' +
                '}';
    }
}
