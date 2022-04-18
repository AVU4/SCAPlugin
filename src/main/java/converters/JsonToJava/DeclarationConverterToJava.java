package converters.JsonToJava;

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import static converters.JsonToJava.ExpressionConverterToJava.*;
import static converters.JsonToJava.StatementConverterToJava.getStatement;
import static converters.JsonToJava.TypeConverterToJava.getReferenceTypes;
import static converters.JsonToJava.TypeConverterToJava.getType;
import static converters.JsonToJava.UtilConverterToJava.*;

public class DeclarationConverterToJava {

    public static NodeList<BodyDeclaration<?>> getDeclarations(JsonArray jsonDeclarations) {
        NodeList<BodyDeclaration<?>> bodyDeclarations = new NodeList<>();
        for (JsonElement jsonElement : jsonDeclarations) {
            bodyDeclarations.add(getDeclaration(jsonElement.getAsJsonObject()));
        }
        return bodyDeclarations;
    }

    public static BodyDeclaration<?> getDeclaration(JsonObject jsonObject) {
        JsonObject declaration = jsonObject.getAsJsonObject("declaration");
        if (declaration.get("mode").getAsString().equals(AnnotationDeclaration.class.getName())) {
            return getAnnotationDeclaration(declaration);
        }  else if (declaration.get("mode").getAsString().equals(EnumDeclaration.class.getName())) {
            return getEnumDeclaration(declaration);
        } else if (declaration.get("mode").getAsString().equals(FieldDeclaration.class.getName())) {
            return getFieldDeclaration(declaration);
        } else if (declaration.get("mode").getAsString().equals(MethodDeclaration.class.getName())) {
            return getMethodDeclaration(declaration);
        } else if (declaration.get("mode").getAsString().equals(AnnotationMemberDeclaration.class.getName())) {
            return getAnnotationMemberDeclaration(declaration);
        } else if (declaration.get("mode").getAsString().equals(ClassOrInterfaceDeclaration.class.getName())) {
            return getClassOrInterfaceDeclaration(declaration);
        } else if (declaration.get("mode").getAsString().equals(CompactConstructorDeclaration.class.getName())) {
            return getCompactConstructorDeclaration(declaration);
        } else if (declaration.get("mode").getAsString().equals(ConstructorDeclaration.class.getName())) {
            return getConstructorDeclaration(declaration);
        } else if (declaration.get("mode").getAsString().equals(EnumConstantDeclaration.class.getName())) {
            return getEnumConstantDeclaration(declaration);
        } else if (declaration.get("mode").getAsString().equals(InitializerDeclaration.class.getName())) {
            return getInitializerDeclaration(declaration);
        } else if (declaration.get("mode").getAsString().equals(RecordDeclaration.class.getName())) {
            return getRecordDeclaration(declaration);
        }
        return null;
    }

    private static RecordDeclaration getRecordDeclaration(JsonObject jsonObject) {
        JsonElement jsonReceiverParameters = jsonObject.get("receiverParameter");
        return new RecordDeclaration(
                getModifiers(jsonObject.getAsJsonArray("modifiers")),
                getAnnotationExpressions(jsonObject, "annotations"),
                new SimpleName(jsonObject.get("name").getAsString()),
                getParameters(jsonObject.getAsJsonArray("parameters")),
                getTypeParameters(jsonObject.getAsJsonArray("typeParameters")),
                getClassOrInterfaceTypes(jsonObject.getAsJsonArray("implementedTypes")),
                getDeclarations(jsonObject.getAsJsonArray("members")),
                jsonReceiverParameters != null ? getReceiverParameter(jsonReceiverParameters.getAsJsonObject()) : null
        );
    }

    private static InitializerDeclaration getInitializerDeclaration(JsonObject jsonObject) {
        InitializerDeclaration initializerDeclaration =  new InitializerDeclaration(
                jsonObject.get("isStatic").getAsBoolean(),
                (BlockStmt) getStatement(jsonObject.getAsJsonObject("body"))
        );
        initializerDeclaration.setAnnotations(getAnnotationExpressions(jsonObject, "annotations"));
        return initializerDeclaration;
    }

    private static ConstructorDeclaration getConstructorDeclaration(JsonObject jsonObject) {
        JsonElement jsonReceiverParameter = jsonObject.get("receiverParameter");
        return new ConstructorDeclaration(
                getModifiers(jsonObject.getAsJsonArray("modifiers")),
                getAnnotationExpressions(jsonObject, "annotations"),
                getTypeParameters(jsonObject.getAsJsonArray("typeParameters")),
                new SimpleName(jsonObject.get("name").getAsString()),
                getParameters(jsonObject.getAsJsonArray("parameters")),
                getReferenceTypes(jsonObject.getAsJsonArray("thrownExceptions")),
                (BlockStmt) getStatement(jsonObject.getAsJsonObject("body")),
                !jsonReceiverParameter.isJsonNull() ? getReceiverParameter(jsonReceiverParameter.getAsJsonObject()) : null
        );
    }

