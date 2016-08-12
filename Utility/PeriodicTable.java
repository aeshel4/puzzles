import java.lang.Comparable;
import java.util.*;

import Utility.Grids.NDimensionalGrid;
import Utility.Words.SymbolsCanSpell;

class Element implements Comparable<Element>
{
	private String name;
	private String symbol;
	private int group;
	private int period;
	private int number; // AtomicNumber

	public Element(String symbol, String name, int group, int period, int number)
	{
		this.symbol = symbol;
		this.name = name;
		this.group = group;
		this.period = period;
		this.number = number;
	}

	public String Name() {return name;}
	public String Symbol() {return symbol;}
	public int Group() {return group;}
	public int Period() {return period;}
	public int AtomicNumber() {return number;}

	public int compareTo(Element that)
	{
		return this.symbol.compareTo(that.symbol);
	}

	public String toString()
	{
		return Name();
	}
	
	public String toStringLongName()
	{
		return AtomicNumber() + " " + Symbol() + " " + Name();
	}
}

class PeriodicTableOfElements
{
	private Element[] table;
	private String[][] grid;

	public static void main(String[] args)
	{
		PeriodicTableOfElements PToE =  new PeriodicTableOfElements();
//		System.out.println(PToE.GetTableAsGrid());
/*
		for (int i = 0; i < PToE.Size(); ++i)
		{
			int len = PToE.table[i].Symbol().length();
			if (len == 1)
			{
				System.out.println(i + ": " + PToE.table[i]);
			}
		}

		Element elem = PToE.GetElementFromSymbol(args[0]);
		if (elem != null)
		{
			System.out.println(elem);
		}
*/
//		System.out.println(PToE.GetElementFromSymbol(args[0]).toStringLongName());
	/*		int sum = 0;
		for (int i = 0; i < PToE.Size(); ++i)
		{
			if (PToE.table[i].AtomicNumber() % 3 == 2)
			{
				int len = PToE.table[i].Name().length();
				sum += len;
				System.out.println(PToE.table[i].AtomicNumber() + " " + PToE.table[i]);
				//System.out.println((char)('A'+i) + ": " + PToE.table[i].Symbol());
			}
		}
		System.out.println(sum);
*/
		if (!PToE.CanSpellPhrase(args[0], true))
		{
			System.out.println("Can't spell: " + args[0]);
		}
	}

	public Element GetElementFromSymbol(String symbol)
	{
		for (int i = 0; i < Size(); ++i)
		{
			if (table[i].Symbol().equalsIgnoreCase(symbol))
			{
				return table[i];
			}
		}
		return null;
	}

	public Element GetElementFromName(String name)
	{
		for (int i = 0; i < Size(); ++i)
		{
			if (table[i].Name().equalsIgnoreCase(name))
			{
				return table[i];
			}
		}
		return null;
	}
	
	public Element GetElementFromAtomicNumber(int num)
	{
		if (table[num - 1].AtomicNumber() == num)
		{
			return table[num - 1];
		}
		else
		{
			throw new RuntimeException("Table index with atomic number not consistent...");
		}
	}

	public boolean CanSpellPhrase(String phrase, boolean print)
	{
		SymbolsCanSpell rule = new SymbolsCanSpell(GetSymbols());
		rule.SetPrintOnMatch();
		return rule.IsAMatch(phrase);
	}
	
	public int Size()
	{
		return table.length;
	}
	
	private Element[] CopyOfTable()
	{
		Element[] tableCopy = new Element[Size()];
		for (int i = 0; i < Size(); ++i)
		{
			tableCopy[i] = table[i];
		}
		return tableCopy;
	}

	private String[] GetSymbols()
	{
		String[] symbols = new String[Size()];
		for (int i = 0; i < Size(); ++i)
		{
			symbols[i] = table[i].Symbol();
		}
		return symbols;
	}

	private boolean IsASymbol(String sym)
	{
		for (int i = 0; i < Size(); ++i)
		{
			if (table[i].Symbol().equalsIgnoreCase(sym))
			{
				return true;
			}
		}
		return false;
	}

	public NDimensionalGrid GetTableAsGrid()
	{
		return new NDimensionalGrid(grid);
	}

