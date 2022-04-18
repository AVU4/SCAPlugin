package converters.JavaToJson;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.stmt.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import static converters.JavaToJson.DeclarationConverterToJSON.getDeclaration;
import static converters.JavaToJson.ExpressionConverterToJSON.getExpression;
import static converters.JavaToJson.ExpressionConverterToJSON.getExpressions;
import static converters.JavaToJson.TypeConverterToJSON.getTypes;
import static converters.JavaToJson.UtilConverterToJSON.getSwitchEntries;

public class StatementConverterToJSON {

    public static JsonArray getStatements(NodeList<Statement> statements) {
        JsonArray jsonStatements = new JsonArray();
        statements.stream().map(StatementConverterToJSON::getStatement).forEach(jsonStatements::add);
        return jsonStatements;
    }

    public static JsonObject getStatement(Statement statement) {
        JsonObject jsonStatement = new JsonObject();
        if (statement.isAssertStmt()) {
            jsonStatement.add("statement", getAssertStatement((AssertStmt) statement));
        } else if (statement.isBlockStmt()) {
            jsonStatement.add("statement", getBlockStatement((BlockStmt) statement));
        } else if (statement.isBreakStmt()) {
            jsonStatement.add("statement", getBreakStatement((BreakStmt) statement));
        } else if (statement.isContinueStmt()) {
            jsonStatement.add("statement", getContinueStatement((ContinueStmt) statement));
        } else if (statement.isDoStmt()) {
            jsonStatement.add("statement", getDoStatement((DoStmt) statement));
        } else if (statement.isEmptyStmt()) {
            jsonStatement.add("statement", getEmptyStatement((EmptyStmt) statement));
        } else if (statement.isExplicitConstructorInvocationStmt()) {
            jsonStatement.add("statement", getExplicitConstructorInvocationStatement((ExplicitConstructorInvocationStmt) statement));
        } else if (statement.isExpressionStmt()) {
            jsonStatement.add("statement", getExpressionStatement((ExpressionStmt) statement));
        } else if (statement.isForEachStmt()) {
            jsonStatement.add("statement", getForEachStatement((ForEachStmt) statement));
        } else if (statement.isForStmt()) {
            jsonStatement.add("statement", getForStatement((ForStmt) statement));
        } else if (statement.isIfStmt()) {
            jsonStatement.add("statement", getIfStatement((IfStmt) statement));
        } else if (statement.isLabeledStmt()) {
            jsonStatement.add("statement", getLabeledStatement((LabeledStmt) statement));
        } else if (statement.isLocalClassDeclarationStmt()) {
            jsonStatement.add("statement", getLocalClassDeclarationStatement((LocalClassDeclarationStmt) statement));
        } else if (statement.isLocalRecordDeclarationStmt()) {
            jsonStatement.add("statement", getLocalRecordDeclarationStatement((LocalRecordDeclarationStmt) statement));
        } else if (statement.isReturnStmt()) {
            jsonStatement.add("statement", getReturnStatement((ReturnStmt) statement));
        } else if (statement.isSwitchStmt()) {
            jsonStatement.add("statement", getSwitchStatement((SwitchStmt) statement));
        } else if (statement.isSynchronizedStmt()) {
            jsonStatement.add("statement", getSynchronizedStatement((SynchronizedStmt) statement));
        } else if (statement.isThrowStmt()) {
            jsonStatement.add("statement", getThrowStatement((ThrowStmt) statement));
        } else if (statement.isTryStmt()) {
            jsonStatement.add("statement", getTryStatement((TryStmt) statement));
        } else if (statement.isUnparsableStmt()) {
            jsonStatement.add("statement", getUnparsableStatement((UnparsableStmt) statement));
        } else if (statement.isWhileStmt()) {
            jsonStatement.add("statement", getWhileStatement((WhileStmt) statement));
        } else if (statement.isYieldStmt()) {
            jsonStatement.add("statement", getYieldStatement((YieldStmt) statement));
        }
        return jsonStatement;
    }

    public static JsonObject getYieldStatement(YieldStmt yieldStmt) {
        JsonObject jsonYieldStmt = new JsonObject();
        jsonYieldStmt.addProperty("mode", YieldStmt.class.getName());
        jsonYieldStmt.add("expression", getExpression(yieldStmt.getExpression()));
        return jsonYieldStmt;
    }

    public static JsonObject getWhileStatement(WhileStmt whileStmt) {
        JsonObject jsonWhileStmt = new JsonObject();
        jsonWhileStmt.addProperty("mode", WhileStmt.class.getName());
        jsonWhileStmt.add("condition", getExpression(whileStmt.getCondition()));
        jsonWhileStmt.add("body", getStatement(whileStmt.getBody()));
        return jsonWhileStmt;
    }

