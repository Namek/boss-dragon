package net.bossdragon.util.collections;

public interface RandomAccessWithKey<K,V> extends RandomAccess<V> {
	K getKey(int index);
}
