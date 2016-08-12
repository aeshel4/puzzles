package Utility;

public class VigenereCipher
{
	public static void main(String[] args)
	{
		if (args.length != 3)
		{
			System.out.println("java VigenereCipher [encrypt|decrypt|keyword]");
			System.out.println("        e <plainText> <key>");
			System.out.println("        d <cipherText> <key>");
			System.out.println("        k <plainText> <cipherText>");
			return;
		}

		char choice = args[0].toUpperCase().charAt(0);
		String plain, key, cipher;
		if (choice == 'E')
		{
			plain = args[1];
			key = args[2];
			cipher = encrypt(plain, key);
		}
		else if (choice == 'D')
		{
			cipher = args[1];
			key = args[2];
			plain = decrypt(cipher, key);
		}
		else if (choice == 'K')
		{
			plain = args[1];
			cipher = args[2];
			key = keyword(plain, cipher);
		}
		else
		{
			System.out.println("Unknown option: " + args[0]);
			return;
		}

		if (
			(!cipher.equalsIgnoreCase(encrypt(plain, key))) ||
			(!plain.equalsIgnoreCase(decrypt(cipher, key))) ||
			(!key.equalsIgnoreCase(keyword(plain, cipher)))
		   )
		{
			System.out.println("##### ERROR #####");
			System.out.println(" Something didn't match up with:");
		}
		System.out.println(" Plain- " + plain);
		System.out.println(" Cipher- " + cipher);
		System.out.println(" Keyword- " + key);
	}

	public static String keyword(String plainText, String cipherText)
	{
		String keyStr = keytext(plainText, cipherText);
		for (int keyLen = 1; keyLen < keyStr.length(); ++keyLen)
		{
			String proposedKey = keyStr.substring(0, keyLen);
			if (isKeyGood(keyStr, proposedKey))
			{
				return proposedKey;
			}
		}
		return keyStr;
	}

	private static boolean isKeyGood(String keytext, String keyword)
	{
		for (int i = 0; i < keytext.length(); ++i)
		{
			if (keytext.charAt(i) != keyword.charAt(i % keyword.length()))
			{
				return false;
			}
		}
		return true;
	}

	public static String keytext(String plainText, String cipherText)
	{
		if (plainText.length() != cipherText.length())
		{
			return "";
		}
		String keyText = "";
		for (int i = 0; i < plainText.length(); ++i)
		{
			keyText += CaesarShift.WhatIsShift(plainText.charAt(i), cipherText.charAt(i));
		}
		return keyText;
	}

	public static String decrypt(String cipherText, String keyword)
	{
		return crypt(cipherText, keyword, false);
	}

	public static String encrypt(String plainText, String keyword)
	{
		return crypt(plainText, keyword, true);
	}

	private static String crypt(String text, String keyword, boolean en)
	{
		int mult = en ? 1 : -1;
		keyword = keyword.toUpperCase();
		String convertedText = "";
		for (int i = 0; i < text.length(); ++i)
		{
			convertedText += CaesarShift.Shift(text.charAt(i), mult * (keyword.charAt(i % keyword.length()) - 'A'));
		}
		return convertedText;
	}
} 