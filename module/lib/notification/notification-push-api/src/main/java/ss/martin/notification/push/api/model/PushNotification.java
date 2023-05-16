package ss.martin.notification.push.api.model;

/**
 * Push notification.
 * @author alex
 */
public record PushNotification(
        String title,
        String body,
        String icon,
        String clickAction,
        String clickActionLabel,
        String ttlInSeconds,
        String data
) {}