	public PeriodicTableOfElements()
	{
		final int NumElements = 118;
		table = new Element[NumElements];
		int i = 0;
		{
		table[i++] = new Element("H", "Hydrogen", 1, 1, i);
		table[i++] = new Element("He", "Helium", 18, 1, i);
		table[i++] = new Element("Li", "Lithium", 1, 2, i);
		table[i++] = new Element("Be", "Beryllium", 2, 2, i);
		table[i++] = new Element("B", "Boron", 13, 2, i);
		table[i++] = new Element("C", "Carbon", 14, 2, i);
		table[i++] = new Element("N", "Nitrogen", 15, 2, i);
		table[i++] = new Element("O", "Oxygen", 16, 2, i);
		table[i++] = new Element("F", "Fluorine", 17, 2, i);
		table[i++] = new Element("Ne", "Neon", 18, 2, i);
		table[i++] = new Element("Na", "Sodium", 1, 3, i);
		table[i++] = new Element("Mg", "Magnesium", 2, 3, i);
		table[i++] = new Element("Al", "Aluminium", 13, 3, i);
		table[i++] = new Element("Si", "Silicon", 14, 3, i);
		table[i++] = new Element("P", "Phosphorus", 15, 3, i);
		table[i++] = new Element("S", "Sulfur", 16, 3, i);
		table[i++] = new Element("Cl", "Chlorine", 17, 3, i);
		table[i++] = new Element("Ar", "Argon", 18, 3, i);
		table[i++] = new Element("K", "Potassium", 1, 4, i);
		table[i++] = new Element("Ca", "Calcium", 2, 4, i);
		table[i++] = new Element("Sc", "Scandium", 3, 4, i);
		table[i++] = new Element("Ti", "Titanium", 4, 4, i);
		table[i++] = new Element("V", "Vanadium", 5, 4, i);
		table[i++] = new Element("Cr", "Chromium", 6, 4, i);
		table[i++] = new Element("Mn", "Manganese", 7, 4, i);
		table[i++] = new Element("Fe", "Iron", 8, 4, i);
		table[i++] = new Element("Co", "Cobalt", 9, 4, i);
		table[i++] = new Element("Ni", "Nickel", 10, 4, i);
		table[i++] = new Element("Cu", "Copper", 11, 4, i);
		table[i++] = new Element("Zn", "Zinc", 12, 4, i);
		table[i++] = new Element("Ga", "Gallium", 13, 4, i);
		table[i++] = new Element("Ge", "Germanium", 14, 4, i);
		table[i++] = new Element("As", "Arsenic", 15, 4, i);
		table[i++] = new Element("Se", "Selenium", 16, 4, i);
		table[i++] = new Element("Br", "Bromine", 17, 4, i);
		table[i++] = new Element("Kr", "Krypton", 18, 4, i);
		table[i++] = new Element("Rb", "Rubidium", 1, 5, i);
		table[i++] = new Element("Sr", "Strontium", 2, 5, i);
		table[i++] = new Element("Y", "Yttrium", 3, 5, i);
		table[i++] = new Element("Zr", "Zirconium", 4, 5, i);
		table[i++] = new Element("Nb", "Niobium", 5, 5, i);
		table[i++] = new Element("Mo", "Molybdenum", 6, 5, i);
		table[i++] = new Element("Tc", "Technetium", 7, 5, i);
		table[i++] = new Element("Ru", "Ruthenium", 8, 5, i);
		table[i++] = new Element("Rh", "Rhodium", 9, 5, i);
		table[i++] = new Element("Pd", "Palladium", 10, 5, i);
		table[i++] = new Element("Ag", "Silver", 11, 5, i);
		table[i++] = new Element("Cd", "Cadmium", 12, 5, i);
		table[i++] = new Element("In", "Indium", 13, 5, i);
		table[i++] = new Element("Sn", "Tin", 14, 5, i);
		table[i++] = new Element("Sb", "Antimony", 15, 5, i);
		table[i++] = new Element("Te", "Tellurium", 16, 5, i);
		table[i++] = new Element("I", "Iodine", 17, 5, i);
		table[i++] = new Element("Xe", "Xenon", 18, 5, i);
		table[i++] = new Element("Cs", "Caesium", 1, 6, i);
		table[i++] = new Element("Ba", "Barium", 2, 6, i);
		table[i++] = new Element("La", "Lanthanum", 0, 6, i);
		table[i++] = new Element("Ce", "Cerium", 0, 6, i);
		table[i++] = new Element("Pr", "Praseodymium", 0, 6, i);
		table[i++] = new Element("Nd", "Neodymium", 0, 6, i);
		table[i++] = new Element("Pm", "Promethium", 0, 6, i);
		table[i++] = new Element("Sm", "Samarium", 0, 6, i);
		table[i++] = new Element("Eu", "Europium", 0, 6, i);
		table[i++] = new Element("Gd", "Gadolinium", 0, 6, i);
		table[i++] = new Element("Tb", "Terbium", 0, 6, i);
		table[i++] = new Element("Dy", "Dysprosium", 0, 6, i);
		table[i++] = new Element("Ho", "Holmium", 0, 6, i);
		table[i++] = new Element("Er", "Erbium", 0, 6, i);
		table[i++] = new Element("Tm", "Thulium", 0, 6, i);
		table[i++] = new Element("Yb", "Ytterbium", 0, 6, i);
		table[i++] = new Element("Lu", "Lutetium", 3, 6, i);
		table[i++] = new Element("Hf", "Hafnium", 4, 6, i);
		table[i++] = new Element("Ta", "Tantalum", 5, 6, i);
		table[i++] = new Element("W", "Tungsten", 6, 6, i);
		table[i++] = new Element("Re", "Rhenium", 7, 6, i);
		table[i++] = new Element("Os", "Osmium", 8, 6, i);
		table[i++] = new Element("Ir", "Iridium", 9, 6, i);
		table[i++] = new Element("Pt", "Platinum", 10, 6, i);
		table[i++] = new Element("Au", "Gold", 11, 6, i);
		table[i++] = new Element("Hg", "Mercury", 12, 6, i);
		table[i++] = new Element("Tl", "Thallium", 13, 6, i);
		table[i++] = new Element("Pb", "Lead", 14, 6, i);
		table[i++] = new Element("Bi", "Bismuth", 15, 6, i);
		table[i++] = new Element("Po", "Polonium", 16, 6, i);
		table[i++] = new Element("At", "Astatine", 17, 6, i);
		table[i++] = new Element("Rn", "Radon", 18, 6, i);
		table[i++] = new Element("Fr", "Francium", 1, 7, i);
		table[i++] = new Element("Ra", "Radium", 2, 7, i);
		table[i++] = new Element("Ac", "Actinium", 0, 7, i);
		table[i++] = new Element("Th", "Thorium", 0, 7, i);
		table[i++] = new Element("Pa", "Protactinium", 0, 7, i);
		table[i++] = new Element("U", "Uranium", 0, 7, i);
		table[i++] = new Element("Np", "Neptunium", 0, 7, i);
		table[i++] = new Element("Pu", "Plutonium", 0, 7, i);
		table[i++] = new Element("Am", "Americium", 0, 7, i);
		table[i++] = new Element("Cm", "Curium", 0, 7, i);
		table[i++] = new Element("Bk", "Berkelium", 0, 7, i);
		table[i++] = new Element("Cf", "Californium", 0, 7, i);
		table[i++] = new Element("Es", "Einsteinium", 0, 7, i);
		table[i++] = new Element("Fm", "Fermium", 0, 7, i);
		table[i++] = new Element("Md", "Mendelevium", 0, 7, i);
		table[i++] = new Element("No", "Nobelium", 0, 7, i);
		table[i++] = new Element("Lr", "Lawrencium", 3, 7, i);
		table[i++] = new Element("Rf", "Rutherfordium", 4, 7, i);
		table[i++] = new Element("Db", "Dubnium", 5, 7, i);
		table[i++] = new Element("Sg", "Seaborgium", 6, 7, i);
		table[i++] = new Element("Bh", "Bohrium", 7, 7, i);
		table[i++] = new Element("Hs", "Hassium", 8, 7, i);
		table[i++] = new Element("Mt", "Meitnerium", 9, 7, i);
		table[i++] = new Element("Ds", "Darmstadtium", 10, 7, i);
		table[i++] = new Element("Rg", "Roentgenium", 11, 7, i);
		table[i++] = new Element("Cn", "Copernicium", 12, 7, i);
		table[i++] = new Element("Uut", "Ununtrium", 13, 7, i);
		table[i++] = new Element("Fl", "Flerovium", 14, 7, i);
		table[i++] = new Element("Uup", "Ununpentium", 15, 7, i);
		table[i++] = new Element("Lv", "Livermorium", 16, 7, i);
		table[i++] = new Element("Uus", "Ununseptium", 17, 7, i);
		table[i++] = new Element("Uuo", "Ununoctium", 18, 7, i);
		}

		if (i != NumElements)
		{
			throw new RuntimeException();
		}

		String[][] localGrid = {
			{"H", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", "He"},
			{"Li", "Be", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", "B", "C", "N", "O", "F", "Ne"},
			{"Na", "Mg", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", "Al", "Si", "P", "S", "Cl", "Ar"},
			{"K", "Ca", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", "Sc", "Ti", "V", "Cr", "Mn", "Fe", "Co", "Ni", "Cu", "Zn", "Ga", "Ge", "As", "Se", "Br", "Kr"},
			{"Rb", "Sr", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", "Y", "Zr", "Nb", "Mo", "Tc", "Ru", "Rh", "Pd", "Ag", "Cd", "In", "Sn", "Sb", "Te", "I", "Xe"},
			{"Cs", "Ba", "La", "Ce", "Pr", "Nd", "Pm", "Sm", "Eu", "Gd", "Tb", "Dy", "Ho", "Er", "Tm", "Yb", "Lu", "Hf", "Ta", "W", "Re", "Os", "Ir", "Pt", "Au", "Hg", "Tl", "Pb", "Bi", "Po", "At", "Rn"},
			{"Fr", "Ra", "Ac", "Th", "Pa", "U", "Np", "Pu", "Am", "Cm", "Bk", "Cf", "Es", "Fm", "Md", "No", "Lr", "Rf", "Db", "Sg", "Bh", "Hs", "Mt", "Ds", "Rg", "Cn", "Uut", "Fl", "Uup", "Lv", "Uus", "Uuo"}
		};
		this.grid = localGrid;
	}
}