package Utility.Grids;

import java.util.Iterator;
import java.util.Arrays;

public abstract class GridTessalation implements Cloneable, Iterable<GridPoint>
{
	protected TessalationType type;
	protected int dimension;

	protected GridTessalation(TessalationType type, int dimension)
	{
		this.type = type;
		this.dimension = dimension;
	}

	public int dimension()
	{
		return dimension;
	}

	public TessalationType type()
	{
		return type;
	}

	public GridPoint[] adjacentPoints(GridPoint point, boolean cornersCount)
	{
		GridPoint[] allPoints = point.adjacentPoints(cornersCount);
		GridPoint[] adjPoints = new GridPoint[allPoints.length];
		int numInGrid = 0;
		for (int p = 0; p < allPoints.length; ++p)
		{
			if (this.contains(allPoints[p]))
			{
				adjPoints[numInGrid++] = allPoints[p];
			}
		}
		return Arrays.copyOf(adjPoints, numInGrid);
	}

	public String toString()
	{
		return this.toString(new GridPrinter());
	}

	public GridTessalation clone()
	{
		GridTessalation newClone;
		try
		{
			newClone = (GridTessalation)super.clone();
		}
		catch (CloneNotSupportedException e)
		{
			throw new RuntimeException("GridTessalation's super is Object, so should not have thrown CloneNotSupportedException");
		}
		newClone.type = this.type;
		newClone.dimension = this.dimension;
		return newClone;
	}

	public abstract String toString(GridPrinter printer);
	public abstract Object get(GridPoint p);
	public abstract void set(GridPoint p, Object o);
	public abstract boolean contains(GridPoint p);
	public abstract void shiftWithin(GridPoint point);
}