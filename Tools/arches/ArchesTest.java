package Tools.arches;

import java.io.*;
import java.util.*;
import java.time.*;
import Utility.Grids.*;
import Utility.Words.*;

public class ArchesTest
{
	private final static String testFileSeparator = "!@#";

	public static void main(String[] args) throws FileNotFoundException
	{
		if ((args.length != 1 && args.length != 2) || args[0].contains("?"))
		{
			System.out.println("Must pass a directory where test files can be found or a specific test file.");
			System.out.println("If a second parameter exists, the intended output will be printed.");
			return;
		}

		File passedFile = new File(args[0]);
		File[] testFiles;
		if (passedFile.isDirectory())
		{
			testFiles = passedFile.listFiles(new ArchesTestFileFilter());
		}
		else
		{
			testFiles = new File[1];
			testFiles[0] = passedFile;
		}

		for (int i = 0; i < testFiles.length; ++i)
		{
			try
			{
				ArchesTest test = new ArchesTest(testFiles[i], args.length == 2);
				System.out.println(test.toString() + "\n");
			}
			catch (Exception e)
			{
				System.out.println("FAILED: " + testFiles[i].getName() + "\nCaught exception: ");
				e.printStackTrace();
			}
		}
	}

	private File file;
	private boolean succeeded;
	private GridTessalation grid;
	private GenericWordList words;
	private ArchesSearchParams params;
	private ArchesSolver solver;
	private String failureString;
	private String eliminateOverlap;
	private boolean justProvideSolution;

	private Scanner outputFileScanner;

	private ArchesTest(File file, boolean justProvideSolution) throws FileNotFoundException
	{
		this.file = file;
		this.justProvideSolution = justProvideSolution;
		failureString = "Not validated.";
		succeeded = false;
		params = new ArchesSearchParams();
		parseFile();
		solver = new ArchesSolver(params, grid, words);
		ArrayList<ArchesWord> foundWords = solver.solve();
		if (!eliminateOverlap.equalsIgnoreCase("false"))
		{
			ArchesOverlapEliminator eliminator;
			if (eliminateOverlap.equalsIgnoreCase("points"))
			{
				eliminator = new ArchesOverlapEliminatorAllPoints(foundWords, grid);
			}
			else
			{
				WordListStringArray providedWords = (words instanceof WordListStringArray) ? (WordListStringArray)words : null;
				eliminator = new ArchesOverlapEliminatorGivenWordList(foundWords, providedWords, params.letterOff);
			}
			eliminator.eliminate();
		}
		if (!justProvideSolution)
		{
			validate();
		}
	}

	private void parseFile() throws FileNotFoundException
	{
		Scanner sc = new Scanner(file);

		String gridFilename = sc.nextLine();
		Scanner gridScanner = new Scanner(new File(file.getParentFile(), gridFilename));
		TessalationType gridType = TessalationType.getFromString(sc.nextLine());

		int numDimensions = Integer.parseInt(sc.nextLine());
		String[] delims = new String[numDimensions];
		for (int d = 0; d < numDimensions; ++d)
		{
			delims[d] = sc.nextLine();
		}

		switch (gridType)
		{
			case TRIANGULAR: setFailureString("Don't support triangular grids.", false); break;
			case RECTANGULAR: grid = new NDimensionalGrid(gridScanner, delims); break;
			case HEXAGONAL: grid = new HexGrid2D(gridScanner, delims, Boolean.valueOf(sc.nextLine()), sc.nextLine()); break;
		}

		params.style = ARCHES_SEARCH_STYLE.getFromString(sc.nextLine());
		params.wrapAround = Boolean.valueOf(sc.nextLine());
		params.cardinalOnly = Boolean.valueOf(sc.nextLine());
		params.reuse = Boolean.valueOf(sc.nextLine());
		params.letterOff = Boolean.valueOf(sc.nextLine());
		eliminateOverlap = sc.nextLine();

		outputFileScanner = new Scanner(new File(file.getParentFile(), sc.nextLine()));

		String wordListFileName = sc.nextLine();
		File wordListFile = new File(file.getParentFile(), wordListFileName);
		if (wordListFile.isFile())
		{
			words = new WordList(wordListFile);
			if (sc.hasNextLine())
			{
				throw new RuntimeException(file.getName() + " has more things after the word list!");
			}
		}
		else
		{
			ArrayList<String> providedWords = new ArrayList<String>();
			// Since this wasn't a file, it's the first word in the word list
			providedWords.add(wordListFileName);
			while (sc.hasNextLine())
			{
				providedWords.add(sc.nextLine());
			}
			words = new WordListStringArray(providedWords);
		}
	}

	private void setFailureString(String str, boolean addTo)
	{
//System.out.println(str);
		if (!addTo)
		{
			failureString = "";
		}
		failureString += str;
		succeeded = false;
	}

	private void validate()
	{
//System.out.println(params);
//System.out.println(words);
		setFailureString("", false);
		succeeded = true;

		Scanner solverScanner = new Scanner(solver.toString());
		while (solverScanner.hasNextLine() && outputFileScanner.hasNextLine())
		{
			String solveLine = solverScanner.nextLine();
			String outputLine = outputFileScanner.nextLine();
			if (!solveLine.equals(outputLine))
			{
				setFailureString(solveLine + "###" + outputLine + "###\n", true);
			}
		}

		if (solverScanner.hasNextLine())
		{
			setFailureString(solverScanner.nextLine() + " was unaccounted for.", true);
		}
		if (outputFileScanner.hasNextLine())
		{
			setFailureString(outputFileScanner.nextLine() + " was extra.", true);
		}
	}

	public String toString()
	{
		if (justProvideSolution)
		{
			return solver.toString();
		}

		String str = "";
		str += succeeded ? "Succeeded:\t" : "FAILED:\t";
		str += solver.getSolveTime().toString() + "\t";
		str += file.getName();
		if (!succeeded)
		{
			str += "\n" + failureString;
		}
		return str;
	}
}

class ArchesTestFileFilter implements FileFilter
{
	private final static String testSuffix = "archtest";
	private final static String examplePrefix = "EXAMPLE";
	public boolean accept(File pathname)
	{
		return pathname.isFile() &&
			   pathname.getName().endsWith(testSuffix) &&
			   !pathname.getName().equals(examplePrefix + "." + testSuffix);
	}
}