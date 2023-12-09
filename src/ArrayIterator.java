import java.util.Iterator;

public class ArrayIterator<T> implements Iterator<T> {
	private T[] array;
	private int index = 0;

	public ArrayIterator(T[] array) {
		this.array = array;
	}

	@Override
	public boolean hasNext() {
		return index < array.length;
	}

	@Override
	public T next() {
		return array[index++];
	}

	@Override
	@SuppressWarnings("unchecked")
	public void remove() {
		T[] newArray = (T[]) new Object[array.length - 1];
		System.arraycopy(array, 0, newArray, 0, index - 1);
		System.arraycopy(array, index, newArray, index - 1, array.length - index);
		array = newArray;
	}
}
