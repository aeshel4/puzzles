package Utility.Grids;

public enum TessalationType
{
	TRIANGULAR,
	RECTANGULAR,
	HEXAGONAL;

	public static TessalationType getFromString(String s)
	{
		if (s.equalsIgnoreCase("TRIANGULAR"))
		{
			return TessalationType.TRIANGULAR;
		}
		else if (s.equalsIgnoreCase("RECTANGULAR"))
		{
			return TessalationType.RECTANGULAR;
		}
		else if (s.equalsIgnoreCase("HEXAGONAL"))
		{
			return TessalationType.HEXAGONAL;
		}
		else
		{
			throw new RuntimeException("Don't know the TessalationType: " + s);
		}
	}
}