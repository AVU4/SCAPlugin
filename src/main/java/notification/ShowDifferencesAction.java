package notification;

import TO.ClassDescription;
import TO.Description;
import TO.ModuleDescription;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.ui.popup.ListPopup;
import org.jetbrains.annotations.NotNull;
import popup.ConfirmationListPopupStep;

import java.util.List;
import java.util.stream.Collectors;

public class ShowDifferencesAction extends AnAction {

    public static final String SHOW_DIFFERENCES = "Show differences";

    private ModuleDescription previousState;
    private ModuleDescription newState;
    private Project project;

    private ShowDifferencesAction() {
        super(SHOW_DIFFERENCES);
    }

    private void init(ModuleDescription previousState, ModuleDescription newState, Project project) {
        this.previousState = previousState;
        this.newState = newState;
        this.project = project;
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        List<Description> modifiedFiles =
                newState.getChildren().stream()
                        .filter(desc -> !desc.isLeaf() || desc.isLeaf() && ((ClassDescription) desc).getClassStatus() != ClassDescription.ClassStatus.NOT_MODIFIED)
                        .collect(Collectors.toList());
        ConfirmationListPopupStep confirmationListPopupStep = new ConfirmationListPopupStep("Smart Code Analytics", modifiedFiles, project, previousState);
        ListPopup listPopup = JBPopupFactory.getInstance().createListPopup(confirmationListPopupStep);
        listPopup.setShowSubmenuOnHover(true);
        listPopup.showCenteredInCurrentWindow(project);
    }

    public static ShowDifferencesAction createShowDifferencesAction(ModuleDescription previousState, ModuleDescription newState, Project project) {
        ShowDifferencesAction showDifferencesAction = new ShowDifferencesAction();
        showDifferencesAction.init(previousState, newState, project);
        return showDifferencesAction;
    }
}
