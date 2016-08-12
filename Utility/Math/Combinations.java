package Utility.Math;

import java.util.Arrays;
import java.util.Iterator;
import Utility.Utility;

public class Combinations implements Iterator<int[]>
{
	private int[] currentCounters;
	private final int n;
	private final int k;

	public static void main(String[] args)
	{
		int count = 0;
		Combinations c = new Combinations(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
		while (c.hasNext())
		{
			System.out.println(Utility.ListAsString(c.next()));
			++count;
		}
		System.out.println(count);
	}

	// n Choose k
	public Combinations(int n, int k)
	{
		this.n = n;
		this.k = k;
		currentCounters = new int[k];
		for (int i = 0; i < currentCounters.length; ++i)
		{
			currentCounters[i] = i;
		}
		--currentCounters[currentCounters.length - 1]; // next always advances before returning
	}

	public int[] next()
	{
		int advance = currentCounters.length - 1;
		while (advance >= 0)
		{
			if (++currentCounters[advance] == n + 1 - (k - advance))
			{
				--advance;
			}
			else
			{
				for (int i = advance + 1; i < currentCounters.length; ++i)
				{
					currentCounters[i] = currentCounters[i - 1] + 1;
				}
				break;
			}
		}

		if (advance < 0)
		{
			throw new java.util.NoSuchElementException();
		}

		return currentCounters;
	}

	public boolean hasNext()
	{
		return currentCounters[0] < (n - k);
	}
}