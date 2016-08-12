package Tools.arches;

import java.util.*;
import java.io.*;
import java.time.*;
import Utility.Words.*;
import Utility.Grids.*;

public class ArchesCLI
{
	private Scanner input;

	private GenericWordList wordList;
	private GridTessalation grid;
	private ArchesSearchParams searchParams;

	private ArrayList<ArchesWord> words;
	private ArchesPrinter printer;

	public static void main(String[] args)
	{
		String[] wordsToFind4D = {
			"ABET", "FOUR", "NOTE", "TRUE",
			"BEAR", "GOOD", "PAYS", "UBER",
			"BOND", "IDOL", "POET", "USED",
			"BORN", "JETS", "STUB", "WARP",
			"BUYS", "KIND", "STYX", "WISE",
			"CELL", "LEAD", "SWIM", "WORD",
			"FALL", "LION", "TOLD", "WORK",
			"FONT", "LUCK", "TOUR", "ZONE"
		};

		String[][][][] values4D = {
			{
				{{"B", "G", "W", "T"},
				{"O", "D", "O", "D"},
				{"J", "O", "L", "B"},
				{"Z", "S", "S", "F"}},

				{{"T", "L", "E", "T"},
				{"U", "T", "E", "H"},
				{"N", "B", "L", "O"},
				{"A", "B", "X", "C"}},

				{{"T", "W", "Y", "N"},
				{"O", "U", "O", "S"},
				{"H", "T", "A", "O"},
				{"E", "U", "U", "K"}},

				{{"L", "L", "D", "I"},
				{"D", "U", "S", "N"},
				{"E", "T", "F", "H"},
				{"G", "D", "E", "B"}}
			},

			{
				{{"N", "U", "M", "P"},
				{"B", "N", "A", "E"},
				{"R", "O", "O", "F"},
				{"D", "D", "O", "I"}},

				{{"M", "E", "O", "N"},
				{"R", "O", "N", "S"},
				{"I", "N", "Y", "E"},
				{"O", "U", "W", "N"}},

				{{"O", "E", "S", "T"},
				{"E", "U", "O", "R"},
				{"A", "B", "U", "V"},
				{"E", "L", "R", "E"}},

				{{"T", "D", "D", "B"},
				{"T", "H", "O", "R"},
				{"O", "O", "U", "G"},
				{"H", "L", "A", "S"}}
			},

			{
				{{"A", "N", "I", "P"},
				{"N", "I", "N", "D"},
				{"E", "X", "R", "W"},
				{"J", "U", "I", "N"}},

				{{"T", "O", "A", "E"},
				{"S", "T", "T", "L"},
				{"U", "E", "C", "I"},
				{"A", "Y", "C", "H"}},

				{{"U", "Y", "W", "O"},
				{"R", "E", "T", "D"},
				{"A", "O", "R", "S"},
				{"N", "O", "I", "D"}},

				{{"S", "O", "T", "S"},
				{"H", "I", "O", "E"},
				{"N", "S", "O", "E"},
				{"R", "O", "T", "A"}}
			},

			{
				{{"S", "L", "P", "E"},
				{"U", "K", "H", "A"},
				{"B", "D", "E", "T"},
				{"R", "I", "P", "K"}},

				{{"L", "C", "S", "L"},
				{"A", "E", "L", "L"},
				{"Y", "R", "A", "B"},
				{"E", "S", "Y", "D"}},

				{{"R", "R", "B", "T"},
				{"H", "E", "E", "W"},
				{"O", "O", "A", "R"},
				{"W", "D", "R", "I"}},

				{{"L", "N", "R", "D"},
				{"L", "E", "V", "E"},
				{"R", "W", "S", "E"},
				{"F", "T", "M", "N"}}
			}
		};

		ArchesCLI arches = new ArchesCLI();
		if (args.length > 0)
		{
			if (args[0].equalsIgnoreCase("4"))
			{
				arches.searchParams.style = ARCHES_SEARCH_STYLE.CLASSIC;
				arches.wordList = new WordListStringArray(wordsToFind4D);
				arches.grid = new NDimensionalGrid(values4D);
			}
			else
			{
				System.out.println("Passing in one of: b k h 4, is used to test by providing initial grids");
				System.out.println("Boggle (snakes), Knight (arena), Hex, 4d");
			}
			arches.start();
		}
		arches.menuMain();
	}

