package org.oaristeidou.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.oaristeidou.model.ToDoApplicationOperation;
import org.oaristeidou.validation.GeneralValidation;
import org.openapitools.model.Item;
import org.openapitools.model.NotificationItemReference;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

/**
 * ValidationService is a service for performing all the enum validations,
 * which are categories in different types.
 */
@Service
@RequestScope
public class ValidationService {

  public Boolean validateAddOperation(Item item){
    return Optional.of(new ArrayList<>(GeneralValidation.validate(item, ToDoApplicationOperation.ADD)))
        .filter(list -> !list.isEmpty())
        .map(notificationItemReferences -> addNotificationsIntoResponse(item, notificationItemReferences))
        .orElse(true);
  }

  public Boolean validateUpdateOperation(Item item){
    return Optional.of(new ArrayList<>(GeneralValidation.validate(item, ToDoApplicationOperation.UPDATE)))
        .filter(list -> !list.isEmpty())
        .map(notificationItemReferences -> addNotificationsIntoResponse(item, notificationItemReferences))
        .orElse(true);
  }


  private boolean addNotificationsIntoResponse(Item itemRequest,
      List<NotificationItemReference> notificationItemReferences) {
    itemRequest.setNotification(new ArrayList<>(notificationItemReferences));
    return false;
  }
}
