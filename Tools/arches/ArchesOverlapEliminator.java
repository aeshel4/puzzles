package Tools.arches;

import java.util.*;
import Utility.Utility;
import Utility.Math.Math;
import Utility.Grids.*;
import Utility.Words.*;

abstract class ArchesOverlapEliminator
{
	protected ArrayList<ArchesWord> foundArchesWords;
	protected boolean letterOff;

	public abstract void eliminate();

	protected ArchesOverlapEliminator(ArrayList<ArchesWord> foundWords, boolean letterOff)
	{
		foundArchesWords = foundWords;
		this.letterOff = letterOff;
		Collections.sort(foundArchesWords, new ArchesWordComparatorAlphabetical(false));
	}

	protected GridPoint[] intersect(GridPoint[] existingPoints, GridPoint[] newPoints)
	{
		if (existingPoints == null)
		{
			return newPoints;
		}
		else
		{
			GridPoint[] intersected = new GridPoint[existingPoints.length];
			int iCount = 0;
			for (int e1 = 0; e1 < existingPoints.length; ++e1)
			{
				for (int e2 = 0; e2 < existingPoints.length; ++e2)
				{
					if (existingPoints[e1].equals(newPoints[e2]))
					{
						intersected[iCount++] = existingPoints[e1];
						break;
					}
				}
			}
			return Arrays.copyOf(intersected, iCount);
		}
	}

	protected boolean markInvalidForPoints(GridPoint[] points, String word)
	{
		boolean madeChange = false;
		Iterator<ArchesWord> iter = foundArchesWords.iterator();
		while (iter.hasNext())
		{
			ArchesWord aw = iter.next();
			if (aw.isValid() && !wordsEqual(aw.getWord(false), word))
			{
				for (int fp = 0; fp < points.length; ++fp)
				{
					if (aw.isPointUsed(points[fp]))
					{
						aw.setValidity(false);
						madeChange = true;
					}
				}
			}
		}
		return madeChange;
	}

	protected boolean wordsEqual(String s1, String s2)
	{
		if (!letterOff)
		{
			return s1.equals(s2);
		}
		else
		{
			return Utility.isOneLetterOff(s1, s2);
		}
	}
}

class ArchesOverlapEliminatorAllPoints extends ArchesOverlapEliminator
{
	private GridTessalation grid;

	public ArchesOverlapEliminatorAllPoints(ArrayList<ArchesWord> foundWords, GridTessalation grid)
	{
		super(foundWords, false);
		this.grid = grid;

		eliminate();
	}

	public void eliminate()
	{
		boolean madeChange;
		do
		{
			madeChange = false;
			Iterator<GridPoint> iter = grid.iterator();
			while (iter.hasNext())
			{
				madeChange |= eliminateOverlapToUniquePoint(iter.next());
			}
		} while (madeChange);
	}

	// Go through all the points looking for ones which can only be fulfilled by a single string.
	// Then get the intersection of all points in common amongst those words. These are points
	// which have to be used alongside the unique point. Therefore eliminate all words which use
	// these points.
	private boolean eliminateOverlapToUniquePoint(GridPoint point)
	{
		// might have sample1, sample2 be only words to use pointA, but sample3 doesn't use it
		GridPoint[] fixablePoints = null;
		String uniqueWord = "";
		Iterator<ArchesWord> wordIter = foundArchesWords.iterator();
		while (wordIter.hasNext())
		{
			ArchesWord aw = wordIter.next();
			if (aw.isValid() && aw.isPointUsed(point))
			{
				if (uniqueWord.isEmpty() || wordsEqual(uniqueWord, aw.getWord(false)))
				{
					uniqueWord = aw.getWord(false);
					fixablePoints = intersect(fixablePoints, aw.getPoints(false).toArray(new GridPoint[0]));
				}
				else
				{
					// More than one word uses this location
					//AE note that two different words on a unique point, who otherwise have an overlap could be used
					fixablePoints = null;
					break;
				}
			}
		}

		if (fixablePoints != null)
		{
			invalidateWordsThatDontContainPoint(uniqueWord, point);
			markInvalidForPoints(fixablePoints, uniqueWord);
		}

		return false;
	}

	private boolean invalidateWordsThatDontContainPoint(String word, GridPoint point)
	{
		boolean madeChange = false;
		Iterator<ArchesWord> wordIter = foundArchesWords.iterator();
		while (wordIter.hasNext())
		{
			ArchesWord aw = wordIter.next();
			if (aw.isValid() &&
				wordsEqual(word, aw.getWord(false)) &&
				!aw.isPointUsed(point))
			{
				aw.setValidity(false);
				madeChange = true;
			}
		}
		return madeChange;
	}
}

class ArchesOverlapEliminatorGivenWordList extends ArchesOverlapEliminator
{
	private WordListStringArray wordList;
	private ArrayList<String> words;

	public ArchesOverlapEliminatorGivenWordList(ArrayList<ArchesWord> foundWords, WordListStringArray wordList, boolean letterOff)
	{
		super(foundWords, letterOff);

		this.wordList = wordList;
		if (wordList == null)
		{
			ArrayList<String> words = new ArrayList<String>();

			Iterator<ArchesWord> iter = foundArchesWords.iterator();
			String prevAdded = "";
			while (iter.hasNext())
			{
				ArchesWord aw = iter.next();
				if (aw.isValid())
				{
					String curWord = aw.getWord(false);
					if (!wordsEqual(curWord, prevAdded))
					{
						words.add(curWord);
						prevAdded = curWord;
					}
				}
			}
		}

		eliminate();
	}

	public void eliminate()
	{
		boolean madeChange;
		do
		{
			madeChange = false;
			Iterator<String> iter = iteratorator();
			while (iter.hasNext())
			{
				madeChange |= eliminateOverlapToFixedPoint(iter.next());
			}
		} while (madeChange);
	}

	private Iterator<String> iteratorator()
	{
		if (wordList == null)
		{
			return words.iterator();
		}
		else
		{
			return wordList.iterator();
		}
	}

	private boolean eliminateOverlapToFixedPoint(String word)
	{
		// The degenerate version of this handles single word left
		Iterator<ArchesWord> iter = foundArchesWords.iterator();
		GridPoint[] fixedPoints = null;
		while (iter.hasNext())
		{
			ArchesWord aw = iter.next();
			if (aw.isValid() && wordsEqual(word, aw.getWord(false)))
			{
				fixedPoints = intersect(fixedPoints, aw.getPoints(false).toArray(new GridPoint[0]));
			}
		}

		if (fixedPoints == null)
		{
			return false;
		}
		return markInvalidForPoints(fixedPoints, word);
	}
}