package api;

import TO.ClassDescription;
import TO.Description;
import TO.PackageDescription;
import com.github.javaparser.ast.ImportDeclaration;
import com.intellij.lang.java.JavaLanguage;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;
import com.intellij.psi.PsiJavaFile;

import java.util.Arrays;
import java.util.function.BiConsumer;

public class JavaGenerator {

    private Project project;
    private final BiConsumer<Description, PsiDirectory> descriptionBiConsumer = ((description, psiDirectory) -> {
        if (!description.isLeaf()) {
            PackageDescription packageDescription = (PackageDescription) description;
            generateFolder(packageDescription, ProjectUtils.getPsiDirectoryByName(packageDescription.getName(), psiDirectory));
        } else {
            generateClassDescription((ClassDescription) description, psiDirectory);
        }
    });

    public JavaGenerator(Project project) {
        this.project = project;
    }

    public void generateFolder(Description description, PsiDirectory psiDirectory) {
        description.getChildren().forEach(desc -> descriptionBiConsumer.accept(desc, psiDirectory));
    }

    public void generateClassDescription(ClassDescription classDescription, PsiDirectory psiDirectory) {
        StringBuilder classText = new StringBuilder("");
        classDescription.getImportDeclarations().stream()
                .map(ImportDeclaration::toString)
                .map(text -> text.replace("\r", ""))
                .forEach(classText::append);
        classDescription.getBodyDeclarations().stream()
                .map(Util::getDeclarationData)
                .map(text -> text.replace("\r", ""))
                .forEach(classText::append);

        PsiFile psiFile = PsiFileFactory.getInstance(project).createFileFromText(classDescription.getName(), JavaLanguage.INSTANCE, classText);

        Arrays.stream(((PsiJavaFile) psiFile).getClasses()).forEach(psiClass -> {
            if (ProjectUtils.directoryContainsFileWithName(psiClass.getName(), psiDirectory)) {
                PsiFile fileToRemove = ProjectUtils.getFileFromDirectoryByName(psiClass.getName(), psiDirectory);
                fileToRemove.delete();
            }
            psiDirectory.add(psiClass);
        });
    }
}
