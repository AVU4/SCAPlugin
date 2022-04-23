package TO;

import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.intellij.openapi.externalSystem.service.execution.NotSupportedException;

import java.util.List;
import java.util.Objects;

public class ClassDescription extends Description {
    private NodeList<ImportDeclaration> importDeclarations;
    private NodeList<BodyDeclaration<?>> bodyDeclarations;

    public ClassDescription(NodeList<ImportDeclaration> importDeclarations, NodeList<BodyDeclaration<?>> bodyDeclarations, String name) {
        super(name);
        this.bodyDeclarations = bodyDeclarations;
        this.importDeclarations = importDeclarations;
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

    @Override
    public boolean isLeaf() {
        return true;
    }

    @Override
    public List<Description> getChildren() {
        return null;
    }

    @Override
    public void add(Description description) {
        throw new NotSupportedException("This is Leaf");
    }

    @Override
    public void add(List<? extends Description> descriptions) {
        throw new NotSupportedException("This is Leaf");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClassDescription that = (ClassDescription) o;
        return super.equals(o) && Objects.equals(importDeclarations, that.importDeclarations) && Objects.equals(bodyDeclarations, that.bodyDeclarations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(importDeclarations, bodyDeclarations);
    }
}
