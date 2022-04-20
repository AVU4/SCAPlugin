package converters.JsonToJava;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.ReferenceType;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Arrays;
import java.util.Locale;
import java.util.stream.Collectors;

import static converters.JsonToJava.DeclarationConverterToJava.getDeclarations;
import static converters.JsonToJava.StatementConverterToJava.getStatement;
import static converters.JsonToJava.TypeConverterToJava.getType;
import static converters.JsonToJava.TypeConverterToJava.getTypes;
import static converters.JsonToJava.UtilConverterToJava.*;

public class ExpressionConverterToJava {

    public static NodeList<Expression> getExpressions(JsonArray jsonExpressions) {
        NodeList<Expression> expressions = new NodeList<>();
        for (JsonElement jsonElement : jsonExpressions) {
            expressions.add(getExpression(jsonElement.getAsJsonObject()));
        }
        return expressions;
    }

    public static Expression getExpression(JsonObject jsonObject) {
        JsonObject expression = jsonObject.getAsJsonObject("expression");
        if (expression.get("mode").getAsString().equals(ArrayAccessExpr.class.getName())) {
            return getArrayAccessExpression(expression);
        } else if (expression.get("mode").getAsString().equals(CastExpr.class.getName())) {
            return getCastExpression(expression);
        } else if (expression.get("mode").getAsString().equals(AssignExpr.class.getName())) {
            return getAssignExpression(expression);
        } else if (expression.get("mode").getAsString().equals(BinaryExpr.class.getName())) {
            return getBinaryExpression(expression);
        } else if (expression.get("mode").getAsString().equals(ArrayCreationExpr.class.getName())) {
            return getArrayCreationExpression(expression);
        } else if (expression.get("mode").getAsString().equals(ArrayInitializerExpr.class.getName())) {
            return getArrayInitializerExpression(expression);
        } else if (expression.get("mode").getAsString().equals(BooleanLiteralExpr.class.getName())) {
            return getBooleanLiteralExpression(expression);
        } else if (expression.get("mode").getAsString().equals(CharLiteralExpr.class.getName())) {
            return getCharLiteralExpression(expression);
        } else if (expression.get("mode").getAsString().equals(ClassExpr.class.getName())) {
            return getClassExpression(expression);
        } else if (expression.get("mode").getAsString().equals(ConditionalExpr.class.getName())) {
            return getConditionalExpression(expression);
        } else if (expression.get("mode").getAsString().equals(DoubleLiteralExpr.class.getName())) {
            return getDoubleLiteralExpression(expression);
        } else if (expression.get("mode").getAsString().equals(EnclosedExpr.class.getName())) {
            return getEnclosedExpression(expression);
        } else if (expression.get("mode").getAsString().equals(FieldAccessExpr.class.getName())) {
            return getFieldAccessException(expression);
        } else if (expression.get("mode").getAsString().equals(InstanceOfExpr.class.getName())) {
            return getInstanceOfExpression(expression);
        } else if (expression.get("mode").getAsString().equals(PatternExpr.class.getName())) {
            return getPatternExpression(expression);
        } else if (expression.get("mode").getAsString().equals(IntegerLiteralExpr.class.getName())) {
            return getIntegerLiteralExpression(expression);
        } else if (expression.get("mode").getAsString().equals(LambdaExpr.class.getName())) {
            return getLambdaExpression(expression);
        } else if (expression.get("mode").getAsString().equals(MarkerAnnotationExpr.class.getName())) {
            return getMarkerAnnotationExpression(expression);
        } else if (expression.get("mode").getAsString().equals(MethodCallExpr.class.getName())) {
            return getMethodCallExpression(expression);
        } else if (expression.get("mode").getAsString().equals(MethodReferenceExpr.class.getName())) {
            return getMethodReferenceExpression(expression);
        } else if (expression.get("mode").getAsString().equals(NameExpr.class.getName())) {
            return getNameExpression(expression);
        } else if (expression.get("mode").getAsString().equals(NormalAnnotationExpr.class.getName())) {
            return getNormalAnnotationExpression(expression);
        } else if (expression.get("mode").getAsString().equals(NullLiteralExpr.class.getName())) {
            return getNullLiteralExpression(expression);
        } else if (expression.get("mode").getAsString().equals(ObjectCreationExpr.class.getName())) {
            return getObjectCreationExpression(expression);
        } else if (expression.get("mode").getAsString().equals(SingleMemberAnnotationExpr.class.getName())) {
            return getSimpleMemberAnnotationExpression(expression);
        } else if (expression.get("mode").getAsString().equals(StringLiteralExpr.class.getName())) {
            return getStringLiteralExpression(expression);
        } else if (expression.get("mode").getAsString().equals(SuperExpr.class.getName())) {
            return getSuperExpression(expression);
        } else if (expression.get("mode").getAsString().equals(SwitchExpr.class.getName())) {
            return getSwitchExpression(expression);
        } else if (expression.get("mode").getAsString().equals(TextBlockLiteralExpr.class.getName())) {
            return getTextBlockLiteralExpression(expression);
        } else if (expression.get("mode").getAsString().equals(ThisExpr.class.getName())) {
            return getThisExpression(expression);
        } else if (expression.get("mode").getAsString().equals(TypeExpr.class.getName())) {
            return getTypeExpression(expression);
        } else if (expression.get("mode").getAsString().equals(UnaryExpr.class.getName())) {
            return getUnaryExpression(expression);
        } else if (expression.get("mode").getAsString().equals(VariableDeclarationExpr.class.getName())) {
            return getVariableDeclarationExpression(expression);
        } else if (expression.get("mode").getAsString().equals(LongLiteralExpr.class.getName())) {
            return getLongLiteralExpression(expression);
        }
        return null;
    }

