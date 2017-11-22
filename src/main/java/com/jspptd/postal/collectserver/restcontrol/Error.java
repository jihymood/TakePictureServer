package com.jspptd.postal.collectserver.restcontrol;

public class Error {

    public static int IllegalRequest = 99999;
    public static int PasswordInvalid = 10000;
    public static int TokenInvalid = 10001;
    public static int ClientIdInvalid = 10002;
    public static int DataNotFound = 99998;


    public Error() {

    }

    public Error(int errCode, String errMessage) {
        this.setErrCode(errCode);
        this.setErrMessage(errMessage);

    }

    public Error(String errMessage) {
        this.setErrMessage(errMessage);
        this.setErrCode(-1);

    }

    public int errCode;

    public int getErrCode() {
        return errCode;
    }

    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }

    public String errMessage;

    // 暂时还没这么复杂
    // public T errdata;
    public String getErrMessage() {
        return errMessage;
    }

    public void setErrMessage(String errMessage) {
        this.errMessage = errMessage;
    }
}
