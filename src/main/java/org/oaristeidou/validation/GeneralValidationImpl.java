package org.oaristeidou.validation;

import java.util.Optional;
import java.util.function.Predicate;
import org.apache.logging.log4j.util.Strings;
import org.openapitools.model.Item;
import org.openapitools.model.StatusType;

public class GeneralValidationImpl {

  public static Predicate<Item> isPastDueStatusTypeGiven(){
    return item -> Optional.ofNullable(item)
        .map(Item::getStatus)
        .map(StatusType.PAST_DUE::equals)
        .orElse(false);
  }

  public static Predicate<Item> isDueDateGiven(){
    return item -> Optional.ofNullable(item)
        .map(temp -> item.getDueDate() != null)
        .orElse(false);
  }

  public static Predicate<Item> isDetailsItemIncomplete(){
    return item -> Optional.ofNullable(item)
        .map(temp -> item.getDueDate() == null
            || Strings.isBlank(item.getDescription())
            || item.getStatus() == null)
        .orElse(false);
  }

}