	public ArchesCLI()
	{
		input = new Scanner(System.in);
		searchParams = new ArchesSearchParams();
	}

	private int getNumber(int lBound, int uBound)
	{
		int choice;
		do
		{
			try
			{
				String line = input.nextLine();
				choice = Integer.parseInt(line);
				if (choice >= lBound && choice <= uBound)
				{
					break;
				}
			}
			catch (NumberFormatException e)
			{
				// Do nothing, since need a correct number to break out of loop anyway
			}
			System.out.print("Invalid choice, please try again: ");
		} while (true);

		return choice;
	}

    private boolean getYesNo(String displayStr)
    {
        while (true)
        {
            System.out.print(displayStr + " [Y/N]? ");
            String strChoice = input.nextLine();
            strChoice = strChoice.toUpperCase();
            if (strChoice.equals("Y") || strChoice.equals("YES"))
            {
                return true;
            }
            else if (strChoice.equals("N") || strChoice.equals("NO"))
            {
                return false;
            }
        }
    }

	public void menuMain()
	{
		do
		{
			System.out.println("\n***** ARCHES MAIN MENU *****");
			System.out.println("1 - Setup menu");
			System.out.println("2 - Find words");
			System.out.println("3 - Change words found");
			System.out.println("4 - Print menu");
			System.out.println("0 - Quit");
			System.out.print(" Choice: ");
			int choice = getNumber(0, 4);
			switch (choice)
			{
				case 1: menuSetup(); break;
				case 2: start(); break;
				case 3: menuModifyWords(); break;
				case 4: menuPrinting(); break;
				case 0:
					if (getYesNo("Are you sure"))
					{
						return;
					}
					else
					{
						break;
					}
				default: throw new RuntimeException("Shouldn't have been able to select that.");
			}
		} while (true);
	}

	private void menuModifyWords()
	{
		do
		{
			System.out.println("\n***** ARCHES MODIFY WORDS FOUND MENU *****");
			System.out.println("1 - All at once");
			System.out.println("2 - One at a time");
			System.out.println("3 - Auto eliminate overlaps: keep all words.");
			System.out.println("4 - Auto eliminate overlaps: keep all points.");
			System.out.println("0 - Back to main menu");
			System.out.print(" Choice: ");
			int choice = getNumber(0, 4);
			switch (choice)
			{
				case 1: menuModifyWordsFoundAllAtOnce(); break;
				case 2: menuModifyWordsFoundIndividually(); break;
				case 3: // fall through
				case 4: modifyWordsFoundAutomaticallyEliminateOverlaps(choice == 3); break;
				case 0: return;
			}
		} while (true);
	}

	private void modifyWordsFoundAutomaticallyEliminateOverlaps(boolean keepAllWords)
	{
		if (null == words)
		{
			System.out.println("Need to find words first");
			return;
		}

		ArchesOverlapEliminator eliminator;
		if (keepAllWords)
		{
			WordListStringArray providedWords = (wordList instanceof WordListStringArray) ? (WordListStringArray)wordList : null;
			eliminator = new ArchesOverlapEliminatorGivenWordList(words, providedWords, searchParams.letterOff);
		}
		else
		{
			eliminator = new ArchesOverlapEliminatorAllPoints(words, grid);
		}
		eliminator.eliminate();
	}

