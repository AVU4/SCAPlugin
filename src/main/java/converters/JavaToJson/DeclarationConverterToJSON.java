package converters.JavaToJson;

import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import static converters.JavaToJson.ExpressionConverterToJSON.getExpression;
import static converters.JavaToJson.ExpressionConverterToJSON.getExpressions;
import static converters.JavaToJson.StatementConverterToJSON.getStatement;
import static converters.JavaToJson.TypeConverterToJSON.getType;
import static converters.JavaToJson.TypeConverterToJSON.getTypes;
import static converters.JavaToJson.UtilConverterToJSON.*;

public class DeclarationConverterToJSON {


    public static JsonArray getDeclarations(NodeList<BodyDeclaration<?>> bodyDeclarations) {
        JsonArray jsonDeclarations = new JsonArray();
        bodyDeclarations.stream().map(DeclarationConverterToJSON::getDeclaration).forEach(jsonDeclarations::add);
        return jsonDeclarations;
    }

    public static JsonObject getDeclaration(BodyDeclaration<?> bodyDeclaration) {
        JsonObject jsonDeclaration = new JsonObject();
        if (bodyDeclaration.isAnnotationDeclaration()) {
            jsonDeclaration.add("declaration", getAnnotationDeclaration((AnnotationDeclaration) bodyDeclaration));
        } else if (bodyDeclaration.isEnumDeclaration()) {
            jsonDeclaration.add("declaration", getEnumDeclaration((EnumDeclaration) bodyDeclaration));
        } else if (bodyDeclaration.isFieldDeclaration()) {
            jsonDeclaration.add("declaration", getFieldDeclaration((FieldDeclaration) bodyDeclaration));
        } else if (bodyDeclaration.isMethodDeclaration()) {
            jsonDeclaration.add("declaration", getMethodDeclaration((MethodDeclaration) bodyDeclaration));
        } else if (bodyDeclaration.isAnnotationMemberDeclaration()) {
            jsonDeclaration.add("declaration", getAnnotationMemberDeclaration((AnnotationMemberDeclaration) bodyDeclaration));
        } else if (bodyDeclaration.isClassOrInterfaceDeclaration()) {
            jsonDeclaration.add("declaration", getClassOrInterfaceDeclaration((ClassOrInterfaceDeclaration) bodyDeclaration));
        } else if (bodyDeclaration.isCompactConstructorDeclaration()) {
            jsonDeclaration.add("declaration", getCompactConstructorDeclaration((CompactConstructorDeclaration) bodyDeclaration));
        } else if (bodyDeclaration.isConstructorDeclaration()) {
            jsonDeclaration.add("declaration", getConstructorDeclaration((ConstructorDeclaration) bodyDeclaration));
        } else if (bodyDeclaration.isEnumConstantDeclaration()) {
            jsonDeclaration.add("declaration", getEnumConstantDeclaration((EnumConstantDeclaration) bodyDeclaration));
        } else if (bodyDeclaration.isInitializerDeclaration()) {
            jsonDeclaration.add("declaration", getInitializerDeclaration((InitializerDeclaration) bodyDeclaration));
        } else if (bodyDeclaration.isRecordDeclaration()) {
            jsonDeclaration.add("declaration", getRecordDeclaration((RecordDeclaration) bodyDeclaration));
        }
        return jsonDeclaration;
    }

    public static JsonObject getRecordDeclaration(RecordDeclaration recordDeclaration) {
        JsonObject jsonRecordDeclaration = new JsonObject();
        jsonRecordDeclaration.addProperty("mode", RecordDeclaration.class.getName());
        jsonRecordDeclaration.add("typeParameters", getTypeParameters(recordDeclaration.getTypeParameters()));
        jsonRecordDeclaration.add("implementedTypes", getTypes(recordDeclaration.getImplementedTypes()));
        jsonRecordDeclaration.add("receiverParameter", recordDeclaration.getReceiverParameter().isPresent() ? getReceiverParameter(recordDeclaration.getReceiverParameter().get()) : null);
        jsonRecordDeclaration.add("parameters", getParameters(recordDeclaration.getParameters()));
        jsonRecordDeclaration.addProperty("name", recordDeclaration.getNameAsString());
        jsonRecordDeclaration.add("modifiers", getModifiers(recordDeclaration.getModifiers()));
        jsonRecordDeclaration.add("members", getDeclarations(recordDeclaration.getMembers()));
        jsonRecordDeclaration.add("annotations", getExpressions(recordDeclaration.getAnnotations()));
        return jsonRecordDeclaration;
    }

