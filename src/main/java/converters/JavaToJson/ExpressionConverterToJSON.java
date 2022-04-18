package converters.JavaToJson;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import static converters.JavaToJson.DeclarationConverterToJSON.getDeclarations;
import static converters.JavaToJson.StatementConverterToJSON.getStatement;
import static converters.JavaToJson.TypeConverterToJSON.getType;
import static converters.JavaToJson.TypeConverterToJSON.getTypes;
import static converters.JavaToJson.UtilConverterToJSON.*;

public class ExpressionConverterToJSON {

    public static JsonArray getExpressions(NodeList<? extends Expression> expressions) {
        JsonArray jsonArray = new JsonArray();
        expressions.stream().map(ExpressionConverterToJSON::getExpression).forEach(jsonArray::add);
        return jsonArray;
    }

    public static JsonObject getExpression(Expression expression) {
        JsonObject jsonExpression = new JsonObject();

        if (expression.isArrayAccessExpr()) {
            jsonExpression.add("expression", getArrayAccessExpression((ArrayAccessExpr) expression));
        } else if (expression.isCastExpr()) {
            jsonExpression.add("expression", getCastExpression((CastExpr) expression));
        } else if (expression.isAssignExpr()) {
            jsonExpression.add("expression", getAssignExpression((AssignExpr) expression));
        } else if (expression.isBinaryExpr()) {
            jsonExpression.add("expression", getBinaryExpression((BinaryExpr) expression));
        } else if (expression.isArrayCreationExpr()) {
            jsonExpression.add("expression", getArrayCreationExpression((ArrayCreationExpr) expression));
        } else if (expression.isArrayInitializerExpr()) {
            jsonExpression.add("expression", getArrayInitializerExpression((ArrayInitializerExpr) expression));
        } else if (expression.isBooleanLiteralExpr()) {
            jsonExpression.add("expression", getBooleanLiteralExpression((BooleanLiteralExpr) expression));
        } else if (expression.isCharLiteralExpr()) {
            jsonExpression.add("expression", getCharLiteralExpression((CharLiteralExpr) expression));
        } else if (expression.isClassExpr()) {
            jsonExpression.add("expression", getClassExpression((ClassExpr) expression));
        } else if (expression.isConditionalExpr()) {
            jsonExpression.add("expression", getConditionalExpression((ConditionalExpr) expression));
        } else if (expression.isDoubleLiteralExpr()) {
            jsonExpression.add("expression", getDoubleLiteralExpression((DoubleLiteralExpr) expression));
        } else if (expression.isEnclosedExpr()) {
            jsonExpression.add("expression", getEnclosedExpression((EnclosedExpr) expression));
        } else if (expression.isFieldAccessExpr()) {
            jsonExpression.add("expression", getFieldAccessExpression((FieldAccessExpr) expression));
        } else if (expression.isInstanceOfExpr()) {
            jsonExpression.add("expression", getInstanceOfExpression((InstanceOfExpr) expression));
        } else if (expression.isPatternExpr()) {
            jsonExpression.add("expression", getPatternExpression((PatternExpr) expression));
        } else if (expression.isIntegerLiteralExpr()) {
            jsonExpression.add("expression", getIntegerLiteralExpression((IntegerLiteralExpr) expression));
        } else if (expression.isLambdaExpr()) {
            jsonExpression.add("expression", getLambdaExpression((LambdaExpr) expression));
        } else if (expression.isMarkerAnnotationExpr()) {
            jsonExpression.add("expression", getMarkerAnnotationExpression((MarkerAnnotationExpr) expression));
        } else if (expression.isMethodCallExpr()) {
            jsonExpression.add("expression", getMethodCallExpression((MethodCallExpr) expression));
        } else if (expression.isMethodReferenceExpr()) {
            jsonExpression.add("expression", getMethodReferenceExpression((MethodReferenceExpr) expression));
        } else if (expression.isNameExpr()) {
            jsonExpression.add("expression", getNameExpression((NameExpr) expression));
        } else if (expression.isNormalAnnotationExpr()) {
            jsonExpression.add("expression", getNormalAnnotationExpression((NormalAnnotationExpr) expression));
        } else if (expression.isNullLiteralExpr()) {
            jsonExpression.add("expression", getNullLiteralExpression((NullLiteralExpr) expression));
        } else if (expression.isObjectCreationExpr()) {
            jsonExpression.add("expression", getObjectCreationExpression((ObjectCreationExpr) expression));
        } else if (expression.isSingleMemberAnnotationExpr()) {
            jsonExpression.add("expression", getSingleMemberAnnotationExpression((SingleMemberAnnotationExpr) expression));
        } else if (expression.isStringLiteralExpr()) {
            jsonExpression.add("expression", getStringLiteralExpression((StringLiteralExpr) expression));
        } else if (expression.isSuperExpr()) {
            jsonExpression.add("expression", getSuperExpression((SuperExpr) expression));
        } else if (expression.isSwitchExpr()) {
            jsonExpression.add("expression", getSwitchExpression((SwitchExpr) expression));
        } else if (expression.isTextBlockLiteralExpr()) {
            jsonExpression.add("expression", getTextBlockLiteralExpression((TextBlockLiteralExpr) expression));
        } else if (expression.isThisExpr()) {
            jsonExpression.add("expression", getThisExpression((ThisExpr) expression));
        } else if (expression.isTypeExpr()) {
            jsonExpression.add("expression", getTypeExpression((TypeExpr) expression));
        } else if (expression.isUnaryExpr()) {
            jsonExpression.add("expression", getUnaryExpression((UnaryExpr) expression));
        } else if (expression.isVariableDeclarationExpr()) {
            jsonExpression.add("expression", getVariableDeclarationExpression((VariableDeclarationExpr) expression));
        } else if (expression.isLongLiteralExpr()) {
            jsonExpression.add("expression", getLongLiteralExpression((LongLiteralExpr) expression));
        }else if (expression.isLiteralStringValueExpr()) {
            jsonExpression.add("expression", getLiteralStringValueExpression((LiteralStringValueExpr) expression));
        } else if (expression.isLiteralExpr()) {
            jsonExpression.add("expression", getLiteralExpression((LiteralExpr) expression));
        }
//        if (expression.isAnnotationExpr()) {
//            jsonExpression.add("expression", getAnnotationExpression((AnnotationExpr) expression));
//        } else
            //todo Make all Подумать о наследственности, annotationExpr перекрывает MarkerAnnotationExpr
        return jsonExpression;
    }