    public static JsonObject getUnparsableStatement(UnparsableStmt unparsableStmt) {
        JsonObject jsonUnparsableStmt = new JsonObject();
        jsonUnparsableStmt.addProperty("mode", UnparsableStmt.class.getName());
        return jsonUnparsableStmt;
    }

    public static JsonObject getTryStatement(TryStmt tryStmt) {
        JsonObject jsonTryStmt = new JsonObject();
        jsonTryStmt.addProperty("mode", TryStmt.class.getName());
        jsonTryStmt.add("resources", getExpressions(tryStmt.getResources()));
        jsonTryStmt.add("tryBlock", getStatement(tryStmt.getTryBlock()));
        jsonTryStmt.add("finallyBlock", tryStmt.getFinallyBlock().isPresent() ? getStatement(tryStmt.getFinallyBlock().get()) : null);
//        jsonTryStmt.add(""); todo CatchClause
        return jsonTryStmt;
    }

    public static JsonObject getThrowStatement(ThrowStmt throwStmt) {
        JsonObject jsonThrowStmt = new JsonObject();
        jsonThrowStmt.addProperty("mode", ThrowStmt.class.getName());
        jsonThrowStmt.add("expression", getExpression(throwStmt.getExpression()));
        return jsonThrowStmt;
    }

    public static JsonObject getSynchronizedStatement(SynchronizedStmt synchronizedStmt) {
        JsonObject jsonSynchronizedStmt = new JsonObject();
        jsonSynchronizedStmt.addProperty("mode", SynchronizedStmt.class.getName());
        jsonSynchronizedStmt.add("expression", getExpression(synchronizedStmt.getExpression()));
        jsonSynchronizedStmt.add("body", getStatement(synchronizedStmt.getBody()));
        return jsonSynchronizedStmt;
    }

    public static JsonObject getSwitchStatement(SwitchStmt switchStmt) {
        JsonObject jsonSwitchStmt = new JsonObject();
        jsonSwitchStmt.addProperty("mode", SwitchStmt.class.getName());
        jsonSwitchStmt.add("selector", getExpression(switchStmt.getSelector()));
        jsonSwitchStmt.add("entries", getSwitchEntries(switchStmt.getEntries()));
        return jsonSwitchStmt;
    }

    public static JsonObject getReturnStatement(ReturnStmt returnStmt) {
        JsonObject jsonReturnStmt = new JsonObject();
        jsonReturnStmt.addProperty("mode", ReturnStmt.class.getName());
        jsonReturnStmt.add("expression", returnStmt.getExpression().isPresent() ? getExpression(returnStmt.getExpression().get()) : null);
        return jsonReturnStmt;
    }

    public static JsonObject getLocalRecordDeclarationStatement(LocalRecordDeclarationStmt localRecordDeclarationStmt) {
        JsonObject jsonLocalRecordDeclarationStmt = new JsonObject();
        jsonLocalRecordDeclarationStmt.addProperty("mode", LocalRecordDeclarationStmt.class.getName());
        jsonLocalRecordDeclarationStmt.add("recordDeclaration", getDeclaration(localRecordDeclarationStmt.getRecordDeclaration()));
        return jsonLocalRecordDeclarationStmt;
    }

    public static JsonObject getLocalClassDeclarationStatement(LocalClassDeclarationStmt localClassDeclarationStmt) {
        JsonObject jsonLocalClassDeclarationStmt = new JsonObject();
        jsonLocalClassDeclarationStmt.addProperty("mode", LocalClassDeclarationStmt.class.getName());
        jsonLocalClassDeclarationStmt.add("classDeclaration", getDeclaration(localClassDeclarationStmt.getClassDeclaration()));
        return jsonLocalClassDeclarationStmt;
    }

    public static JsonObject getLabeledStatement(LabeledStmt labeledStmt) {
        JsonObject jsonLabeledStatement = new JsonObject();
        jsonLabeledStatement.addProperty("mode", LabeledStmt.class.getName());
        jsonLabeledStatement.addProperty("label", labeledStmt.getLabel().asString());
        jsonLabeledStatement.add("statement", getStatement(labeledStmt.getStatement()));
        return jsonLabeledStatement;
    }

    public static JsonObject getIfStatement(IfStmt ifStmt) {
        JsonObject jsonIfStmt = new JsonObject();
        jsonIfStmt.addProperty("mode", IfStmt.class.getName());
        jsonIfStmt.add("condition", getExpression(ifStmt.getCondition()));
        jsonIfStmt.add("thenStmt", getStatement(ifStmt.getThenStmt()));
        jsonIfStmt.add("elseStmt", ifStmt.getElseStmt().isPresent() ? getStatement(ifStmt.getElseStmt().get()) : null);
        return jsonIfStmt;
    }