	private void menuModifyWordsFoundAllAtOnce()
	{
		if (null == words)
		{
			System.out.println("Need to find words first");
			return;
		}

		System.out.println("1 - Modify words one at a time");
		System.out.println("2 - Mark all valid");
		System.out.println("3 - Mark all invalid");
		System.out.print(" Choice: ");
		int choice = getNumber(1, 3);
		if (choice == 1)
		{
			Collections.sort(words, new ArchesWordComparatorAlphabetical(true));
			for (int w = 0; w < words.size(); ++w)
			{
				ArchesWord currentWord = words.get(w);
				System.out.println(currentWord);
				boolean keep = getYesNo(" Keep?");
				currentWord.setValidity(keep);
			}
		}
		else
		{
			for (int w = 0; w < words.size(); ++w)
			{
				ArchesWord currentWord = words.get(w);
				currentWord.setValidity(choice == 2);
			}
		}
		//AE print valid words separate from invalid words now
	}

	private void menuModifyWordsFoundIndividually()
	{
		do
		{
			System.out.println(printer.wordsOrderedAlphabetically(true));
			System.out.println("0 - Back to main menu");
			System.out.print(" Toggle: ");
			int choice = getNumber(0, words.size());
			if (choice == 0)
			{
				break;
			}
			ArchesWord word = words.get(choice - 1);
			word.setValidity(!word.isValid());
		} while(true);
	}

	private String menuPrintingGenericRepeats()
	{
		boolean exact = getYesNo("Should the repeats be == ? (Otherwise: >=) ");
		System.out.print("How many repeats? ");
		int repeats = getNumber(0, 100);
		return printer.repeats(repeats, exact);
	}

	private void menuPrintingWordsFoundOneAtATime()
	{
		do
		{
			System.out.println(printer.wordsOrderedAlphabetically(true));
			System.out.println("0 - Back to print menu");
			System.out.print(" Which word: ");
			int choice = getNumber(0, words.size());
			if (choice == 0)
			{
				break;
			}
			ArchesWord word = words.get(choice - 1);
			System.out.println(printer.singleWord(word));
		} while(true);
	}

	private String menuWordsWithPoint(int choice)
	{
		GridPoint p = grid.iterator().next().promptForPoint(input);
		switch (choice)
		{
			case 6: return printer.wordsStartingAtPoint(p, false);
			case 7: return printer.wordsContainingPoint(p, false);
			case 8: return printer.wordsEndingAtPoint(p, false);
			default: throw new RuntimeException("Programmer error");
		}
	}

	private String menuPrintingWordsFound()
	{
		System.out.println("\n***** ARCHES PRINT WORDS FOUND MENU *****");
		System.out.println("1 - Grid order");
		System.out.println("2 - Alphabetical order");
		System.out.println("3 - Length order");
		System.out.println("4 - Multiple times");
		System.out.println("5 - One at a time on grid");
		System.out.println("6 - Starting at a point");
		System.out.println("7 - Containing a point");
		System.out.println("8 - Ending at a point");
		System.out.println("0 - Back to print menu");
		System.out.print(" Choice: ");
		int choice = getNumber(0, 8);
		String str = "";
		try
		{
			switch (choice)
			{
				case 1: str = printer.wordsOrderedByGrid(false); break;
				case 2: str = printer.wordsOrderedAlphabetically(false); break;
				case 3: str = printer.wordsOrderedByLength(false); break;
				case 4: str = printer.wordsFoundMultipleTimes(false); break;
				case 5: menuPrintingWordsFoundOneAtATime(); // fall through
				case 0: str = ""; break;
				case 6: // fall through
				case 7: // fall through
				case 8: str = menuWordsWithPoint(choice); break;
				default: throw new RuntimeException("Shouldn't have been able to select that.");
			}
		}
		catch (NullPointerException e)
		{
			System.out.println("Need to find words first.");
		}

		return str;
	}

