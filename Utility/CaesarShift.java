package Utility;

public class CaesarShift
{
	public static String Shift(String word, int shift)
	{
		String shifted = "";
		for (int index = 0; index < word.length(); ++index)
		{
			shifted += Shift(word.charAt(index), shift);
		}
		return shifted;
	}
	
	public static char Shift(char letter, int shift)
	{
		if (!Character.isLetter(letter))
		{
			return letter;
		}
		shift = Make0to25(shift);
		char a = Character.isUpperCase(letter) ? 'A' : 'a';
		return (char)(((int)letter + shift - a) % 26 + a);
	}

	public static char WhatIsShift(char c1, char c2)
	{
		c1 = Character.toUpperCase(c1);
		c2 = Character.toUpperCase(c2);
		return (char)('A' + Make0to25((int)c2 - (int)c1));
	}

	private static int Make0to25(int n)
	{
		return ((n % 26) + 26) % 26;
	}
}