package Utility;

import java.util.*;
import java.io.*;

public class Utility
{
	// Use this for testing things.
	public static void main(String[] args) throws FileNotFoundException
	{

	}

	public static void Pause()
	{
		Scanner sc = new Scanner(System.in);
		sc.nextLine();
	}

	public static String ListAsString(int[] list)
	{
		String str = "";
		for (int i = 0; i < list.length; ++i)
		{
			str += (list[i] + " ");
		}
		return str;
	}

	public static String MatrixAsString(int[][] matrix)
	{
		String str = "";
		for (int i = 0; i < matrix.length; ++i)
		{
			for (int j = 0; j < matrix[i].length; ++j)
			{
				str += (matrix[i][j] + " ");
			}
			str += '\n';
		}
		return str;
	}

	public static String MatrixAsString(Object[][] matrix)
	{
		String str = "";
		for (int i = 0; i < matrix.length; ++i)
		{
			for (int j = 0; j < matrix[i].length; ++j)
			{
				str += (matrix[i][j].toString() + " ");
			}
			str += '\n';
		}
		return str;
	}

	public static String Spaces(int numSpaces)
	{
		String str = "";
		for (int i = 0; i < numSpaces; ++i)
		{
			str += ' ';
		}
		return str;
	}

	public static int GetRandomInt(int min, int max) // inclusive, exclusive
	{
		Random rand = new Random();
		return min + rand.nextInt(max - min);
	}

	public static int[] RandomizeList(int[] array)
	{
		int[] ret = new int[array.length];
		boolean[] set = new boolean[ret.length];
		for (int i = 0; i < set.length; ++i) {set[i] = false;}

		int count = 0;
		while (count < ret.length)
		{
			int rand = GetRandomInt(0, ret.length);
			if (!set[rand])
			{
				set[rand] = true;
				ret[rand] = array[count++];
			}
		}
		return ret;
	}

	public static int[] RemoveFirstElementOfIntArray(int[] array)
	{
		int[] ret = new int[array.length - 1];
		for (int i = 1; i < array.length; ++i)
		{
			ret[i - 1] = array[i];
		}
		return ret;
	}

	public static String[] RemoveFirstElementOfStringArray(String[] array)
	{
		String[] ret = new String[array.length - 1];
		for (int i = 1; i < array.length; ++i)
		{
			ret[i - 1] = array[i];
		}
		return ret;
	}

	private static final GregorianCalendar NULL_DATE = new GregorianCalendar(0, 0, 0);

	public static boolean IsNullDate(GregorianCalendar date)
	{
		return date.equals(NULL_DATE);
	}

	public static String PrintDate(GregorianCalendar date)
	{
		if (IsNullDate(date))
		{
			return "No date.";
		}

		String str = "";
		str += (date.get(Calendar.MONTH) + 1) + "/" +
		       date.get(Calendar.DAY_OF_MONTH) + "/" +
			   date.get(Calendar.YEAR);
		return str;
	}

	public static GregorianCalendar ParseStringIntoDate(String date)
	{
		String[] fields = date.split("/");
		if (fields.length != 3)
		{
			return NULL_DATE;
			//throw new IllegalArgumentException("Incorrect number of /'s for date parsing.");
		}
		int day = Integer.valueOf(fields[1]);
		int month = Integer.valueOf(fields[0]) - 1; // January == 0
		int year = Integer.valueOf(fields[2]);
		switch (fields[2].length())
		{
			case 4: break; // Then we're done already.
			case 2: // Then we need to add the century (either this or previous)
			{
				GregorianCalendar today = new GregorianCalendar();
				int currentYear = today.get(Calendar.YEAR);
				if (year <= (currentYear % 100))
				{
					year += (currentYear / 100) * 100;
				}
				else
				{
					year += ((currentYear / 100) - 1) * 100;
				}
				break;
			}
			default: return NULL_DATE; //throw new IllegalArgumentException("Incorrect number of digits for year.");
		}
		return new GregorianCalendar(year, month, day);
	}

	public static void PrintFileThings(String filename) throws FileNotFoundException
	{
		Scanner sc = new Scanner(new File(filename));
		while (sc.hasNextLine())
		{
			String line = sc.nextLine();
//			System.out.println(line + ": " + WordValue.ValueOf(line, 0)); 
		}
	}

	public static boolean isOneLetterOff(String s1, String s2)
	{
		if (s1.length() != s2.length())
		{
			return false;
		}

		boolean oneOff = false;
		for (int i = 0; i < s1.length(); ++i)
		{
			if (s1.charAt(i) != s2.charAt(i))
			{
				if (oneOff)
				{
					// Already found one that is off
					return false;
				}
				oneOff = true;
			}
		}
		return oneOff;
	}

	public static String ReverseString(String str)
	{
		String ret = "";
		for (int i = (str.length() - 1); i >= 0; --i)
		{
			ret += str.charAt(i);
		}
		return ret;
	}

	public static boolean IsAPalindrome(String word, boolean charOnly)
	{
		word = word.toUpperCase();
		int iStart = -1, iEnd = word.length();
		while (true)
		{
			do
			{
				++iStart;
			} while (!charOnly && !Character.isLetter(word.charAt(iStart)));

			do
			{
				--iEnd;
			} while (!charOnly && !Character.isLetter(word.charAt(iEnd)));

			if (iStart >= iEnd)
			{
				return true;
			}

			if (word.charAt(iStart) != word.charAt(iEnd))
			{
				return false;
			}
		}
	}

	private final static int MAX_INT = 10000000;
	public static String AlphabetSortedByFreq(String[] words)
	{
		int[] freq = new int[26];
		for (int i = 0; i < words.length; ++i)
		{
			String upperWord = words[i].toUpperCase();
			for (int c = 0; c < upperWord.length(); ++c)
			{
				freq[upperWord.charAt(c) - 'A'] += 1;
			}
		}

		String sortedAlphabet = "";
		for (int i = 0; i < 26; ++i)
		{
			int curMin = MAX_INT;
			int idxMin = 26;
			for (int j = 0; j < 26; ++j)
			{
				if (freq[j] < curMin)
				{
					curMin = freq[j];
					idxMin = j;
				}
			}
			if (freq[idxMin] > 0)
			{
				sortedAlphabet += (char)(idxMin + 'A');
			}
			freq[idxMin] = MAX_INT;
		}
		return sortedAlphabet;
	}
}