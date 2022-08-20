package org.oaristeidou.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.oaristeidou.entity.ItemEntity;
import org.openapitools.model.Item;
import org.openapitools.model.StatusType;

class MappingServiceTest {

  static MappingService mappingService;

  @BeforeAll
  static void init(){
    mappingService = new MappingService(null);
  }

  @Test
  @DisplayName("Add an item: StatusType.NOT_DONE")
  void createItemEntity_testCase_1() {
    ItemEntity itemEntity = new ItemEntity();
    Item itemRequest = createItem("Test", StatusType.NOT_DONE, LocalDateTime.now().plusDays(1));

    mappingService.createItemEntity(itemRequest, itemEntity);

    assertNotNull(itemEntity);
    assertEquals(0, itemEntity.getId());
    assertNotNull(itemEntity.getStatus());
    assertNotNull(itemEntity.getDescription());
    assertNotNull(itemEntity.getDueDate());
    assertNotNull(itemEntity.getCreationDate());
  }

  @Test
  @DisplayName("Add an item: StatusType.DONE")
  void createItemEntity_testCase_2() {
    ItemEntity itemEntity = new ItemEntity();
    Item itemRequest = createItem("Test", StatusType.DONE, LocalDateTime.now().plusDays(1));

    mappingService.createItemEntity(itemRequest, itemEntity);

    assertNotNull(itemEntity);
    assertEquals(0, itemEntity.getId());
    assertNotNull(itemEntity.getStatus());
    assertNotNull(itemEntity.getDescription());
    assertNotNull(itemEntity.getDueDate());
    assertNotNull(itemEntity.getCreationDate());
    assertNotNull(itemEntity.getDoneDate());
  }

  @Test
  @DisplayName("Add an item: StatusType.NOT_DONE with DueDate ended!")
  void createItemEntity_testCase_3() {
    ItemEntity itemEntity = new ItemEntity();
    Item itemRequest = createItem("Test", StatusType.NOT_DONE, LocalDateTime.now().minusDays(1));

    mappingService.createItemEntity(itemRequest, itemEntity);

    assertNotNull(itemEntity);
    assertEquals(0, itemEntity.getId());
    assertNotNull(itemEntity.getStatus());
    assertEquals(StatusType.PAST_DUE, itemEntity.getStatus());
    assertNotNull(itemEntity.getDescription());
    assertNotNull(itemEntity.getDueDate());
    assertNotNull(itemEntity.getCreationDate());
    assertNull(itemEntity.getDoneDate());
  }

  @Test
  @DisplayName("Update an itemEntity: StatusType.DONE")
  void mapUpdateItem_testCase_1() {
    ItemEntity itemEntity = createItemEntity(StatusType.NOT_DONE, "");
    Item itemRequest = createItem(1L, "Test", StatusType.DONE, LocalDateTime.now().plusDays(1));

    mappingService.mapUpdateItem(itemRequest, itemEntity);

    assertNotNull(itemEntity);
    assertEquals(1L, itemEntity.getId());
    assertNotNull(itemEntity.getStatus());
    assertEquals(StatusType.DONE, itemEntity.getStatus());
    assertNotNull(itemEntity.getDescription());
    assertNotNull(itemEntity.getDueDate());
    assertNotNull(itemEntity.getCreationDate());
  }

  @Test
  @DisplayName("Update an itemEntity: StatusType.NOT_DONE ")
  void mapUpdateItem_testCase_2() {
    ItemEntity itemEntity = createItemEntity(StatusType.NOT_DONE, "");
    Item itemRequest = createItem(1L, "Test", StatusType.DONE, LocalDateTime.now().plusDays(1));

    mappingService.mapUpdateItem(itemRequest, itemEntity);

    assertNotNull(itemEntity);
    assertEquals(1L, itemEntity.getId());
    assertNotNull(itemEntity.getStatus());
    assertEquals(StatusType.DONE, itemEntity.getStatus());
    assertNotNull(itemEntity.getDescription());
    assertNotNull(itemEntity.getDueDate());
    assertNotNull(itemEntity.getCreationDate());
  }

  @Test
  @DisplayName("Update an itemEntity: StatusType.NOT_DONE with DueDate ended")
  void mapUpdateItem_testCase_3() {
    ItemEntity itemEntity = createItemEntity(StatusType.DONE, "");
    Item itemRequest = createItem(1L, "Test", StatusType.NOT_DONE, LocalDateTime.now().minusDays(1));

    mappingService.mapUpdateItem(itemRequest, itemEntity);

    assertNotNull(itemEntity);
    assertEquals(1L, itemEntity.getId());
    assertNotNull(itemEntity.getStatus());
    assertEquals(StatusType.PAST_DUE, itemEntity.getStatus());
    assertNotNull(itemEntity.getDescription());
    assertNotNull(itemEntity.getDueDate());
    assertNotNull(itemEntity.getCreationDate());
  }

  @Test
  @DisplayName("Status empty: returns null")
  void getStatusTypeByString_testCase_1() {
    assertNull(mappingService.getStatusTypeByString(""));
  }

  @Test
  @DisplayName("Status null returns null")
  void getStatusTypeByString_testCase_2() {
    assertNull(mappingService.getStatusTypeByString(null));
  }

  @Test
  @DisplayName("Status NOT_DONE is founded")
  void getStatusTypeByString_testCase_3() {
    assertEquals(StatusType.NOT_DONE, mappingService.getStatusTypeByString("not done"));
  }

  @Test
  @DisplayName("Status PAST_DUE is founded")
  void getStatusTypeByString_testCase_4() {
    assertEquals(StatusType.PAST_DUE, mappingService.getStatusTypeByString("past due"));
  }

  @Test
  @DisplayName("Status DONE is founded")
  void getStatusTypeByString_testCase_5() {
    assertEquals(StatusType.DONE, mappingService.getStatusTypeByString("done"));
  }

  @Test
  @DisplayName("Status TEST returns null")
  void getStatusTypeByString_testCase_6() {
    assertNull(mappingService.getStatusTypeByString("test"));
  }

  private ItemEntity createItemEntity(StatusType status, String description) {
    ItemEntity itemEntity = new ItemEntity();
    itemEntity.setStatus(status);
    itemEntity.setDescription(description);
    return itemEntity;
  }

  private Item createItem(String description, StatusType statusType,
      LocalDateTime dueDate) {
    Item itemRequest = new Item();
    itemRequest.setDescription(description);
    itemRequest.setStatus(statusType);
    itemRequest.setDueDate(dueDate);
    return itemRequest;
  }

  private Item createItem(Long itemId, String description, StatusType statusType,
      LocalDateTime dueDate) {
    Item itemRequest = new Item();
    itemRequest.setItemId(itemId);
    itemRequest.setDescription(description);
    itemRequest.setStatus(statusType);
    itemRequest.setDueDate(dueDate);
    return itemRequest;
  }
}