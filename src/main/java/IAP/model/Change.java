package IAP.model;

public class Change<T> {
    private T before;
    private T after;

    public Change() {}

    public Change(T before, T after) {
        this.before = before;
        this.after = after;
    }

    public T getBefore() {
        return before;
    }

    public void setBefore(T before) {
        this.before = before;
    }

    public T getAfter() {
        return after;
    }

    public void setAfter(T after) {
        this.after = after;
    }

    @Override
    public String toString() {
        return "Change{before=" + before + ", after=" + after + "}";
    }
}
