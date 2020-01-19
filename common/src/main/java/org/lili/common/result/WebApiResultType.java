package org.lili.common.result;

public enum WebApiResultType {
    Success("0", "rtSuccess", "成功"),
    SystemError("10001", "rtSystemError", "失败");

    public String code;
    public String configKey;
    public String message;

    WebApiResultType(String code, String configKey, String message) {
        this.code = code;
        this.configKey = configKey;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getConfigKey() {
        return configKey;
    }

    public void setConfigKey(String configKey) {
        this.configKey = configKey;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}