    public static JsonObject getInitializerDeclaration(InitializerDeclaration initializerDeclaration) {
        JsonObject jsonInitializerDeclaration = new JsonObject();
        jsonInitializerDeclaration.addProperty("mode", InitializerDeclaration.class.getName());
        jsonInitializerDeclaration.addProperty("isStatic", initializerDeclaration.isStatic());
        jsonInitializerDeclaration.add("body", getStatement(initializerDeclaration.getBody()));
        jsonInitializerDeclaration.add("annotations", getExpressions(initializerDeclaration.getAnnotations()));
        return jsonInitializerDeclaration;
    }

    public static JsonObject getConstructorDeclaration(ConstructorDeclaration constructorDeclaration) {
        JsonObject jsonConstructorDeclaration = new JsonObject();
        jsonConstructorDeclaration.addProperty("mode", ConstructorDeclaration.class.getName());
        jsonConstructorDeclaration.add("modifiers", getModifiers(constructorDeclaration.getModifiers()));
        jsonConstructorDeclaration.add("annotations", getExpressions(constructorDeclaration.getAnnotations()));
        jsonConstructorDeclaration.add("typeParameters", getTypeParameters(constructorDeclaration.getTypeParameters()));
        jsonConstructorDeclaration.addProperty("name", constructorDeclaration.getNameAsString());
        jsonConstructorDeclaration.add("parameters", getParameters(constructorDeclaration.getParameters()));
        jsonConstructorDeclaration.add("thrownExceptions", getTypes(constructorDeclaration.getThrownExceptions()));
        jsonConstructorDeclaration.add("body", getStatement(constructorDeclaration.getBody()));
        jsonConstructorDeclaration.add("receiverParameter", constructorDeclaration.getReceiverParameter().isPresent() ? getReceiverParameter(constructorDeclaration.getReceiverParameter().get()) : null);
        return jsonConstructorDeclaration;

    }

    public static JsonObject getCompactConstructorDeclaration(CompactConstructorDeclaration compactConstructorDeclaration) {
        JsonObject jsonCompactConstructorDeclaration = new JsonObject();
        jsonCompactConstructorDeclaration.addProperty("mode", CompactConstructorDeclaration.class.getName());
        jsonCompactConstructorDeclaration.addProperty("name", compactConstructorDeclaration.getNameAsString());
        jsonCompactConstructorDeclaration.add("modifiers", getModifiers(compactConstructorDeclaration.getModifiers()));
        jsonCompactConstructorDeclaration.add("body", getStatement(compactConstructorDeclaration.getBody()));
        jsonCompactConstructorDeclaration.add("typeParameters", getTypeParameters(compactConstructorDeclaration.getTypeParameters()));
        jsonCompactConstructorDeclaration.add("thrownExceptions", getTypes(compactConstructorDeclaration.getThrownExceptions()));
        jsonCompactConstructorDeclaration.add("annotations", getExpressions(compactConstructorDeclaration.getAnnotations()));
        return jsonCompactConstructorDeclaration;
    }

