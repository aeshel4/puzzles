package Utility.Words;

import java.io.*;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class WordsOfLengthN implements RuleForMatching
{
    private int n;
    private boolean greaterThanOrEqual;

    public WordsOfLengthN(int n, boolean greaterThanOrEqual)
    {
        this.n = n;
        this.greaterThanOrEqual = greaterThanOrEqual;
    }

    public boolean IsAMatch(String word)
    {
        if (greaterThanOrEqual)
        {
            return word.length() >= n;
        }
        else
        {
            return word.length() == n;
        }
    }
}

public abstract class GenericWordList implements Iterable<String>
{
    public abstract StrExistsAs searchPrefix(String pre);

    protected final static char UNKNOWN = '?';

    public static void main(String[] args) throws Exception
    {
        WordList wl = new WordList("C:\\Users\\Amos\\SkyDrive\\ProgramHierarchy\\wordlists\\dictionary.txt");
        WordsOfLengthN rule = new WordsOfLengthN(4, true);
        File file = new File("C:\\Users\\Amos\\SkyDrive\\ProgramHierarchy\\wordlists\\dictionary_ge_4.txt");
        wl.printToFileThroughWords(rule, file);
    }

    public static StrExistsAs matchesByUnknowns(String str, String word)
    {
        if (str.length() > word.length())
        {
            return StrExistsAs.NONE;
        }

        for (int c = 0; c < str.length(); ++c)
        {
            char ch = str.charAt(c);
            if (ch != UNKNOWN && ch != word.charAt(c))
            {
                return StrExistsAs.NONE;
            }
        }
        return (str.length() == word.length()) ? StrExistsAs.WORD : StrExistsAs.PREFIX;
    }

	// pattern    - Substring to search for
    // .    for any character
    // A..Z for that specific character
    // 0..9 when assigned a character, assigns to all locations of same #
    public String[] searchForWords(String myStylePattern)
    {
        RegexPatternRule rule = new RegexPatternRule(myStylePattern);
        try
        {
            return collectThroughWords(rule);
        }
        catch (IOException e)
        {
            return null; // fail
        }
    }

    public String[] searchForWords(Pattern pattern)
    {
        RegexPatternRule rule = new RegexPatternRule(pattern);
        try
        {
            return collectThroughWords(rule);
        }
        catch (IOException e)
        {
            return null; // fail
        }
    }

    public String[] collectThroughWords(RuleForMatching rule) throws IOException
    {
        ArrayList<String> matchedList = new ArrayList<String>();
		Iterator<String> wlIter = iterator();
        String word;

        while (wlIter.hasNext())
        {
            word = wlIter.next();
            if (rule.IsAMatch(word))
            {
                matchedList.add(word);
            }
        }
        return matchedList.toArray(new String[0]);
    }

    public int countThroughWords(RuleForMatching rule) throws IOException
    {
        int count = 0;
		Iterator<String> wlIter = iterator();
        String word;
        while (wlIter.hasNext())
        {
            word = wlIter.next();
            if (rule.IsAMatch(word))
            {
                ++count;
            }
        }
        return count;
    }

    public void printThroughWords(RuleForMatching rule) throws IOException
    {
		Iterator<String> wlIter = iterator();
        String word;
        while (wlIter.hasNext())
        {
            word = wlIter.next();
            if (rule.IsAMatch(word))
            {
                System.out.println(word);
            }
        }
    }

    public void printToFileThroughWords(RuleForMatching rule, File writeToFile) throws IOException
    {
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(writeToFile), "UTF-8"));
		Iterator<String> wlIter = iterator();
        String word;
        while (wlIter.hasNext())
        {
            word = wlIter.next();
            if (rule.IsAMatch(word))
            {
                writer.write(word);
                writer.write('\n');
            }
        }
        writer.close();
    }
	
	public StrExistsAs searchAsValidSentence(String sentence)
    {
        String[] words = sentence.split(" ");
        for (int i = 0; i < words.length - 1; ++i)
        {
            StrExistsAs sea = searchPrefix(words[i]);
            if (!(sea.isWord() || sea.isPrefix()))
            {
                return StrExistsAs.NONE;
            }
        }

        // The last word might be incomplete, if previous are all words, return based on last word
        return searchPrefix(words[words.length - 1]);
    }
}