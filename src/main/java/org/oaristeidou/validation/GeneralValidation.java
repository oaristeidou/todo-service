package org.oaristeidou.validation;

import static org.oaristeidou.validation.GeneralValidationImpl.isDetailsItemIncomplete;
import static org.oaristeidou.validation.GeneralValidationImpl.isDueDateGiven;
import static org.oaristeidou.validation.GeneralValidationImpl.isPastDueStatusTypeGiven;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javax.validation.constraints.NotNull;
import org.oaristeidou.model.ToDoApplicationOperation;
import org.openapitools.model.Item;
import org.openapitools.model.NotificationItemReference;
import org.openapitools.model.NotificationType;

public enum GeneralValidation {
  PAST_DUE_CANNOT_BE_CHANGE(createNotificationItemReference("01",
      NotificationType.ERROR,
      "The status type cannot be changed to 'past due'!"),
      ToDoApplicationOperation.UPDATE,
      isPastDueStatusTypeGiven()),
  DUEDATE_CANNOT_BE_CHANGE(createNotificationItemReference("02",
      NotificationType.ERROR,
      "The due date cannot be changed!"),
      ToDoApplicationOperation.UPDATE,
      isDueDateGiven()),
  DETAILS_ITEM_CANNOT_BE_NULL_OR_EMPTY(createNotificationItemReference("03",
      NotificationType.ERROR,
      "The description, status and due date are missing!"),
      ToDoApplicationOperation.ADD,
      isDetailsItemIncomplete())

  ;

  NotificationItemReference notificationItemReference;
  ToDoApplicationOperation applicationOperation;
  Predicate<Item> condition;

  GeneralValidation(NotificationItemReference notificationItemReference,
      Predicate<Item> condition) {
    this.notificationItemReference = notificationItemReference;
    this.condition = condition;
  }

  GeneralValidation(NotificationItemReference notificationItemReference,
      ToDoApplicationOperation applicationOperation,
      Predicate<Item> condition) {
    this.notificationItemReference = notificationItemReference;
    this.applicationOperation = applicationOperation;
    this.condition = condition;
  }

  public static List<NotificationItemReference> validate(Item validationObject,
      ToDoApplicationOperation applicationOperation){
    return getValidationConditions().stream()
        .filter(validation -> validation.applicationOperation == null
            || validation.applicationOperation == applicationOperation )
        .filter(validation -> validation.condition.test(validationObject))
        .map(GeneralValidation::getNotificationItemReference)
        .collect(Collectors.toList());

  }

  public static List<GeneralValidation> getValidationConditions(){
    return Arrays.stream(GeneralValidation.values())
        .filter(message -> message.condition != null)
        .collect(Collectors.toList());
  }

  @NotNull
  static NotificationItemReference createNotificationItemReference(String identityNumber,
      NotificationType notificationType, String messageText) {
    NotificationItemReference notificationItemReference = new NotificationItemReference();
    notificationItemReference.setIdentityNumber(identityNumber);
    notificationItemReference.setNotificationType(notificationType);
    notificationItemReference.setText(messageText);
    return notificationItemReference;

  }

  public NotificationItemReference getNotificationItemReference() {
    return notificationItemReference;
  }
}
