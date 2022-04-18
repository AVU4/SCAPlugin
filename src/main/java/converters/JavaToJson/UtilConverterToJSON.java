package converters.JavaToJson;

import com.github.javaparser.ast.ArrayCreationLevel;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.ReceiverParameter;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.MemberValuePair;
import com.github.javaparser.ast.expr.Name;
import com.github.javaparser.ast.expr.UnaryExpr;
import com.github.javaparser.ast.stmt.SwitchEntry;
import com.github.javaparser.ast.type.TypeParameter;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import static converters.JavaToJson.ExpressionConverterToJSON.getExpression;
import static converters.JavaToJson.ExpressionConverterToJSON.getExpressions;
import static converters.JavaToJson.StatementConverterToJSON.getStatements;
import static converters.JavaToJson.TypeConverterToJSON.getType;
import static converters.JavaToJson.TypeConverterToJSON.getTypes;

public class UtilConverterToJSON {

    public static JsonObject getName(Name name) {
        JsonObject jsonName = new JsonObject();
        jsonName.addProperty("mode", Name.class.getName());
        jsonName.addProperty("identifier", name.getIdentifier());
        jsonName.add("qualifier", name.getQualifier().isPresent() ? getName(name.getQualifier().get()) : null);
        return jsonName;
    }




    public static JsonArray getMemberValuePairs(NodeList<MemberValuePair> memberValuePairs) {
        JsonArray jsonMemberValuePairs = new JsonArray();
        memberValuePairs.stream().map(UtilConverterToJSON::getMemberValuePair).forEach(jsonMemberValuePairs::add);
        return jsonMemberValuePairs;
    }

    public static JsonObject getMemberValuePair(MemberValuePair memberValuePair) {
        JsonObject jsonMemberValuePair = new JsonObject();
        jsonMemberValuePair.addProperty("mode", MemberValuePair.class.getName());
        jsonMemberValuePair.addProperty("name", memberValuePair.getNameAsString());
        jsonMemberValuePair.add("expression", getExpression(memberValuePair.getValue()));
        return jsonMemberValuePair;
    }




    public static JsonArray getSwitchEntries(NodeList<SwitchEntry> switchEntries) {
        JsonArray jsonSwitchEntries = new JsonArray();
        switchEntries.stream().map(UtilConverterToJSON::getSwitchEntry).forEach(jsonSwitchEntries::add);
        return jsonSwitchEntries;
    }

    public static JsonObject getSwitchEntry(SwitchEntry switchEntry) {
        JsonObject jsonSwitchEntry = new JsonObject();
        jsonSwitchEntry.addProperty("mode", SwitchEntry.class.getName());
        jsonSwitchEntry.addProperty("type", switchEntry.getType().name());
        jsonSwitchEntry.add("labels", getExpressions(switchEntry.getLabels()));
        jsonSwitchEntry.add("statements", getStatements(switchEntry.getStatements()));
        return jsonSwitchEntry;
    }




    public static JsonObject getOperator(UnaryExpr.Operator operator) {
        JsonObject jsonOperator = new JsonObject();
        jsonOperator.addProperty("mode", UnaryExpr.Operator.class.getName());
        jsonOperator.addProperty("isPostfix", operator.isPostfix());
        jsonOperator.addProperty("codeRepresentation", operator.asString());
        return jsonOperator;
    }




    public static JsonArray getArrayCreationLevels(NodeList<ArrayCreationLevel> arrayCreationLevels) {
        JsonArray jsonArray = new JsonArray();
        arrayCreationLevels.stream().map(UtilConverterToJSON::getArrayCreationLevel).forEach(jsonArray::add);
        return jsonArray;
    }

    public static JsonObject getArrayCreationLevel(ArrayCreationLevel arrayCreationLevel) {
        JsonObject jsonArrayCreationLevel = new JsonObject();
        jsonArrayCreationLevel.addProperty("mode", ArrayCreationLevel.class.getName());
        jsonArrayCreationLevel.add("dimension", arrayCreationLevel.getDimension().isPresent() ? getExpression(arrayCreationLevel.getDimension().get()) : null);
        jsonArrayCreationLevel.add("annotations", getExpressions(arrayCreationLevel.getAnnotations()));
        return jsonArrayCreationLevel;
    }




