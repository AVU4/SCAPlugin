package api;

import TO.ClassDescription;
import TO.Description;
import TO.ModuleDescription;
import TO.PackageDescription;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.intellij.openapi.project.Project;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

import static converters.JsonToJava.DeclarationConverterToJava.getDeclarations;
import static converters.JsonToJava.DeclarationConverterToJava.getImportDeclarations;

public class ConverterToJava {

    private final BiConsumer<Description, JsonArray> jsonArrayConsumer = ((description, jsonArray) -> {
        for (JsonElement element : jsonArray) {
            String key = Util.getKey(element.getAsJsonObject());
            if (!key.equals("WithoutPackage")) {
                description.add(getPackageDescription(element.getAsJsonObject(), key));
            } else {
                description.add(getClassDescriptions(element.getAsJsonObject(), key));
            }
        }
    });

    public ModuleDescription parseJsonToJava(JsonElement jsonElement, Project project) {
        //todo Remade when add multi modules
        ModuleDescription moduleDescription = new ModuleDescription(project.getName());
        JsonArray module = jsonElement.getAsJsonObject().getAsJsonArray("java");
        jsonArrayConsumer.accept(moduleDescription, module);
        return moduleDescription;
    }

    public PackageDescription getPackageDescription(JsonObject jsonObject, String key) {
        PackageDescription packageDescription = new PackageDescription(key);
        JsonArray jsonPackage = jsonObject.getAsJsonArray(key);
        jsonArrayConsumer.accept(packageDescription, jsonPackage);
        return packageDescription;
    }

    public List<ClassDescription> getClassDescriptions(JsonObject jsonObject, String key) {
        List<ClassDescription> classDescriptions = new ArrayList<>();
        JsonArray jsonClasses = jsonObject.getAsJsonArray(key);
        for (JsonElement jsonElement : jsonClasses) {
            classDescriptions.add(getClassDescription(jsonElement.getAsJsonObject()));
        }
        return classDescriptions;
    }

    public ClassDescription getClassDescription(JsonObject jsonObject) {
        JsonArray imports = jsonObject.getAsJsonArray("imports");
        JsonArray declarations = jsonObject.getAsJsonObject().getAsJsonArray("declarations");
        NodeList<BodyDeclaration<?>> bodyDeclarations = getDeclarations(declarations);
        return new ClassDescription(getImportDeclarations(imports), bodyDeclarations, Util.resolveNameFile(bodyDeclarations.get(0)));
    }
}
