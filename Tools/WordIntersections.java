package Tools;

public class WordIntersections
{
	public static void main(String[] args)
	{
		if (args.length != 2)
		{
			System.out.println("Call pattern:\nrun <word1> <word2>");
			return;
		}

		for (int i = 0; i < args[0].length(); ++i)
		{
			for (int j = 0; j < args[1].length(); ++j)
			{
				if (args[0].charAt(i) == args[1].charAt(j))
				{
					System.out.print(args[0].charAt(i) + " ");
				}
			}
		}
	}
}