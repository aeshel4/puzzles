package Utility.Words;

import java.io.*;
import java.util.Iterator;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;

public class WordList extends GenericWordList
{
    private RandomAccessFile randFile;
    private File file;

    public WordList()
    {
        this(true);
    }
    public WordList(boolean CheckForValidity)
    {
        try
        {
            WordListConstructor("wordlists\\enable.txt", CheckForValidity);
        }
        catch (FileNotFoundException e)
        {
            throw new RuntimeException("The default word list is named enable.txt");
        }
    }
    public WordList(String filename) throws FileNotFoundException
    {
        this(filename, true);
    }
    public WordList(String filename, boolean CheckForValidity) throws FileNotFoundException
    {
        WordListConstructor(filename, CheckForValidity);
    }
    public WordList(File file) throws FileNotFoundException
    {
        this(file, true);
    }
    public WordList(File file, boolean CheckForValidity) throws FileNotFoundException
    {
        this(file.toString(), CheckForValidity);
    }
	public WordList(int LengthOfWords) throws FileNotFoundException
	{
		this("wordlists\\enable_e_" + LengthOfWords + ".txt");
	}
	
    private void WordListConstructor(String filename, boolean CheckForValidity) throws FileNotFoundException
    {
        if (CheckForValidity)
        {
            CheckIsValidWordList(filename);
        }

        randFile = new RandomAccessFile(filename, "r");
        file = new File(filename);
    }

    public StrExistsAs searchPrefix(String pre)
    {
        if (pre.isEmpty())
        {
            return StrExistsAs.PREFIX;
        }

        pre = pre.toUpperCase();
        int unknownLocation = pre.indexOf(super.UNKNOWN);
        if (unknownLocation == 0)
        {
            return searchPrefixWithInitialUnknown(pre);
        }
        else if (unknownLocation == -1)
        {
            return searchPrefixNoUnknowns(pre);
        }
        else
        {
            return searchPrefixWithKnownFirstButUnknowns(pre);
        }
    }

    private StrExistsAs searchPrefixWithKnownFirstButUnknowns(String pre)
    {
        String firstLetter = String.valueOf(pre.charAt(0));
        try
        {
            long front = 0;
            long back = randFile.length();

            long mid = GetPointerToMidWord(front, back);
            String word = GetWordAtPointer(mid);

            do
            {
                int compare = word.compareTo(firstLetter);
                if (compare < 0)
                {
                    front = mid;
                }
                else if (compare > 0)
                {
                    back = mid;
                }
                else // compare == 0
                {
                    front = mid;
                    back = mid;
                }
                mid = GetPointerToMidWord(front, back);
                word = GetWordAtPointer(mid);
            } while (back - front > 50);

            word = GetWordAtPointer(front);

            while (!word.startsWith(firstLetter))
            {
                front = MovePointerToStartOfWord(front + 1);
                word = GetWordAtPointer(front);
            }

            boolean asWrd = false;
            boolean asPre = false;
            while (!(asWrd && asPre))
            {
                front = MovePointerToStartOfWord(front + 1);
                word = GetWordAtPointer(front);
                if (!word.startsWith(firstLetter))
                {
                    break;
                }

                StrExistsAs sea = super.matchesByUnknowns(pre, word);
                asWrd |= sea.isWord();
                asPre |= sea.isPrefix();
            }

            return StrExistsAs.Construct(asPre, asWrd, false);
        }
        catch (IOException e)
        {
            return StrExistsAs.FAIL;
        }   
    }

    private StrExistsAs searchPrefixWithInitialUnknown(String pre)
    {
        boolean asWrd = false;
        boolean asPre = false;
        Iterator<String> iter = iterator();
        while ((iter.hasNext()) && !(asWrd && asPre))
        {
            StrExistsAs sea = super.matchesByUnknowns(pre, iter.next());
            asWrd |= sea.isWord();
            asPre |= sea.isPrefix();
        }

        return StrExistsAs.Construct(asPre, asWrd, false);
    }

    private StrExistsAs searchPrefixNoUnknowns(String pre)
    {
        try
        {
            long front = 0;
            long back = randFile.length();

            long mid = GetPointerToMidWord(front, back);
            String word = GetWordAtPointer(mid);

            do
            {
                int compare = word.compareTo(pre);
                if (compare < 0)
                {
                    front = mid;
                }
                else if (compare > 0)
                {
                    back = mid;
                }
                else // compare == 0
                {
                    front = mid;
                    back = mid;
                }
                mid = GetPointerToMidWord(front, back);
                word = GetWordAtPointer(mid);
            } while (back - front > 50);

            word = GetWordAtPointer(front);

            while (word.compareTo(pre) < 0)
            {
                front = MovePointerToStartOfWord(front + 1);
                word = GetWordAtPointer(front);
				if (word == null)
				{
					return StrExistsAs.NONE;
				}
            }

            if (word.equals(pre))
            {
                try
                {
                    if (GetWordAtPointer(MovePointerToStartOfWord(front + 1)).startsWith(pre))
                    {
                        return StrExistsAs.BOTH;
                    }
                    else
                    {
                        return StrExistsAs.WORD;
                    }
                }
                catch (IOException|NullPointerException e)
                {
                    return StrExistsAs.WORD;
                }
            }
            else if (word.startsWith(pre))
            {
                return StrExistsAs.PREFIX;
            }
            else
            {
                return StrExistsAs.NONE;
            }
        }
        catch (IOException e)
        {
            return StrExistsAs.FAIL;
        }
    }

