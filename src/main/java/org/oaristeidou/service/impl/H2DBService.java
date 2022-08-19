package org.oaristeidou.service.impl;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.oaristeidou.exception.ToDoAPIApiExceptionThrowable;
import org.oaristeidou.entity.ItemEntity;
import org.oaristeidou.repository.ItemRepository;
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

  default void checkDueDate(ItemEntity itemEntity){
    Optional.ofNullable(itemEntity)
        .filter(entity -> entity.getDueDate() != null)
        .filter(entity -> entity.getDueDate().isBefore(LocalDateTime.now()))
        .filter(entity -> StatusType.DONE != entity.getStatus())
        .ifPresent(entity -> {
          entity.setStatus(StatusType.PAST_DUE);
          entity.setDueDate(LocalDateTime.now());
          save(entity);
        });
  }

  default void updateItem(ItemEntity itemEntity)
      throws ToDoAPIApiExceptionThrowable {
    Optional.ofNullable(itemEntity)
        .map(ItemEntity::getId)
        .map(this::findById)
        .filter(Optional::isPresent)
        .map(Optional::get)
        .filter(item -> StatusType.PAST_DUE != item.getStatus())
        .map(entity -> setAndSaveEntity(itemEntity, entity))
        .orElseThrow(this::createToDoAPIApiExceptionThrowable)
    ;
  }

  private ItemEntity setAndSaveEntity(ItemEntity itemEntity, ItemEntity entity) {
    entity.setDescription(itemEntity.getDescription());
    entity.setStatus(itemEntity.getStatus());
    entity.setDoneDate(itemEntity.getDoneDate());
    save(entity);
    return entity;
  }

  private ToDoAPIApiExceptionThrowable createToDoAPIApiExceptionThrowable() {
    return new ToDoAPIApiExceptionThrowable(new ToDoApiException(), HttpStatus.BAD_REQUEST);
  }
}
