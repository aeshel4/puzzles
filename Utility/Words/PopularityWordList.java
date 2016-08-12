package Utility.Words;

//THE,26548583149

import java.io.*;
import java.util.*;
import java.util.regex.*;

class testRule implements RuleForMatching
{
	public boolean IsAMatch(String word)
	{
		for (int i = 1; i < word.length(); ++i)
		{
			if (word.charAt(i) == word.charAt(i - 1))
			{
				return true;
			}
		}
		return false;
	}
}
/*
public class PopularityWordList extends GenericWordList
{
	private File file;
	private int threshold;

	public static void main(String[] args) throws FileNotFoundException, IOException
	{
		PopularityWordList pwl = new PopularityWordList(5);
		pwl.printThroughWords(new testRule());
	}

	public PopularityWordList() throws FileNotFoundException, IOException
	{
		this(500000, "wordlists\\google-books-1grams.txt");
	}
	public PopularityWordList(long popularityThreshold, String filename) throws FileNotFoundException, IOException
	{
		this.threshold = popularityThreshold;
		this.file = new File(filename);
	}

    public StrExistsAs searchPrefix(String pre)
    {
    	FileReader fr = new FileReader(file);
		BufferedReader br = new BufferedReader(fr);
        String line, word;
        boolean asWrd = false;
        boolean asPre = false;
        StrExistsAs sea;
		while (!(asWrd && asPre) &&
				(line = readerPassedIn.readLine()) != null)
		{
			sea = super.matchesByUnknowns(pre, line.substring(0, line.indexOf(",")));
			asWrd |= sea.isWord();
			asPre |= sea.isPrefix();
		}

		br.close();
		fr.close();

        return StrExistsAs.Construct(asPre, asWrd, false);
    }

	public Iterator<String> iterator()
	{
		return new WordListIter(file);
	}
}
*/

public class PopularityWordList extends GenericWordList
{
	private WordList backingWordList;

	public static void main(String[] args) throws FileNotFoundException, IOException
	{
		PopularityWordList pwl = new PopularityWordList(5);
		pwl.printThroughWords(new testRule());
	}

	public PopularityWordList() throws FileNotFoundException, IOException
	{
		this(0);
	}
	public PopularityWordList(int wordLength) throws FileNotFoundException, IOException
	{
		this(500000, "wordlists\\google-books-1grams.txt", wordLength);
	}
	public PopularityWordList(long popularityThreshold, String filename) throws FileNotFoundException, IOException
	{
		this(popularityThreshold, filename, 0);
	}
	public PopularityWordList(long popularityThreshold, String filename, int wordLength) throws FileNotFoundException, IOException
	{
		File filePassedIn = new File(filename);
		BufferedReader readerPassedIn = new BufferedReader(new FileReader(filePassedIn));

		File fileOfConstrainedList = new File(filePassedIn.getParentFile().getAbsolutePath(), "tempDict.txt");
		fileOfConstrainedList.delete(); // In case we last quit badly
		fileOfConstrainedList.createNewFile();
		fileOfConstrainedList.deleteOnExit();

        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileOfConstrainedList), "UTF-8"));

        Pattern uppercase = Pattern.compile("[A-Z]*");

        String line;
		while ((line = readerPassedIn.readLine()) != null)
		{
			String[] wordAndPopularity = line.split(",");
			long popularity = Long.parseLong(wordAndPopularity[1]);
			if (popularity < popularityThreshold)
			{
				break;
			}

			if ((wordLength == 0) || (wordLength == wordAndPopularity[0].length()))
			{
				Matcher matcher = uppercase.matcher(wordAndPopularity[0]);
				if ((wordAndPopularity[0].length() > 1) &&
					matcher.matches())
				{
		            writer.write(wordAndPopularity[0]);
		            writer.write('\n');
		        }
	        }
        }

        writer.close();
        readerPassedIn.close();

        String filenameOfConstrainedList = fileOfConstrainedList.getAbsolutePath();

        WordList.MakeIntoValidWordList(filenameOfConstrainedList, true);

        backingWordList = new WordList(filenameOfConstrainedList);
	}

    public StrExistsAs searchPrefix(String pre)
    {
    	return backingWordList.searchPrefix(pre);
    }

	public Iterator<String> iterator()
	{
		return backingWordList.iterator();
	}
}
