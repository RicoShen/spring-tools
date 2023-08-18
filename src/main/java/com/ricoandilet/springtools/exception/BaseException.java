package com.ricoandilet.springtools.exception;

/**
 * @author rico
 */
public class BaseException extends RuntimeException {

    private String errorCode;
    private String errorMsg;

    public BaseException(String message) {
        super(message);
        this.errorMsg = message;
    }

    public BaseException(String code, String message) {
        super(message);
        this.errorCode = code;
        this.errorMsg = message;
    }

    public BaseException(String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
