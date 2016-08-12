package Utility.Solvers;

import java.util.Arrays;
import Utility.Math.Permutations;

// Treats upper and lower case letters as different characters
// Assumes base 10
// Does not allow digits to be reused for different characters
// Only allows addition and only one one side of the equality

public class Cryptarithm // aka alphametics
{
	private String[] addends;
	private String sum;
	private char[] letters;
	private int[] values;
	private int numSolutions;
	private boolean printSolutions;

	public static void main(String[] args)
	{
		String[] addends = Arrays.copyOfRange(args, 0, args.length - 1);
		String sum = args[args.length - 1];
		Cryptarithm crypt = new Cryptarithm(addends, sum, true);
		crypt.solve();
		System.out.println(crypt.numSolutions);
	}

	public Cryptarithm(String[] addends, String sum, boolean printSolutions)
	{
		this.numSolutions = 0;
		this.printSolutions = printSolutions;
		this.addends = addends;
		this.sum = sum;

		String lets = "";
		for (int i = 0; i < addends.length; ++i)
		{
			lets = addUniqueLetters(addends[i], lets);
		}
		lets = addUniqueLetters(sum, lets);
		letters = lets.toCharArray();

		values = new int[letters.length];
		Arrays.fill(values, -1);
	}

	public void solve()
	{
		Permutations p = new Permutations(10, letters.length); // base 10 assumption
		while (p.hasNext())
		{
			int[] next = new int[letters.length];
			next = Arrays.copyOf(p.next(), next.length); // base 10 assumption
			if (isValidAssignment(next))
			{
				++numSolutions;
				values = Arrays.copyOf(next, values.length);
				if (printSolutions)
				{
					System.out.println(this);
				}
			}
		}
	}

	private boolean isValidAssignment(int[] vals)
	{
		String[] localAddends = new String[this.addends.length];
		for (int a = 0; a < addends.length; ++a)
		{
			localAddends[a] = addends[a];
		}

		String localSum = this.sum;
		for (int i = 0; i < vals.length; ++i)
		{
			for (int a = 0; a < localAddends.length; ++a)
			{
				localAddends[a] = localAddends[a].replace(letters[i], (char)('0' + vals[i])); // <= base 10 assumption
				if (localAddends[a].charAt(0) == '0')
				{
					// Numbers can't start with a 0
					return false;
				}
			}
			localSum = localSum.replace(letters[i], (char)('0' + vals[i])); // <= base 10 assumption
			if (localSum.charAt(0) == '0')
			{
				// Numbers can't start with a 0
				return false;
			}
		}

		return isValidSum(localAddends, localSum);
	}
	
	private boolean isValidSum(String[] localAddends, String localSum)
	{
		int addendsSummed = 0;
		for (int i = 0; i < localAddends.length; ++i)
		{
			addendsSummed += Integer.parseInt(localAddends[i]); // base 10 assumption
		}
		int sumAsNum = Integer.parseInt(localSum); // base 10 assumption
		return (addendsSummed == sumAsNum);
	}

	public String toString()
	{
		String str = "";
		for (int i = 0; i < addends.length; ++i)
		{
			if (i != 0)
			{
				str += " + ";
			}
			str += addends[i];
		}
		str += " = " + sum + "\n";

		for (int i = 0; i < letters.length; ++i)
		{
			str += letters[i] + ": " + values[i] + "\n";
		}

		return str;
	}

	private String addUniqueLetters(String newChars, String existingLetters)
	{
		for (int c = 0; c < newChars.length(); ++c)
		{
			char ch = newChars.charAt(c);
			if (existingLetters.indexOf(ch) == -1)
			{
				existingLetters += ch;
			}
		}
		return existingLetters;
	}
}