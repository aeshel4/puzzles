package Utility.Grids;

public class NDimensionalGridEdge extends GridEdge
{
	public NDimensionalGridEdge(NDimensionalPoint[] nPoints)
	{
		super(nPoints);
	}

	public boolean isConnectedTo(GridEdge that)
	{
		return false; //AE
	}

}