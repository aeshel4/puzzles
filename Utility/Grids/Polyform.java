package Utility.Grids;

import java.util.Iterator;

// For polyform types following the standard storage mechanism, much of the code
// is provided. Weirder polyform shapes should override all functions with comment:
// WEIRD Override
abstract public class Polyform implements Cloneable
{
	// types (Polyomino, Polycube, Poliamond, Polyhex) // 4dimensional would be kind of absurd
	protected TessalationType type;
	protected int dimension;
	protected GridTessalation grid;

	protected Polyform(TessalationType type, int dimension)
	{
		this(type, dimension, null);
	}

	protected Polyform(TessalationType type, int dimension, GridTessalation grid)
	{
		this.type = type;
		this.dimension = dimension;
		this.grid = grid;
	}

	public int dimension()
	{
		return dimension;
	}

	public TessalationType type()
	{
		return type;
	}

	public Polyform clone()
	{
		Polyform newClone;
		try
		{
			newClone = (Polyform)super.clone();
		}
		catch (CloneNotSupportedException e)
		{
			throw new RuntimeException("Polyform's super is Object, so should not have thrown CloneNotSupportedException");
		}

		newClone.type = this.type;
		newClone.dimension = this.dimension;
		newClone.grid = this.grid.clone();
		return newClone;
	}

	public abstract void mirror(int axis);
	public abstract int numAxes(); // the number of valid values that can be passed to mirror

	public abstract void rotate();
	public abstract int numRotations(); // Calling rotate() this many times comes back to the original orientation

	// WEIRD Override
	public int numBaseShapes() // e.g. number of squares in a polyomino
	{
		int count = 0;
		Iterator<GridPoint> iter = grid.iterator();
		while (iter.hasNext())
		{
			Object value = grid.get(iter.next());
			if (value != null)
			{
				++count;
			}
		}
		return count;
	}

	protected GridPoint[] getAsPointsWhileClearing()
	{
		int count = numBaseShapes();
		GridPoint[] points = new GridPoint[count];
		int iPt = 0;
		Iterator<GridPoint> iter = grid.iterator();
		while (iter.hasNext())
		{
			GridPoint curPoint = iter.next();
			Object value = grid.get(curPoint);
			if (value != null)
			{
				points[iPt++] = curPoint;
				grid.set(curPoint, null);
			}
		}
		return points;
	}

	// WEIRD Override
	public String toString()
	{
		return grid.toString(new GridPrinterTruth());
	}
}

class GridPrinterTruth extends GridPrinter
{
	public GridPrinterTruth()
	{
		super(1);
	}

	public String value(GridTessalation grid, GridPoint point)
	{
		Object value = grid.get(point);
		if (value != null)
		{
			return String.valueOf(value.toString().charAt(0));
		}
		else
		{
			return ".";
		}
	}
}