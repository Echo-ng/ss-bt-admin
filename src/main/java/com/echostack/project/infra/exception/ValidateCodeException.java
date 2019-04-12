package com.echostack.project.infra.exception;

/**
 * @Author: Echo
 * @Date: 2019/4/12 1:48
 * @Description:
 */
public class ValidateCodeException extends RuntimeException {

    public ValidateCodeException() {
    }

    public ValidateCodeException(String message) {
        super(message);
    }

    public ValidateCodeException(String message, Throwable cause) {
        super(message, cause);
    }
}
