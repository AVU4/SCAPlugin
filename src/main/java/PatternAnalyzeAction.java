import com.github.javaparser.*;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.printer.YamlPrinter;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import org.jetbrains.annotations.NotNull;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

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

                VariableDeclarator variableDeclarator1 = new VariableDeclarator();
                variableDeclarator1.setName("person");
                variableDeclarator1.setType(new ClassOrInterfaceType("Person"));

                FieldDeclaration fieldDeclaration = new FieldDeclaration();
                fieldDeclaration.setModifiers(Modifier.Keyword.PRIVATE);
                fieldDeclaration.setVariables(NodeList.nodeList(variableDeclarator));

                FieldDeclaration fieldDeclaration1 = new FieldDeclaration();
                fieldDeclaration1.setModifiers(Modifier.Keyword.PRIVATE);
                fieldDeclaration1.setVariables(NodeList.nodeList(variableDeclarator1));



                Parameter parameter = new Parameter();
                parameter.setType(long.class);
                parameter.setName("id");


                BlockStmt blockStmt = new BlockStmt();
                ExpressionStmt expressionStmt = new ExpressionStmt();
                AssignExpr assignExpr = new AssignExpr();
                assignExpr.setOperator(AssignExpr.Operator.ASSIGN);
                assignExpr.setTarget(new FieldAccessExpr().setName("id"));
                assignExpr.setValue(new NameExpr().setName("id"));

                expressionStmt.setExpression(assignExpr);
                blockStmt.setStatements(NodeList.nodeList(expressionStmt));

                MethodDeclaration setterId = new MethodDeclaration();
                setterId.setModifiers(Modifier.Keyword.PUBLIC);
                setterId.setType(void.class);
                setterId.setName("setId");
                setterId.setParameters(NodeList.nodeList(parameter));
                setterId.setBody(blockStmt);

                BlockStmt blockStmt1 = new BlockStmt();
                ReturnStmt returnStmt = new ReturnStmt();
                returnStmt.setExpression(new NameExpr().setName("id"));
                blockStmt1.setStatements(NodeList.nodeList(returnStmt));


                MethodDeclaration getterId = new MethodDeclaration();
                getterId.setType(long.class);
                getterId.setModifiers(Modifier.Keyword.PUBLIC);
                getterId.setName("getId");
                getterId.setBody(blockStmt1);

                testClass.add(psiElementFactory.createMethodFromText(getterId.toString().replace("\r", ""), testClass));
                testClass.add(psiElementFactory.createMethodFromText(setterId.toString().replace("\r", ""), testClass));
                testClass.add(psiElementFactory.createFieldFromText(fieldDeclaration.toString(), testClass));
                testClass.add(psiElementFactory.createFieldFromText(fieldDeclaration1.toString(), testClass));

                // todo think about PsiBuilder, PsiParser

//                ASTNode astNode = testClass.getNode();
//                ASTNode root = astNode.getFirstChildNode();
//                try {
//                    exampleOfGenerateASTTreeInYAML();
//                } catch (FileNotFoundException ex) {
//                    ex.printStackTrace();
//                }
                PsiFile[] psiFiles = psiDirectory.getFiles();
                try {
                    PsiFile psiFile = psiFiles[4];
                    JavaParser javaParser = new JavaParser();
                    ParseResult<CompilationUnit> result = javaParser.parse(psiFile.getVirtualFile().getInputStream());
                    CompilationUnit compilationUnit = result.getResult().get();
                    List<Node> children = compilationUnit.getChildNodes();
                    YamlPrinter yamlPrinter = new YamlPrinter(true);
                    System.out.println(yamlPrinter.output(compilationUnit.getChildNodes().get(0)));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                if (ProjectUtils.directoryContainsFileWithName(testClass.getName(), psiDirectory)) {
                    PsiFile psiFile = ProjectUtils.getFileFromDirectoryByName(testClass.getName(), psiDirectory);
                    psiFile.delete();
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