    public static JsonObject getAnnotationMemberDeclaration(AnnotationMemberDeclaration annotationMemberDeclaration) {
        JsonObject jsonAnnotationMemberDeclaration = new JsonObject();
        jsonAnnotationMemberDeclaration.addProperty("mode", AnnotationMemberDeclaration.class.getName());
        jsonAnnotationMemberDeclaration.addProperty("name", annotationMemberDeclaration.getNameAsString());
        jsonAnnotationMemberDeclaration.add("annotations", getExpressions(annotationMemberDeclaration.getAnnotations()));
        jsonAnnotationMemberDeclaration.add("modifiers", getModifiers(annotationMemberDeclaration.getModifiers()));
        jsonAnnotationMemberDeclaration.add("type", getType(annotationMemberDeclaration.getType()));
        jsonAnnotationMemberDeclaration.add("defaultValue", annotationMemberDeclaration.getDefaultValue().isPresent() ? getExpression(annotationMemberDeclaration.getDefaultValue().get()) : null);
        return jsonAnnotationMemberDeclaration;
    }

    public static JsonObject getMethodDeclaration(MethodDeclaration methodDeclaration) {
        JsonObject jsonMethodDeclaration = new JsonObject();
        jsonMethodDeclaration.addProperty("mode", MethodDeclaration.class.getName());
        jsonMethodDeclaration.add("annotations", getExpressions(methodDeclaration.getAnnotations()));
        jsonMethodDeclaration.add("type", getType(methodDeclaration.getType()));
        jsonMethodDeclaration.add("body", methodDeclaration.getBody().isPresent() ? getStatement(methodDeclaration.getBody().get()) : null);
        jsonMethodDeclaration.add("modifiers", getModifiers(methodDeclaration.getModifiers()));
        jsonMethodDeclaration.add("typeParameters", getTypeParameters(methodDeclaration.getTypeParameters()));
        jsonMethodDeclaration.addProperty("name", methodDeclaration.getNameAsString());
        jsonMethodDeclaration.add("parameters", getParameters(methodDeclaration.getParameters()));
        jsonMethodDeclaration.add("thrownExceptions", getTypes(methodDeclaration.getThrownExceptions()));
        jsonMethodDeclaration.add("receiverParameter", methodDeclaration.getReceiverParameter().isPresent() ?  getReceiverParameter(methodDeclaration.getReceiverParameter().get()) : null);
        return jsonMethodDeclaration;
    }

    public static JsonObject getFieldDeclaration(FieldDeclaration fieldDeclaration) {
        JsonObject jsonFieldDeclaration = new JsonObject();
        jsonFieldDeclaration.addProperty("mode", FieldDeclaration.class.getName());
        jsonFieldDeclaration.add("modifiers", getModifiers(fieldDeclaration.getModifiers()));
        jsonFieldDeclaration.add("annotations", getExpressions(fieldDeclaration.getAnnotations()));
        jsonFieldDeclaration.add("variables", getVariableDeclarators(fieldDeclaration.getVariables()));
        return jsonFieldDeclaration;
    }

    public static JsonObject getEnumDeclaration(EnumDeclaration enumDeclaration) {
        JsonObject jsonEnumDeclaration = new JsonObject();
        jsonEnumDeclaration.addProperty("mode", EnumDeclaration.class.getName());
        jsonEnumDeclaration.addProperty("name", enumDeclaration.getNameAsString());
        jsonEnumDeclaration.add("implementedTypes", getTypes(enumDeclaration.getImplementedTypes()));
        jsonEnumDeclaration.add("entries", getEnumConstantDeclarations(enumDeclaration.getEntries()));
        jsonEnumDeclaration.add("annotations", getExpressions(enumDeclaration.getAnnotations()));
        jsonEnumDeclaration.add("modifiers", getModifiers(enumDeclaration.getModifiers()));
        jsonEnumDeclaration.add("members", getDeclarations(enumDeclaration.getMembers()));
        return jsonEnumDeclaration;
    }

