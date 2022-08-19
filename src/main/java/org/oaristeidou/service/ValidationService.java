package org.oaristeidou.service;

import java.util.ArrayList;
import java.util.List;
import org.oaristeidou.model.ToDoApplicationOperation;
import org.oaristeidou.validation.GeneralValidation;
import org.openapitools.model.Item;
import org.openapitools.model.NotificationItemReference;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

@Service
@RequestScope
public class ValidationService {

  public List<NotificationItemReference> validate(Item item,
      ToDoApplicationOperation update){
    return new ArrayList<>(GeneralValidation.validate(item, update));
  }

}
