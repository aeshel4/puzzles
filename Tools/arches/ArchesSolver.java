package Tools.arches;

import java.util.*;
import java.io.*;
import java.time.Duration;
import java.time.Instant;
import Utility.Words.*;
import Utility.Grids.*;

class ArchesSearchParams
{
	public ARCHES_SEARCH_STYLE style;
	public boolean wrapAround;
	public boolean cardinalOnly; // Doesn't apply to all styles, e.g. knight. Causes straight lines in hex grids
	public boolean reuse; // Doesn't apply in some combinations, e.g. CLASSIC & !wrapAround
	public boolean letterOff; // Allow words to have a letter changed, i.e. PEWTER->PEWTOR

	public ArchesSearchParams()
	{
		style = ARCHES_SEARCH_STYLE.CLASSIC;
		wrapAround = cardinalOnly = reuse = false;
	}
}

public class ArchesSolver
{
	private GridTessalation grid;
	private GenericWordList wordList;
	private ArrayList<ArchesWord> foundWords;
	private ArchesSearchStyle searchStyle;
	private ArchesSearchParams searchParams;
	private Duration timeTakenToSolve;

	public ArchesSolver(ArchesSearchParams searchParams, GridTessalation grid, GenericWordList wordList)
	{
		this.searchParams = searchParams;
		this.grid = grid;
		this.wordList = wordList;
		resetFoundWords();
		initSearchStyle();
	}

	public void resetFoundWords()
	{
		foundWords = new ArrayList<ArchesWord>();
	}

	private void initSearchStyle()
	{
		switch (searchParams.style)
		{
			case CLASSIC: // fall through
			case BOGGLE: searchStyle = new ArchesSearchStyle_Normal(searchParams.style, searchParams.cardinalOnly); break;
			case KNIGHT: searchStyle = new ArchesSearchStyle_Knight(); break;
			case JUMP: searchStyle = new ArchesSearchStyle_Jump(grid); break;
		}
	}

	private void operateOnWord(ArchesWord archesWord)
	{
		String strWord = archesWord.getWord(false);
		StrExistsAs validity;

		if (searchParams.letterOff)
		{
			char[] chars = strWord.toCharArray();
			boolean asWrd = false;
			boolean asPre = false;
			char ch;
			for (int i = 0; i < strWord.length(); ++i)
			{
				ch = chars[i];
				chars[i] = '?';
				validity = wordList.searchPrefix(String.valueOf(chars));
				chars[i] = ch;
				asWrd |= validity.isWord();
				asPre |= validity.isPrefix();
			}
			validity = StrExistsAs.Construct(asPre, asWrd, false);
		}
		else
		{
			validity = wordList.searchPrefix(strWord);
		}

		if (validity.isWord())
		{
			foundWords.add(archesWord);
		}

		if (!validity.isPrefix())
		{
			return;
		}

		GridPoint[] nextPoints = searchStyle.getValidNextPoints(archesWord);
		for (int p = 0; p < nextPoints.length; ++p)
		{
			GridPoint nextPoint = nextPoints[p];
			if (!searchParams.reuse && archesWord.isPointUsed(nextPoint))
			{
				continue;
			}
			else if (!modifyPointWithin(nextPoint))
			{
				continue;
			}

			ArchesWord nextWord = new ArchesWord(archesWord);
			nextWord.add(nextPoint, grid.get(nextPoint).toString());
			operateOnWord(nextWord);
		}
	}

	private boolean modifyPointWithin(GridPoint point)
	{
		if (grid.contains(point))
		{
			return true;
		}
		else if (!searchParams.wrapAround)
		{
			return false;
		}
		else
		{
			grid.shiftWithin(point);
			return true;
		}
	}

	public ArrayList<ArchesWord> solve()
	{
		Instant startTime = Instant.now();

		GridPoint point;
		Iterator<GridPoint> iter = grid.iterator();
		while (iter.hasNext())
		{
			ArchesWord archesWord = new ArchesWord();
			point = iter.next();

			// Make a copy of the point being added, since this one gets changed as we loop
			archesWord.add(point.clone(), (String)grid.get(point));
			operateOnWord(archesWord);
		}

		Instant endTime = Instant.now();
		timeTakenToSolve = Duration.between(startTime, endTime);

		return foundWords;
	}

	public Duration getSolveTime()
	{
		return timeTakenToSolve;
	}

	public String toString()
	{
		String str = grid.toString() + "\n\n\n";
		ArchesPrinter printer = new ArchesPrinter(grid, foundWords, searchParams);
		str += printer.wordsOrderedByGrid(false);
		return str;
	}
}