    private static LongLiteralExpr getLongLiteralExpression(JsonObject jsonObject) {
        return new LongLiteralExpr(jsonObject.get("value").getAsString());
    }

    private static VariableDeclarationExpr getVariableDeclarationExpression(JsonObject jsonObject) {
        return new VariableDeclarationExpr(
                getModifiers(jsonObject.getAsJsonArray("modifiers")),
                getAnnotationExpressions(jsonObject, "annotations"),
                getVariableDeclarators(jsonObject.getAsJsonArray("variables"))
        );
    }

    private static UnaryExpr getUnaryExpression(JsonObject jsonObject) {
        return new UnaryExpr(
                getExpression(jsonObject.getAsJsonObject("expression")),
                getOperator(jsonObject.getAsJsonObject("operator"))
        );
    }

    private static TypeExpr getTypeExpression(JsonObject jsonObject) {
        return new TypeExpr(getType(jsonObject.getAsJsonObject("type")));
    }

    private static ThisExpr getThisExpression(JsonObject jsonObject) {
        JsonElement jsonName = jsonObject.get("name");
        return new ThisExpr(!jsonName.isJsonNull()? getName(jsonName.getAsJsonObject()) : null);
    }

    private static TextBlockLiteralExpr getTextBlockLiteralExpression(JsonObject jsonObject) {
        return new TextBlockLiteralExpr(jsonObject.get("value").getAsString());
    }

    private static SwitchExpr getSwitchExpression(JsonObject jsonObject) {
        return new SwitchExpr(
                getExpression(jsonObject.getAsJsonObject("selector")),
                getSwitchEntries(jsonObject.getAsJsonArray("entries"))
        );
    }

    private static SuperExpr getSuperExpression(JsonObject jsonObject) {
        JsonElement jsonTypeName = jsonObject.get("typeName");
        return new SuperExpr(!jsonTypeName.isJsonNull() ? getName(jsonTypeName.getAsJsonObject()) : null);
    }

    private static StringLiteralExpr getStringLiteralExpression(JsonObject jsonObject) {
        return new StringLiteralExpr(jsonObject.get("value").getAsString());
    }

