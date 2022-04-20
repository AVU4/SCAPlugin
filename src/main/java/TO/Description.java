package TO;

import java.util.List;

public abstract class Description {

    private String name;

    public Description(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract boolean isLeaf();

    public abstract List<Description> getChildren();

    public abstract void add(Description description);

    public abstract void add(List<? extends Description> descriptions);
}
