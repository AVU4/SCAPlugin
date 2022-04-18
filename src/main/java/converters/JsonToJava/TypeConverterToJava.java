package converters.JsonToJava;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.type.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Locale;

import static converters.JsonToJava.UtilConverterToJava.getAnnotationExpressions;

//todo ArrayLevel, ReferenceType

public class TypeConverterToJava {

    public static NodeList<Type> getTypes(JsonArray jsonArray) {
        NodeList<Type> nodeList = new NodeList<>();
        for (JsonElement jsonElement : jsonArray) {
            nodeList.add(getType(jsonElement.getAsJsonObject()));
        }
        return nodeList;
    }

    public static Type getType(JsonObject jsonObject) {
        JsonObject jsonType = jsonObject.getAsJsonObject("type");
        if (jsonType.get("mode").getAsString().equals(ArrayType.class.getName())) {
            return getArrayType(jsonType);
        } else if (jsonType.get("mode").getAsString().equals(PrimitiveType.class.getName())) {
            return getPrimitiveType(jsonType);
        } else if (jsonType.get("mode").getAsString().equals(UnionType.class.getName())) {
            return getUnionType(jsonType);
        } else if (jsonType.get("mode").getAsString().equals(ClassOrInterfaceType.class.getName())) {
            return getClassOrInterfaceType(jsonType);
        } else if (jsonType.get("mode").getAsString().equals(IntersectionType.class.getName())) {
            return getIntersectionType(jsonType);
        } else if (jsonType.get("mode").getAsString().equals(TypeParameter.class.getName())) {
            return getTypeParameter(jsonType);
        } else if (jsonType.get("mode").getAsString().equals(WildcardType.class.getName())) {
            return getWildcardType(jsonType);
        } else if (jsonType.get("mode").getAsString().equals(UnknownType.class.getName())) {
            return getUnknownType(jsonType);
        } else if (jsonType.get("mode").getAsString().equals(VoidType.class.getName())) {
            return getVoidType(jsonType);
        } else if (jsonType.get("mode").getAsString().equals(VarType.class.getName())) {
            return getVarType(jsonType);
        }
        return null;
    }

    private static VarType getVarType(JsonObject jsonObject) {
        VarType varType = new VarType();
        varType.setAnnotations(getAnnotationExpressions(jsonObject, "annotations"));
        return varType;
    }

    private static VoidType getVoidType(JsonObject jsonObject) {
        VoidType voidType = new VoidType();
        voidType.setAnnotations(getAnnotationExpressions(jsonObject, "annotations"));
        return voidType;
    }

    private static UnknownType getUnknownType(JsonObject jsonObject) {
        UnknownType unknownType = new UnknownType();
        unknownType.setAnnotations(getAnnotationExpressions(jsonObject, "annotations"));
        return unknownType;
    }

    private static WildcardType getWildcardType(JsonObject jsonObject) {
        return new WildcardType(
                (ReferenceType) getType(jsonObject.getAsJsonObject("extendedType")),
                (ReferenceType) getType(jsonObject.getAsJsonObject("superType")),
                getAnnotationExpressions(jsonObject, "annotations")
        );
    }

    private static TypeParameter getTypeParameter(JsonObject jsonObject) {
        return new TypeParameter(
                new SimpleName(jsonObject.get("name").getAsString()),
                getClassOrInterfaceTypes(jsonObject),
                getAnnotationExpressions(jsonObject, "annotations")
        );
    }

    private static IntersectionType getIntersectionType(JsonObject jsonObject) {
        IntersectionType intersectionType = new IntersectionType(getReferenceTypes(jsonObject.getAsJsonArray("elements")));
        intersectionType.setAnnotations(getAnnotationExpressions(jsonObject, "annotations"));
        return intersectionType;
    }

    private static ClassOrInterfaceType getClassOrInterfaceType(JsonObject jsonObject) {
        JsonElement typeArgumentsJson = jsonObject.get("typeArguments");
        JsonElement jsonScope = jsonObject.get("scope");
        NodeList<Type> typeArguments = !typeArgumentsJson.isJsonNull() ? getTypes(typeArgumentsJson.getAsJsonArray()) : null;
        return new ClassOrInterfaceType(
                !jsonScope.isJsonNull() ? (ClassOrInterfaceType) getType(jsonScope.getAsJsonObject()) : null,
                new SimpleName(jsonObject.get("simpleName").getAsString()),
                typeArguments,
                getAnnotationExpressions(jsonObject, "annotations")
        );
    }

    private static UnionType getUnionType(JsonObject jsonObject) {
        UnionType unionType = new UnionType(getReferenceTypes(jsonObject.getAsJsonArray("elements")));
        unionType.setAnnotations(getAnnotationExpressions(jsonObject, "annotations"));
        return unionType;
    }

    private static PrimitiveType getPrimitiveType(JsonObject jsonObject) {
        return new PrimitiveType(
                PrimitiveType.Primitive.valueOf(jsonObject.get("type").getAsString().toUpperCase(Locale.ROOT)),
                getAnnotationExpressions(jsonObject, "annotations")
        );
    }

    private static ArrayType getArrayType(JsonObject jsonObject) {
        return new ArrayType(
                getType(jsonObject.getAsJsonObject("componentType")),
                ArrayType.Origin.valueOf(jsonObject.get("origin").getAsString().toUpperCase(Locale.ROOT)),
                getAnnotationExpressions(jsonObject, "annotations")
        );
    }

    private static NodeList<ClassOrInterfaceType> getClassOrInterfaceTypes(JsonObject jsonObject) {
        NodeList<ClassOrInterfaceType> classOrInterfaceTypes = new NodeList<>();
        getTypes(jsonObject.getAsJsonArray("typeBound"))
                .stream()
                .map(type -> (ClassOrInterfaceType) type)
                .forEach(classOrInterfaceTypes::add);
        return classOrInterfaceTypes;
    }

    public static NodeList<ReferenceType> getReferenceTypes(JsonArray jsonArray) {
        NodeList<ReferenceType> referenceTypes = new NodeList<>();
        for (JsonElement jsonElement : jsonArray) {
            referenceTypes.add((ReferenceType) getType(jsonElement.getAsJsonObject()));
        }
        return referenceTypes;
    }
}