    public static JsonObject getAnnotationDeclaration(AnnotationDeclaration annotationDeclaration) {
        JsonObject jsonAnnotationDeclaration = new JsonObject();
        jsonAnnotationDeclaration.addProperty("mode", AnnotationDeclaration.class.getName());
        jsonAnnotationDeclaration.add("annotations", getExpressions(annotationDeclaration.getAnnotations()));
        jsonAnnotationDeclaration.addProperty("name", annotationDeclaration.getNameAsString());
        jsonAnnotationDeclaration.add("modifiers", getModifiers(annotationDeclaration.getModifiers()));
        jsonAnnotationDeclaration.add("members", getDeclarations(annotationDeclaration.getMembers()));
        return jsonAnnotationDeclaration;
    }

    public static JsonObject getClassOrInterfaceDeclaration(ClassOrInterfaceDeclaration classOrInterfaceDeclaration) {
        JsonObject jsonClassOrInterfaceDeclaration = new JsonObject();
        jsonClassOrInterfaceDeclaration.addProperty("mode", ClassOrInterfaceDeclaration.class.getName());
        jsonClassOrInterfaceDeclaration.addProperty("name", classOrInterfaceDeclaration.getNameAsString());
        jsonClassOrInterfaceDeclaration.add("modifiers", getModifiers(classOrInterfaceDeclaration.getModifiers()));
        jsonClassOrInterfaceDeclaration.add("members", getDeclarations(classOrInterfaceDeclaration.getMembers()));
        jsonClassOrInterfaceDeclaration.add("annotations", getExpressions(classOrInterfaceDeclaration.getAnnotations()));
        jsonClassOrInterfaceDeclaration.addProperty("isInterface", classOrInterfaceDeclaration.isInterface());
        jsonClassOrInterfaceDeclaration.add("typeParameters", getTypes(classOrInterfaceDeclaration.getTypeParameters()));
        jsonClassOrInterfaceDeclaration.add("extendedTypes", getTypes(classOrInterfaceDeclaration.getExtendedTypes()));
        jsonClassOrInterfaceDeclaration.add("implementedTypes", getTypes(classOrInterfaceDeclaration.getImplementedTypes()));
        return jsonClassOrInterfaceDeclaration;
    }


    public static JsonArray getEnumConstantDeclarations(NodeList<EnumConstantDeclaration> enumConstantDeclarations) {
        JsonArray jsonEnumConstantDeclarations = new JsonArray();
        enumConstantDeclarations.stream().map(DeclarationConverterToJSON::getDeclaration).forEach(jsonEnumConstantDeclarations::add);
        return jsonEnumConstantDeclarations;
    }

    public static JsonObject getEnumConstantDeclaration(EnumConstantDeclaration enumConstantDeclaration) {
        JsonObject jsonEnumConstantDeclaration = new JsonObject();
        jsonEnumConstantDeclaration.addProperty("mode", EnumConstantDeclaration.class.getName());
        jsonEnumConstantDeclaration.addProperty("name", enumConstantDeclaration.getNameAsString());
        jsonEnumConstantDeclaration.add("arguments", getExpressions(enumConstantDeclaration.getArguments()));
        jsonEnumConstantDeclaration.add("classBody", getDeclarations(enumConstantDeclaration.getClassBody()));
        jsonEnumConstantDeclaration.add("annotations", getExpressions(enumConstantDeclaration.getAnnotations()));
        return jsonEnumConstantDeclaration;
    }

    public static JsonArray getImportDeclarations(NodeList<ImportDeclaration> importDeclarations) {
        JsonArray jsonImportDeclarations = new JsonArray();
        importDeclarations.stream().map(DeclarationConverterToJSON::getImportDeclaration).forEach(jsonImportDeclarations::add);
        return jsonImportDeclarations;
    }

    public static JsonObject getImportDeclaration(ImportDeclaration importDeclaration) {
        JsonObject jsonImportDeclaration = new JsonObject();
        jsonImportDeclaration.addProperty("mode", ImportDeclaration.class.getName());
        jsonImportDeclaration.add("name", getName(importDeclaration.getName()));
        jsonImportDeclaration.addProperty("isStatic", importDeclaration.isStatic());
        jsonImportDeclaration.addProperty("isAsterisk", importDeclaration.isAsterisk());
        return jsonImportDeclaration;
    }
}
