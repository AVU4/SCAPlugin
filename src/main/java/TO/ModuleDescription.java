package TO;

import java.util.ArrayList;
import java.util.List;

public class ModuleDescription extends Description {

    List<Description> children = new ArrayList<>();

    public ModuleDescription(String name) {
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
