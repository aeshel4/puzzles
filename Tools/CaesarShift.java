package Tools;

import java.util.Scanner;

public class CaesarShift
{
	private static Scanner sc = new Scanner(System.in);

	private static void PrintHelp()
	{
		System.out.println("java CaesarShift [word] [shift]");
		System.out.println("  Passing no args prompts for a string and decrypts to all 26 possibilities.");
		System.out.println("  Passing just word decrypts it to all 26 possibilities.");
		System.out.println("  Passing both word and shift, shifts the word by just the shift.");
	}
	
	public static void main(String[] args)
	{
		if (args.length == 1 && (args[0].indexOf('?') != -1))
		{
			PrintHelp();
			return;
		}
		String original = "";
		int startShift = 0;
		int endShift = 26;
		if (args.length == 0)
		{
			System.out.println("Enter string to be decrypted.");
			original = sc.nextLine();
			System.out.println();
		}
		else if (args.length >= 1)
		{
			original = args[0];
			if (args.length == 2)
			{
				startShift = Integer.parseInt(args[1]);
				endShift = startShift + 1;
			}
		}

		for (int shift = startShift; shift < endShift; ++shift)
		{
			System.out.print((char)('A' + shift) + "-" + ((shift < 10) ? " " : "") + shift + ": ");
			System.out.println(Utility.CaesarShift.Shift(original, shift));
		}
	}
}