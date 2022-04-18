package api;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class JavaParserAdapter {

    public static JsonElement parseModule(PsiDirectory psiDirectory) {
        //todo Make for Subdirectories
        //todo Need to refresh project to changes will be access
        PsiFile[] psiFiles = psiDirectory.getFiles();
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
        try {
            ParseResult<CompilationUnit> result =  parser.parse(psiFile.getVirtualFile().getInputStream());
            if (result.isSuccessful() && result.getResult().isPresent()) {
                return result.getResult().get();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
