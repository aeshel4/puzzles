package Utility.Math;

import java.util.ArrayList;
import java.util.Arrays;

public class Math
{
	public static void main(String[] args)
	{
		String[][] matrix = {{"1", "2"}, {"3", "4"}, {"5", "6"}};
		System.out.println(Utility.Utility.MatrixAsString(matrix));
		matrix = Transpose(matrix);
		System.out.println(Utility.Utility.MatrixAsString(matrix));
	}

	public static int GetNthDigitInBaseB(int v, int n, int b)
	{
		for (int i = 0; i < n; ++i)
		{
			v /= b;
		}
		return (v % b);
	}

	public static Object[] Intersection(Object[] g1, Object[] g2)
	{
		Object[] intersected = new Object[g1.length];
		int iCount = 0;
		for (int e1 = 0; e1 < g1.length; ++e1)
		{
			for (int e2 = 0; e2 < g1.length; ++e2)
			{
				if (g1[e1].equals(g2[e2]))
				{
					intersected[iCount++] = g1[e1];
					break;
				}
			}
		}
		return Arrays.copyOf(intersected, iCount);
	}

	public static boolean Contains(Object[] g, Object e)
	{
		for (int i = 0; i < g.length; ++i)
		{
			if (e.equals(g[i]))
			{
				return true;
			}
		}
		return false;
	}

	public static Object[] Union(Object[] g1, Object[] g2)
	{
		Object[] combined = new Object[g1.length + g2.length];
		int uCount = 0;
		for (int i = 0; i < g1.length; ++i)
		{
			if (!Contains(combined, g1[i]))
			{
				combined[uCount++] = g1[i];
			}
		}
		for (int i = 0; i < g2.length; ++i)
		{
			if (!Contains(combined, g2[i]))
			{
				combined[uCount++] = g2[i];
			}
		}

		return Arrays.copyOf(combined, uCount);
	}

	public static int MaxIndex(int[] elems)
	{
		int maxIndex = 0;
		for (int i = 1; i < elems.length; ++i)
		{
			if (elems[maxIndex] < elems[i])
			{
				maxIndex = i;
			}
		}
		return maxIndex;
	}

	public static <T extends Comparable<T>> int MaxIndex(T[] elems)
	{
		int maxIndex = 0;
		for (int i = 1; i < elems.length; ++i)
		{
			if (elems[maxIndex].compareTo(elems[i]) < 0)
			{
				maxIndex = i;
			}
		}
		return maxIndex;
	}

	public static int MinIndex(int[] elems)
	{
		int minIndex = 0;
		for (int i = 1; i < elems.length; ++i)
		{
			if (elems[minIndex] > elems[i])
			{
				minIndex = i;
			}
		}
		return minIndex;
	}

	public static <T extends Comparable<T>> int MinIndex(T[] elems)
	{
		int minIndex = 0;
		for (int i = 1; i < elems.length; ++i)
		{
			if (elems[minIndex].compareTo(elems[i]) > 0)
			{
				minIndex = i;
			}
		}
		return minIndex;
	}

	public static String[][] Transpose(String[][] matrix)
	{
		String[][] newMatrix = new String[matrix[0].length][matrix.length];
		for (int i = 0; i < matrix.length; ++i)
		{
			for (int j = 0; j < matrix[i].length; ++j)
			{
				newMatrix[j][i] = matrix[i][j];
			}
		}
		return newMatrix;
	}

	public static Object[][] Transpose(Object[][] matrix)
	{
		Object[][] newMatrix = new Object[matrix[0].length][matrix.length];
		for (int i = 0; i < matrix.length; ++i)
		{
			for (int j = 0; j < matrix[i].length; ++j)
			{
				newMatrix[j][i] = matrix[i][j];
			}
		}
		return newMatrix;
	}

	public static long Factorial(int n)
	{
		long f = 1;
		while (n > 1)
		{
			f *= n--;
		}
		return f;
	}

//	n!/((n-k)!k!)
	public static long Combinations(int n, int k)
	{
		long c = 1;
		int n_k = n - k;
		while (n > k)
		{
			c *= n--;
		}
		while (n_k > 1)
		{
			c /= n_k--;
		}
		return c;
	}
	
//	n!/(n-k)!
	public static long Permutations(int n, int k)
	{
		long p = 1;
		int n_k = n - k;
		while (n > n_k)
		{
			p *= n--;
		}
		return p;
	}

	public static boolean IsPrime(int n)
	{
		// Consider 1 and negatives to be not prime.
		if (n < 2)
		{
			return false;
		}

		// Do an early return on evens, so that the loop can look at odds only
		if ((n % 2 == 0) && (n != 2))
		{
			return false;
		}

		for (int i = 3; i < n; i += 2)
		{
			if (n % i == 0)
			{
				// Then i is a factor of n and strictly less than.
				return false;
			}
		}

		return true;
	}

	// NOTE: This is slow for large numbers.
	public static int[] PrimesUpTo(int n)
	{
        ArrayList<Integer> primes = new ArrayList<Integer>();

		for (int i = 2; i <= n; ++i)
		{
			boolean isPrime = true;
			for (int p = 0; p < primes.size(); ++p)
			{
				if (i % primes.get(p) == 0)
				{
					isPrime = false;
					break;
				}
			}
			if (isPrime)
			{
				primes.add(i);
			}
		}

		int[] ret = new int[primes.size()];
		for (int p = 0; p < primes.size(); ++p)
		{
			ret[p] = primes.get(p);
		}
        return ret;
	}
}