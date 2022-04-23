package TO;

import java.util.List;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Description that = (Description) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
