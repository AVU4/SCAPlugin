package api;

import TO.ClassDescription;
import TO.Description;
import TO.PackageDescription;
import com.intellij.lang.java.JavaLanguage;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;
import com.intellij.psi.PsiJavaFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public class JavaGenerator {

    private Project project;
    private final BiConsumer<Description, PsiDirectory> generateDescriptionConsumer = ((description, psiDirectory) -> {
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
        VirtualFileManager.getInstance().syncRefresh();
        description.getChildren().forEach(desc -> generateDescriptionConsumer.accept(desc, psiDirectory));
    }

    public void generateClassDescription(ClassDescription classDescription, PsiDirectory psiDirectory) {
        String classText = classDescription.toString();
        PsiFile psiFile = PsiFileFactory.getInstance(project).createFileFromText(classDescription.getName(), JavaLanguage.INSTANCE, classText);
        Arrays.stream(((PsiJavaFile) psiFile).getClasses()).forEach(psiClass -> {
            if (ProjectUtils.directoryContainsFileWithName(psiClass.getName(), psiDirectory)) {
                PsiFile fileToRemove = ProjectUtils.getFileFromDirectoryByName(psiClass.getName(), psiDirectory);
                fileToRemove.delete();
            }
            psiDirectory.add(psiClass);
        });
    }

    public void deleteExtraClassFromDirectory(Description description, Description previousDescription, PsiDirectory psiDirectory) {
        List<Description> descriptionsToCheck = new ArrayList<>();
        List<Description> currentChildren = description.getChildren();
        currentChildren.forEach(description1 -> {
            List<String> previousDescriptionName = previousDescription.getChildren().stream().map(Description::getName).collect(Collectors.toList());

            if (!previousDescriptionName.contains(description1.getName())) {
                if (description1.isLeaf()) {
                    ProjectUtils.deleteFile(description1.getName(), psiDirectory);
                } else {
                    ProjectUtils.deleteDirectory(description1.getName(), psiDirectory);
                }
            } else if (!description1.isLeaf() && previousDescription.getChildren().contains(description1)) {
                descriptionsToCheck.add(description1);
            }
        });

        descriptionsToCheck.forEach(description1 -> {
            Description descriptionFromPreviousState = previousDescription.getChildren().get(previousDescription.getChildren().indexOf(description1));
            deleteExtraClassFromDirectory(description1, descriptionFromPreviousState, ProjectUtils.getPsiDirectoryByName(description1.getName(), psiDirectory));
        });

        VirtualFileManager.getInstance().syncRefresh();
    }
}