    public static JsonObject getVariableDeclarationExpression(VariableDeclarationExpr variableDeclarationExpr) {
        JsonObject jsonVariableDeclarationExpr = new JsonObject();
        jsonVariableDeclarationExpr.addProperty("mode", VariableDeclarationExpr.class.getName());
        jsonVariableDeclarationExpr.add("annotations", getExpressions(variableDeclarationExpr.getAnnotations()));
        jsonVariableDeclarationExpr.add("modifiers", getModifiers(variableDeclarationExpr.getModifiers()));
        jsonVariableDeclarationExpr.add("variables", getVariableDeclarators(variableDeclarationExpr.getVariables()));
        return jsonVariableDeclarationExpr;
    }

    public static JsonObject getUnaryExpression(UnaryExpr unaryExpr) {
        JsonObject jsonUnaryExpr = new JsonObject();
        jsonUnaryExpr.addProperty("mode", UnaryExpr.class.getName());
        jsonUnaryExpr.add("expression", getExpression(unaryExpr.getExpression()));
        jsonUnaryExpr.add("operator", getOperator(unaryExpr.getOperator()));
        return jsonUnaryExpr;
    }

    public static JsonObject getTypeExpression(TypeExpr typeExpr) {
        JsonObject jsonTypeExpr = new JsonObject();
        jsonTypeExpr.addProperty("mode", TypeExpr.class.getName());
        jsonTypeExpr.add("type", getType(typeExpr.getType()));
        return jsonTypeExpr;
    }

    public static JsonObject getThisExpression(ThisExpr thisExpr) {
        JsonObject jsonThisExpr = new JsonObject();
        jsonThisExpr.addProperty("mode", ThisExpr.class.getName());
        jsonThisExpr.add("name", thisExpr.getTypeName().isPresent() ? getName(thisExpr.getTypeName().get()) : null);
        return jsonThisExpr;
    }

