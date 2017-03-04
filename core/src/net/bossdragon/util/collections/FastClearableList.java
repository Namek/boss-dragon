package net.bossdragon.util.collections;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

import com.badlogic.gdx.utils.Array;

public final class FastClearableList<T> implements List<T>, Iterable<T>, Cloneable {
	private T[] _items;
	private int _wantedCapacity;
	private int _size;
	
	@SuppressWarnings("unchecked")
	public FastClearableList(int capacity) {
		_wantedCapacity = capacity;
		_items = (T[])new Object[capacity];
		_size = 0;
	}
	
	public boolean add(T el) {
		_items[_size++] = el;
		return true;
	}
	
	public T get(int index) {
		return _items[index];
	}
	
	/**
	 * Look out for a garbage! To really lose object references use {@link #realClear()}.
	 * When {@code setNewArray} is set to {@code true} then a new array is created,
	 * otherwise it is not touched (only {@code size = 0}).
	 * @param setNewArray
	 */
	public void fastClear(boolean setNewArray) {
		_size = 0;
		if (setNewArray) {
			_items = (T[])new Object[_wantedCapacity];
		}
	}
	
	public void fastClear() {
		fastClear(false);
	}
	
	/**
	 * Removes references to all objects in array. It's cheaper to use {@link #fastClear()}.
	 */
	public void realClear() {
		_size = 0;
		for (int i = 0, n = _items.length; i < n; ++i) {
			_items[i] = null;
		}
	}

	@Override
	public Iterator<T> iterator() {
		return new FastClearableListIterator<T>(this);
	}

	@Override
	public void add(int index, T element) {
		throw new RuntimeException("Operation not supported.");
	}

	@Override
	public boolean addAll(Collection<? extends T> elements) {
		if (_items.length < _size + elements.size()) {
			throw new IndexOutOfBoundsException();
		}
		
		for (T element : elements) {
			_items[_size++] = element;
		}
		return true;
	}

	@Override
	public boolean addAll(int startIndex, Collection<? extends T> elements) {
		throw new RuntimeException("Operation not supported.");
	}

	@Override
	public void clear() {
		throw new RuntimeException("Operation not supported. Use fastClear or realClear!");
	}

	@Override
	public boolean contains(Object obj) {
		for (int i = 0; i < _size; ++i) {
			if (_items[i] == obj) {
				return true;
			}
		}

		return false;
	}

	@Override
	public boolean containsAll(Collection<?> arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int indexOf(Object arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isEmpty() {
		return _size == 0;
	}

	@Override
	public int lastIndexOf(Object element) {
		throw new RuntimeException("Operation not supported.");
	}

	@Override
	public ListIterator<T> listIterator() {
		throw new RuntimeException("Operation not supported.");
	}

	@Override
	public ListIterator<T> listIterator(int arg0) {
		throw new RuntimeException("Operation not supported.");
	}

	@Override
	public boolean remove(Object arg0) {
		throw new RuntimeException("Operation not supported.");
	}

	@Override
	public T remove(int index) {
		throw new RuntimeException("Operation not supported.");
	}

	@Override
	public boolean removeAll(Collection<?> arg0) {
		throw new RuntimeException("Operation not supported.");
	}

	@Override
	public boolean retainAll(Collection<?> arg0) {
		throw new RuntimeException("Operation not supported.");
	}

	@Override
	public T set(int index, T newElement) {
		T oldElement = _items[index];
		_items[index] = newElement;
		return oldElement;
	}

	@Override
	public int size() {
		return _size;
	}

	@Override
	public List<T> subList(int start, int end) {
		throw new RuntimeException("Operation not supported.");
	}

	@Override
	public Object[] toArray() {
		throw new RuntimeException("Operation not supported.");
	}

	@Override
	public <T> T[] toArray(T[] arg0) {
		throw new RuntimeException("Operation not supported.");
	}

	public void copyElementsTo(List<T> list) {
		for (int i = 0; i < _items.length; ++i) {
			list.add(_items[i]);
		}
	}
	
	public void copyElementsTo(Array<T> array) {
		for (int i = 0; i < _items.length; ++i) {
			array.add(_items[i]);
		}
	}




	static public class FastClearableListIterator<T> implements Iterator<T> {
		private final FastClearableList<T> array;
		int index;

		public FastClearableListIterator(FastClearableList<T> array) {
			this.array = array;
		}

		public boolean hasNext () {
			return index < array._size;
		}

		public T next () {
			if (index >= array._size) throw new NoSuchElementException(String.valueOf(index));
			return array._items[index++];
		}

		public void remove () {
			throw new RuntimeException("You can't remove items one by one from VeryFastList collection.");
		}

		public void reset () {
			index = 0;
		}
	}
}