    private static SingleMemberAnnotationExpr getSimpleMemberAnnotationExpression(JsonObject jsonObject) {
        return new SingleMemberAnnotationExpr(
                getName(jsonObject.getAsJsonObject("name")),
                getExpression(jsonObject.getAsJsonObject("memberValue"))
        );
    }

    private static ObjectCreationExpr getObjectCreationExpression(JsonObject jsonObject) {
        JsonElement jsonScope = jsonObject.get("scope");
        JsonElement jsonAnonymousClassBody = jsonObject.get("anonymousClassBody");
        JsonElement jsonTypeArguments = jsonObject.get("typeArguments");
        return new ObjectCreationExpr(
                !jsonScope.isJsonNull()? getExpression(jsonScope.getAsJsonObject()) : null,
                (ClassOrInterfaceType) getType(jsonObject.getAsJsonObject("type")),
                !jsonTypeArguments.isJsonNull() ? getTypes(jsonObject.getAsJsonArray("typeArguments")) : null,
                getExpressions(jsonObject.getAsJsonArray("arguments")),
                !jsonAnonymousClassBody.isJsonNull() ? getDeclarations(jsonAnonymousClassBody.getAsJsonArray()) : null
        );
    }

    private static NullLiteralExpr getNullLiteralExpression(JsonObject jsonObject) {
        return new NullLiteralExpr();
    }

    private static NormalAnnotationExpr getNormalAnnotationExpression(JsonObject jsonObject) {
        return new NormalAnnotationExpr(
                getName(jsonObject.getAsJsonObject("name")),
                getMemberValuePairs(jsonObject.getAsJsonArray("pairs"))
        );
    }

    private static NameExpr getNameExpression(JsonObject jsonObject) {
        return new NameExpr(
                new SimpleName(jsonObject.get("name").getAsString())
        );
    }

    private static MethodReferenceExpr getMethodReferenceExpression(JsonObject jsonObject) {
        return new MethodReferenceExpr(
                getExpression(jsonObject.getAsJsonObject("scope")),
                getTypes(jsonObject.getAsJsonArray("typeArguments")),
                jsonObject.get("identifier").getAsString()
        );
    }

    private static MethodCallExpr getMethodCallExpression(JsonObject jsonObject) {
        JsonElement jsonScope = jsonObject.get("scope");
        JsonElement jsonTypeArguments = jsonObject.get("typeArguments");
        return new MethodCallExpr(
                !jsonScope.isJsonNull() ? getExpression(jsonScope.getAsJsonObject()) : null,
                !jsonTypeArguments.isJsonNull() ? getTypes(jsonObject.getAsJsonArray("typeArguments")) : null,
                new SimpleName(jsonObject.get("name").getAsString()),
                getExpressions(jsonObject.getAsJsonArray("arguments"))
        );
    }

    private static MarkerAnnotationExpr getMarkerAnnotationExpression(JsonObject jsonObject) {
        return new MarkerAnnotationExpr(getName(jsonObject.getAsJsonObject("name")));
    }

    private static LambdaExpr getLambdaExpression(JsonObject jsonObject) {
        return new LambdaExpr(
                getParameters(jsonObject.getAsJsonArray("parameters")),
                getStatement(jsonObject.getAsJsonObject("body")),
                jsonObject.get("isEnclosingParameters").getAsBoolean()
        );
    }

    private static IntegerLiteralExpr getIntegerLiteralExpression(JsonObject jsonObject) {
        return new IntegerLiteralExpr(jsonObject.get("value").getAsString());
    }

    private static PatternExpr getPatternExpression(JsonObject jsonObject) {
        return new PatternExpr(
                (ReferenceType) getType(jsonObject.getAsJsonObject("type")),
                new SimpleName(jsonObject.get("name").getAsString())
        );
    }

    private static InstanceOfExpr getInstanceOfExpression(JsonObject jsonObject) {
        JsonElement jsonPattern = jsonObject.get("pattern");
        return new InstanceOfExpr(
                getExpression(jsonObject.getAsJsonObject("expression")),
                (ReferenceType) getType(jsonObject.getAsJsonObject("type")),
                !jsonPattern.isJsonNull() ? (PatternExpr) getExpression(jsonPattern.getAsJsonObject()) : null
        );
    }

