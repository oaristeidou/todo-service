package org.oaristeidou.component;

import io.swagger.annotations.Api;
import java.io.IOException;
import javax.annotation.Nullable;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.oaristeidou.exception.ToDoAPIApiExceptionThrowable;
import org.openapitools.model.Item;
import org.openapitools.model.ResponseItems;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

@Api(value = "v1/todo-list/items")
public interface ItemApi {

  @PostMapping(value = "v1/todo-list/items/addItem", produces = {"application/json"}, consumes = {"application/json"})
  ResponseEntity<Item> addItem(@Valid @NotNull @RequestBody Item request)
      throws ToDoAPIApiExceptionThrowable;

  @PatchMapping(value = "v1/todo-list/items/updateItem", produces = {"application/json"}, consumes = {"application/json"})
  @ResponseStatus(HttpStatus.NO_CONTENT)
  ResponseEntity<Item> updateItem(@Valid @NotNull @RequestBody Item request)
      throws ToDoAPIApiExceptionThrowable, IOException;

  @GetMapping(value = "v1/todo-list/items/getAllItems", produces = {"application/json"}, consumes = {"application/json"})
  ResponseEntity<ResponseItems> getAllItems(@RequestParam("status") @Nullable final String status)
      throws ToDoAPIApiExceptionThrowable;

  @GetMapping(value = "v1/todo-list/items/getAllDetailsById/{id}", produces = {"application/json"})
  ResponseEntity<Item> getAllDetailsById(@PathVariable("id") final Long id)
      throws ToDoAPIApiExceptionThrowable;

}
