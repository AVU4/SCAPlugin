package api;

import TO.ClassDescription;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import static converters.JsonToJava.DeclarationConverterToJava.*;

public class ConverterToJava {

    public List<ClassDescription> parseJsonToJava(JsonElement jsonElement) {
        List<ClassDescription> classDescriptions = new ArrayList<>();
        JsonArray module = jsonElement.getAsJsonArray();

        for (JsonElement element : module) {
            JsonArray imports = element.getAsJsonObject().getAsJsonArray("imports");
            JsonArray declarations = element.getAsJsonObject().getAsJsonArray("declarations");
            NodeList<BodyDeclaration<?>> bodyDeclarations = getDeclarations(declarations);
            classDescriptions.add(new ClassDescription(getImportDeclarations(imports), bodyDeclarations, resolveNameFile(bodyDeclarations.get(0))));
        }

        return classDescriptions;
    }

    public String resolveNameFile(BodyDeclaration<?> bodyDeclaration) {
        if (bodyDeclaration.isEnumDeclaration()) {
            return ((EnumDeclaration) bodyDeclaration).getNameAsString();
        } else if (bodyDeclaration.isClassOrInterfaceDeclaration()) {
            return ((ClassOrInterfaceDeclaration) bodyDeclaration).getNameAsString();
        }
        return "";
    }
}
