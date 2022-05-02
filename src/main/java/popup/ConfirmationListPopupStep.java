package popup;

import TO.ClassDescription;
import TO.Description;
import com.intellij.diff.DiffContentFactory;
import com.intellij.diff.DiffManager;
import com.intellij.diff.contents.DiffContent;
import com.intellij.diff.requests.SimpleDiffRequest;
import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.PopupStep;
import com.intellij.openapi.ui.popup.util.BaseListPopupStep;
import com.intellij.openapi.util.NlsContexts;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Collectors;

public class ConfirmationListPopupStep extends BaseListPopupStep<Description> {

    private Project project;
    private Description previousState;

    public ConfirmationListPopupStep(@NlsContexts.PopupTitle @Nullable String title,
                                     List<? extends Description> values,
                                     Project project,
                                     Description previousState) {
        super(title, values);
        this.project = project;
        this.previousState = previousState;
    }

    @Override
    public @NotNull String getTextFor(Description value) {
        return value.getName();
    }

    @Override
    public boolean hasSubstep(Description selectedValue) {
        return !selectedValue.isLeaf();
    }

    @Override
    public @Nullable PopupStep<?> onChosen(Description selectedValue, boolean finalChoice) {
        if (selectedValue.isLeaf()) {
            //todo think about status
            List<Description> prevState = previousState.getChildren().stream().filter(desc -> desc.getName().equals(selectedValue.getName())).collect(Collectors.toList());
            if (prevState.size() != 0) {
                DiffManager.getInstance().showDiff(project, createSimpleDiffRequest(prevState.get(0).toString(), selectedValue.toString()));
            } else {
                DiffManager.getInstance().showDiff(project, createSimpleDiffRequest("", selectedValue.toString()));
            }
            return FINAL_CHOICE;
        } else {
            Description description = previousState.getChildren().stream().filter(desc -> selectedValue.getName().equals(desc.getName())).collect(Collectors.toList()).get(0);
            List<Description> modifiedFiles =
                    selectedValue.getChildren().stream()
                            .filter(desc -> !desc.isLeaf() || desc.isLeaf() && ((ClassDescription) desc).getClassStatus() != ClassDescription.ClassStatus.NOT_MODIFIED)
                            .collect(Collectors.toList());
            return new ConfirmationListPopupStep(null, modifiedFiles, project, description);
        }
    }

    private SimpleDiffRequest createSimpleDiffRequest(String prevContent, String newContent) {
        DiffContent prevStateOfFile = DiffContentFactory.getInstance().create(prevContent, JavaFileType.INSTANCE);
        DiffContent nextStateOfFile = DiffContentFactory.getInstance().create(newContent, JavaFileType.INSTANCE);
        return new SimpleDiffRequest("Comparing files", prevStateOfFile, nextStateOfFile, "Previous state", "New State");
    }

}
