package notification;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationDisplayType;
import com.intellij.notification.NotificationGroup;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.actionSystem.AnAction;

public class PatternAnalyzeNotification {

    private static final NotificationGroup notificationGroup =
            new NotificationGroup("pattern.analyze.notification", NotificationDisplayType.STICKY_BALLOON, true);

    public static Notification createNotification(AnAction rollbackAction, AnAction showDifferences) {
        Notification notification = notificationGroup.createNotification("Do you want to rollback modifications?", NotificationType.INFORMATION);
        notification.addAction(rollbackAction);
        notification.addAction(showDifferences);
        return notification;
    }
}
