import TO.ModuleDescription;
import api.ConverterToJava;
import api.JavaGenerator;
import api.JavaParserAdapter;
import api.ProjectUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.intellij.notification.Notification;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.psi.PsiDirectory;
import notification.PatternAnalyzeNotification;
import notification.RollbackAction;
import notification.ShowDifferencesAction;
import org.jetbrains.annotations.NotNull;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class PatternAnalyzeAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        if (project != null) {
            PsiDirectory psiDirectory = ProjectUtils.getSourceDirectory(project);
            VirtualFileManager.getInstance().asyncRefresh(() -> generateCode(project, psiDirectory));
        }
    }

    private void generateCode(Project project, PsiDirectory psiDirectory) {
        WriteCommandAction.runWriteCommandAction(project, () -> {
            JavaGenerator javaGenerator = new JavaGenerator(project);
            ConverterToJava converter = new ConverterToJava();

            JsonElement json = JavaParserAdapter.parseModule(psiDirectory);
            ModuleDescription previousState = converter.parseJsonToJava(json, project);
            System.out.println(json);
            try {
                JsonElement jsonObject = JsonParser.parseReader(new InputStreamReader(new FileInputStream("C:\\Users\\lexa2\\Desktop\\JsonMock.txt")));

                ModuleDescription newState = converter.parseJsonToJava(jsonObject, project);
                javaGenerator.generateFolder(newState, psiDirectory);

                RollbackAction rollbackAction = RollbackAction.createNotificationAction(newState, previousState, project, psiDirectory);
                ShowDifferencesAction showDifferencesAction = ShowDifferencesAction.createShowDifferencesAction(previousState, newState, project);
                Notification notification = PatternAnalyzeNotification.createNotification(rollbackAction, showDifferencesAction);
                Notifications.Bus.notify(notification, project);

            }catch (IOException exception) {
                System.out.println(exception.getMessage());
            }

        });
    }
}
