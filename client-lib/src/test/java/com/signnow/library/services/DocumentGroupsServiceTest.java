package com.signnow.library.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.signnow.library.dto.DocumentGroup;
import com.signnow.library.dto.GenericId;
import com.signnow.library.dto.GroupInvite;
import com.signnow.library.exceptions.SNApiException;
import com.signnow.library.exceptions.SNException;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DocumentGroupsServiceTest extends CommonServiceTestCase {
  private DocumentGroupsService service;

  @BeforeEach
  void setUp() {
    super.setUp();
    service = new DocumentGroupsService(clientMock);
  }

  @Test
  void getDocumentGroup() throws SNException {
    DocumentGroup expectedDocumentGroup = mock(DocumentGroup.class);
    when(clientMock.get(anyString(), anyMap(), eq(DocumentGroup.class)))
        .thenReturn(expectedDocumentGroup);
    DocumentGroup actualDocumentGroup = service.getDocumentGroup("15");

    assertEquals(expectedDocumentGroup, actualDocumentGroup);
    verify(clientMock, times(1)).get(anyString(), anyMap(), eq(DocumentGroup.class));
  }

  @Test
  void createDocumentGroup() throws SNException {
    final String groupName = "Some group";
    GenericId genericIdMock = getGenericIdMock();

    final String path = "/documentgroup";
    when(clientMock.post(
            eq(path),
            eq(null),
            any(DocumentGroup.DocumentGroupCreateRequest.class),
            eq(GenericId.class)))
        .thenReturn(genericIdMock);

    List<String> documentIds = new ArrayList<>();
    String actualDocumentGroup = service.createDocumentGroup(documentIds, groupName);

    assertNull(actualDocumentGroup);
    verify(clientMock, times(1))
        .post(
            eq(path),
            eq(null),
            any(DocumentGroup.DocumentGroupCreateRequest.class),
            eq(GenericId.class));
  }

  private GenericId getGenericIdMock() {
    return mock(GenericId.class);
  }

  @Test
  void getUserDocumentGroups_incorrectLimit() {
    final int limit = -5;
    final int offset = 0;
    assertThrows(SNApiException.class, () -> service.getUserDocumentGroups(limit, offset));
  }

  @Test
  void getUserDocumentGroups_incorrectOffset() {
    final int limit = 5;
    final int offset = -5;
    assertThrows(SNApiException.class, () -> service.getUserDocumentGroups(limit, offset));
  }

  @Test
  void getUserDocumentGroups() throws SNException {
    final String path = "/user/documentgroups";
    when(clientMock.get(eq(path), anyMap(), eq(DocumentGroup.DocumentGroupsListResponse.class)))
        .thenReturn(null);

    service.getUserDocumentGroups(5, 5);

    verify(clientMock, times(1))
        .get(eq(path), anyMap(), eq(DocumentGroup.DocumentGroupsListResponse.class));
  }

  @Test
  void deleteDocumentGroup() throws SNException {
    final String path = "/documentgroup/{documentGroupId}";
    DocumentGroup.DocumentGroupDeleteResponse responseMock =
        mock(DocumentGroup.DocumentGroupDeleteResponse.class);

    when(clientMock.delete(eq(path), anyMap(), eq(DocumentGroup.DocumentGroupDeleteResponse.class)))
        .thenReturn(responseMock);

    String status = service.deleteDocumentGroup("1");

    assertEquals(responseMock.status, status); // not sure if useful as is null
    verify(clientMock, times(1))
        .delete(eq(path), anyMap(), eq(DocumentGroup.DocumentGroupDeleteResponse.class));
  }

  @Test
  void moveDocumentGroup() throws SNException {
    final String path = "/v2/document-groups/{documentGroupId}/move";
    final DocumentGroup.MoveDocumentGroupResponse responseMock =
        mock(DocumentGroup.MoveDocumentGroupResponse.class);

    when(clientMock.post(
            eq(path),
            anyMap(),
            any(DocumentGroup.MoveDocumentGroupRequest.class),
            eq(DocumentGroup.MoveDocumentGroupResponse.class)))
        .thenReturn(responseMock);

    service.moveDocumentGroup("1", "2");

    verify(clientMock, times(1))
        .post(
            eq(path),
            anyMap(),
            any(DocumentGroup.MoveDocumentGroupRequest.class),
            eq(DocumentGroup.MoveDocumentGroupResponse.class));
  }

  @Test
  void createDocumentGroupInvite() throws SNException {
    GroupInvite groupInviteMock = mock(GroupInvite.class);
    final String path = "/documentgroup/{documentGroupId}/groupinvite";
    when(clientMock.post(eq(path), anyMap(), eq(groupInviteMock), eq(GenericId.class)))
        .thenReturn(getGenericIdMock());

    service.createDocumentGroupInvite("1", groupInviteMock);

    verify(clientMock, times(1)).post(eq(path), anyMap(), eq(groupInviteMock), eq(GenericId.class));
  }

  @Test
  void resendInvites() throws SNException {
    final String path = "/documentgroup/{documentGroupId}/groupinvite/{inviteId}/resendinvites";
    when(clientMock.post(eq(path), anyMap(), eq(null), eq(GenericId.class)))
        .thenReturn(getGenericIdMock());

    service.resendInvites("1", "2", null);

    verify(clientMock, times(1)).post(eq(path), anyMap(), eq(null), eq(GenericId.class));
  }
}
