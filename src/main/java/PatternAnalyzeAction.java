import com.github.javaparser.*;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.metamodel.NodeMetaModel;
import com.github.javaparser.printer.XmlPrinter;
import com.github.javaparser.printer.YamlPrinter;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import com.intellij.designer.model.MetaModel;
import com.intellij.lang.*;
import com.intellij.lang.impl.PsiBuilderFactoryImpl;
import com.intellij.lang.impl.PsiBuilderImpl;
import com.intellij.navigation.NavigationItem;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.DumbService;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.*;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.ui.popup.Balloon;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.psi.*;
import com.intellij.psi.impl.file.PsiDirectoryFactory;
import com.intellij.psi.impl.file.PsiJavaDirectoryImpl;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.ProjectScope;
import com.intellij.ui.awt.RelativePoint;
import org.apache.tools.ant.taskdefs.Java;
import org.jetbrains.annotations.NotNull;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Arrays;

public class PatternAnalyzeAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        if (project != null) {
            PsiDirectory psiDirectory = ProjectUtils.getSourceDirectory(project);

            WriteCommandAction.runWriteCommandAction(project, () -> {
                PsiElementFactory psiElementFactory = JavaPsiFacade.getInstance(project).getElementFactory();

                PsiClass testClass = psiElementFactory.createClass("Test");

                VariableDeclarator variableDeclarator = new VariableDeclarator();
                variableDeclarator.setName("id");
                variableDeclarator.setType(long.class);

                FieldDeclaration fieldDeclaration = new FieldDeclaration();
                fieldDeclaration.setModifiers(Modifier.Keyword.PRIVATE);
                fieldDeclaration.setVariables(NodeList.nodeList(variableDeclarator));

                Parameter parameter = new Parameter();
                parameter.setType(long.class);
                parameter.setName("id");


                MethodDeclaration setterId = new MethodDeclaration();
                setterId.setModifiers(Modifier.Keyword.PUBLIC);
                setterId.setType(void.class);
                setterId.setName("setId");
                setterId.setParameters(NodeList.nodeList(parameter));



                MethodDeclaration getterId = new MethodDeclaration();
                getterId.setType(long.class);
                getterId.setModifiers(Modifier.Keyword.PUBLIC);
                getterId.setName("getId");

                testClass.add(psiElementFactory.createMethodFromText(getterId.toString().replace("\r", ""), testClass));
                testClass.add(psiElementFactory.createMethodFromText(setterId.toString().replace("\r", ""), testClass));
                testClass.add(psiElementFactory.createFieldFromText(fieldDeclaration.toString(), testClass));

                // todo think about PsiBuilder, PsiParser

                ASTNode astNode = testClass.getNode();
                ASTNode root = astNode.getFirstChildNode();
                try {
                    exampleOfGenerateASTTreeInYAML();
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                }

                psiDirectory.add(testClass);
            });


        }
    }

    private void exampleOfGenerateASTTreeInYAML() throws FileNotFoundException {
        JavaParser javaParser = new JavaParser();

        Node node = javaParser.parseBodyDeclaration("class Test{ public int x;" +
                "public void setX(int x) {" +
                "if (x < 5) {" +
                "   this.x = 5;" +
                "}" +
                "} }").getResult().get();
        System.out.println(new YamlPrinter(true).output(node));

    }
}
