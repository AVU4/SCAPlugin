package notification;

import com.intellij.notification.*;
import com.intellij.notification.impl.NotificationGroupEP;
import com.intellij.openapi.actionSystem.AnAction;

public class PatternAnalyzeNotification {

    private static final NotificationGroup notificationGroup =
            new NotificationGroup("pattern.analyze.notification", NotificationDisplayType.STICKY_BALLOON, true);

    public static Notification createNotification(AnAction anAction) {
        Notification notification = notificationGroup.createNotification("Do you want to rollback modifications?", NotificationType.INFORMATION);
        notification.addAction(anAction);
        return notification;
    }
}