    private static CompactConstructorDeclaration getCompactConstructorDeclaration(JsonObject jsonObject) {
        return new CompactConstructorDeclaration(
                getModifiers(jsonObject.getAsJsonArray("modifiers")),
                getAnnotationExpressions(jsonObject, "annotations"),
                getTypeParameters(jsonObject.getAsJsonArray("typeParameters")),
                new SimpleName(jsonObject.get("name").getAsString()),
                getReferenceTypes(jsonObject.getAsJsonArray("thrownExceptions")),
                (BlockStmt) getStatement(jsonObject.getAsJsonObject("body"))
        );
    }

    private static ClassOrInterfaceDeclaration getClassOrInterfaceDeclaration(JsonObject jsonObject) {
        return new ClassOrInterfaceDeclaration(
                getModifiers(jsonObject.getAsJsonArray("modifiers")),
                getAnnotationExpressions(jsonObject, "annotations"),
                jsonObject.get("isInterface").getAsBoolean(),
                new SimpleName(jsonObject.get("name").getAsString()),
                getTypeParameters(jsonObject.getAsJsonArray("typeParameters")),
                getClassOrInterfaceTypes(jsonObject.getAsJsonArray("extendedTypes")),
                getClassOrInterfaceTypes(jsonObject.getAsJsonArray("implementedTypes")),
                getDeclarations(jsonObject.getAsJsonArray("members"))
                );
    }

    private static AnnotationMemberDeclaration getAnnotationMemberDeclaration(JsonObject jsonObject) {
        JsonElement jsonDefaultValue = jsonObject.get("defaultValue");
        return new AnnotationMemberDeclaration(
                getModifiers(jsonObject.getAsJsonArray("modifiers")),
                getAnnotationExpressions(jsonObject, "annotations"),
                getType(jsonObject.getAsJsonObject("type")),
                new SimpleName(jsonObject.get("name").getAsString()),
                !jsonDefaultValue.isJsonNull() ? getExpression(jsonDefaultValue.getAsJsonObject()) : null
        );
    }

    private static MethodDeclaration getMethodDeclaration(JsonObject jsonObject) {
        JsonElement jsonBody = jsonObject.get("body");
        JsonElement jsonReceiverParameter = jsonObject.get("receiverParameter");
        return new MethodDeclaration(
                getModifiers(jsonObject.getAsJsonArray("modifiers")),
                getAnnotationExpressions(jsonObject, "annotations"),
                getTypeParameters(jsonObject.getAsJsonArray("typeParameters")),
                getType(jsonObject.getAsJsonObject("type")),
                new SimpleName(jsonObject.get("name").getAsString()),
                getParameters(jsonObject.getAsJsonArray("parameters")),
                getReferenceTypes(jsonObject.getAsJsonArray("thrownExceptions")),
                !jsonBody.isJsonNull() ? (BlockStmt) getStatement(jsonBody.getAsJsonObject()) : null,
                !jsonReceiverParameter.isJsonNull() ? getReceiverParameter(jsonReceiverParameter.getAsJsonObject()) : null
        );
    }

    private static FieldDeclaration getFieldDeclaration(JsonObject jsonObject) {
        return new FieldDeclaration(
                getModifiers(jsonObject.getAsJsonArray("modifiers")),
                getAnnotationExpressions(jsonObject,"annotations"),
                getVariableDeclarators(jsonObject.getAsJsonArray("variables"))
        );
    }

    private static EnumDeclaration getEnumDeclaration(JsonObject jsonObject) {
        return new EnumDeclaration(
                getModifiers(jsonObject.getAsJsonArray("modifiers")),
                getAnnotationExpressions(jsonObject, "annotations"),
                new SimpleName(jsonObject.get("name").getAsString()),
                getClassOrInterfaceTypes(jsonObject.getAsJsonArray("implementedTypes")),
                getEnumConstantDeclarations(jsonObject.getAsJsonArray("entries")),
                getDeclarations(jsonObject.getAsJsonArray("members"))
        );
    }

    private static NodeList<EnumConstantDeclaration> getEnumConstantDeclarations(JsonArray jsonArray) {
        NodeList<EnumConstantDeclaration> nodeList = new NodeList<>();
        for (JsonElement jsonElement : jsonArray) {
            nodeList.add((EnumConstantDeclaration) getDeclaration(jsonElement.getAsJsonObject()));
        }
        return nodeList;
    }

    private static EnumConstantDeclaration getEnumConstantDeclaration(JsonObject jsonObject) {
        return new EnumConstantDeclaration(
                getAnnotationExpressions(jsonObject, "annotations"),
                new SimpleName(jsonObject.get("name").getAsString()),
                getExpressions(jsonObject.getAsJsonArray("arguments")),
                getDeclarations(jsonObject.getAsJsonArray("classBody"))
        );
    }

    private static AnnotationDeclaration getAnnotationDeclaration(JsonObject jsonObject) {
        return new AnnotationDeclaration(
                getModifiers(jsonObject.getAsJsonArray("modifiers")),
                getAnnotationExpressions(jsonObject, "annotations"),
                new SimpleName(jsonObject.get("name").getAsString()),
                getDeclarations(jsonObject.getAsJsonArray("members"))
        );
    }
}
