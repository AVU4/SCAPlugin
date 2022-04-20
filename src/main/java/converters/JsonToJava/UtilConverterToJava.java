package converters.JsonToJava;

import com.github.javaparser.ast.ArrayCreationLevel;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.ReceiverParameter;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.stmt.SwitchEntry;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.TypeParameter;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Arrays;
import java.util.Locale;
import java.util.stream.Collectors;

import static converters.JsonToJava.ExpressionConverterToJava.getExpression;
import static converters.JsonToJava.ExpressionConverterToJava.getExpressions;
import static converters.JsonToJava.StatementConverterToJava.getStatements;
import static converters.JsonToJava.TypeConverterToJava.getType;

public class UtilConverterToJava {

    public static NodeList<VariableDeclarator> getVariableDeclarators(JsonArray jsonArray) {
        NodeList<VariableDeclarator> variableDeclaratorNodeList = new NodeList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            variableDeclaratorNodeList.add(getVariableDeclarator(jsonArray.get(i).getAsJsonObject()));
        }
        return variableDeclaratorNodeList;
    }

    public static VariableDeclarator getVariableDeclarator(JsonObject jsonObject) {
        JsonElement jsonInitializer = jsonObject.get("initializer");
        return new VariableDeclarator(
                getType(jsonObject.getAsJsonObject("type")),
                new SimpleName(jsonObject.get("name").getAsString()),
                !jsonInitializer.isJsonNull() ? getExpression(jsonInitializer.getAsJsonObject()) : null
        );
    }

    public static UnaryExpr.Operator getOperator(JsonObject jsonObject) {
        return Arrays.stream(UnaryExpr.Operator.values())
                .filter(operator -> operator.asString().equals(jsonObject.get("codeRepresentation").getAsString()))
                .collect(Collectors.toList())
                .get(0);
    }

    public static Name getName(JsonObject jsonObject) {
        JsonElement jsonQualifier = jsonObject.get("qualifier");
        return new Name(
            !jsonQualifier.isJsonNull() ? getName(jsonQualifier.getAsJsonObject()) : null,
            jsonObject.get("identifier").getAsString()
        );
    }

    public static NodeList<Parameter> getParameters(JsonArray jsonArray) {
        NodeList<Parameter> parameterNodeList = new NodeList<>();
        for (int i = 0; i < jsonArray.size(); i ++) {
            parameterNodeList.add(getParameter(jsonArray.get(i).getAsJsonObject()));
        }
        return parameterNodeList;
    }

    public static Parameter getParameter(JsonObject jsonObject) {
        return new Parameter(
                getModifiers(jsonObject.getAsJsonArray("modifiers")),
                getAnnotationExpressions(jsonObject, "annotations"),
                getType(jsonObject.getAsJsonObject("type")),
                jsonObject.get("isVarArgs").getAsBoolean(),
                getAnnotationExpressions(jsonObject, "varArgsAnnotations"),
                new SimpleName(jsonObject.get("name").getAsString())
        );
    }

    public static NodeList<Modifier> getModifiers(JsonArray jsonArray) {
        NodeList<Modifier> modifiers = new NodeList<>();
        for (int i = 0; i < jsonArray.size(); i ++) {
            modifiers.add(getModifier(jsonArray.get(i).getAsJsonObject()));
        }
        return modifiers;
    }

    public static Modifier getModifier(JsonObject jsonObject) {
        String code = jsonObject.get("keyword").getAsString();
        return new Modifier(Modifier.Keyword.valueOf(code.toUpperCase(Locale.ROOT)));
    }


    public static NodeList<ArrayCreationLevel> getArrayCreationLevels(JsonArray jsonArray) {
        NodeList<ArrayCreationLevel> arrayCreationLevels = new NodeList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            arrayCreationLevels.add(getArrayCreationLevel(jsonArray.get(i).getAsJsonObject()));
        }
        return arrayCreationLevels;
    }

    public static ArrayCreationLevel getArrayCreationLevel(JsonObject jsonObject) {
        JsonElement jsonDimension = jsonObject.get("dimension");
        return new ArrayCreationLevel(
                !jsonDimension.isJsonNull() ? getExpression(jsonDimension.getAsJsonObject()) : null,
                getAnnotationExpressions(jsonObject, "keyword")
        );
    }

    public static NodeList<AnnotationExpr> getAnnotationExpressions(JsonObject jsonObject, String keyword) {
        NodeList<AnnotationExpr> annotationExprs = new NodeList<>();
        getExpressions(jsonObject.get(keyword).getAsJsonArray())
                .stream()
                .map(expression -> (AnnotationExpr) expression)
                .forEach(annotationExprs::add);
        return annotationExprs;
    }

    public static NodeList<MemberValuePair> getMemberValuePairs(JsonArray jsonArray) {
        NodeList<MemberValuePair> memberValuePairs = new NodeList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            memberValuePairs.add(getMemberValuePair(jsonArray.get(i).getAsJsonObject()));
        }
        return memberValuePairs;
    }

    public static MemberValuePair getMemberValuePair(JsonObject jsonObject) {
        return new MemberValuePair(
                new SimpleName(jsonObject.get("name").getAsString()),
                getExpression(jsonObject.getAsJsonObject("expression"))
        );
    }

    public static NodeList<SwitchEntry> getSwitchEntries(JsonArray jsonArray) {
        NodeList<SwitchEntry> switchEntries = new NodeList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            switchEntries.add(getSwitchEntry(jsonArray.get(i).getAsJsonObject()));
        }
        return switchEntries;
    }

    public static SwitchEntry getSwitchEntry(JsonObject jsonObject) {
        return new SwitchEntry(
                getExpressions(jsonObject.getAsJsonArray("labels")),
                SwitchEntry.Type.valueOf(jsonObject.get("type").getAsString().toUpperCase(Locale.ROOT)),
                getStatements(jsonObject.getAsJsonArray("statements"))
        );
    }

    public static NodeList<ClassOrInterfaceType> getClassOrInterfaceTypes(JsonArray jsonArray) {
        NodeList<ClassOrInterfaceType> nodeList = new NodeList<>();
        for (JsonElement jsonElement : jsonArray) {
            nodeList.add((ClassOrInterfaceType) getType(jsonElement.getAsJsonObject()));
        }
        return nodeList;
    }

    public static NodeList<TypeParameter> getTypeParameters(JsonArray jsonArray) {
        NodeList<TypeParameter> nodeList = new NodeList<>();
        for (JsonElement jsonElement : jsonArray) {
            nodeList.add(getTypeParameter(jsonElement.getAsJsonObject()));
        }
        return nodeList;
    }

    public static TypeParameter getTypeParameter(JsonObject jsonObject) {
        return new TypeParameter(
                new SimpleName(jsonObject.get("name").getAsString()),
                getClassOrInterfaceTypes(jsonObject.getAsJsonArray("typeBound")),
                getAnnotationExpressions(jsonObject, "annotations")
        );
    }

    public static ReceiverParameter getReceiverParameter(JsonObject jsonObject) {
        return new ReceiverParameter(
                getAnnotationExpressions(jsonObject, "annotations"),
                getType(jsonObject.getAsJsonObject("type")),
                getName(jsonObject.getAsJsonObject("name"))
        );
    }

}
