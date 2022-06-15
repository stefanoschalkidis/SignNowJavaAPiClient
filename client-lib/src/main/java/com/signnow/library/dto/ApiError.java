package com.signnow.library.dto;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.signnow.library.dto.AuthError.Type;
import java.util.List;

@SuppressWarnings("java:S1104") // field name equal to JsonProperty
public class ApiError {
  public Type error;
  public List<ErrorInfo> errors;

  public static class ErrorInfo {
    public Integer code;
    public String message;
  }

  @JsonSetter("error")
  public void setType(String type) {
    this.error = Type.typeOf(type);
  }
}