    public static JsonObject getTextBlockLiteralExpression(TextBlockLiteralExpr textBlockLiteralExpr) {
        JsonObject jsonTextBlockLiteralExpr = new JsonObject();
        jsonTextBlockLiteralExpr.addProperty("mode", TextBlockLiteralExpr.class.getName());
        jsonTextBlockLiteralExpr.addProperty("value", textBlockLiteralExpr.getValue());
        return jsonTextBlockLiteralExpr;
    }

    public static JsonObject getSwitchExpression(SwitchExpr switchExpr) {
        JsonObject jsonSwitchExpr = new JsonObject();
        jsonSwitchExpr.addProperty("mode", SwitchExpr.class.getName());
        jsonSwitchExpr.add("selector", getExpression(switchExpr.getSelector()));
        jsonSwitchExpr.add("entries", getSwitchEntries(switchExpr.getEntries()));
        return jsonSwitchExpr;
    }

    public static JsonObject getSuperExpression(SuperExpr superExpr) {
        JsonObject jsonSuperExpr = new JsonObject();
        jsonSuperExpr.addProperty("mode", SuperExpr.class.getName());
        jsonSuperExpr.add("typeName", superExpr.getTypeName().isPresent() ? getName(superExpr.getTypeName().get()) : null);
        return jsonSuperExpr;
    }


    public static JsonObject getStringLiteralExpression(StringLiteralExpr stringLiteralExpr) {
        JsonObject jsonStringLiteralExpr = new JsonObject();
        jsonStringLiteralExpr.addProperty("mode", StringLiteralExpr.class.getName());
        jsonStringLiteralExpr.addProperty("value", stringLiteralExpr.getValue());
        return jsonStringLiteralExpr;
    }

    public static JsonObject getSingleMemberAnnotationExpression(SingleMemberAnnotationExpr singleMemberAnnotationExpr) {
        JsonObject jsonSingleMemberAnnotationExpr = new JsonObject();
        jsonSingleMemberAnnotationExpr.addProperty("mode", SingleMemberAnnotationExpr.class.getName());
        jsonSingleMemberAnnotationExpr.add("memberValue", getExpression(singleMemberAnnotationExpr.getMemberValue()));
        jsonSingleMemberAnnotationExpr.add("name", getName(singleMemberAnnotationExpr.getName()));
        return jsonSingleMemberAnnotationExpr;
    }

    public static JsonObject getObjectCreationExpression(ObjectCreationExpr objectCreationExpr) {
        JsonObject jsonObjectCreationExpr = new JsonObject();
        jsonObjectCreationExpr.addProperty("mode", ObjectCreationExpr.class.getName());
        jsonObjectCreationExpr.add("scope", objectCreationExpr.getScope().isPresent() ? getExpression(objectCreationExpr.getScope().get()) : null);
        jsonObjectCreationExpr.add("type", getType(objectCreationExpr.getType()));
        jsonObjectCreationExpr.add("typeArguments", objectCreationExpr.getTypeArguments().isPresent() ? getTypes(objectCreationExpr.getTypeArguments().get()) : null);
        jsonObjectCreationExpr.add("arguments", getExpressions(objectCreationExpr.getArguments()));
        jsonObjectCreationExpr.add("anonymousClassBody",objectCreationExpr.getAnonymousClassBody().isPresent() ? getDeclarations(objectCreationExpr.getAnonymousClassBody().get()) : null);
        return jsonObjectCreationExpr;
    }

    public static JsonObject getNullLiteralExpression(NullLiteralExpr nullLiteralExpr) {
        JsonObject jsonNullLiteralExpr = new JsonObject();
        jsonNullLiteralExpr.addProperty("mode", NullLiteralExpr.class.getName());
        return jsonNullLiteralExpr;
    }

    public static JsonObject getNormalAnnotationExpression(NormalAnnotationExpr normalAnnotationExpr) {
        JsonObject jsonNormalAnnotationExpr = new JsonObject();
        jsonNormalAnnotationExpr.addProperty("mode", NormalAnnotationExpr.class.getName());
        jsonNormalAnnotationExpr.add("name", getName(normalAnnotationExpr.getName()));
        jsonNormalAnnotationExpr.add("pairs", getMemberValuePairs(normalAnnotationExpr.getPairs()));
        return jsonNormalAnnotationExpr;
    }

