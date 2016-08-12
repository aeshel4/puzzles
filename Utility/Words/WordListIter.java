package Utility.Words;

import java.io.*;
import java.util.Scanner;
import java.util.Iterator;

public class WordListIter implements Iterator<String>
{
	private Scanner scanner;

	public WordListIter(File file) throws FileNotFoundException
	{
		scanner = new Scanner(file);
	}

	public boolean hasNext()
	{
		boolean retValue = scanner.hasNextLine();
		if (!retValue)
		{
			scanner.close();
		}
		return retValue;
	}

	public String next()
	{
		return scanner.nextLine();
	}
}

