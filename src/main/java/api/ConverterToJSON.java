package api;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.List;

import static converters.JavaToJson.DeclarationConverterToJSON.getDeclaration;
import static converters.JavaToJson.DeclarationConverterToJSON.getImportDeclaration;

public class ConverterToJSON {

    public JsonArray converter(List<CompilationUnit> nodes) {
        JsonArray module = new JsonArray();

        for (int index = 0; index < nodes.size(); index ++) {
            CompilationUnit unit = nodes.get(index);

            List<Node> children = unit.getChildNodes();
            JsonArray jsonImports = new JsonArray();
            JsonArray jsonDeclarations = new JsonArray();
            for (int i = 0; i < children.size(); i ++) {
                Node node = children.get(i);
                if (node instanceof ImportDeclaration) {
                    jsonImports.add(getImportDeclaration((ImportDeclaration) node));
                } else if (node instanceof BodyDeclaration<?>) {
                    jsonDeclarations.add(getDeclaration((BodyDeclaration<?>) node));
                }
            }

            JsonObject jsonClass = new JsonObject();
            jsonClass.add("imports", jsonImports);
            jsonClass.add("declarations", jsonDeclarations);

            module.add(jsonClass);

        }

        return module;
    }

}
