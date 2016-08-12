package Tools;

import java.util.Scanner;

public class CryptographySolveHelper
{
	private static String encrypted;
	private static char[] decrypted;
	private static boolean ignoreCase = true;
	private static boolean puncIsCipher = false;
	private static Scanner sc = new Scanner(System.in);
	private static int width = 150;

	public static void main(String[] args)
	{
		if (args.length == 1 && (args[0].indexOf('?') != -1))
		{
			System.out.println("-case: Enable case sensitive");
			System.out.println("-punc: Enable punctuation as encrypted");
			System.out.println("-width n: Sets the # of chars per line to n");
			return;
		}

		for (int i = 0; i < args.length; ++i)
		{
			if (args[i].equals("-case"))
			{
				ignoreCase = false;
				System.out.println("Case sensitive enabled.");
			}
			else if (args[i].equals("-punc"))
			{
				puncIsCipher = true;
				System.out.println("Punctuation treated as encrypted.");
			}
			else if (args[i].equals("-width"))
			{
				width = Integer.valueOf(args[i+1]);
				System.out.println("Width set to " + width);
				i++;
			}
		}
		System.out.println("Enter string to be decrypted.");
		encrypted = sc.nextLine();
		if (ignoreCase)
		{
			encrypted = encrypted.toUpperCase();
		}
		decrypted = encrypted.toCharArray();
		if (puncIsCipher)
		{
			clearAll();
		}
		else
		{
			clearChars();
		}
		startSolving();
	}

	private static void startSolving()
	{
		String input;
		while(true)
		{
			print();

			System.out.print("Now what?  ");
			input = sc.nextLine();
			if (input.equalsIgnoreCase("key") ||
			    input.equalsIgnoreCase("rkey"))
			{
				printKey(input.equalsIgnoreCase("rkey"));
			}
			else if (input.equalsIgnoreCase("done") ||
			         input.equalsIgnoreCase("exit") ||
			         input.equalsIgnoreCase("stop") ||
			         input.equalsIgnoreCase("quit"))
			{
				printKey(false);
				System.out.println();
				printKey(true);
				break;
			}
			else if (input.startsWith("clear") ||
				     input.startsWith("clr") ||
				     input.startsWith("del") ||
				     input.startsWith("delete"))
			{
				replace(input.charAt(input.length()-1), ' ');
			}
			else if (input.startsWith("keyis"))
			{
				String alphabet = "";
				for (char ch = 'A'; ch <= 'Z'; ++ch)
				{
					int index = encrypted.indexOf(ch);
					if (index != -1)
					{
						alphabet += ch;
					}
				}

				String newKey = input.substring(6); // length of: keyis + ' '
				for (int i = 0; i < newKey.length(); ++i)
				{
					if (newKey.charAt(i) != ' ')
					{
						replace(alphabet.charAt(i), newKey.charAt(i));
					}
				}
			}
			else if (input.contains("to") ||
				     input.contains("is") ||
				     input.contains("->"))
			{
				char orig = input.charAt(0);
				char repl = input.charAt(input.length()-1);
				replace(orig, repl);
			}
			else if (input.equalsIgnoreCase("freq"))
			{
				printFrequencies();
			}
			else
			{
				System.out.println("KEYWORDS: ");
				System.out.println("  done; exit; stop; quit");
				System.out.println("  key; rkey");
				System.out.println("  freq");
				System.out.println("  clear @; clr @; del @; delete @");
				System.out.println("  @ to @; @ is @; @ -> @");
				System.out.println("  keyis @@@...@@@     (Note: only supports alphabet currently)");
			}
		}
	}

	private static void print()
	{
		boolean overlong;
		for (int i = 0; i < encrypted.length(); i += width)
		{
			overlong = (i + width) > (encrypted.length()-1);
			System.out.println(encrypted.substring(i, !overlong ? (i + width) : (encrypted.length())));
			System.out.println(new String(decrypted, i, !overlong ? width : (encrypted.length() - i)));
			System.out.println();
		}
	}

	private static void printKey(boolean reverse)
	{
		String alphabet = "";
		String key = "";
		String decryptedStr = new String(decrypted);
		for (char ch = ' '; ch <= '~'; ++ch)
		{
			int index = reverse ? decryptedStr.indexOf(ch) : encrypted.indexOf(ch);
			if (index != -1)
			{
				alphabet += ch;
				key += reverse ? encrypted.charAt(index) : decrypted[index];
			}
		}
		System.out.println(alphabet);
		System.out.println(key);
	}

	private static void printFrequencies()
	{
		int count = 26;
		if (!ignoreCase)
		{
			count *= 2;
		}
		int[] cipherAlphabet = new int[count];
		for (int c = 0; c < cipherAlphabet.length; ++c)
		{
			cipherAlphabet[c] = 0;
		}

		int totalCount = 0;
		for (int i = 0; i < encrypted.length(); ++i)
		{
			char ch = encrypted.charAt(i);
			if (Character.isLetter(ch))
			{
				++totalCount;
				char alphaStart = Character.isUpperCase(ch) ? 'A' : 'a';
				cipherAlphabet[(ch - alphaStart) + (Character.isUpperCase(ch) ? 0 : 26)] += 1;
			}
		}

		int nonZeroCount = 0;
		for (int c = 0; c < cipherAlphabet.length; ++c)
		{
			char ch = (c >= 26) ? ('a' - 26) : 'A';
			boolean nonZero = cipherAlphabet[c] != 0;
			System.out.print((char)(ch + c) + ":");
			if (nonZero)
			{
				System.out.print(cipherAlphabet[c]);
			}
			else
			{
				System.out.print('-');
			}
			System.out.print("  ");
			if ((c + 1) % 13 == 0)
			{
				System.out.println();
			}
			if (nonZero)
			{
				++nonZeroCount;
			}
		}
		System.out.println("Number of unique characters: " + nonZeroCount + "          Total count: " + totalCount);
	}

	private static void replace(char orig, char repl)
	{
		if (ignoreCase)
		{
			orig = Character.toUpperCase(orig);
			repl = Character.toUpperCase(repl);
		}
		for (int i = 0; i < encrypted.length(); ++i)
		{
			if (encrypted.charAt(i) == orig)
			{
				decrypted[i] = repl;
			}
		}
	}

	private static void clearChars()
	{
		for (int i = 0; i < decrypted.length; ++i)
		{
			if (Character.isLetter(decrypted[i]))
			{
				decrypted[i] = ' ';
			}
		}
	}
	private static void clearAll()
	{
		for (int i = 0; i < decrypted.length; ++i)
		{
			decrypted[i] = ' ';
		}
	}
}