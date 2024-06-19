package util;

public class Pair {
    private String string;
    private Object object;

    public Pair(String string, Object object) {
        this.string = string;
        this.object = object;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}
