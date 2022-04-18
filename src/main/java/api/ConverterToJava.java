package api;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import static converters.JsonToJava.DeclarationConverterToJava.getDeclarations;

public class ConverterToJava {

    public List<NodeList<BodyDeclaration<?>>> parseJsonToJava(JsonElement jsonElement) {
        List<NodeList<BodyDeclaration<?>>> list = new ArrayList<>();
        JsonArray module = jsonElement.getAsJsonArray();

        for (JsonElement element : module) {
            JsonArray declarations = element.getAsJsonArray();
            list.add(getDeclarations(declarations));
        }
        return list;
    }
}
