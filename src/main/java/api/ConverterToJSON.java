package api;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.List;

import static converters.JavaToJson.DeclarationConverterToJSON.getDeclaration;

public class ConverterToJSON {

    public JsonArray converter(List<CompilationUnit> nodes) {
        JsonArray module = new JsonArray();

        for (int index = 0; index < nodes.size(); index ++) {
            CompilationUnit unit = nodes.get(index);

            List<Node> children = unit.getChildNodes();
            JsonArray jsonChildren = new JsonArray();

            for (int i = 0; i < children.size(); i ++) {
                Node node = children.get(i);
                jsonChildren.add(getDeclaration((BodyDeclaration<?>) node));
            }

            module.add(jsonChildren);

        }

        return module;
    }

}
