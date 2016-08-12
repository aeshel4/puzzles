package Utility.Grids;

public class GridPrinter
{
	public static final String NotPrinted = "-";
	public int numSpacesBetweenCharsForPrinting;

	protected GridPrinter()
	{
		this(5);
	}

	protected GridPrinter(int spaces)
	{
		numSpacesBetweenCharsForPrinting = spaces;
	}

	// Note that this will not be used by all grids
	public int getNumSpaces()
	{
		return numSpacesBetweenCharsForPrinting;
	}

	// OVERRIDE this function to make changes
	public String value(GridTessalation g, GridPoint p)
	{
		return g.get(p).toString();
	}
}