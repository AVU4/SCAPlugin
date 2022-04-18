package converters.JavaToJson;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.type.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import static converters.JavaToJson.ExpressionConverterToJSON.*;

public class TypeConverterToJSON {

    public static JsonArray getTypes(NodeList<? extends Type> nodeList) {
        JsonArray jsonArray = new JsonArray();
        nodeList.stream().map(TypeConverterToJSON::getType).forEach(jsonArray::add);
        return jsonArray;
    }

    public static JsonObject getType(Type type) {
        JsonObject jsonType = new JsonObject();

        JsonObject jsonSpecificType = new JsonObject();

        jsonSpecificType.add("annotations", getExpressions(type.getAnnotations()));
        jsonSpecificType.addProperty("arrayLevel", type.getArrayLevel());

        if (type.isArrayType()) {
            jsonType.add("type", getArrayType((ArrayType) type, jsonSpecificType));
        } else if (type.isPrimitiveType()) {
            jsonType.add("type", getPrimitiveType((PrimitiveType) type, jsonSpecificType));
        } else if (type.isUnionType()) {
            jsonType.add("type", getUnionType((UnionType) type, jsonSpecificType));
        } else if (type.isClassOrInterfaceType()) {
            jsonType.add("type", getClassOrInterfaceType((ClassOrInterfaceType) type, jsonSpecificType));
        } else if (type.isIntersectionType()) {
            jsonType.add("type", getIntersectionType((IntersectionType) type, jsonSpecificType));
        } else if (type.isTypeParameter()) {
            jsonType.add("type", getTypeParameter((TypeParameter) type, jsonSpecificType));
        } else if (type.isWildcardType()) {
            jsonType.add("type", getWildcardType((WildcardType) type, jsonSpecificType));
        } else if (type.isUnknownType()) {
            jsonSpecificType.addProperty("mode", UnknownType.class.getName());
            jsonType.add("type", jsonSpecificType);
        } else if (type.isReferenceType()) {
            jsonSpecificType.addProperty("mode", ReferenceType.class.getName());
            jsonType.add("type", jsonSpecificType);
        } else if (type.isVoidType()) {
            jsonSpecificType.addProperty("mode", VoidType.class.getName());
            jsonType.add("type", jsonSpecificType);
        } else if (type.isVarType()) {
            jsonSpecificType.addProperty("mode", VarType.class.getName());
            jsonType.add("type", jsonSpecificType);
        }
        return jsonType;
    }

    public static JsonObject getWildcardType(WildcardType type, JsonObject jsonWildcardType) {
        jsonWildcardType.addProperty("mode", WildcardType.class.getName());
        jsonWildcardType.add("extendedType", type.getExtendedType().isPresent() ? getType(type.getExtendedType().get()) : null);
        jsonWildcardType.add("superType", type.getSuperType().isPresent() ? getType(type.getSuperType().get()) : null);
        return jsonWildcardType;
    }

    public static JsonObject getTypeParameter(TypeParameter type, JsonObject jsonTypeParameter) {
        jsonTypeParameter.addProperty("mode", TypeParameter.class.getName());
        jsonTypeParameter.addProperty("name", type.getNameAsString());
        jsonTypeParameter.add("typeBound", getTypes(type.getTypeBound()));
        return jsonTypeParameter;
    }

    public static JsonObject getIntersectionType(IntersectionType type, JsonObject jsonIntersectionType) {
        jsonIntersectionType.addProperty("mode", IntersectionType.class.getName());
        jsonIntersectionType.add("elements", getTypes(type.getElements()));
        return jsonIntersectionType;
    }

    public static JsonObject getClassOrInterfaceType(ClassOrInterfaceType classOrInterfaceType, JsonObject jsonClassOrInterfaceType) {
        jsonClassOrInterfaceType.addProperty("mode", ClassOrInterfaceType.class.getName());
        jsonClassOrInterfaceType.add("typeArguments", classOrInterfaceType.getTypeArguments().isPresent() ? getTypes(classOrInterfaceType.getTypeArguments().get()) : null);
        jsonClassOrInterfaceType.add("scope", classOrInterfaceType.getScope().isPresent() ? getType(classOrInterfaceType.getScope().get()) : null);
        jsonClassOrInterfaceType.addProperty("simpleName", classOrInterfaceType.getNameAsString());
        return jsonClassOrInterfaceType;
    }

    public static JsonObject getUnionType(UnionType type, JsonObject jsonUnionType) {
        jsonUnionType.addProperty("mode", UnionType.class.getName());
        jsonUnionType.add("elements", getTypes(type.getElements()));
        return jsonUnionType;
    }

    public static JsonObject getPrimitiveType(PrimitiveType primitiveType, JsonObject jsonPrimitiveType) {
        jsonPrimitiveType.addProperty("mode", PrimitiveType.class.getName());
        jsonPrimitiveType.addProperty("type", primitiveType.getType().asString());
        return jsonPrimitiveType;
    }

    public static JsonObject getArrayType(ArrayType arrayType, JsonObject jsonArrayType) {
        jsonArrayType.addProperty("mode", ArrayType.class.getName());
        jsonArrayType.add("componentType", getType(arrayType.getComponentType()));
        jsonArrayType.addProperty("origin", arrayType.getOrigin().name());
        return jsonArrayType;
    }
}
