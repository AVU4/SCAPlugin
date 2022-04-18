package converters.JsonToJava;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.RecordDeclaration;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.stmt.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import static converters.JsonToJava.DeclarationConverterToJava.getDeclaration;
import static converters.JsonToJava.ExpressionConverterToJava.getExpression;
import static converters.JsonToJava.ExpressionConverterToJava.getExpressions;
import static converters.JsonToJava.TypeConverterToJava.getTypes;
import static converters.JsonToJava.UtilConverterToJava.getSwitchEntries;

public class StatementConverterToJava {

    public static NodeList<Statement> getStatements(JsonArray jsonArray) {
        NodeList<Statement> statements = new NodeList<>();
        for (JsonElement jsonElement : jsonArray) {
            statements.add(getStatement(jsonElement.getAsJsonObject()));
        }
        return statements;
    }


    public static Statement getStatement(JsonObject jsonObject) {
        JsonObject statement = jsonObject.getAsJsonObject("statement");
        if (statement.get("mode").getAsString().equals(AssertStmt.class.getName())) {
            return getAssertStatement(statement);
        } else if (statement.get("mode").getAsString().equals(BlockStmt.class.getName())) {
            return getBlockStatement(statement);
        } else if (statement.get("mode").getAsString().equals(BreakStmt.class.getName())) {
            return getBreakStatement(statement);
        } else if (statement.get("mode").getAsString().equals(ContinueStmt.class.getName())) {
            return getContinueStatement(statement);
        } else if (statement.get("mode").getAsString().equals(DoStmt.class.getName())) {
            return getDoStatement(statement);
        } else if (statement.get("mode").getAsString().equals(EmptyStmt.class.getName())) {
            return getEmptyStatement(statement);
        } else if (statement.get("mode").getAsString().equals(ExplicitConstructorInvocationStmt.class.getName())) {
            return getExplicitConstructorInvocationStatement(statement);
        } else if (statement.get("mode").getAsString().equals(ExpressionStmt.class.getName())) {
            return getExpressionStatement(statement);
        } else if (statement.get("mode").getAsString().equals(ForEachStmt.class.getName())) {
            return getForEachStatement(statement);
        } else if (statement.get("mode").getAsString().equals(ForStmt.class.getName())) {
            return getForStatement(statement);
        } else if (statement.get("mode").getAsString().equals(IfStmt.class.getName())) {
            return getIfStatement(statement);
        } else if (statement.get("mode").getAsString().equals(LabeledStmt.class.getName())) {
            return getLabeledStatement(statement);
        } else if (statement.get("mode").getAsString().equals(LocalClassDeclarationStmt.class.getName())) {
            return getLocalClassDeclarationStatement(statement);
        } else if (statement.get("mode").getAsString().equals(LocalRecordDeclarationStmt.class.getName())) {
            return getLocalRecordDeclarationStatement(statement);
        } else if (statement.get("mode").getAsString().equals(ReturnStmt.class.getName())) {
            return getReturnStatement(statement);
        } else if (statement.get("mode").getAsString().equals(SwitchStmt.class.getName())) {
            return getSwitchStatement(statement);
        } else if (statement.get("mode").getAsString().equals(SynchronizedStmt.class.getName())) {
            return getSynchronizedStatement(statement);
        } else if (statement.get("mode").getAsString().equals(ThrowStmt.class.getName())) {
            return getThrowStatement(statement);
        } else if (statement.get("mode").getAsString().equals(TryStmt.class.getName())) {
            return getTryStatement(statement);
        } else if (statement.get("mode").getAsString().equals(UnparsableStmt.class.getName())) {
            return getUnparsableStatement(statement);
        } else if (statement.get("mode").getAsString().equals(WhileStmt.class.getName())) {
            return getWhileStatement(statement);
        } else if (statement.get("mode").getAsString().equals(YieldStmt.class.getName())) {
            return getYieldStatement(statement);
        }
        return null;
    }

    private static YieldStmt getYieldStatement(JsonObject jsonObject) {
        return new YieldStmt(getExpression(jsonObject.getAsJsonObject("expression")));
    }

    private static WhileStmt getWhileStatement(JsonObject jsonObject) {
        return new WhileStmt(
                getExpression(jsonObject.getAsJsonObject("condition")),
                getStatement(jsonObject.getAsJsonObject("body"))
        );
    }

    private static UnparsableStmt getUnparsableStatement(JsonObject jsonObject) {
        return new UnparsableStmt();
    }

    private static TryStmt getTryStatement(JsonObject jsonObject) {
        JsonElement jsonFinallyBlock = jsonObject.get("finallyBlock");
        return new TryStmt(
                getExpressions(jsonObject.getAsJsonArray("resources")),
                (BlockStmt) getStatement(jsonObject.getAsJsonObject("tryBlock")),
                null, //todo CatchClause
                !jsonFinallyBlock.isJsonNull() ? (BlockStmt) getStatement(jsonFinallyBlock.getAsJsonObject()) : null
        );
    }

    private static ThrowStmt getThrowStatement(JsonObject jsonObject) {
        return new ThrowStmt(getExpression(jsonObject.getAsJsonObject("expression")));
    }

