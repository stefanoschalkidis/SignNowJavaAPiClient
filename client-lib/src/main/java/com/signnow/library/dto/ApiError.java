package com.signnow.library.dto;

import java.util.List;

@SuppressWarnings("java:S1104")  // field name equal to JsonProperty
public class ApiError {
    public List<ErrorInfo> errors;

    public static class ErrorInfo {
        public Integer code;
        public String message;
    }
}
