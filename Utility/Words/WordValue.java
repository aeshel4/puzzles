package Utility.Words;

import Utility.RomanNumeral;

public class WordValue
{
	private static void PrintHelp()
	{
		System.out.println("java WordValue [0-3] word1 word2 ...");
		System.out.println("  0: Sum of letters where A=1, B=2, ..., Z=26");
		System.out.println("  1: Sum of letters' scrabble value");
		System.out.println("  2: Reads out I's and O's and converts to a binary value.");
		System.out.println("  3: Reads out sequences of roman numerals and adds them together.");
	}

	public static void main(String[] args)
	{
		if (args.length < 2)
		{
			PrintHelp();
			return;
		}
		
		int choice = Integer.parseInt(args[0]);
		for (int w = 1; w < args.length; ++w)
		{
			System.out.println(args[w] + ": " + ValueOf(args[w], choice));
		}
	}

	public static int ValueOf(String word, int type)
	{
		if (type == 2)
		{
			return ValueOf_IO_AsBinary(word);
		}
		else if (type == 3)
		{
			return RomanNumeral.valueOfWord(word);
		}

		int sum = 0;
		for (int c = 0; c < word.length(); ++c)
		{
			char ch = word.charAt(c);
			int value;
			switch (type)
			{
				case 0: value = Ato1_Zto26(ch); break;
				case 1: value = ScrabbleValueOf(ch); break;
				default: return -1;
			}
			sum += value;
		}
		return sum;
	}

	private static int[] ScrabbleValue = {
		1, // A
		3, // B
		3, // C
		2, // D
		1, // E
		4, // F
		2, // G
		4, // H
		1, // I
		8, // J
		5, // K
		1, // L
		3, // M
		1, // N
		1, // O
		3, // P
		10, // Q
		1, // R
		1, // S
		1, // T
		1, // U
		4, // V
		4, // W
		8, // X
		4, // Y
		10 // Z
	};

	public static int ScrabbleValueOf(char ch)
	{
		return ScrabbleValue[Ato1_Zto26(ch) - 1];
	}

	public static int Ato1_Zto26(char ch)
	{
		ch = Character.toUpperCase(ch);
		if (ch >= 'A' && ch <= 'Z')
		{
			return (int)(ch - 'A' + 1);
		}
		else
		{
			return 0;
		}
	}

	public static int ValueOf_IO_AsBinary(String word)
	{
		word = word.toUpperCase();
		int power = 0;
		int value = 0;
		for (int c = (word.length() - 1); c >= 0 ; --c)
		{
			if (word.charAt(c) == 'I')
			{
				value += (1 << power);
				++power;
			}
			else if (word.charAt(c) == 'O')
			{
				++power;
			}
		}

		// Want to differentiate having an O to be 0 vs. having nothing
		if (power == 0)
		{
			value = -1;
		}
		return value;
	}
}