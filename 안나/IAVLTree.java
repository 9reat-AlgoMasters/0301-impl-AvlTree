package AVL;

public interface IAVLTree {
	void insert(int value);
	boolean contains(int value);
	void delete(int value);
	int getSize();

}
