package Utility.Math;

import java.util.Arrays;
import java.util.Iterator;
import Utility.Utility;

public class Permutations implements Iterator<int[]>
{
	private int[] list;
	private long current;
	private long totalNum;
	private int truncateTo;

	public static void main(String[] args)
	{
		int count = 0;
		Permutations p = new Permutations(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
		while (p.hasNext())
		{
			System.out.println(Utility.ListAsString(p.next()));
			++count;
		}
		System.out.println(count);
	}

	public Permutations(int num)
	{
		this(num, num);
	}

	public Permutations(int num, int truncateTo)
	{
		this.truncateTo = truncateTo;
		list = new int[num];
		current = 0;
		totalNum = Math.Factorial(num);
		for (int i = 0; i < list.length; ++i)
		{
			list[i] = i;
		}
	}

//	1.Find the largest index k such that a[k] < a[k + 1]. If no such index exists, the permutation is the last permutation.
//	2.Find the largest index l greater than k such that a[k] < a[l].
//	3.Swap the value of a[k] with that of a[l].
//	4.Reverse the sequence from a[k + 1] up to and including the final element a[n].
	public int[] next()
	{
		long skipAmount = Math.Factorial(list.length - truncateTo);
		do
		{
			if (current != 0) // The first one was set by the constructor, so don't advance
			{
				int k = findK();
				int l = findL(k);
				swap(k, l);
				reversePostK(k);
			}
			++current;
		} while (--skipAmount > 0);
		return Arrays.copyOfRange(list, 0, truncateTo);
	}
	
	public boolean hasNext()
	{
		return current < totalNum;
	}

	private int findK()
	{
		for (int k = list.length - 2; k >= 0; --k)
		{
			if (list[k] < list[k + 1])
			{
				return k;
			}
		}
		throw new java.util.NoSuchElementException();
	}
	
	private int findL(int k)
	{
		for (int l = list.length - 1; l > k; --l)
		{
			if (list[k] < list[l])
			{
				return l;
			}
		}
		throw new java.util.NoSuchElementException();
	}
	
	private void swap(int k, int l)
	{
		int temp = list[k];
		list[k] = list[l];
		list[l] = temp;
	}
	
	private void reversePostK(int k)
	{
		int[] origValues = new int[list.length - k - 1];
		for (int i = 0; i < origValues.length; ++i)
		{
			origValues[i] = list[i + k + 1];
		}
		for (int i = 0; i < origValues.length; ++i)
		{
			list[list.length - i - 1] = origValues[i];
		}
	}
}