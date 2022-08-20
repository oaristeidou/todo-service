package org.oaristeidou.component;

import java.util.Optional;
import javax.annotation.Nullable;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.oaristeidou.entity.ItemEntity;
import org.oaristeidou.exception.ToDoAPIApiExceptionThrowable;
import org.oaristeidou.service.MappingService;
import org.oaristeidou.service.ValidationService;
import org.oaristeidou.service.impl.H2DBService;
import org.openapitools.model.Item;
import org.openapitools.model.ResponseItems;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ItemController implements ItemApi{

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
        .filter(validationService::validateAddOperation)
        .map(request -> mappingService.createItemEntity(itemRequest, new ItemEntity()))
        .map(h2DBService::save)
        .map(entity -> new ResponseEntity<>(mappingService.createResponseItem(entity, itemRequest), HttpStatus.CREATED))
        .orElse(new ResponseEntity<>(itemRequest, HttpStatus.BAD_REQUEST));
  }

  @Override
  public ResponseEntity<Item> updateItem(Item itemRequest) {
    return Optional.ofNullable(itemRequest)
        .filter(validationService::validateUpdateOperation)
        .map(mappingService::updateItemOptional)
        .filter(Optional::isPresent)
        .map(Optional::get)
        .orElse(new ResponseEntity<>(itemRequest, HttpStatus.BAD_REQUEST));
  }

  @Override
  public ResponseEntity<ResponseItems> getAllItems(@RequestParam("status") @Nullable final String status) {
    return Optional.ofNullable(h2DBService.findAllByStatus(mappingService.getStatusTypeByString(status)))
        .map(h2DBService::checkDueDate)
        .map(itemEntities -> new ResponseEntity<>(mappingService.mapEntitiesToResponseItems(itemEntities), HttpStatus.OK))
        .orElse(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
  }

  @Override
  public ResponseEntity<Item> getAllDetailsById(@PathVariable("id") final Long id) {
    return Optional.ofNullable(id)
        .map(h2DBService::getAllDetailsById)
        .map(h2DBService::checkDueDate)
        .map(itemEntity -> new ResponseEntity<>(mappingService.createResponseItem(itemEntity, new Item()), HttpStatus.OK))
        .orElse(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
  }


}
