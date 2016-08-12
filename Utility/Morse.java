package Utility;

public class Morse
{
	public static void main(String[] args)
	{
		String str = "";
		for (String s : args)
		{
			str += s + " ";
		}
		str = str.trim();

		char[] chars = possibilitiesThatStart(str);
		for (char c : chars)
		{
			System.out.print(c);
		}
	}

	private final static String[] letters =
	{
		".-", // A
		"-...", // B
		"-.-.", // C
		"-..", // D
		".", // E
		"..-.", // F
		"--.", // G
		"....", // H
		"..", // I
		".---", // J
		"-.-", // K
		".-..", // L
		"--", // M
		"-.", // N
		"---", // O
		".--.", // P
		"--.-", // Q
		".-.", // R
		"...", // S
		"-", // T
		"..-", // U
		"...-", // V
		".--", // W
		"-..-", // X
		"-.--", // Y
		"--..", // Z
	};

	private final static String[] numbers =
	{
		"-----", // 0
		".----", // 1
		"..---", // 2
		"...--", // 3
		"....-", // 4
		".....", // 5
		"-....", // 6
		"--...", // 7
		"---..", // 8
		"----.", // 9
	};

	public static String encode(String val)
	{
		String str = "";
		for (int i = 0; i < val.length(); ++i)
		{
			str += encode(val.charAt(i));
			str += ' ';
		}
		return str;
	}

	public static String encode(char ch)
	{
		if (Character.isDigit(ch))
		{
			return numbers[ch - '0'];
		}
		else if (Character.isLetter(ch))
		{
			ch = Character.toUpperCase(ch);
			return letters[ch - 'A'];
		}
		else
		{
			return (ch == ' ') ? " " : "?";
		}
	}

	public static String decode(String code)
	{
		String[] letters = code.split(" ");
		String ret = "";
		for (String s : letters)
		{
			ret += decodeSingleChar(s);
		}
		return ret;
	}

	public static char[] possibilitiesThatStart(String code)
	{
		int count = 0;
		for (String s : letters)
		{
			if (s.startsWith(code))
			{
				++count;
			}
		}

		char[] ret = new char[count];
		int cur = 0;
		for (int i = 0; i < letters.length; ++i)
		{
			if (letters[i].startsWith(code))
			{
				ret[cur++] = (char)('A' + i);
			}
		}

		if (cur != count) {throw new RuntimeException("Programmer error in possibilitiesThatStart");}

		return ret;
	}

	private static char decodeSingleChar(String code)
	{
		for (int c = 0; c < letters.length; ++c)
		{
			if (code.equals(letters[c]))
			{
				return (char)('A' + c);
			}
		}

		for (int n = 0; n < numbers.length; ++n)
		{
			if (code.equals(numbers[n]))
			{
				return (char)(n + '0');
			}
		}

		return '?';
	}
}