    public static JsonObject getNameExpression(NameExpr nameExpr) {
        JsonObject jsonNameExpr = new JsonObject();
        jsonNameExpr.addProperty("mode", NameExpr.class.getName());
        jsonNameExpr.addProperty("name", nameExpr.getNameAsString());
        return jsonNameExpr;
    }

    public static JsonObject getMethodReferenceExpression(MethodReferenceExpr methodReferenceExpr) {
        JsonObject jsonMethodReferenceExpr = new JsonObject();
        jsonMethodReferenceExpr.addProperty("mode", MethodReferenceExpr.class.getName());
        jsonMethodReferenceExpr.addProperty("identifier", methodReferenceExpr.getIdentifier());
        jsonMethodReferenceExpr.add("scope", getExpression(methodReferenceExpr.getScope()));
        jsonMethodReferenceExpr.add("typeArguments", methodReferenceExpr.getTypeArguments().isPresent() ? getTypes(methodReferenceExpr.getTypeArguments().get()) : null);
        return jsonMethodReferenceExpr;
    }

    public static JsonObject getMethodCallExpression(MethodCallExpr methodCallExpr) {
        JsonObject jsonMethodCallExpr = new JsonObject();
        jsonMethodCallExpr.addProperty("mode", MethodCallExpr.class.getName());
        jsonMethodCallExpr.add("scope", methodCallExpr.getScope().isPresent() ? getExpression(methodCallExpr.getScope().get()) : null);
        jsonMethodCallExpr.add("typeArguments", methodCallExpr.getTypeArguments().isPresent() ? getTypes(methodCallExpr.getTypeArguments().get()) : null);
        jsonMethodCallExpr.addProperty("name", methodCallExpr.getNameAsString());
        jsonMethodCallExpr.add("arguments", getExpressions(methodCallExpr.getArguments()));
        return jsonMethodCallExpr;
    }

    public static JsonObject getMarkerAnnotationExpression(MarkerAnnotationExpr markerAnnotationExpr) {
        JsonObject jsonMarkerAnnotationExpr = new JsonObject();
        jsonMarkerAnnotationExpr.addProperty("mode", MarkerAnnotationExpr.class.getName());
        jsonMarkerAnnotationExpr.add("name", getName(markerAnnotationExpr.getName()));
        return jsonMarkerAnnotationExpr;
    }

    public static JsonObject getLongLiteralExpression(LongLiteralExpr longLiteralExpr) {
        JsonObject jsonLongLiteralExpr = new JsonObject();
        jsonLongLiteralExpr.addProperty("mode", LongLiteralExpr.class.getName());
        jsonLongLiteralExpr.addProperty("value", longLiteralExpr.getValue());
        return jsonLongLiteralExpr;
    }

    public static JsonObject getLiteralStringValueExpression(LiteralStringValueExpr literalStringValueExpr) {
        JsonObject jsonLiteralStringValueExpr = new JsonObject();
        jsonLiteralStringValueExpr.addProperty("mode", LiteralStringValueExpr.class.getName());
        jsonLiteralStringValueExpr.addProperty("value", literalStringValueExpr.getValue());
        return jsonLiteralStringValueExpr;
    }

    public static JsonObject getLiteralExpression(LiteralExpr literalExpr) {
        JsonObject jsonLiteralExpr = new JsonObject();
        jsonLiteralExpr.addProperty("mode", LiteralExpr.class.getName());
        return jsonLiteralExpr;
    }

    public static JsonObject getLambdaExpression(LambdaExpr lambdaExpr) {
        JsonObject jsonLambdaExpr = new JsonObject();
        jsonLambdaExpr.addProperty("mode", LambdaExpr.class.getName());
        jsonLambdaExpr.add("parameters", getParameters(lambdaExpr.getParameters()));
        jsonLambdaExpr.add("body", getStatement(lambdaExpr.getBody()));
        jsonLambdaExpr.addProperty("isEnclosingParameters", lambdaExpr.isEnclosingParameters());
        return jsonLambdaExpr;
    }

