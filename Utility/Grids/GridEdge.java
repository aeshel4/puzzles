package Utility.Grids;

import java.util.Arrays;

public abstract class GridEdge
{
	private Object o;
	protected GridPoint[] points;

	protected GridEdge(GridPoint[] points)
	{
		this.points = points;
	}

	public Object get()
	{
		return o;
	}

	public void set(Object o)
	{
		this.o = o;
	}

	public GridPoint[] adjacentPoints()
	{
		return points;
	}

	public boolean equals(GridEdge that)
	{
		return Arrays.deepEquals(this.points, that.points);
	}

	public int hashCode()
	{
		int h = o.hashCode();
		for (int p = 0; p < points.length; ++p)
		{
			h *= points[p].hashCode();
		}
		return h;
	}

	public abstract boolean isConnectedTo(GridEdge that);
}