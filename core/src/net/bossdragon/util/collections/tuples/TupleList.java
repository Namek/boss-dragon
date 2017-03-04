package net.bossdragon.util.collections.tuples;

import net.bossdragon.util.data.Tuple;

import java.util.ArrayList;
import java.util.Iterator;

public class TupleList<E1, E2> implements Iterable<Tuple<E1, E2>> {
	private final ArrayList<Tuple<E1, E2>> tuples = new ArrayList<Tuple<E1, E2>>();
	
	public static <E1, E2> TupleList<E1, E2> create() {
		return new TupleList<E1, E2>();
	}
	
	public TupleList<E1, E2> add(E1 item1, E2 item2) {
		tuples.add(new Tuple<E1, E2>(item1, item2));
		return this;
	}
	
	public int size() {
		return tuples.size();
	}
	
	@Override
	public Iterator<Tuple<E1, E2>> iterator() {
		return tuples.iterator();
	}
}