    private static FieldAccessExpr getFieldAccessException(JsonObject jsonObject) {
        JsonElement jsonTypeArguments = jsonObject.get("typeArguments");
        return new FieldAccessExpr(
                getExpression(jsonObject.getAsJsonObject("scope")),
                !jsonTypeArguments.isJsonNull() ? getTypes(jsonTypeArguments.getAsJsonArray()) : null,
                new SimpleName(jsonObject.get("name").getAsString())
        );
    }

    private static EnclosedExpr getEnclosedExpression(JsonObject jsonObject) {
        return new EnclosedExpr(getExpression(jsonObject.getAsJsonObject("inner")));
    }

    private static DoubleLiteralExpr getDoubleLiteralExpression(JsonObject jsonObject) {
        return new DoubleLiteralExpr(jsonObject.get("value").getAsDouble());
    }

    private static ConditionalExpr getConditionalExpression(JsonObject jsonObject) {
        return new ConditionalExpr(
                getExpression(jsonObject.getAsJsonObject("condition")),
                getExpression(jsonObject.getAsJsonObject("thenExpr")),
                getExpression(jsonObject.getAsJsonObject("elseExpr"))
        );
    }

    private static ClassExpr getClassExpression(JsonObject jsonObject) {
        return new ClassExpr(
                getType(jsonObject.getAsJsonObject("type"))
        );
    }

    private static CharLiteralExpr getCharLiteralExpression(JsonObject jsonObject) {
        return new CharLiteralExpr(jsonObject.get("value").getAsString());
    }

    private static BooleanLiteralExpr getBooleanLiteralExpression(JsonObject jsonObject) {
        return new BooleanLiteralExpr(jsonObject.get("value").getAsBoolean());
    }

    private static ArrayInitializerExpr getArrayInitializerExpression(JsonObject jsonObject) {
        return new ArrayInitializerExpr(
                getExpressions(jsonObject.getAsJsonArray("values"))
        );
    }

    private static ArrayCreationExpr getArrayCreationExpression(JsonObject jsonObject) {
        //todo Think about levels
        JsonElement jsonInitializer = jsonObject.get("initializer");
        return new ArrayCreationExpr(
                getType(jsonObject.getAsJsonObject("elementType")),
                getArrayCreationLevels(jsonObject.getAsJsonArray("levels")),
                !jsonInitializer.isJsonNull() ? (ArrayInitializerExpr) getExpression(jsonInitializer.getAsJsonObject()) : null
        );
    }

    private static BinaryExpr getBinaryExpression(JsonObject jsonObject) {
        return new BinaryExpr(
                getExpression(jsonObject.getAsJsonObject("left")),
                getExpression(jsonObject.getAsJsonObject("right")),
                Arrays.stream(BinaryExpr.Operator.values())
                        .filter(operator -> operator.asString().equals(jsonObject.get("operator").getAsString()))
                        .collect(Collectors.toList()).get(0)
        );
    }

    private static AssignExpr getAssignExpression(JsonObject jsonObject) {
        return new AssignExpr(
                getExpression(jsonObject.getAsJsonObject("target")),
                getExpression(jsonObject.getAsJsonObject("value")),
                Arrays.stream(AssignExpr.Operator.values())
                        .filter(operator -> operator.asString().equals(jsonObject.get("operator").getAsString()))
                        .collect(Collectors.toList()).get(0)
        );
    }

    private static CastExpr getCastExpression(JsonObject jsonObject) {
        return new CastExpr(
                getType(jsonObject.getAsJsonObject("type")),
                getExpression(jsonObject.getAsJsonObject("expression"))
        );
    }

    private static ArrayAccessExpr getArrayAccessExpression(JsonObject jsonObject) {
        return new ArrayAccessExpr(
                getExpression(jsonObject.getAsJsonObject("index")),
                getExpression(jsonObject.getAsJsonObject("name"))
        );
    }
}