	private void menuPrinting()
	{
		do
		{
			System.out.println("\n***** ARCHES PRINT MENU *****");
			System.out.println("1 - Original grid");
			System.out.println("2 - Words found");
			System.out.println("3 - Letters used");
			System.out.println("4 - Leftover letters on the grid");
			System.out.println("5 - Leftover letters in 'order'");
			System.out.println("6 - Letters used multiple times");
			System.out.println("7 - Generic repeated letters");
			System.out.println("0 - Back to main menu");
			System.out.print(" Choice: ");
			int choice = getNumber(0, 7);
			String str;
			try
			{
				switch (choice)
				{
					case 1: str = grid.toString(); break;
					case 2: str = menuPrintingWordsFound(); break;
					case 3: str = printer.foundOnGrid(); break;
					case 4: str = printer.leftoversOnGrid(); break;
					case 5: str = printer.leftoversDirect(); break;
					case 6: str = printer.repeats(); break;
					case 7: str = menuPrintingGenericRepeats(); break;
					case 0: return;
					default: throw new RuntimeException("Shouldn't have been able to select that.");
				}
				System.out.println(str);
			}
			catch (NullPointerException e)
			{
				System.out.println("Need to find words first.");
			}
		} while (true);
	}

	private void menuWordList()
	{
		System.out.println("\n***** ARCHES WORD LIST MENU *****");
		System.out.println("1 - Provide words");
		System.out.println("2 - Specify a dictionary");
		System.out.println("3 - Use default dictionary");
		System.out.print(" Choice: ");
		int choice = getNumber(1, 3);
		switch (choice)
		{
			case 1: provideWordList(); break;
			case 2: provideDictionary(); break;
			case 3: useDefaultDictionary(); break;
			default: throw new RuntimeException("Shouldn't have been able to select that.");
		}
	}

	private void menuSearchStyle()
	{
		System.out.println("\n***** ARCHES SEARCH STYLE MENU *****");
		System.out.println("1 - Classic");
		System.out.println("2 - Boggle");
		System.out.println("3 - Knight");
		System.out.println("4 - Jump");
		System.out.print(" Choice: ");
		int choice = getNumber(1, 4);
		switch (choice)
		{
			case 1: searchParams.style = ARCHES_SEARCH_STYLE.CLASSIC; break;
			case 2: searchParams.style = ARCHES_SEARCH_STYLE.BOGGLE; break;
			case 3: searchParams.style = ARCHES_SEARCH_STYLE.KNIGHT; break;
			case 4: searchParams.style = ARCHES_SEARCH_STYLE.JUMP; break;
			default: throw new RuntimeException("Shouldn't have been able to select that.");
		}
	}

	private void menuSearchParams()
	{
		do
		{
			System.out.println("\n***** ARCHES SEARCH PARAMETERS MENU *****");
			System.out.println("1 - Change search style (" + searchParams.style + ")");
			System.out.println("2 - Toggle " + ((grid.type() == TessalationType.HEXAGONAL) ? "straight-lines" : "cardinal") + " only (" + searchParams.cardinalOnly + ")");
			System.out.println("3 - Toggle wrap around grid (" + searchParams.wrapAround + ")");
			System.out.println("4 - Toggle reuse letters within single word (" + searchParams.reuse + ")");
			System.out.println("5 - Toggle allowing words to have one letter wrong (" + searchParams.letterOff + ")");
			System.out.println("0 - Back to setup menu");
			System.out.print(" Choice: ");
			int choice = getNumber(0, 5);
			switch (choice)
			{
				case 1: menuSearchStyle(); break;
				case 2: searchParams.cardinalOnly ^= true; break;
				case 3: searchParams.wrapAround ^= true; break;
				case 4: searchParams.reuse ^= true; break;
				case 5: searchParams.letterOff ^= true; break;
				case 0: return;
				default: throw new RuntimeException("Shouldn't have been able to select that.");
			}
		} while (true);
	}

	private void menuSetup()
	{
		do
		{
			System.out.println("\n***** ARCHES SETUP MENU *****");
			System.out.println("1 - " + ((null == grid) ? "Set" : "Change") + " grid");
			System.out.println("2 - " + ((null == wordList) ? "Set" : "Change") + " word list");
			System.out.println("3 - Change search parameters");
			System.out.println("0 - Back to main menu");
			System.out.print(" Choice: ");
			int choice = getNumber(0, 3);
			switch (choice)
			{
				case 1: provideGrid(); break;
				case 2: menuWordList(); break;
				case 3: menuSearchParams(); break;
				case 0: return;
				default: throw new RuntimeException("Shouldn't have been able to select that.");
			}
		} while (true);
	}

