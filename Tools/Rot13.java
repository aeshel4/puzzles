package Tools;

import Utility.CaesarShift;
import java.util.Scanner;

public class Rot13
{
	public static void main(String[] args)
	{
		if (args.length == 0)
		{
			Scanner sc = new Scanner(System.in);
			System.out.println("Enter string to be decrypted.");
			String line = sc.nextLine();
			System.out.println();
			System.out.println(CaesarShift.Shift(line, 13) + " ");
		}
		else
		{
			System.out.println();
			for (int i = 0; i < args.length; ++i)
			{
				System.out.print(CaesarShift.Shift(args[i], 13) + " ");
			}
			System.out.println();
		}
	}
}