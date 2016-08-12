package Utility.Words;

public class SymbolsCanSpell implements RuleForMatching
{
	private String[] symbols;
	private boolean print;
	private int minSymLen;
	private int maxSymLen;

	public SymbolsCanSpell(String[] symbols)
	{
		this.symbols = symbols;
		print = false;

		minSymLen = Integer.MAX_VALUE;
		maxSymLen = 0;

		for (int i = 0; i < symbols.length; ++i)
		{
			if (symbols[i].length() > maxSymLen)
			{
				maxSymLen = symbols[i].length();
			}
			if (symbols[i].length() < minSymLen)
			{
				minSymLen = symbols[i].length();
			}
		}
	}

	public void SetPrintOnMatch()
	{
		print = true;
	}

    public boolean IsAMatch(String word)
	{
		if (word.length() == 0)
		{
			return true;
		}

		for (int symLen = maxSymLen; symLen >= minSymLen; --symLen)
		{
			if (word.length() >= symLen)
			{
				String sym = word.substring(0, symLen);
				int symIdx = IsASymbol(sym);
				if (symIdx != -1)
				{
					if (IsAMatch(word.substring(symLen)))
					{
						if (print) {System.out.print(" " + symbols[symIdx]);}
						return true;
					}
				}
			}
		}
		return false;
	}

	private int IsASymbol(String sym)
	{
		for (int i = 0; i < symbols.length; ++i)
		{
			if (symbols[i].equalsIgnoreCase(sym))
			{
				return i;
			}
		}
		return -1;
	}
}