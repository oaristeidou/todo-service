package org.oaristeidou.validation;

import javax.validation.constraints.NotNull;
import org.openapitools.model.NotificationItemReference;
import org.openapitools.model.NotificationType;

/**
 * Database validation class is an enum for containing
 * the check of all the Database notification types.
 */
public enum DatabaseValidation {
  ERROR_BY_SEARCHING_ITEM_ID(createNotificationItemReference("04",
      NotificationType.ERROR,
      "Error by finding the item id!"))
  ;

  NotificationItemReference notificationItemReference;

  DatabaseValidation(
      NotificationItemReference notificationItemReference) {
    this.notificationItemReference = notificationItemReference;
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
