package com.signnow.library;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.signnow.library.dto.ApiError;
import com.signnow.library.dto.AuthError;
import com.signnow.library.exceptions.SNException;
import javax.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SNClientTest {

  private Response response;

  @BeforeEach
  void setup() {
    response = mock(Response.class);
  }

  @Test
  void checkAPIException_ResponseWasNull() {
    assertThrows(NullPointerException.class, () -> SNClient.checkAPIException(null));
  }

  @Test
  void checkAPIException_ResponseGetStatus401() {
    AuthError authError = mock(AuthError.class);
    when(response.getStatus()).thenReturn(401);
    when(response.readEntity(AuthError.class)).thenReturn(authError);

    assertThrows(SNException.class, () -> SNClient.checkAPIException(response));
  }

  @Test
  void checkAPIException_ResponseGetStatus403() {
    AuthError authError = mock(AuthError.class);
    when(response.getStatus()).thenReturn(403);
    when(response.readEntity(AuthError.class)).thenReturn(authError);

    assertThrows(SNException.class, () -> SNClient.checkAPIException(response));
  }

  @Test
  void checkAPIException_ResponseGetStatus400() {
    ApiError apiError = mock(ApiError.class);
    when(response.getStatus()).thenReturn(400);
    when(response.readEntity(ApiError.class)).thenReturn(apiError);

    assertThrows(SNException.class, () -> SNClient.checkAPIException(response));
  }
}
