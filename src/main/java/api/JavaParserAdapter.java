package api;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class JavaParserAdapter {

    public static JsonElement parseModule(PsiDirectory psiModule) {
        //todo Need to refresh project to changes will be access

        JsonObject jsonModule = new JsonObject();
        jsonModule.add(psiModule.getName(), parseDirectory(psiModule));
        return jsonModule;
    }

    private static JsonElement parseDirectory(PsiDirectory psiDirectory) {
        JsonArray jsonDirectories = new JsonArray();
        for (PsiDirectory subDirectory : psiDirectory.getSubdirectories()) {
            JsonObject jsonDirectory = new JsonObject();
            jsonDirectory.add(subDirectory.getName(), parseDirectory(subDirectory));
            jsonDirectories.add(jsonDirectory);
        }
        JsonElement jsonFiles = parseFilesFromDirectory(psiDirectory.getFiles());
        JsonObject jsonFilesNotInPackage = new JsonObject();
        jsonFilesNotInPackage.add("WithoutPackage", jsonFiles);
        jsonDirectories.add(jsonFilesNotInPackage);
        return jsonDirectories;
    }

    private static JsonElement parseFilesFromDirectory(PsiFile[] psiFiles) {
        JavaParser javaParser = new JavaParser();

        List<CompilationUnit> nodes = Arrays.stream(psiFiles)
                .map(psiFile -> parseFile(psiFile, javaParser))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        ConverterToJSON converter = new ConverterToJSON();

        JsonElement json = converter.converter(nodes);

        return json;
    }

    private static CompilationUnit parseFile(PsiFile psiFile, com.github.javaparser.JavaParser parser) {
        ParseResult<CompilationUnit> result =  parser.parse(psiFile.getText());
        if (result.isSuccessful() && result.getResult().isPresent()) {
            return result.getResult().get();
        }
        return null;
    }
}
