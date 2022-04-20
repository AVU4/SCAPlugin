import TO.ModuleDescription;
import api.*;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import org.jetbrains.annotations.NotNull;

import java.io.*;

public class PatternAnalyzeAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        if (project != null) {
            PsiDirectory psiDirectory = ProjectUtils.getSourceDirectory(project);
            JavaGenerator javaGenerator = new JavaGenerator(project);

            WriteCommandAction.runWriteCommandAction(project, () -> {

                JsonElement json = JavaParserAdapter.parseModule(psiDirectory);
                System.out.println(json);
                try {
                    JsonElement jsonObject = JsonParser.parseReader(new InputStreamReader(new FileInputStream("C:\\Users\\lexa2\\Desktop\\JsonMock.txt")));
                    ConverterToJava converter = new ConverterToJava();
                    ModuleDescription result = converter.parseJsonToJava(jsonObject, project);
                    javaGenerator.generateFolder(result, psiDirectory);
                }catch (IOException exception) {
                    System.out.println(exception.getMessage());
                }

            });


        }
    }
}
