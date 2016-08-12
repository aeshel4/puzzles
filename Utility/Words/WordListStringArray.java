package Utility.Words;

import java.util.Iterator;
import java.util.ArrayList;

public class WordListStringArray extends GenericWordList
{
    private ArrayList<String> words;

	public WordListStringArray(String[] strWords)
	{
		words = new ArrayList<String>(strWords.length);
		for (int i = 0; i < strWords.length; ++i)
		{
			words.add(strWords[i].toUpperCase());
		}
	}

    public WordListStringArray(ArrayList<String> words)
    {
        this.words = new ArrayList<String>(words.size());
        for (int i = 0; i < words.size(); ++i)
        {
            this.words.add(words.get(i).toUpperCase());
        }
    }

	public Iterator<String> iterator()
	{
		return words.iterator();
	}

    public StrExistsAs searchPrefix(String pre)
    {
        pre = pre.toUpperCase();
        boolean asWrd = false;
        boolean asPre = false;
        for (int i = 0; (i < words.size()) && !(asWrd && asPre); ++i)
        {
            StrExistsAs sea = super.matchesByUnknowns(pre, words.get(i));
            asWrd |= sea.isWord();
            asPre |= sea.isPrefix();
        }

        return StrExistsAs.Construct(asPre, asWrd, false);
    }
}