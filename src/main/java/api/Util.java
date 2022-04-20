package api;

import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.google.gson.JsonObject;

public class Util {


    public static String getKey(JsonObject jsonObject) {
        return (String) jsonObject.keySet().toArray()[0];
    }

    public static String getDeclarationData(BodyDeclaration<?> bodyDeclaration) {
        if (bodyDeclaration.isClassOrInterfaceDeclaration()) {
            ClassOrInterfaceDeclaration classOrInterfaceDeclaration = (ClassOrInterfaceDeclaration) bodyDeclaration;
            classOrInterfaceDeclaration.setName(classOrInterfaceDeclaration.getNameAsString());
            return classOrInterfaceDeclaration.toString();
        } else if (bodyDeclaration.isEnumDeclaration()) {
            EnumDeclaration enumDeclaration = (EnumDeclaration) bodyDeclaration;
            enumDeclaration.setName(enumDeclaration.getNameAsString());
            return enumDeclaration.toString();
        }
        return "";
    }

    public static String resolveNameFile(BodyDeclaration<?> bodyDeclaration) {
        if (bodyDeclaration.isEnumDeclaration()) {
            return ((EnumDeclaration) bodyDeclaration).getNameAsString();
        } else if (bodyDeclaration.isClassOrInterfaceDeclaration()) {
            return ((ClassOrInterfaceDeclaration) bodyDeclaration).getNameAsString();
        }
        return "";
    }


}
