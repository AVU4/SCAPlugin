package api;

import TO.ClassDescription;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.intellij.lang.Language;
import com.intellij.lang.java.JavaLanguage;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import org.apache.tools.ant.taskdefs.Java;

import java.util.Arrays;

public class ProjectUtils {

    public static PsiDirectory getSourceDirectory(Project project) {

        //todo Think about multi modules projects
        ModuleManager moduleManager = ModuleManager.getInstance(project);
        Module module = moduleManager.findModuleByName(project.getName());
        ModuleRootManager moduleRootManager = ModuleRootManager.getInstance(module);
        VirtualFile[] virtualFiles = moduleRootManager.getSourceRoots();
        for (VirtualFile virtualFile : virtualFiles) {
            System.out.println(virtualFile.getPath());
            if (virtualFile.getPath().endsWith("main/java")) {
                return PsiManager.getInstance(project).findDirectory(virtualFile);
            }
        }
        return null;
    }

    public static boolean directoryContainsFileWithName(String name, PsiDirectory directory) {
        for (PsiFile psiFile : directory.getFiles()) {
            if (psiFile.getName().equals(name + ".java")) {
                return true;
            }
        }
        return false;
    }

    public static PsiFile getFileFromDirectoryByName(String name, PsiDirectory directory) {
        for (PsiFile psiFile : directory.getFiles()) {
            if (psiFile.getName().equals(name + ".java")) {
                return psiFile;
            }
        }
        return null;
    }

    //todo Think about append Plugin
    public static void generateClasses(ClassDescription classDescription, Project project, PsiDirectory psiDirectory) {
        StringBuilder classText = new StringBuilder("");
        classDescription.getImportDeclarations().stream()
                .map(ImportDeclaration::toString)
                .map(text -> text.replace("\r", ""))
                .forEach(classText::append);
        classDescription.getBodyDeclarations().stream()
                .map(ProjectUtils::getDeclarationData)
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

    public static String getDeclarationData(BodyDeclaration<?> bodyDeclaration) {
        if (bodyDeclaration.isClassOrInterfaceDeclaration()) {
            ClassOrInterfaceDeclaration classOrInterfaceDeclaration = (ClassOrInterfaceDeclaration) bodyDeclaration;
            classOrInterfaceDeclaration.setName(classOrInterfaceDeclaration.getNameAsString());
            return classOrInterfaceDeclaration.toString();
        } else if (bodyDeclaration.isEnumDeclaration()) {
            EnumDeclaration enumDeclaration = (EnumDeclaration) bodyDeclaration;
            enumDeclaration.setName(enumDeclaration.getNameAsString());
            return enumDeclaration.toString();
        }
        return "";
    }

}