    private static SynchronizedStmt getSynchronizedStatement(JsonObject jsonObject) {
        return new SynchronizedStmt(
                getExpression(jsonObject.getAsJsonObject("expression")),
                (BlockStmt) getStatement(jsonObject.getAsJsonObject("body"))
        );
    }

    private static SwitchStmt getSwitchStatement(JsonObject jsonObject) {
        return new SwitchStmt(
                getExpression(jsonObject.getAsJsonObject("selector")),
                getSwitchEntries(jsonObject.getAsJsonArray("entries"))
        );
    }

    private static ReturnStmt getReturnStatement(JsonObject jsonObject) {
        JsonElement jsonExpression = jsonObject.get("expression");
        return new ReturnStmt(
                !jsonExpression.isJsonNull() ? getExpression(jsonExpression.getAsJsonObject()) : null
        );
    }

    private static LocalRecordDeclarationStmt getLocalRecordDeclarationStatement(JsonObject jsonObject) {
        return new LocalRecordDeclarationStmt(
                (RecordDeclaration) getDeclaration(jsonObject.getAsJsonObject("recordDeclaration"))
        );
    }

    private static LocalClassDeclarationStmt getLocalClassDeclarationStatement(JsonObject jsonObject) {
        return new LocalClassDeclarationStmt(
                (ClassOrInterfaceDeclaration) getDeclaration(jsonObject.getAsJsonObject("classDeclaration"))
        );
    }

    private static LabeledStmt getLabeledStatement(JsonObject jsonObject) {
        return new LabeledStmt(
                new SimpleName(jsonObject.get("label").getAsString()),
                getStatement(jsonObject.getAsJsonObject("statement"))
        );
    }

    private static IfStmt getIfStatement(JsonObject jsonObject) {
        JsonElement jsonElseStatement = jsonObject.get("elseStmt");
        return new IfStmt(
                getExpression(jsonObject.getAsJsonObject("condition")),
                getStatement(jsonObject.getAsJsonObject("thenStmt")),
                !jsonElseStatement.isJsonNull() ? getStatement(jsonObject.getAsJsonObject("elseStmt")) : null
        );
    }

    private static ForStmt getForStatement(JsonObject jsonObject) {
        JsonElement jsonCompare = jsonObject.get("compare");
        return new ForStmt(
                getExpressions(jsonObject.getAsJsonArray("initialization")),
                !jsonCompare.isJsonNull() ? getExpression(jsonCompare.getAsJsonObject()) : null,
                getExpressions(jsonObject.getAsJsonArray("update")),
                getStatement(jsonObject.getAsJsonObject("body"))
        );
    }

    private static ForEachStmt getForEachStatement(JsonObject jsonObject) {
        return new ForEachStmt(
                (VariableDeclarationExpr) getExpression(jsonObject.getAsJsonObject("variable")),
                getExpression(jsonObject.getAsJsonObject("iterable")),
                getStatement(jsonObject.getAsJsonObject("body"))
        );
    }

    private static ExpressionStmt getExpressionStatement(JsonObject jsonObject) {
        return new ExpressionStmt(getExpression(jsonObject.getAsJsonObject("expression")));
    }

    private static ExplicitConstructorInvocationStmt getExplicitConstructorInvocationStatement(JsonObject jsonObject) {
        JsonElement jsonTypeArguments = jsonObject.get("typeArguments");
        JsonElement jsonExpression = jsonObject.get("expression");
        return new ExplicitConstructorInvocationStmt(
                !jsonTypeArguments.isJsonNull() ? getTypes(jsonTypeArguments.getAsJsonArray()) : null,
                jsonObject.get("isThis").getAsBoolean(),
                !jsonExpression.isJsonNull() ? getExpression(jsonExpression.getAsJsonObject()) : null,
                getExpressions(jsonObject.getAsJsonArray("arguments"))
        );
    }

    private static EmptyStmt getEmptyStatement(JsonObject jsonObject) {
        return new EmptyStmt();
    }

    private static DoStmt getDoStatement(JsonObject jsonObject) {
        return new DoStmt(getStatement(jsonObject.getAsJsonObject("body")), getExpression(jsonObject.getAsJsonObject("condition")));
    }

    private static ContinueStmt getContinueStatement(JsonObject jsonObject) {
        String label = jsonObject.get("label").getAsString();
        return label != null ? new ContinueStmt(new SimpleName(label)) : new ContinueStmt();
    }

    private static BreakStmt getBreakStatement(JsonObject jsonObject) {
        String label = jsonObject.get("label").getAsString();
        return label != null ? new BreakStmt(new SimpleName(label)) : new BreakStmt();
    }

    private static BlockStmt getBlockStatement(JsonObject jsonObject) {
        return new BlockStmt(getStatements(jsonObject.getAsJsonArray("statements")));
    }

    private static AssertStmt getAssertStatement(JsonObject jsonObject) {
        JsonElement jsonMessage = jsonObject.get("message");
        return new AssertStmt(
                getExpression(jsonObject.getAsJsonObject("check")),
                !jsonMessage.isJsonNull() ? getExpression(jsonMessage.getAsJsonObject()) : null
        );
    }
}
