package com.ricoandilet.springtools.exception;

import lombok.RequiredArgsConstructor;

import java.text.MessageFormat;

/**
 * @author rico
 */
public class BusinessException extends BaseException {
    private final IResponseEnum responseEnum;
    private final Object[] args;

    public BusinessException(IResponseEnum responseEnum, Object... args) {
        super(responseEnum.getErrorCode(), MessageFormat.format(responseEnum.getErrorMessage(), args));
        this.responseEnum = responseEnum;
        this.args = args;
    }

    public BusinessException(IResponseEnum responseEnum, Throwable cause, Object... args) {
        super(responseEnum.getErrorCode(), MessageFormat.format(responseEnum.getErrorMessage(), args), cause);
        this.responseEnum = responseEnum;
        this.args = args;
    }

    @Override
    public String getLocalizedMessage() {
        return responseEnum.getLocalizedMessage(args);
    }

    @RequiredArgsConstructor
    public enum BaseErrorCode implements IResponseEnum {
        SERIALIZATION_FAILURE("SERIALIZATION_FAILURE", "Serialization failure: {0}"),
        ;

        @Override
        public String getErrorCode() {
            return null;
        }

        @Override
        public String getErrorMessage() {
            return null;
        }

        private final String code;

        private final String msg;
    }
}
