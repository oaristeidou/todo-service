package org.oaristeidou.service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.oaristeidou.entity.ItemEntity;
import org.oaristeidou.exception.ToDoAPIApiExceptionThrowable;
import org.oaristeidou.service.impl.H2DBService;
import org.openapitools.model.Item;
import org.openapitools.model.ResponseItems;
import org.openapitools.model.StatusType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class MappingService {
  final
  H2DBService h2DBService;

  private static final Logger LOGGER = LoggerFactory.getLogger(MappingService.class);

  public MappingService(
      H2DBService h2DBService) {
    this.h2DBService = h2DBService;
  }

  public ItemEntity createItemEntity(Item itemRequest, ItemEntity entity){
    return Optional.of(entity)
        .map(itemEntity -> mapDescription(itemRequest, itemEntity))
        .map(itemEntity -> mapStatus(itemRequest, itemEntity))
        .map(this::mapCreationDate)
        .map(itemEntity -> mapDueDate(itemRequest, itemEntity))
        .map(itemEntity -> mapDoneDate(itemRequest, itemEntity))
        .orElse(null);
  }

  public Item createResponseItem(ItemEntity itemEntity, Item item){
    return Optional.of(item)
        .map(responseItem -> mapItemId(itemEntity, item))
        .map(responseItem -> mapDescription(itemEntity, item))
        .map(responseItem -> mapStatus(itemEntity, item))
        .map(responseItem -> mapCreationDate(itemEntity, item))
        .map(responseItem -> mapDueDate(itemEntity, item))
        .map(responseItem -> mapDoneDate(itemEntity, item))
        .orElse(null);
  }

  public ResponseItems createResponseItems(List<Item> items, ResponseItems responseItems){
    responseItems.setItems(items);
    return responseItems;
  }

  public ItemEntity mapUpdateItem(Item itemRequest, ItemEntity entity){
    return Optional.of(entity)
        .map(itemEntity -> mapItemId(itemRequest, itemEntity))
        .map(itemEntity -> createItemEntity(itemRequest, itemEntity))
        .orElse(null);
  }

  public ResponseItems mapEntitiesToResponseItems(List<ItemEntity> allItems) {
      return createResponseItems(Optional.ofNullable(allItems)
          .stream().flatMap(Collection::stream)
          .map(itemEntity -> this.createResponseItem(itemEntity, new Item()))
          .collect(Collectors.toList()), new ResponseItems());
  }

  private Item mapItemId(ItemEntity itemEntity, Item itemResponse) {
    Optional.of(itemEntity)
        .map(ItemEntity::getId)
        .ifPresent(itemResponse::setItemId);
    return itemResponse;
  }

  private ItemEntity mapItemId(Item itemRequest, ItemEntity itemEntity) {
    Optional.of(itemRequest)
        .map(Item::getItemId)
        .ifPresent(itemEntity::setId);
    return itemEntity;
  }

  private ItemEntity mapDescription(Item itemRequest, ItemEntity itemEntity) {
    Optional.of(itemRequest)
        .map(Item::getDescription)
        .ifPresent(itemEntity::setDescription);
    return itemEntity;
  }

  private Item mapDescription(ItemEntity itemEntity, Item itemResponse) {
    Optional.of(itemEntity)
        .map(ItemEntity::getDescription)
        .ifPresent(itemResponse::setDescription);
    return itemResponse;
  }

  private ItemEntity mapStatus(Item itemRequest, ItemEntity itemEntity) {
    itemEntity.setStatus(Optional.of(itemRequest)
            .map(Item::getStatus)
            .orElse(StatusType.NOT_DONE));
    return itemEntity;
  }

  private Item mapStatus(ItemEntity itemEntity, Item itemResponse) {
    Optional.of(itemEntity)
        .map(ItemEntity::getStatus)
        .ifPresent(itemResponse::setStatus);
    return itemResponse;
  }

  private ItemEntity mapCreationDate(ItemEntity itemEntity) {
    Optional.of(LocalDateTime.now())
        .filter(localDateTime -> itemEntity.getCreationDate() == null)
        .ifPresent(itemEntity::setCreationDate);
    return itemEntity;
  }

  private Item mapCreationDate(ItemEntity itemEntity, Item itemResponse) {
    Optional.of(itemEntity)
        .map(ItemEntity::getCreationDate)
        .filter(localDateTime -> itemEntity.getCreationDate() == null)
        .ifPresent(itemResponse::setCreationDate);
    return itemResponse;
  }

  private ItemEntity mapDueDate(Item itemRequest, ItemEntity itemEntity) {
    Optional.of(itemRequest)
        .map(Item::getDueDate)
        .map(dueDate -> setDueDate(itemEntity, dueDate))
        .filter(dueDate -> dueDate.isBefore(LocalDateTime.now()))
        .ifPresent(temp -> markStatusAsPassDue(itemRequest, itemEntity));
    return itemEntity;
  }

  private LocalDateTime setDueDate(ItemEntity itemEntity, LocalDateTime dueDate) {
    Optional.ofNullable(dueDate)
        .ifPresent(itemEntity::setDueDate);
    return dueDate;
  }

  private Item mapDueDate(ItemEntity itemEntity, Item itemResponse) {
    Optional.of(itemEntity)
        .map(ItemEntity::getDueDate)
        .ifPresent(itemResponse::setDueDate);
    return itemResponse;
  }

  private ItemEntity mapDoneDate(Item itemRequest, ItemEntity itemEntity) {
    Optional.of(itemRequest)
        .map(Item::getStatus)
        .filter(StatusType.DONE::equals)
        .filter(statusType -> itemEntity.getDoneDate() == null)
        .ifPresent(temp -> itemEntity.setDoneDate(LocalDateTime.now()));
    return itemEntity;
  }

  private Item mapDoneDate(ItemEntity itemEntity, Item itemResponse) {
    Optional.of(itemEntity)
        .map(ItemEntity::getDoneDate)
        .ifPresent(itemResponse::setDoneDate);
    return itemResponse;
  }

  private void markStatusAsPassDue(Item itemRequest, ItemEntity itemEntity) {
    itemRequest.setStatus(StatusType.PAST_DUE);
    itemRequest.setDueDate(LocalDateTime.now());
    itemEntity.setStatus(StatusType.PAST_DUE);
    itemEntity.setDueDate(LocalDateTime.now());
  }

  public StatusType getStatusTypeByString(String status) {
    return Optional.ofNullable(status)
        .map(s -> findStatusType(status))
        .orElse(null);
  }

  private StatusType findStatusType(String status) {
    try {
      return StatusType.fromValue(status);
    } catch (IllegalArgumentException argumentException){
      LOGGER.error("Error by search status type: " + argumentException);
    }
    return null;
  }

  public Optional<ResponseEntity<Item>> updateItemOptional(Item itemRequest) {
    return Optional.ofNullable(itemRequest)
        .map(item -> {
          Item responseItem = null;
          try {
            ItemEntity entity = new ItemEntity();
            h2DBService.updateItem(mapUpdateItem(itemRequest, entity));
            responseItem = createResponseItem(h2DBService.checkDueDate(entity), itemRequest);
          } catch (ToDoAPIApiExceptionThrowable toDoAPIApiExceptionThrowable) {
            itemRequest.setNotification(toDoAPIApiExceptionThrowable.getToDoApiException().getNotification());
            return new ResponseEntity<>(itemRequest, HttpStatus.BAD_REQUEST);
          }
          return new ResponseEntity<>(responseItem, HttpStatus.CREATED);
        });
  }

}
