package Tools.arches;

import java.util.*;
import java.io.*;
import Utility.Words.*;
import Utility.Grids.*;

class ArchesPrinter
{
	private GridTessalation grid;
	private ArrayList<ArchesWord> words;
	private ArchesSearchParams params;

	public ArchesPrinter(GridTessalation grid, ArrayList<ArchesWord> foundWords, ArchesSearchParams params)
	{
		this.grid = grid;
		this.words = foundWords;
		this.params = params;
	}

	private static String wordsGiven(ArrayList<ArchesWord> words, Comparator<ArchesWord> comparator, boolean ignoreValidityCheck)
	{
		Collections.sort(words, comparator);

		String str = "";
		for (int w = 0, display = 1; w < words.size(); ++w)
		{
			if (ignoreValidityCheck || words.get(w).isValid())
			{
				str += (display) + ": " + words.get(w).toString() + "\n";
				++display;
			}
		}

		if (words.size() == 0)
		{
			str += "No words meeting criteria.";
		}
		return str;
	}

	private ArchesWord findArchesWordFromWord(String word, int time, boolean ignoreValidityCheck)
	{
		int found = 0;
		for (int w = 0; w < words.size(); ++w)
		{
			if (word.equalsIgnoreCase(words.get(w).getWord(ignoreValidityCheck)))
			{
				if (found == time)
				{
					return words.get(w);
				}
				else
				{
					++found;
				}
			}
		}
		return new ArchesWord();
	}

	public String singleWord(String word)
	{
		return singleWord(word, 0, true);
	}

	public String singleWord(String word, int time, boolean ignoreValidityCheck)
	{
		ArchesWord archesWord = findArchesWordFromWord(word, time, ignoreValidityCheck);
		if (archesWord.getWord(ignoreValidityCheck).equals(""))
		{
			return "";
		}
		else
		{
			return singleWord(archesWord);
		}
	}

	public String singleWord(ArchesWord archesWord)
	{
		ArrayList<ArchesWord> singleArray = new ArrayList<ArchesWord>(1);
		singleArray.add(archesWord);
		return grid.toString(new GridPrinterRepeats(singleArray, 1, false));
	}

	public String wordsFoundMultipleTimes(boolean ignoreValidityCheck)
	{
		ArrayList<ArchesWord> multiples = new ArrayList<ArchesWord>();
		for (int w = 0; w < words.size(); ++w)
		{
			if (!findArchesWordFromWord(words.get(w).getWord(ignoreValidityCheck), 1, ignoreValidityCheck).getWord(ignoreValidityCheck).equals(""))
			{
				multiples.add(words.get(w));
			}
		}
		return wordsGiven(multiples, new ArchesWordComparatorAlphabetical(ignoreValidityCheck), ignoreValidityCheck);
	}

	public String wordsContainingPoint(GridPoint p, boolean ignoreValidityCheck)
	{
		ArrayList<ArchesWord> containing = new ArrayList<ArchesWord>();
		for (int w = 0; w < words.size(); ++w)
		{
			ArrayList<GridPoint> points = words.get(w).getPoints(ignoreValidityCheck);
//Iterator<GridPoint> iter = points.iterator();
//while (iter.hasNext())
{
//	GridPoint tempP = iter.next();
//	System.out.println(p.equals(tempP) + " " + (p.hashCode() == tempP.hashCode()));
}
			if (points.contains(p))
			{
//System.out.println(words.get(w));
				containing.add(words.get(w));
			}
		}
		return wordsGiven(containing, new ArchesWordComparatorAlphabetical(ignoreValidityCheck), ignoreValidityCheck);
	}

	public String wordsStartingAtPoint(GridPoint p, boolean ignoreValidityCheck)
	{
		ArrayList<ArchesWord> starting = new ArrayList<ArchesWord>();
		for (int w = 0; w < words.size(); ++w)
		{
			try
			{
				if (words.get(w).getPoints(ignoreValidityCheck).get(0).equals(p))
				{
					starting.add(words.get(w));
				}
			}
			catch (IndexOutOfBoundsException e)
			{
				continue;
			}
		}
		return wordsGiven(starting, new ArchesWordComparatorAlphabetical(ignoreValidityCheck), ignoreValidityCheck);
	}

	public String wordsEndingAtPoint(GridPoint p, boolean ignoreValidityCheck)
	{
		ArrayList<ArchesWord> ending = new ArrayList<ArchesWord>();
		for (int w = 0; w < words.size(); ++w)
		{
			try
			{
				ArrayList<GridPoint> points = words.get(w).getPoints(ignoreValidityCheck);
				if (points.get(points.size() - 1).equals(p))
				{
					ending.add(words.get(w));
				}
			}
			catch (IndexOutOfBoundsException e)
			{
				continue;
			}
		}
		return wordsGiven(ending, new ArchesWordComparatorAlphabetical(ignoreValidityCheck), ignoreValidityCheck);
	}

	public String wordsOrderedByGrid(boolean ignoreValidityCheck)
	{
		try
		{
			return wordsGiven(words, new ArchesWordComparatorJumpGrid(ignoreValidityCheck), ignoreValidityCheck);
		}
		catch (Exception e)
		{
			return wordsGiven(words, new ArchesWordComparatorGrid(ignoreValidityCheck), ignoreValidityCheck);
		}
	}

	public String wordsOrderedAlphabetically(boolean ignoreValidityCheck)
	{
		return wordsGiven(words, new ArchesWordComparatorAlphabetical(ignoreValidityCheck), ignoreValidityCheck);
	}

	public String wordsOrderedByLength(boolean ignoreValidityCheck)
	{
		return wordsGiven(words, new ArchesWordComparatorLength(ignoreValidityCheck), ignoreValidityCheck);
	}

	public String leftoversOnGrid()
	{
		return repeats(0, true);
	}
	
	public String leftoversDirect()
	{
		String onGrid = leftoversOnGrid();
		onGrid = onGrid.replaceAll(" ", "");
		onGrid = onGrid.replaceAll(GridPrinter.NotPrinted, "");
		onGrid = onGrid.replaceAll("\n", "");
		onGrid = onGrid.replaceAll("\t", "");
		return onGrid;
	}

	public String foundOnGrid()
	{
		return repeats(1, false);
	}

	public String repeats()
	{
		return repeats(2, false);
	}

	public String repeats(int numRepeatToUse, boolean strictMatch)
	{
		return grid.toString(new GridPrinterRepeats(words, numRepeatToUse, strictMatch));
	}
}

class GridPrinterRepeats extends GridPrinter
{
	private ArrayList<ArchesWord> words;
	private int repeats;
	private boolean strictMatch;

	public GridPrinterRepeats(ArrayList<ArchesWord> words, int repeats, boolean strictMatch)
	{
		this.words = words;
		this.repeats = repeats;
		this.strictMatch = strictMatch;
	}

	public String value(GridTessalation grid, GridPoint point)
	{
		int numActualRepeats = numRepeats(point);

		boolean passesCheck;
		if (strictMatch)
		{
			passesCheck = (numActualRepeats == repeats);
		}
		else
		{
			passesCheck = (numActualRepeats >= repeats);
		}

		if (passesCheck)
		{
			return grid.get(point).toString();
		}
		else
		{
			return NotPrinted;
		}
	}

	private int numRepeats(GridPoint point)
	{
		int count = 0;
		for (int w = 0; w < words.size(); ++w)
		{
			ArrayList<GridPoint> points = words.get(w).getPoints(false);
			for (int p = 0; p < points.size(); ++p)
			{
				if (point.equals(points.get(p)))
				{
					++count;
				}
			}
		}
		return count;
	}
}