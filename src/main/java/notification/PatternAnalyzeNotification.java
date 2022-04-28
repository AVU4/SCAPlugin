package notification;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationDisplayType;
import com.intellij.notification.NotificationGroup;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.actionSystem.AnAction;

public class PatternAnalyzeNotification {

    private static final NotificationGroup notificationGroup =
            new NotificationGroup("pattern.analyze.notification", NotificationDisplayType.BALLOON, true);

    public static Notification createNotification(AnAction anAction) {
        Notification notification = notificationGroup.createNotification("Do you want to rollback modifications?", NotificationType.INFORMATION);
        notification.addAction(anAction);
        return notification;
    }
}
