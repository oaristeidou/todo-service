package org.oaristeidou.validation;

import java.util.Optional;
import java.util.function.Predicate;
import org.apache.logging.log4j.util.Strings;
import org.openapitools.model.Item;
import org.openapitools.model.StatusType;

/**
 * The implementations of the General validations
 */
public class GeneralValidationImpl {

  /**
   * Check if the Item has status 'past due'
   * @return Predicate<Item>
   */
  public static Predicate<Item> isPastDueStatusTypeGiven(){
    return item -> Optional.ofNullable(item)
        .map(Item::getStatus)
        .map(StatusType.PAST_DUE::equals)
        .orElse(false);
  }

  /**
   * Check if the due date of an item is not null
   * @return Predicate<Item>
   */
  public static Predicate<Item> isDueDateGiven(){
    return item -> Optional.ofNullable(item)
        .map(temp -> item.getDueDate() != null)
        .orElse(false);
  }

  /**
   * Check if the description, status or due date is missing
   * @return Predicate<Item>
   */
  public static Predicate<Item> isDetailsItemIncomplete(){
    return item -> Optional.ofNullable(item)
        .map(temp -> item.getDueDate() == null
            || Strings.isBlank(item.getDescription())
            || item.getStatus() == null)
        .orElse(false);
  }

}