    public static JsonObject getIntegerLiteralExpression(IntegerLiteralExpr integerLiteralExpr) {
        JsonObject jsonIntegerLiteralExpr = new JsonObject();
        jsonIntegerLiteralExpr.addProperty("mode", IntegerLiteralExpr.class.getName());
        jsonIntegerLiteralExpr.addProperty("value", integerLiteralExpr.getValue());
        return jsonIntegerLiteralExpr;
    }

    public static JsonObject getPatternExpression(PatternExpr patternExpr) {
        JsonObject jsonPatternExpr = new JsonObject();
        jsonPatternExpr.addProperty("mode", PatternExpr.class.getName());
        jsonPatternExpr.addProperty("name", patternExpr.getNameAsString());
        jsonPatternExpr.add("type", getType(patternExpr.getType()));
        return jsonPatternExpr;
    }

    public static JsonObject getInstanceOfExpression(InstanceOfExpr instanceOfExpr) {
        JsonObject jsonInstanceOfExpr = new JsonObject();
        jsonInstanceOfExpr.addProperty("mode", InstanceOfExpr.class.getName());
        jsonInstanceOfExpr.add("expression", getExpression(instanceOfExpr.getExpression()));
        jsonInstanceOfExpr.add("type", getType(instanceOfExpr.getType()));
        jsonInstanceOfExpr.add("pattern", instanceOfExpr.getPattern().isPresent() ? getExpression(instanceOfExpr.getPattern().get()) : null);
        return jsonInstanceOfExpr;
    }

    public static JsonObject getFieldAccessExpression(FieldAccessExpr fieldAccessExpr) {
        JsonObject jsonFieldAccessExpr = new JsonObject();
        jsonFieldAccessExpr.addProperty("mode", FieldAccessExpr.class.getName());
        jsonFieldAccessExpr.add("scope", getExpression(fieldAccessExpr.getScope()));
        jsonFieldAccessExpr.add("typeArguments", fieldAccessExpr.getTypeArguments().isPresent() ? getTypes(fieldAccessExpr.getTypeArguments().get()) : null);
        jsonFieldAccessExpr.addProperty("name", fieldAccessExpr.getNameAsString());
        return jsonFieldAccessExpr;
    }

    public static JsonObject getEnclosedExpression(EnclosedExpr enclosedExpr) {
        JsonObject jsonEnclosedExpr = new JsonObject();
        jsonEnclosedExpr.addProperty("mode", EnclosedExpr.class.getName());
        jsonEnclosedExpr.add("inner", getExpression(enclosedExpr.getInner()));
        return jsonEnclosedExpr;
    }

    public static JsonObject getDoubleLiteralExpression(DoubleLiteralExpr doubleLiteralExpr) {
        JsonObject jsonDoubleLiteralExpr = new JsonObject();
        jsonDoubleLiteralExpr.addProperty("mode", DoubleLiteralExpr.class.getName());
        jsonDoubleLiteralExpr.addProperty("value", doubleLiteralExpr.getValue());
        return jsonDoubleLiteralExpr;
    }

    public static JsonObject getConditionalExpression(ConditionalExpr conditionalExpr) {
        JsonObject jsonConditionalExpr = new JsonObject();
        jsonConditionalExpr.addProperty("mode", ConditionalExpr.class.getName());
        jsonConditionalExpr.add("condition", getExpression(conditionalExpr.getCondition()));
        jsonConditionalExpr.add("thenExpr", getExpression(conditionalExpr.getThenExpr()));
        jsonConditionalExpr.add("elseExpr", getExpression(conditionalExpr.getElseExpr()));
        return jsonConditionalExpr;
    }

    public static JsonObject getClassExpression(ClassExpr classExpr) {
        JsonObject jsonClassExpr = new JsonObject();
        jsonClassExpr.addProperty("mode", ClassExpr.class.getName());
        jsonClassExpr.add("type", getType(classExpr.getType()));
        return jsonClassExpr;
    }

