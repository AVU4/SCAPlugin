package notification;

import TO.ModuleDescription;
import api.ConverterToJava;
import api.JavaGenerator;
import com.google.gson.JsonElement;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDirectory;
import org.jetbrains.annotations.NotNull;

public class PatternAnalyzeNotificationAction extends AnAction {

    private Project project;
    private PsiDirectory module;
    private JavaGenerator javaGenerator;
    private ConverterToJava converter;
    private ModuleDescription newState;
    private ModuleDescription previousState;

    private static final String ROLLBACK_MODIFICATIONS = "Rollback modifications";

    public PatternAnalyzeNotificationAction() {
        super(ROLLBACK_MODIFICATIONS);
    }

    public void init(ModuleDescription newState, ModuleDescription previousState, Project project, PsiDirectory module) {
        this.javaGenerator = new JavaGenerator(project);
        this.converter = new ConverterToJava();
        this.newState = newState;
        this.previousState = previousState;
        this.module = module;
        this.project = project;
    }


    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        WriteCommandAction.runWriteCommandAction(project, () -> {
            javaGenerator.generateFolder(previousState, module);
            javaGenerator.deleteExtraClassFromDirectory(newState, previousState, module);
        });
    }


    public static PatternAnalyzeNotificationAction createNotificationAction(ModuleDescription newState, ModuleDescription previousState, Project project, PsiDirectory module) {
        PatternAnalyzeNotificationAction patternAnalyzeNotificationAction = new PatternAnalyzeNotificationAction();
        patternAnalyzeNotificationAction.init(newState, previousState, project, module);
        return patternAnalyzeNotificationAction;
    }


}
