package Utility;

import java.io.*;
import java.util.*;

import Utility.Words.WordValue;

public class NATO
{
	private static String[] alphabet =
	{
		"ALPHA",
		"BRAVO",
		"CHARLIE",
		"DELTA",
		"ECHO",
		"FOXTROT",
		"GULF",
		"HOTEL",
		"INDIA",
		"JULIET",
		"KILO",
		"LIMA",
		"MIKE",
		"NOVEMBER",
		"OSCAR",
		"PAPA",
		"QUEBEC",
		"ROMEO",
		"SIERRA",
		"TANGO",
		"UNIFORM",
		"VICTOR",
		"WHISKEY",
		"XRAY",
		"YANKEE",
		"ZULU"
	};

	public static void main(String[] args) throws FileNotFoundException
	{
		if (args.length != 1)
		{
			System.out.println("Pass the name of a txt file to print out NATO letters in the file.");
			return;
		}

		Scanner sc = new Scanner(new File(args[0]));
		String alphaText = "";
		while (sc.hasNextLine())
		{
			String line = sc.nextLine();
			for (int i = 0; i < line.length(); ++i)
			{
				char ch = line.charAt(i);
				if (Character.isLetter(ch))
				{
					alphaText += ch;
				}
			}
		}

		
	}

	public static String getLetter(char ch)
	{
		int val = WordValue.Ato1_Zto26(ch);
		try
		{
			return alphabet[val - 1];
		}
		catch (ArrayIndexOutOfBoundsException e)
		{
			return "";
		}
	}

	public static String[] getAlphabet()
	{
		return alphabet;
	}
}