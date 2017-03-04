package net.bossdragon.util.data;

public class Tuple<E1, E2>
{
	public Tuple(E1 item1, E2 item2)
	{
		Item1 = item1;
		Item2 = item2;
	}

	public E1 Item1;
	public E2 Item2;
}