    public static JsonObject getForStatement(ForStmt forStmt) {
        JsonObject jsonForStmt = new JsonObject();
        jsonForStmt.addProperty("mode", ForStmt.class.getName());
        jsonForStmt.add("initialization", getExpressions(forStmt.getInitialization()));
        jsonForStmt.add("compare", forStmt.getCompare().isPresent() ? getExpression(forStmt.getCompare().get()) : null);
        jsonForStmt.add("update", getExpressions(forStmt.getUpdate()));
        jsonForStmt.add("body", getStatement(forStmt.getBody()));
        return jsonForStmt;
    }

    public static JsonObject getForEachStatement(ForEachStmt forEachStmt) {
        JsonObject jsonForEachStmt = new JsonObject();
        jsonForEachStmt.addProperty("mode", ForEachStmt.class.getName());
        jsonForEachStmt.add("body", getStatement(forEachStmt.getBody()));
        jsonForEachStmt.add("iterable", getExpression(forEachStmt.getIterable()));
        jsonForEachStmt.add("variable", getExpression(forEachStmt.getVariable()));
        return jsonForEachStmt;
    }

    public static JsonObject getExpressionStatement(ExpressionStmt expressionStmt) {
        JsonObject jsonExpressionStmt = new JsonObject();
        jsonExpressionStmt.addProperty("mode", ExpressionStmt.class.getName());
        jsonExpressionStmt.add("expression", getExpression(expressionStmt.getExpression()));
        return jsonExpressionStmt;
    }

    public static JsonObject getExplicitConstructorInvocationStatement(ExplicitConstructorInvocationStmt explicitConstructorInvocationStmt) {
        JsonObject jsonExplicitConstructorInvocationStmt = new JsonObject();
        jsonExplicitConstructorInvocationStmt.addProperty("mode", ExplicitConstructorInvocationStmt.class.getName());
        jsonExplicitConstructorInvocationStmt.add("arguments", getExpressions(explicitConstructorInvocationStmt.getArguments()));
        jsonExplicitConstructorInvocationStmt.add("typeArguments", explicitConstructorInvocationStmt.getTypeArguments().isPresent() ? getTypes(explicitConstructorInvocationStmt.getTypeArguments().get()) : null);
        jsonExplicitConstructorInvocationStmt.add("expression", explicitConstructorInvocationStmt.getExpression().isPresent() ? getExpression(explicitConstructorInvocationStmt.getExpression().get()) : null);
        jsonExplicitConstructorInvocationStmt.addProperty("isThis", explicitConstructorInvocationStmt.isThis());
        return jsonExplicitConstructorInvocationStmt;
    }
    public static JsonObject getEmptyStatement(EmptyStmt emptyStmt) {
        JsonObject jsonEmptyStmt = new JsonObject();
        jsonEmptyStmt.addProperty("mode", EmptyStmt.class.getName());
        return jsonEmptyStmt;
    }

    public static JsonObject getDoStatement(DoStmt doStmt) {
        JsonObject jsonDoStmt = new JsonObject();
        jsonDoStmt.addProperty("mode", DoStmt.class.getName());
        jsonDoStmt.add("body", getStatement(doStmt.getBody()));
        jsonDoStmt.add("condition", getExpression(doStmt.getCondition()));
        return jsonDoStmt;
    }

    public static JsonObject getContinueStatement(ContinueStmt continueStmt) {
        JsonObject jsonContinueStmt = new JsonObject();
        jsonContinueStmt.addProperty("mode", ContinueStmt.class.getName());
        jsonContinueStmt.addProperty("label", continueStmt.getLabel().isPresent() ? continueStmt.getLabel().get().asString() : null);
        return jsonContinueStmt;
    }

    public static JsonObject getBreakStatement(BreakStmt breakStmt) {
        JsonObject jsonBreakStmt = new JsonObject();
        jsonBreakStmt.addProperty("mode", BreakStmt.class.getName());
        jsonBreakStmt.addProperty("label", breakStmt.getLabel().isPresent() ? breakStmt.getLabel().get().asString() : null);
        return jsonBreakStmt;
    }

    public static JsonObject getBlockStatement(BlockStmt blockStmt) {
        JsonObject jsonBlockStmt = new JsonObject();
        jsonBlockStmt.addProperty("mode", BlockStmt.class.getName());
        jsonBlockStmt.add("statements", getStatements(blockStmt.getStatements()));
        return jsonBlockStmt;
    }

    public static JsonObject getAssertStatement(AssertStmt assertStmt) {
        JsonObject jsonAssertStmt = new JsonObject();
        jsonAssertStmt.addProperty("mode", AssertStmt.class.getName());
        jsonAssertStmt.add("check", getExpression(assertStmt.getCheck()));
        jsonAssertStmt.add("message", assertStmt.getMessage().isPresent() ? getExpression(assertStmt.getMessage().get()) : null);
        return jsonAssertStmt;
    }


}
