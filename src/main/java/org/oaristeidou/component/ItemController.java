package org.oaristeidou.component;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import javax.annotation.Nullable;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.oaristeidou.exception.ToDoAPIApiExceptionThrowable;
import org.oaristeidou.entity.ItemEntity;
import org.oaristeidou.model.ToDoApplicationOperation;
import org.oaristeidou.service.MappingService;
import org.oaristeidou.service.ValidationService;
import org.oaristeidou.service.impl.H2DBService;
import org.openapitools.model.Item;
import org.openapitools.model.NotificationItemReference;
import org.openapitools.model.ResponseItems;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ItemController implements ItemApi{

  private static final Logger LOGGER = LoggerFactory.getLogger(ItemController.class);

  private final H2DBService h2DBService;
  private final MappingService mappingService;
  private final ValidationService validationService;

  @Autowired
  public ItemController(final H2DBService h2DBService,
      final MappingService mappingService,
      ValidationService validationService) {
    this.h2DBService = h2DBService;
    this.mappingService = mappingService;
    this.validationService = validationService;
  }

  @Override
  public ResponseEntity<Item> addItem(@Valid @NotNull @RequestBody Item itemRequest) {
    return Optional.ofNullable(itemRequest)
        .map(request -> mappingService.createItemEntity(itemRequest, new ItemEntity()))
        .map(h2DBService::save)
        .map(entity -> new ResponseEntity<>(mappingService.createResponseItem(entity, itemRequest), HttpStatus.CREATED))
        .orElse(new ResponseEntity<>(itemRequest, HttpStatus.NOT_ACCEPTABLE));
  }

  @Override
  public void updateItem(Item itemRequest, HttpServletResponse response) {
    Optional.ofNullable(validationService.validate(itemRequest, ToDoApplicationOperation.UPDATE))
        .filter(list -> !list.isEmpty())
        .stream().flatMap(Collection::stream)
        .findFirst()
        .map(NotificationItemReference::getText)
        .map(text -> setResponseErrorHttpStatus(response, text))
        .or(() -> updateItemOptional(itemRequest, response));
  }

  @Override
  public ResponseEntity<ResponseItems> getAllItems(@RequestParam("status") @Nullable final String status) {
    return Optional.ofNullable(h2DBService.findAllByStatus(mappingService.getStatusTypeByString(status)))
        .map(this::checkDueDateItemEntities)
        .map(itemEntities -> new ResponseEntity<>(mappingService.mapEntitiesToResponseItems(itemEntities), HttpStatus.OK))
        .orElse(new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE));
  }

  @Override
  public ResponseEntity<Item> getAllDetailsById(@PathVariable("id") final Long id) {
    return Optional.ofNullable(id)
        .map(h2DBService::getAllDetailsById)
        .map(this::checkDueDate)
        .map(itemEntity -> new ResponseEntity<>(mappingService.createResponseItem(itemEntity, new Item()), HttpStatus.OK))
        .orElse(new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE));
  }

  private ItemEntity checkDueDate(ItemEntity itemEntity) {
    h2DBService.checkDueDate(itemEntity);
    return itemEntity;
  }

  private List<ItemEntity> checkDueDateItemEntities(List<ItemEntity> itemEntities) {
    itemEntities.forEach(this::checkDueDate);
    return itemEntities;
  }

  private Optional<String> updateItemOptional(Item itemRequest,
      HttpServletResponse response) {
    return Optional.ofNullable(itemRequest)
        .map(item -> {
          try {
            ItemEntity entity = new ItemEntity();
            h2DBService.updateItem(mappingService.mapUpdateItem(itemRequest, entity));
            h2DBService.checkDueDate(entity);
          } catch (ToDoAPIApiExceptionThrowable toDoAPIApiExceptionThrowable) {
            setResponseErrorHttpStatus(response, "Error by data update!");
          }
          return "OK";
        });
  }

  private String setResponseErrorHttpStatus(HttpServletResponse response, String text) {
    try {
      response.sendError(HttpStatus.BAD_REQUEST.value(), text);
    } catch (IOException e) {
      LOGGER.error("Error by processing the data!");
    }
    return "NOT";
  }


}
