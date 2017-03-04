package net.bossdragon.util.collections.tuples;

import net.bossdragon.util.data.Triple;

import java.util.ArrayList;
import java.util.Iterator;


public class TripleList<E1, E2, E3> implements Iterable<Triple<E1, E2, E3>>{
	private final ArrayList<Triple<E1, E2, E3>> triples = new ArrayList<Triple<E1, E2, E3>>();
	
	public static <E1, E2, E3> TripleList<E1, E2, E3> create() {
		return new TripleList<E1, E2, E3>();
	}
	
	public TripleList<E1, E2, E3> add(E1 item1, E2 item2, E3 item3) {
		triples.add(new Triple<E1, E2, E3>(item1, item2, item3));
		return this;
	}
	
	public int size() {
		return triples.size();
	}

	@Override
	public Iterator<Triple<E1, E2, E3>> iterator() {
		return triples.iterator();
	}
}
