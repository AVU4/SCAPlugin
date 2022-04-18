package TO;

import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.BodyDeclaration;

import java.util.List;

public class ClassDescription {
    private String name;
    private NodeList<ImportDeclaration> importDeclarations;
    private NodeList<BodyDeclaration<?>> bodyDeclarations;

    public ClassDescription(NodeList<ImportDeclaration> importDeclarations, NodeList<BodyDeclaration<?>> bodyDeclarations, String name) {
        this.bodyDeclarations = bodyDeclarations;
        this.importDeclarations = importDeclarations;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public NodeList<ImportDeclaration> getImportDeclarations() {
        return importDeclarations;
    }

    public NodeList<BodyDeclaration<?>> getBodyDeclarations() {
        return bodyDeclarations;
    }

    public void setBodyDeclarations(NodeList<BodyDeclaration<?>> bodyDeclarations) {
        this.bodyDeclarations = bodyDeclarations;
    }

    public void setImportDeclarations(NodeList<ImportDeclaration> importDeclarations) {
        this.importDeclarations = importDeclarations;
    }
}