	public WordListIter iterator()
	{
		try
		{
			return new WordListIter(file);
		}
		catch (FileNotFoundException e)
		{
			System.out.println("WordList in a bad state, since iter couldn't open file: " + file);
			return null;
		}
	}

    public String toString()
    {
        return file.toString();
    }

    private long GetPointerToMidWord(long front, long back) throws IOException
    {
        return MovePointerToStartOfWord((front + back)/2);
    }

    private long MovePointerToStartOfWord(long pointer) throws IOException
    {
        if (pointer == 0) {return 0;}

        randFile.seek(pointer - 1);
        while (randFile.readUnsignedByte() != (int)'\n') {;}
        return randFile.getFilePointer();
    }

    private String GetWordAtPointer(long pointer) throws IOException
    {
        randFile.seek(pointer);
        return randFile.readLine();
    }

/************************/
/* Static methods below */
/************************/

    public static void CheckIsValidWordList(String filename) throws FileNotFoundException
    {
        Scanner sc = new Scanner(new File(filename));
        String str = "", lastStr;
        while (sc.hasNextLine())
        {
            lastStr = str;
            str = sc.nextLine();
            if (!str.equals(str.toUpperCase()))
            {
                throw new RuntimeException("Words must be in all uppercase: " + str);
            }
            if (str.compareTo(lastStr) < 0)
            {
                throw new RuntimeException("Word List file must be sorted: " + str + " " + lastStr);
            }
            if (str.indexOf(' ') != -1)
            {
                throw new RuntimeException("Words must not contain any spaces:" + str);
            }
        }
        sc.close();
    }

    public static void MakeIntoValidWordList(String filename, boolean deleteBackupFilesWhenDone)
       throws FileNotFoundException, UnsupportedEncodingException, IOException
    {
        File oldFile = new File(filename + "_original");
        File origFile = new File(filename);
        if (!origFile.renameTo(oldFile))
        {
            throw new RuntimeException("Could not rename " + origFile + " to " + oldFile);
        }

        Scanner sc = new Scanner(oldFile);
        File tempNewFile = new File(filename + "_temp");
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(tempNewFile), "UTF-8"));
        String str;
        String[] strs;

        while (sc.hasNextLine())
        {
            str = sc.nextLine();
            str = str.toUpperCase(); // make everything uppercase
            strs = str.split(" "); // if a line has a space, turn it into multiple words
            for (int i = 0; i < strs.length; ++i)
            {
                writer.write(strs[i]);
                writer.write('\n');
            }
        }

        writer.close();
        sc.close();

        // still need to sort the list
        ArrayList<String> list = new ArrayList<String>();
        Scanner sc2 = new Scanner(tempNewFile);
        while (sc2.hasNextLine())
        {
            str = sc2.nextLine();
            list.add(str);
        }
        Collections.sort(list);

        File newFile = new File(filename);
        writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(newFile), "UTF-8"));

        String word = list.get(0);
        String prevWord;
        writer.write(word);
        for (int i = 1; i < list.size(); ++i)
        {
            prevWord = word;
            word = list.get(i);
            if (!word.equals(prevWord))
            {
                writer.write('\n');
                writer.write(word);
            }
        }
        writer.close();
        sc2.close();
        tempNewFile.delete();
        if (deleteBackupFilesWhenDone)
        {
            oldFile.delete();
        }
    }

    public static void main(String[] args) throws Exception
    {
        if (args.length >= 2 &&
            args[0].equals("-words"))
        {
            WordList words = new WordList();
            for (int i = 1; i < args.length; ++i)
            {
                System.out.println(args[i]);
                RegexPatternRule rule = new RegexPatternRule(args[i]);
                words.printThroughWords(rule);
                System.out.println();
            }
        }
        else if (args.length != 1 ||
                 args[0].equals("/?") ||
                 args[0].equals("-?") ||
                 args[0].equals("help"))
        {
            System.out.println("Syntax: WordList -words {String}");
            System.out.println("  Will print all strings that fit the pattern");
            System.out.println("  Multiple space separated strings can be passed: syntax");
            System.out.println("  .    for a character");
            System.out.println("  A..Z for that specific character");
            System.out.println("  0..9 when assigned a character, assigns to all locations of same #");
            System.out.println("\nOR\n");
            System.out.println("Syntax: WordList [filename]");
            System.out.println("  Running this will convert the word list stored in filename to the");
            System.out.println("  format expected by the WordList class. The old filename will be");
            System.out.println("  changed to have a \"_original\" as a suffix");
        }
        else
        {
            MakeIntoValidWordList(args[0], false);
        }
    }
}