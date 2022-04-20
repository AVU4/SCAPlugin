package TO;

import java.util.ArrayList;
import java.util.List;

public class PackageDescription extends Description {

    private List<Description> children = new ArrayList<>();

    public PackageDescription(String name) {
        super(name);
    }

    @Override
    public boolean isLeaf() {
        return false;
    }

    @Override
    public List<Description> getChildren() {
        return children;
    }

    @Override
    public void add(Description description) {
        children.add(description);
    }

    public void add(List<? extends Description> descriptions) {
        children.addAll(descriptions);
    }
}
