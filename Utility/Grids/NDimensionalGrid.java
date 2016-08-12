package Utility.Grids;

import java.util.Scanner;
import java.io.*;

import java.util.Iterator;
import Utility.Utility;

public class NDimensionalGrid extends GridTessalation
{
	public static void main(String[] args)
	{
		int[] dimSize = {2, 6, 7};
		int[] centerPoint = {Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2])};
		NDimensionalPoint center = new NDimensionalPoint(centerPoint);
		NDimensionalGrid ndg = new NDimensionalGrid(dimSize, "*");
		NDimensionalGridIterator iter = ndg.iterator();
		while (iter.hasNext())
		{
			NDimensionalPoint p = (NDimensionalPoint)iter.next();
			ndg.set(p, p.manhattanDistance(center));
		}
		System.out.println(ndg);
	}

/*	{
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

//		NDimensionalGrid ndg = new NDimensionalGrid(values4D);
		String[] arenaDelimeters = {"\n\n", "\n", ""};
		Scanner gridScanner;
		try
		{
			gridScanner = new Scanner(new File("C:\\Users\\Amos\\SkyDrive\\Programs\\WordSearches\\arena.txt"));
		}
		catch (FileNotFoundException e)
		{
			System.out.print("Invalid file name, please try again: ");
			return;
		}

		String fileContents = "";
		while (gridScanner.hasNextLine())
		{
			fileContents += gridScanner.nextLine() + "\n";
		}
		NDimensionalGrid ndg = new NDimensionalGrid(fileContents, arenaDelimeters);
		int[] intarray = new int[3];
		for (int i = 0; i < intarray.length; ++i)
		{
			intarray[i] = Integer.parseInt(args[i]);
		}
		NDimensionalPoint p = new NDimensionalPoint(intarray);
		System.out.println(ndg.get(p));
		System.out.println(ndg);
	}*/

	private NDimensionalGrid[] nextLevel;

	protected NDimensionalGrid()
	{
		super(TessalationType.RECTANGULAR, 0);
	}

	public NDimensionalGrid(int[] sizes, Object defaultValue)
	{
		super(TessalationType.RECTANGULAR, sizes.length);

		if (dimension > 1)
		{
			nextLevel = new NDimensionalGrid[sizes[0]];
			int[] levelDown = Utility.RemoveFirstElementOfIntArray(sizes);
			for (int i = 0; i < nextLevel.length; ++i)
			{
				nextLevel[i] = new NDimensionalGrid(levelDown, defaultValue);
			}
		}
		else if (dimension == 1)
		{
			nextLevel = new NDimensionalGridElement[sizes[0]];
			for (int i = 0; i < nextLevel.length; ++i)
			{
				nextLevel[i] = new NDimensionalGridElement(defaultValue);
			}
		}
		else
		{
			throw new RuntimeException("Can't construct an NDimensionalGrid of dimension 0.");
		}
	}

	public NDimensionalGrid(String fileText, String[] delimiters)
	{
		super(TessalationType.RECTANGULAR, delimiters.length);
		initializeWithFileText(fileText, delimiters);
	}

	public NDimensionalGrid(Scanner fileScanner, String[] delimiters)
	{
		super(TessalationType.RECTANGULAR, delimiters.length);
		String fileContents = "";
		while (fileScanner.hasNextLine())
		{
			fileContents += fileScanner.nextLine() + "\n";
		}
		initializeWithFileText(fileContents, delimiters);
	}

	private void initializeWithFileText(String fileText, String[] delimiters)
	{
		String[] strs = fileText.split(delimiters[0]); // This will throw if trying to construct NDimensionalGrid of 0 dimension
//System.out.println(delimiters[0]);
		//AE at some point the below was set equal to addTo, with the comment following. But this seems to be wrong on spock.txt
		// delimiters[0].isEmpty() ? 1 : 0; // Empty delimiter always has an extra blank first string in the array
		int addTo = 0;
		if (dimension > 1)
		{
			nextLevel = new NDimensionalGrid[strs.length - addTo];
			for (int i = 0; i < nextLevel.length; ++i)
			{
//System.out.println(strs[i]);
				nextLevel[i] = new NDimensionalGrid(strs[i + addTo], Utility.RemoveFirstElementOfStringArray(delimiters));
			}
		}
		else if (dimension == 1)
		{
			nextLevel = new NDimensionalGridElement[strs.length - addTo];
			for (int i = 0; i < nextLevel.length; ++i)
			{
				nextLevel[i] = new NDimensionalGridElement(strs[i + addTo]);
			}
		}
	}

	// Convenience constructors for small values of N
	public NDimensionalGrid(Object[] values)
	{
		super(TessalationType.RECTANGULAR, 1);
		nextLevel = new NDimensionalGridElement[values.length];
		for (int i = 0; i < nextLevel.length; ++i)
		{
			nextLevel[i] = new NDimensionalGridElement(values[i]);
		}
	}
	public NDimensionalGrid(Object[][] values)
	{
		super(TessalationType.RECTANGULAR, 2);
		nextLevel = new NDimensionalGrid[values.length];
		for (int i = 0; i < nextLevel.length; ++i)
		{
			nextLevel[i] = new NDimensionalGrid(values[i]);
		}
	}
	public NDimensionalGrid(Object[][][] values)
	{
		super(TessalationType.RECTANGULAR, 3);
		nextLevel = new NDimensionalGrid[values.length];
		for (int i = 0; i < nextLevel.length; ++i)
		{
			nextLevel[i] = new NDimensionalGrid(values[i]);
		}
	}
	public NDimensionalGrid(Object[][][][] values)
	{
		super(TessalationType.RECTANGULAR, 4);
		nextLevel = new NDimensionalGrid[values.length];
		for (int i = 0; i < nextLevel.length; ++i)
		{
			nextLevel[i] = new NDimensionalGrid(values[i]);
		}
	}

	public NDimensionalGrid clone()
	{
		NDimensionalGrid newClone = (NDimensionalGrid)super.clone();
		if (dimension != 0)
		{
			newClone.nextLevel = new NDimensionalGrid[this.nextLevel.length];
			for (int i = 0; i < nextLevel.length; ++i)
			{
				newClone.nextLevel[i] = this.nextLevel[i].clone();
			}
		}
		return newClone;
	}

	public Object get(GridPoint p)
	{
		NDimensionalPoint point = (NDimensionalPoint)p;

		if (point.dimension() == this.dimension)
		{
//			try
			{
				return nextLevel[point.get(0)].get(point.trimFirstDimension());
//System.out.println(point + " # " +  dim);
			}
//			catch (ArrayIndexOutOfBoundsException e)
//			{
//System.out.println(point + " - ");
//				return -1;
//			}
		}
		else
		{
			throw new RuntimeException("Point used has the wrong dimension.");
		}
	}

	public void set(GridPoint p, Object value)
	{
		NDimensionalPoint point = (NDimensionalPoint)p;

		if (point.dimension() == this.dimension)
		{
			nextLevel[point.get(0)].set(point.trimFirstDimension(), value);
		}
		else
		{
			throw new RuntimeException("Point used has the wrong dimension.");
		}
	}

	public boolean contains(GridPoint p)
	{
		NDimensionalPoint point = (NDimensionalPoint)p;

		if (point.dimension() != this.dimension)
		{
			return false;
		}
		else if ((point.get(0) < 0) ||
				 (point.get(0) >= nextLevel.length))
		{
			return false;
		}
		else
		{
			return nextLevel[point.get(0)].contains(point.trimFirstDimension());
		}
	}

	public NDimensionalGridIterator iterator()
	{
		return new NDimensionalGridIterator(this);
	}
	public NDimensionalGridIterator iterator(int[] dimOrder)
	{
		return new NDimensionalGridIterator(this, dimOrder);
	}

	public void shiftWithin(GridPoint p)
	{
		NDimensionalPoint point = (NDimensionalPoint)p;
		if (this.dimension != point.dimension())
		{
			throw new RuntimeException("Point used has the wrong dimension.");
		}

		int curValue = point.get(0);
		if (curValue < 0)
		{
			point.set(0, curValue + nextLevel.length);
		}
		else if (curValue >= nextLevel.length)
		{
			point.set(0, curValue - nextLevel.length);
		}

		nextLevel[point.get(0)].shiftWithin(point.trimFirstDimension());
	}

	public int size(NDimensionalPoint point, int dim)
	{
		if (point.dimension() == this.dimension)
		{
			if (dim == 0)
			{
				return nextLevel.length;
			}
			else
			{
				return nextLevel[point.get(0)].size(point.trimFirstDimension(), dim - 1);
			}
		}
		else
		{
			throw new RuntimeException("Point used has the wrong dimension.");
		}
	}

	private int[] highAndParityReverseDimensionOrder()
	{// 2 0 1 or 3 1 2 0 or 4 2 0 3 1 etc
		int[] dimOrder = new int[dimension];
		int halfwayDimension = dimOrder.length / 2;
		int i = 0;
		for (int d = dimension - 1; d >= 0; ++i, d -= 2)
		{
			dimOrder[i] = d;
		}
		for (int d = dimension - 2; d >= 0; ++i, d-= 2)
		{
			dimOrder[i] = d;
		}
		return dimOrder;
	}

	private String spaceOut(String str, int changedDim, int numSpacesBetweenCharsForPrinting) //AE doesn't do well when first entries in a new line are different length (i.e. periodic table)
	{
		if (changedDim < 0)
		{
			return "";
		}
		String extra;
		String ret = "";
		if ((changedDim % 2) != (dimension % 2))
		{
			extra = "";
			int i = 0;
			for (; i < str.length(); ++i)
			{
				extra += ' ';
			}
			for (; i < numSpacesBetweenCharsForPrinting; ++i)
			{
				ret += ' ';
				extra += ' ';
			}
			// Move the changed dimension up to account for already
			// adding some blanks to ret in the above loop
			changedDim += 2;
		}
		else
		{
			extra = "\n";
		}

		for (int i = 0; i < (this.dimension - changedDim + 1) / 2; ++i)
		{
			ret += extra;
		}
		return ret;
	}

	public String toString(GridPrinter printer)
	{
		int[] dimOrder = highAndParityReverseDimensionOrder();
		String str = "";
		String cell;
		NDimensionalGridIterator iter = iterator(dimOrder);
		while (iter.hasNext())
		{
			GridPoint point = iter.next();
			cell = printer.value(this, point);
			str += spaceOut(cell, iter.lastChangedDimension(), printer.getNumSpaces());
			str += cell.toString();
		}

		return str;
	}
}