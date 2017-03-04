package net.bossdragon.util.data;

public class Triple<E1, E2, E3>
{
	public Triple(E1 item1, E2 item2, E3 item3)
	{
		Item1 = item1;
		Item2 = item2;
		Item3 = item3;
	}

	public E1 Item1;
	public E2 Item2;
	public E3 Item3;
}
