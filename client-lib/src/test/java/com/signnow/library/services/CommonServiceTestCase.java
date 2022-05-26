package com.signnow.library.services;

import static org.mockito.Mockito.mock;

import com.signnow.library.SNClient;
import org.junit.jupiter.api.BeforeEach;

abstract class CommonServiceTestCase {
  protected SNClient clientMock;

  @BeforeEach
  void setUp() {
    clientMock = mock(SNClient.class);
  }
}
