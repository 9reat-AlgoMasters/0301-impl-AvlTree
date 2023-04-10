public interface SearchTree {
    void insert(int value);
    boolean contains(int value);
    void delete(int value);
    boolean isEmpty();
    int size();
}