package Utility.Grids;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class NDimensionalGridIterator implements Iterator<GridPoint>
{
	private NDimensionalGrid grid;
	private NDimensionalPoint currentPoint;
	private int[] dimOrder;
	private int lastChangedDim;
	private int nextChangedDim;
	private boolean more;

	private void init(NDimensionalGrid grid, int[] dimOrder)
	{
		this.grid = grid;
		this.currentPoint = new NDimensionalPoint(grid.dimension());
		this.more = true;
		this.dimOrder = Arrays.copyOf(dimOrder, dimOrder.length);
		this.lastChangedDim = this.nextChangedDim = -1;
	}

	// Package private so you have to get this from the grid
	NDimensionalGridIterator(NDimensionalGrid grid)
	{
		int[] standardOrder = new int[grid.dimension()];
		for (int i = 0; i < standardOrder.length; ++i)
		{
			standardOrder[i] = standardOrder.length - 1 - i;
		}

		init(grid, standardOrder);
	}

	NDimensionalGridIterator(NDimensionalGrid grid, int[] dimOrder)
	{
		init(grid, dimOrder);
	}
	
	public int lastChangedDimension()
	{
		return lastChangedDim;
	}

	public boolean hasNext()
	{
		return more;
	}

	public GridPoint next()
	{
		if (!more)
		{
			throw new NoSuchElementException();
		}

		lastChangedDim = nextChangedDim;
		NDimensionalPoint returnPoint = (NDimensionalPoint)currentPoint.clone();
		int d;
		for (d = 0; d < dimOrder.length; ++d)
		{
			if (currentPoint.advance(dimOrder[d], grid.size(currentPoint, dimOrder[d])))
			{
				break;
			}
			else
			{
				currentPoint.zero(dimOrder[d]);
			}
		}
		more = (d != dimOrder.length);
		nextChangedDim = more ? dimOrder[d] : -1;

		return returnPoint;
	}
}