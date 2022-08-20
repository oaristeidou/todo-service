package org.oaristeidou.service.impl;

import static org.oaristeidou.validation.DatabaseValidation.ERROR_BY_SEARCHING_ITEM_ID;
import static org.oaristeidou.validation.GeneralValidation.PAST_DUE_CANNOT_BE_CHANGE;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.oaristeidou.entity.ItemEntity;
import org.oaristeidou.exception.ToDoAPIApiExceptionThrowable;
import org.oaristeidou.repository.ItemRepository;
import org.openapitools.model.NotificationItemReference;
import org.openapitools.model.StatusType;
import org.openapitools.model.ToDoApiException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public interface H2DBService extends ItemRepository {

  @Override
  default List<ItemEntity> findAllByStatus(StatusType status) {
    return Optional.of(findAll())
        .stream().flatMap(Collection::stream)
        .filter(itemEntity -> status == null || status.equals(itemEntity.getStatus()))
        .collect(Collectors.toList());
  }

  @Override
  default ItemEntity getAllDetailsById(Long id) {
    return Optional.ofNullable(id)
        .map(this::findById)
        .filter(Optional::isPresent)
        .map(Optional::get)
        .orElse(null);
  }

  default ItemEntity checkDueDate(ItemEntity itemEntity){
    return Optional.ofNullable(itemEntity)
        .filter(entity -> entity.getDueDate() != null)
        .filter(entity -> entity.getDueDate().isBefore(LocalDateTime.now()))
        .filter(entity -> StatusType.DONE != entity.getStatus())
        .map(entity -> {
          entity.setStatus(StatusType.PAST_DUE);
          entity.setDueDate(LocalDateTime.now());
          save(entity);
          return entity;
        }).orElse(itemEntity);
  }

  default List<ItemEntity> checkDueDate(List<ItemEntity> itemEntities){
    itemEntities.forEach(this::checkDueDate);
    return itemEntities;
  }

  default ItemEntity updateItem(ItemEntity itemEntity)
      throws ToDoAPIApiExceptionThrowable {
    ItemEntity retrievedEntity = Optional.of(itemEntity)
        .map(ItemEntity::getId)
        .map(this::findById)
        .filter(Optional::isPresent)
        .map(Optional::get)
        .orElseThrow(() -> createToDoAPIApiExceptionThrowable(ERROR_BY_SEARCHING_ITEM_ID.getNotificationItemReference()));
    return Optional.of(retrievedEntity)
        .filter(item -> StatusType.PAST_DUE != item.getStatus())
        .map(entityItem -> setAndSaveEntity(itemEntity, entityItem))
        .orElseThrow(() -> createToDoAPIApiExceptionThrowable(PAST_DUE_CANNOT_BE_CHANGE.getNotificationItemReference()))
    ;
  }

  private ItemEntity setAndSaveEntity(ItemEntity itemEntity, ItemEntity entity) {
    entity.setDescription(itemEntity.getDescription());
    entity.setStatus(itemEntity.getStatus());
    entity.setDoneDate(itemEntity.getDoneDate());
    save(entity);
    return entity;
  }

  private ToDoAPIApiExceptionThrowable createToDoAPIApiExceptionThrowable(
      NotificationItemReference notificationItemReference) {
    ToDoApiException toDoApiException = new ToDoApiException();
    toDoApiException.addNotificationItem(notificationItemReference);
    return new ToDoAPIApiExceptionThrowable(toDoApiException, HttpStatus.BAD_REQUEST);
  }
}
