package ss.martin.notification.email.api.model;

import java.io.File;

/**
 * Email attachment.
 * @author alex
 */
public record EmailAttachment(
        String name,
        String contentType,
        File file
) {}
