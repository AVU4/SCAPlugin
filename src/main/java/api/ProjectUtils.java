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
import java.util.stream.Collectors;

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

    public static PsiDirectory getPsiDirectoryByName(String name, PsiDirectory psiDirectory) {
        return Arrays.stream(psiDirectory.getSubdirectories())
                .filter(subDirectory -> subDirectory.getName().equals(name))
                .collect(Collectors.toList())
                .get(0);
    }



}