    public static JsonObject getCharLiteralExpression(CharLiteralExpr charLiteralExpr) {
        JsonObject jsonCharLiteralExpr = new JsonObject();
        jsonCharLiteralExpr.addProperty("mode", CharLiteralExpr.class.getName());
        jsonCharLiteralExpr.addProperty("value", charLiteralExpr.getValue());
        return jsonCharLiteralExpr;
    }

    public static JsonObject getBooleanLiteralExpression(BooleanLiteralExpr booleanLiteralExpr) {
        JsonObject jsonBooleanLiteralExpr = new JsonObject();
        jsonBooleanLiteralExpr.addProperty("mode", BooleanLiteralExpr.class.getName());
        jsonBooleanLiteralExpr.addProperty("value", booleanLiteralExpr.getValue());
        return jsonBooleanLiteralExpr;
    }

    public static JsonObject getArrayInitializerExpression(ArrayInitializerExpr arrayInitializerExpr) {
        JsonObject jsonArrayInitializerExpr = new JsonObject();
        jsonArrayInitializerExpr.addProperty("mode", ArrayInitializerExpr.class.getName());
        jsonArrayInitializerExpr.add("values", getExpressions(arrayInitializerExpr.getValues()));
        return jsonArrayInitializerExpr;
    }

    public static JsonObject getArrayCreationExpression(ArrayCreationExpr arrayCreationExpr) {
        JsonObject jsonArrayCreationExpr = new JsonObject();
        jsonArrayCreationExpr.addProperty("mode", ArrayCreationExpr.class.getName());
        jsonArrayCreationExpr.add("levels", getArrayCreationLevels(arrayCreationExpr.getLevels()));
        jsonArrayCreationExpr.add("elementType", getType(arrayCreationExpr.getElementType()));
        jsonArrayCreationExpr.add("initializer", arrayCreationExpr.getInitializer().isPresent() ? getExpression(arrayCreationExpr.getInitializer().get()) : null);
        return jsonArrayCreationExpr;
    }

    public static JsonObject getBinaryExpression(BinaryExpr binaryExpr) {
        JsonObject jsonBinaryExpr = new JsonObject();
        jsonBinaryExpr.addProperty("mode", BinaryExpr.class.getName());
        jsonBinaryExpr.add("left", getExpression(binaryExpr.getLeft()));
        jsonBinaryExpr.add("right", getExpression(binaryExpr.getRight()));
        jsonBinaryExpr.addProperty("operator", binaryExpr.getOperator().asString());
        return jsonBinaryExpr;
    }

    public static JsonObject getAssignExpression(AssignExpr assignExpr) {
        JsonObject jsonAssignExpr = new JsonObject();
        jsonAssignExpr.addProperty("mode", AssignExpr.class.getName());
        jsonAssignExpr.add("target", getExpression(assignExpr.getTarget()));
        jsonAssignExpr.add("value", getExpression(assignExpr.getValue()));
        jsonAssignExpr.addProperty("operator", assignExpr.getOperator().asString());
        return jsonAssignExpr;
    }

    public static JsonObject getCastExpression(CastExpr castExpr) {
        JsonObject jsonCastExpression = new JsonObject();
        jsonCastExpression.addProperty("mode", CastExpr.class.getName());
        jsonCastExpression.add("expression", getExpression(castExpr.getExpression()));
        jsonCastExpression.add("type", getType(castExpr.getType()));
        return jsonCastExpression;
    }

    public static JsonObject getArrayAccessExpression(ArrayAccessExpr arrayAccessExpr) {
        JsonObject jsonArrayAccessExpression = new JsonObject();
        jsonArrayAccessExpression.addProperty("mode", ArrayAccessExpr.class.getName());
        jsonArrayAccessExpression.add("index", getExpression(arrayAccessExpr.getIndex()));
        jsonArrayAccessExpression.add("name", getExpression(arrayAccessExpr.getName()));
        return jsonArrayAccessExpression;
    }

    public static JsonObject getAnnotationExpression(AnnotationExpr annotationExpr) {
        JsonObject jsonAnnotationExpression = new JsonObject();
        jsonAnnotationExpression.addProperty("mode", AnnotationExpr.class.getName());
        jsonAnnotationExpression.add("name", getName(annotationExpr.getName()));
        return jsonAnnotationExpression;
    }
}
