package Tools;

import java.io.IOException;
import java.io.FileNotFoundException;
import java.time.*;
import Utility.Words.*;
import Utility.Utility;

// Could probably speed this up by going from both sides, rather than always branching from start.

class LadderRung implements RuleForMatching
{
	private String start;
	private LadderRung[] adjacentRungs;

	public LadderRung(String start)
	{
		this.start = start;
		adjacentRungs = null;
	}

	public boolean IsAMatch(String word)
	{
		return Utility.isOneLetterOff(start, word);
	}

	public int fillAdjacent(GenericWordList wl)
	{
		String[] adjacentWords;
		try
		{
			adjacentWords = wl.collectThroughWords(this);
		}
		catch (IOException e)
		{
			throw new RuntimeException(e.toString());
		}

		adjacentRungs = new LadderRung[adjacentWords.length];
		for (int i = 0; i < adjacentWords.length; ++i)
		{
			adjacentRungs[i] = new LadderRung(adjacentWords[i]);
		}
		return adjacentRungs.length;
	}

	public LadderRung[] getAdjacent()
	{
		return adjacentRungs;
	}

	public String getWord()
	{
		return start;
	}
}

public class WordLadder
{
	private String start;
	private String end;
	private String[] between;
	private GenericWordList wordList;

	public static void main(String[] args) throws FileNotFoundException
	{
		Instant startTime = Instant.now();

		if (args.length != 3)
		{
			System.out.println("exe <start> <end> <maxNumRungs>");
			return;
		}

		WordLadder ladder = new WordLadder(args[0], args[1]);
		if (ladder.fillWordsBetween(Integer.parseInt(args[2])))
		{
			System.out.println(ladder);
		}

		Instant endTime = Instant.now();
		Duration timeTaken = Duration.between(startTime, endTime);
		System.out.println(timeTaken);
	}

	public WordLadder(String start, String end) throws FileNotFoundException
	{
		this(start, end, new WordList("wordlists\\dictionary.txt"));
	}
	public WordLadder(String start, String end, GenericWordList wl)
	{
		this.start = start.toUpperCase();
		this.end = end.toUpperCase();
		this.between = null;
		this.wordList = wl;
	}

	public boolean fillWordsBetween(int maxNumToSearch)
	{
		LadderRung startRung = new LadderRung(start);
		return recurse(startRung, 0, maxNumToSearch);
	}

	private boolean recurse(LadderRung rung, int dist, int maxNumToSearch)
	{
		if (rung.getWord().equals(end))
		{
			between = new String[dist];
			return true;
		}
		else if (dist > maxNumToSearch)
		{
			return false;
		}
		else
		{
			rung.fillAdjacent(wordList);
			LadderRung[] next = rung.getAdjacent();
			for (int i = 0; i < next.length; ++i)
			{
				if (recurse(next[i], dist + 1, maxNumToSearch))
				{
					between[dist] = next[i].getWord();
					return true;
				}
			}
			return false;
		}
	}

	public String[] getBetween()
	{
		return between;
	}

	public String toString()
	{
		String str = "";
		str += start;
		for (int i = 0; i < between.length; ++i)
		{
			str += "\n" + between[i];
		}
		return str;
	}
}