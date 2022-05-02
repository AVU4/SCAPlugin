package notification;

import TO.ModuleDescription;
import api.JavaGenerator;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDirectory;
import org.jetbrains.annotations.NotNull;

public class RollbackAction extends AnAction {

    private Project project;
    private PsiDirectory module;
    private JavaGenerator javaGenerator;
    private ModuleDescription newState;
    private ModuleDescription previousState;

    private static final String ROLLBACK_MODIFICATIONS = "Rollback modifications";

    private RollbackAction() {
        super(ROLLBACK_MODIFICATIONS);
    }

    public void init(ModuleDescription newState, ModuleDescription previousState, Project project, PsiDirectory module) {
        this.javaGenerator = new JavaGenerator(project);
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


    public static RollbackAction createNotificationAction(ModuleDescription newState, ModuleDescription previousState, Project project, PsiDirectory module) {
        RollbackAction rollbackAction = new RollbackAction();
        rollbackAction.init(newState, previousState, project, module);
        return rollbackAction;
    }


}