    public static JsonArray getModifiers(NodeList<Modifier> modifiers) {
        JsonArray jsonModifiers = new JsonArray();
        modifiers.stream().map(UtilConverterToJSON::getModifier).forEach(jsonModifiers::add);
        return jsonModifiers;
    }

    public static JsonObject getModifier(Modifier modifier) {
        JsonObject jsonModifier = new JsonObject();
        jsonModifier.addProperty("mode", Modifier.class.getName());
        jsonModifier.addProperty("keyword", modifier.getKeyword().asString());
        return jsonModifier;
    }



    public static JsonArray getParameters(NodeList<Parameter> parameters) {
        JsonArray jsonParameters = new JsonArray();
        parameters.stream().map(UtilConverterToJSON::getParameter).forEach(jsonParameters::add);
        return jsonParameters;
    }

    public static JsonObject getParameter(Parameter parameter) {
        JsonObject jsonParameter = new JsonObject();
        jsonParameter.addProperty("mode", Parameter.class.getName());
        jsonParameter.addProperty("name", parameter.getNameAsString());
        jsonParameter.add("type", getType(parameter.getType()));
        jsonParameter.addProperty("isVarArgs", parameter.isVarArgs());
        jsonParameter.add("varArgsAnnotations", getExpressions(parameter.getVarArgsAnnotations()));
        jsonParameter.add("modifiers", getModifiers(parameter.getModifiers()));
        jsonParameter.add("annotations", getExpressions(parameter.getAnnotations()));
        return jsonParameter;
    }


    public static JsonArray getTypeParameters(NodeList<TypeParameter> typeParameters) {
        JsonArray jsonTypeParameters = new JsonArray();
        typeParameters.stream().map(UtilConverterToJSON::getTypeParameter).forEach(jsonTypeParameters::add);
        return jsonTypeParameters;
    }

    public static JsonObject getTypeParameter(TypeParameter typeParameter) {
        JsonObject jsonTypeParameter = new JsonObject();
        jsonTypeParameter.addProperty("mode", TypeParameter.class.getName());
        jsonTypeParameter.addProperty("name", typeParameter.getNameAsString());
        jsonTypeParameter.add("typeBound", getTypes(typeParameter.getTypeBound()));
        jsonTypeParameter.add("annotations", getExpressions(typeParameter.getAnnotations()));
        return jsonTypeParameter;
    }


    public static JsonObject getReceiverParameter(ReceiverParameter receiverParameter) {
        JsonObject jsonReceiverParameter = new JsonObject();
        jsonReceiverParameter.addProperty("mode", ReceiverParameter.class.getName());
        jsonReceiverParameter.add("type", getType(receiverParameter.getType()));
        jsonReceiverParameter.add("annotations", getExpressions(receiverParameter.getAnnotations()));
        jsonReceiverParameter.add("name", getName(receiverParameter.getName()));
        return jsonReceiverParameter;
    }



    public static JsonArray getVariableDeclarators(NodeList<VariableDeclarator> variableDeclarators) {
        JsonArray jsonVariableDeclarators = new JsonArray();
        variableDeclarators.stream().map(UtilConverterToJSON::getVariableDeclarator).forEach(jsonVariableDeclarators::add);
        return jsonVariableDeclarators;
    }

    public static JsonObject getVariableDeclarator(VariableDeclarator variableDeclarator) {
        JsonObject jsonVariableDeclarator = new JsonObject();
        jsonVariableDeclarator.addProperty("mode", VariableDeclarator.class.getName());
        jsonVariableDeclarator.addProperty("name", variableDeclarator.getNameAsString());
        jsonVariableDeclarator.add("type", getType(variableDeclarator.getType()));
        jsonVariableDeclarator.add("initializer", variableDeclarator.getInitializer().isPresent() ? getExpression(variableDeclarator.getInitializer().get()) : null);
        return jsonVariableDeclarator;
    }
}
