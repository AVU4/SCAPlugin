package api;

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
    public static void generateObject(BodyDeclaration<?> bodyDeclaration, String newName, Project project, PsiDirectory psiDirectory) {
        PsiFile psiFile = PsiFileFactory.getInstance(project).createFileFromText(newName, JavaLanguage.INSTANCE, bodyDeclaration.toString().replace("\r", ""));
        PsiJavaFile psiJavaFile = (PsiJavaFile) psiFile;
        Arrays.stream(psiJavaFile.getClasses()).forEach(psiDirectory::add);
    }

    public static void generateObjects(NodeList<BodyDeclaration<?>> bodyDeclarations, Project project, PsiDirectory psiDirectory) {
        bodyDeclarations.forEach(bodyDeclaration -> {
            if (bodyDeclaration.isClassOrInterfaceDeclaration()) {
                ClassOrInterfaceDeclaration classOrInterfaceDeclaration = (ClassOrInterfaceDeclaration) bodyDeclaration;
                classOrInterfaceDeclaration.setName(classOrInterfaceDeclaration.getNameAsString() + "Plugin");
                generateObject(classOrInterfaceDeclaration, classOrInterfaceDeclaration.getNameAsString(), project, psiDirectory);
            } else if (bodyDeclaration.isEnumDeclaration()) {
                EnumDeclaration enumDeclaration = (EnumDeclaration) bodyDeclaration;
                enumDeclaration.setName(enumDeclaration.getNameAsString() + "Plugin");
                generateObject(enumDeclaration, enumDeclaration.getNameAsString(), project, psiDirectory);
            }
        });
    }

}
