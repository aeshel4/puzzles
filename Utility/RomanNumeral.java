package Utility;

/*
RETURNS				METHOD CALL					PARAMETER
boolean	RomanNumeral.isValid(					String)
String	RomanNumeral.convertTo(					int)
int		RomanNumeral.convertFrom(				String)
int		RomanNumeral.convertFrom_onlyMDCLXVI(	String)

Last two will throw RuntimeExceptions if not valid RomanNumeral
*/

public class RomanNumeral
{
	public static void main(String[] args)
	{
		System.out.println(convertTo(999));
	}

	public static int valueOfWord(String word)
	{
		int sum = 0;
		for (int c = 0; c < word.length(); ++c)
		{
			String section = "";
			while ((c < word.length()) &&
			       (isValid(word.charAt(c))))
			{
				section += word.charAt(c);
				++c;
			}
			sum += convertFrom(section);
		}
		return sum;
	}

	public static boolean isValid(char ch)
	{
		return isValid(String.valueOf(ch));
	}
	public static boolean isValid(String str)
	{
		// This works by doing the private convert to a number
		// converting that number back into a roman numeral
		// and comparing this back to the original str.
		try
		{
			return str.equalsIgnoreCase(convertTo(private_convertFrom(str)));
		}
		catch (RuntimeException e)
		{
			return false;
		}
	}

	public static int convertFrom(String str)
	{
		int val = private_convertFrom(str);
		String actual = convertTo(val);
		if (str.equalsIgnoreCase(actual))
		{
			// Then we have a valid roman numeral
			return val;
		}
		else
		{
			throw new RuntimeException("Not a valid Roman Numeral. " + val + " would be written as " + actual);
		}
	}

	public static int convertFrom_onlyMDCLXVI(String str)
	{
		str = str.toUpperCase();
		String newStr = "";
		for (int i = 0; i < str.length(); ++i)
		{
			switch(str.charAt(i))
			{
				case 'M' : // all fall through
				case 'D' : //    |
				case 'C' : //    |
				case 'L' : //    |
				case 'X' : //    |
				case 'V' : //    v
				case 'I' : newStr += str.charAt(i);
				default :  // do nothing
			}
		}
		return convertFrom(newStr);
	}

	public static String convertTo(int num)
	{
		String str = "";
		for (int i = 0; i < strs.length; ++i)
		{
			while (num >= vals[i])
			{
				str += strs[i];
				num -= vals[i];
			}
		}
		return str;
	}

// PRIVATE METHODS AND FIELDS

	private static String[] strs = { "M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};
	private static int[] vals =    {1000,  900, 500,  400, 100,   90,  50,   40,  10,    9,   5,    4,   1};

	private static int private_convertFrom(String str)
	{ // This will just give #'s based on minor rules, i.e., IXXL will come out as 49 (= XLIX)
		str = str.toUpperCase();
		int val = 0;
		int curVal;
		for (int i = 0; i < str.length(); ++i)
		{
			curVal = valOf(str.charAt(i));
			if (i == str.length() - 1 || curVal	>= valOf(str.charAt(i + 1)))
			{
				val += curVal;
			}
			else
			{
				val -= curVal;
			}
		}
		return val;
	}

	private static int valOf(char ch)
	{
		for (int i = 0; i < strs.length; ++i)
		{
			if (strs[i].equalsIgnoreCase(String.valueOf(ch)))
			{
				return vals[i];
			}
		}
		throw new RuntimeException("'" + ch + "' is not a valid Roman Numeral character.");
	}
}