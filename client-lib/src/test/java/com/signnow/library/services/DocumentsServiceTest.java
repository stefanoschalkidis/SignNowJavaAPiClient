package com.signnow.library.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.signnow.library.dto.Document;
import com.signnow.library.dto.GenericId;
import com.signnow.library.dto.User;
import com.signnow.library.exceptions.SNException;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.glassfish.jersey.client.JerseyInvocation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;

class DocumentsServiceTest extends CommonServiceTestCase {
  private DocumentsService service;

  private Response responseMock;
  private JerseyInvocation.Builder builderMock;
  private WebTarget webTargetMock;

  @BeforeEach
  void setUp() {
    super.setUp();
    service = new DocumentsService(clientMock);
    responseMock = mock(Response.class);
    builderMock = mock(JerseyInvocation.Builder.class);
    webTargetMock = mock(WebTarget.class);
  }

  @Test
  void uploadDocument() throws SNException {
    given_uploadDocumentMocks();

    final String filename = "test.txt";
    final InputStream someInputStream = getSomeInputStream();

    final String actualDocumentId = service.uploadDocument(someInputStream, filename);

    assertEquals("1", actualDocumentId);
    verify_uploadDocument();
  }

  @Test
  void uploadDocumentWithTags() throws SNException {
    given_uploadDocumentMocks();

    final String filename = "test.txt";
    final InputStream someInputStream = getSomeInputStream();

    final String actualDocumentId = service.uploadDocumentWithTags(someInputStream, filename);

    assertEquals("1", actualDocumentId);
    verify_uploadDocument();
  }

  @Test
  void moveDocument() throws SNException {
    final Document.MoveDocumentResponse responseMock = mock(Document.MoveDocumentResponse.class);

    when(clientMock.post(
            anyString(),
            anyMap(),
            any(Document.MoveDocumentRequest.class),
            eq(Document.MoveDocumentResponse.class)))
        .thenReturn(responseMock);

    final String result = service.moveDocument("1", "12345");

    assertEquals(responseMock.result, result); // not sure if useful as is null
    verify(clientMock, times(1))
        .post(
            anyString(),
            anyMap(),
            any(Document.MoveDocumentRequest.class),
            eq(Document.MoveDocumentResponse.class));
  }

  @Test
  void getDocuments() throws SNException {
    List<Document> lst = new ArrayList<>();
    when(clientMock.get(anyString(), eq(null), ArgumentMatchers.<GenericType<List>>any()))
        .thenReturn(lst);

    final List<Document> documents = service.getDocuments();

    assertEquals(lst.size(), documents.size());
    verify(clientMock, times(1))
        .get(anyString(), eq(null), ArgumentMatchers.<GenericType<List>>any());
  }

  @Test
  void createSigningLink() throws SNException {
    final Document.SigningLinkResponse responseMock = mock(Document.SigningLinkResponse.class);

    when(clientMock.post(
            anyString(),
            eq(null),
            any(Document.SigningLinkRequest.class),
            eq(Document.SigningLinkResponse.class)))
        .thenReturn(responseMock);

    final Document.SigningLinkResponse signingLink = service.createSigningLink("1");

    assertEquals(responseMock, signingLink);
    verify(clientMock, times(1))
        .post(
            anyString(),
            eq(null),
            any(Document.SigningLinkRequest.class),
            eq(Document.SigningLinkResponse.class));
  }

  @Test
  void sendDocumentSignInvite() throws SNException {
    final Document.SigningInviteRequest request = mock(Document.SigningInviteRequest.class);

    when(clientMock.post(anyString(), anyMap(), eq(request), eq(String.class))).thenReturn("");

    service.sendDocumentSignInvite("1", request);

    verify(clientMock, times(1)).post(anyString(), anyMap(), eq(request), eq(String.class));
  }

  @Test
  void testSendDocumentSignInvite() throws SNException {
    final Document.SigningInviteWithRolesRequest request =
        mock(Document.SigningInviteWithRolesRequest.class);

    when(clientMock.post(anyString(), anyMap(), eq(request), eq(String.class))).thenReturn("");

    service.sendDocumentSignInvite("1", request);

    verify(clientMock, times(1)).post(anyString(), anyMap(), eq(request), eq(String.class));
  }

  @Test
  void updateDocumentFields() throws SNException {
    final GenericId responseMock = mock(GenericId.class);
    List<Document.Field> lst = new ArrayList<>();

    when(clientMock.put(anyString(), anyMap(), any(), any())).thenReturn(responseMock);

    String id = service.updateDocumentFields("1", lst);

    assertEquals(responseMock.id, id); // not sure if useful as is null
    verify(clientMock, times(1)).put(anyString(), anyMap(), any(), any());
  }

  @Test
  void getDocument() throws SNException {
    when(clientMock.get(anyString(), anyMap(), eq(Document.class))).thenReturn(null);

    service.getDocument("1");

    verify(clientMock, times(1)).get(anyString(), anyMap(), eq(Document.class));
  }

  @Test
  void deleteDocument() throws SNException {
    Document.DocumentDeleteResponse responseMock = mock(Document.DocumentDeleteResponse.class);
    when(clientMock.delete(anyString(), anyMap(), eq(Document.DocumentDeleteResponse.class)))
        .thenReturn(responseMock);

    String status = service.deleteDocument("1");

    assertEquals(responseMock.status, status); // not sure if useful as is null
    verify(clientMock, times(1))
        .delete(anyString(), anyMap(), eq(Document.DocumentDeleteResponse.class));
  }

  @Test
  void getDownloadLink() throws SNException {
    when(clientMock.post(anyString(), anyMap(), eq(null), eq(Document.DocumentDownloadLink.class)))
        .thenReturn(new Document.DocumentDownloadLink());

    service.getDownloadLink("1");

    verify(clientMock, times(1))
        .post(anyString(), anyMap(), eq(null), eq(Document.DocumentDownloadLink.class));
  }

  private InputStream getSomeInputStream() {
    byte[] bytes = new byte[25];
    Random rnd = new Random();
    rnd.nextBytes(bytes);
    return new ByteArrayInputStream(bytes);
  }

  private void given_snClientMock() {
    when(clientMock.getUser()).thenReturn(new User());
    when(clientMock.getApiWebTarget()).thenReturn(webTargetMock);
  }

  private void given_webTargetMock() {
    when(webTargetMock.path(anyString())).thenReturn(webTargetMock);
    when(webTargetMock.request(any(MediaType.class))).thenReturn(builderMock);
  }

  private void given_builderMock() {
    when(builderMock.header(anyString(), any())).thenReturn(builderMock);
    when(builderMock.post(any())).thenReturn(responseMock);
  }

  private void given_uploadDocumentMocks() {
    given_snClientMock();
    given_webTargetMock();
    given_builderMock();

    final Document doc = new Document();
    doc.id = "1";
    when(responseMock.readEntity(Document.class)).thenReturn(doc);
    when(responseMock.getStatus()).thenReturn(200);
  }

  @SuppressWarnings("ResultOfMethodCallIgnored")
  private void verify_uploadDocument() {
    verify(clientMock, times(1)).getUser();
    verify(clientMock, times(1)).getApiWebTarget();
    verify(webTargetMock, times(1)).path(anyString());
    verify(webTargetMock, times(1)).request(any(MediaType.class));
    verify(builderMock, times(1)).header(anyString(), any());
    verify(builderMock, times(1)).post(any());
  }
}