	private void provideWordList()
	{
		ArrayList<String> words = new ArrayList<String>();
		String line;
		System.out.println("Enter words separated by new lines");
		do
		{
			line = input.nextLine();
			words.add(line.toUpperCase());
		} while (!line.isEmpty());
		wordList = new WordListStringArray(words);
	}

	private void useDefaultDictionary()
	{
		try
		{
			wordList = new WordList("wordlists\\enable_ge_4.txt");
		}
		catch (FileNotFoundException e)
		{
			System.out.println("Couldn't find the default wordlist: " + e.getMessage());
		}
	}

	private void provideDictionary()
	{
		System.out.print("What is the file name of the dictionary: ");
		do
		{
			String filename = input.nextLine();
			try
			{
				wordList = new WordList(filename);
				break;
			}
			catch (FileNotFoundException e)
			{
				System.out.print("Invalid file name, please try again: ");
			}
		} while (true);
	}

	private void provideGrid()
	{
		Scanner gridScanner;
		System.out.print("What is the file name of the grid: ");
		do
		{
			String filename = input.nextLine();
			try
			{
				gridScanner = new Scanner(new File(filename));
				break;
			}
			catch (FileNotFoundException e)
			{
				System.out.print("Invalid file name, please try again: ");
			}
		} while (true);

		TessalationType tessType;
		System.out.println("\nGrid Type");
		System.out.println("1 - Rectangular");
		System.out.println("2 - Hexagonal");
		System.out.println("3 - Triangular");
		System.out.print(" Choice: ");
		int choice = getNumber(1, 3);
		switch (choice)
		{
			case 1: tessType = TessalationType.RECTANGULAR; break;
			case 2: tessType = TessalationType.HEXAGONAL; break;
			case 3: tessType = TessalationType.TRIANGULAR; break;
			default: throw new RuntimeException("Shouldn't have been able to select that.");
		}

		int dim = 2;
		if (tessType == TessalationType.RECTANGULAR)
		{
			System.out.print("What is the dimension of this grid: ");
			dim = getNumber(1, 8);
		}

		String[] delimiters = new String[dim];
		System.out.println(" Just hit enter without typing anything if grid entries aren't separated. \\n, \\t, etc. can also be used");
		for (int i = 0; i < dim; ++i)
		{
			System.out.print("What is the delimiter for dimension " + i + ": ");
			delimiters[i] = input.nextLine();
		}

		if (tessType == TessalationType.RECTANGULAR)
		{
			grid = new NDimensionalGrid(gridScanner, delimiters);			
		}
		else if (tessType == TessalationType.HEXAGONAL)
		{
			boolean north_south = getYesNo("Does this grid run north/south");
			System.out.print("Which character should be skipped?  ");
			String skipChar = input.nextLine();
			grid = new HexGrid2D(gridScanner, delimiters, north_south, skipChar);
		}
		else if (tessType == TessalationType.TRIANGULAR)
		{
			throw new RuntimeException("Don't support triangular grids yet");
//AE		grid = new TriangularGrid?
		}
		System.out.println(grid);
	}

	private void start()
	{
		try
		{
			ArchesSolver solver = new ArchesSolver(this.searchParams, this.grid, this.wordList);
			this.words = new ArrayList<ArchesWord>(solver.solve()); // Make a copy so can modify
			this.printer = new ArchesPrinter(this.grid, this.words, this.searchParams);
			System.out.println("Solve time taken: " + solver.getSolveTime());
		}
		catch (NullPointerException e)
		{
			System.out.println("Caught a null pointer exception, did you provide a grid and word list?");
		}
	}
}