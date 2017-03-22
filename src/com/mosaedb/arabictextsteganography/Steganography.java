package com.mosaedb.arabictextsteganography;

import java.nio.charset.StandardCharsets;

public final class Steganography {

	private Steganography() {
	}

	/**
	 * Hide the secret message into the cover text without change the cover text
	 * appearance.
	 * 
	 * @param coverText an Arabic text
	 * @param secretText an English or an Arabic text
	 * @return the stegoText (Arabic text with ZWJ and ZWNJ Unicode characters).
	 */
	public static String hideSecretMessage(String coverText, String secretText) {
		return Steganography.addZwjOrZwnj(coverText, secretText);
	}

	/**
	 * Retrieve the secret message from the stego text.
	 * 
	 * @param stegoText an Arabic text with ZWJ and ZWNJ Unicode characters
	 * @return The secret message from the stego text
	 */
	public static String retrieveSecretMessage(String stegoText) {
		String secretMessageMask = Steganography.removeZwjZwnj(stegoText);
		int[] zeroOneOfSecretMsg = Steganography.letterToZeroOrOne(secretMessageMask);
		StringBuilder sb = new StringBuilder();
		for (int zeroOrOne : zeroOneOfSecretMsg) {
			sb.append(zeroOrOne);
		}
		String secretMessage = Steganography.binaryToString(sb.toString());
		return secretMessage;
	}

	/**
	 * Convert Arabic letters to an array of zeros and ones.
	 * 
	 * @param coverText an Arabic text
	 * @return the argument as zeros and ones
	 */
	public static int[] letterToZeroOrOne(String coverText) {
		char[] coverTextLetters = coverText.toCharArray();
		int[] zeroOneMask = new int[coverTextLetters.length];

		for (int i = 0; i < coverTextLetters.length; i++) {
			switch (coverTextLetters[i]) {
			case '\u0627':	// Ç
			case '\u0644':	// á
			case '\u0645':	// ã
			case '\u0648':	// æ
			case '\u0647':	// å
			case '\u0631':	// Ñ
			case '\u0639':	// Ú
			case '\u0623':	// Ã
			case '\u062f':	// Ï
			case '\u0633':	// Ó
			case '\u0643':	// ß
			case '\u062d':	// Í
			case '\u0649':	// ì
			case '\u0635':	// Õ
			case '\u0625':	// Å
			case '\u0637':	// Ø
			case '\u0621':	// Á
			case '\u0626':	// Æ
			case '\u0622':	// Â
			case '\u0624':	// Ä
				zeroOneMask[i] = 1;
				break;
			case '\u0646':	// ä
			case '\u064a':	// í
			case '\u0628':	// È
			case '\u0641':	// Ý
			case '\u0642':	// Þ
			case '\u062A':	// Ê
			case '\u0629':	// É
			case '\u062c':	// Ì
			case '\u0630':	// Ð
			case '\u062b':	// Ë
			case '\u062e':	// Î
			case '\u0634':	// Ô
			case '\u0632':	// Ò
			case '\u0636':	// Ö
			case '\u063a':	// Û
			case '\u0638':	// Ù
				zeroOneMask[i] = 0;
				break;
			default:
				zeroOneMask[i] = 0;
				break;
			}
		}
		return zeroOneMask;
	}

	/**
	 * Convert the text to binary.
	 * 
	 * @param secretText an English or an Arabic text
	 * @return the binary of the argument
	 */
	public static String stringToBinary(String secretText) {
		// https://stackoverflow.com/questions/917163/convert-a-string-like-testing123-to-binary-in-java
		String string = secretText;
		byte[] bytes = string.getBytes(StandardCharsets.UTF_8);
		StringBuilder binary = new StringBuilder();
		for (byte b : bytes) {
			int val = b;
			for (int i = 0; i < 8; i++) {
				binary.append((val & 128) == 0 ? 0 : 1);
				val <<= 1;
			}
			binary.append(' ');
		}
		String str = binary.toString();
		return str;
	}

	/**
	 * Convert the binary to text.
	 * 
	 * @param binaryString a String of binary number
	 * @return the string of the argument
	 */
	public static String binaryToString(String binaryString) {
		// https://github.com/hiroponne/cpsf-challenge/blob/master/for_takuro/BinaryToString.java
		String binary = binaryString.replace(" ", "");
		String binaryPer8Bits;
		byte[] byteData;
		byteData = new byte[binary.length() / 8];

		for (int i = 0; i < binary.length() / 8; i++) {
			// To divide the string into 8 characters
			binaryPer8Bits = binary.substring(i * 8, (i + 1) * 8);
			// The integer of decimal string of binary numbers
			Integer integer = new Integer(Integer.parseInt(binaryPer8Bits, 2));
			// The casting to a byte type variable
			byteData[i] = integer.byteValue();
		}
		return new String(byteData);
	}

	/**
	 * Add the Unicode characters (ZWJ and ZWNJ) at correct position in cover
	 * text.
	 * 
	 * @param coverText an Arabic text
	 * @param secretText an English or an Arabic text
	 * @return the stegoText (Arabic text with ZWJ and ZWNJ Unicode characters).
	 */
	public static String addZwjOrZwnj(String coverText, String secretText) throws ArrayIndexOutOfBoundsException {
		char[] coverTextChar = coverText.toCharArray();
		StringBuilder stegoText = new StringBuilder();
		String str = Steganography.stringToBinary(secretText);
		char[] secretTextBinary = str.replace(" ", "").toCharArray();
		int[] zeroOne = Steganography.letterToZeroOrOne(coverText);

		int i = 0;
		int j = 0;
		int temp = Character.getNumericValue(secretTextBinary[i]);
		
		if (i < secretTextBinary.length && zeroOne[j] != temp) {
			// Add ZWNJ -> 'u200C' before ignored letters
			stegoText.append('\u200C');
			// catch the next ZWJ or ZWNJ
			do {
				stegoText.append(coverTextChar[j]);
				j++;
			} while (zeroOne[j] != temp);
			// add ZWNJ or ZWN after ignored letter
			if (isIsolatedToNextLetter(coverTextChar[j - 1])) {
				// Add ZWNJ -> 'u200C' after ignored letters
				stegoText.append('\u200C');
				stegoText.append(coverTextChar[j]);
			} else if (isConnectedToNextLetter(coverTextChar[j - 1])) {
				if (Character.isWhitespace(coverTextChar[j]))
					stegoText.append('\u200C');
				else
					// Add ZWN -> 'u200D' after ignored letters
					stegoText.append('\u200D');
				stegoText.append(coverTextChar[j]);
			}
			i++;
			j++;
		}

		while (i < secretTextBinary.length) {
			temp = Character.getNumericValue(secretTextBinary[i]);
			// Add ZWJ -> '\u200D' or add ZWNJ -> '\u200C'
			if (zeroOne[j] != temp) {
				if (isIsolatedToNextLetter(coverTextChar[j - 1])) {
					// Add ZWNJ -> 'u200C' before ignored letters
					stegoText.append('\u200C');
					// catch the next ZWJ or ZWNJ
					do {
						stegoText.append(coverTextChar[j]);
						j++;
					} while (zeroOne[j] != temp);
					// add ZWNJ or ZWN after ignored letter
					if (isIsolatedToNextLetter(coverTextChar[j - 1])) {
						// Add ZWNJ -> 'u200C' after ignored letters
						stegoText.append('\u200C');
						stegoText.append(coverTextChar[j]);
					} else if (isConnectedToNextLetter(coverTextChar[j - 1])) {
						if (Character.isWhitespace(coverTextChar[j]))
							stegoText.append('\u200C');
						else
							// Add ZWN -> 'u200D' after ignored letters
							stegoText.append('\u200D');
						stegoText.append(coverTextChar[j]);
					}
				} else if (isConnectedToNextLetter(coverTextChar[j - 1])) {
					if (Character.isWhitespace(coverTextChar[j]))
						stegoText.append('\u200C');
					else
						stegoText.append('\u200D');
					// catch the next ZWJ or ZWNJ
					do {
						stegoText.append(coverTextChar[j]);
						j++;
					} while (zeroOne[j] != temp);
					// add ZWNJ or ZWN after ignored letter
					if (isIsolatedToNextLetter(coverTextChar[j - 1])) {
						// Add ZWNJ -> 'u200C' after ignored letters
						stegoText.append('\u200C');
						stegoText.append(coverTextChar[j]);
					} else if (isConnectedToNextLetter(coverTextChar[j - 1])) {
						if (Character.isWhitespace(coverTextChar[j]))
							stegoText.append('\u200C');
						else
							// Add ZWN -> 'u200D' after ignored letters
							stegoText.append('\u200D');
						stegoText.append(coverTextChar[j]);
					}
				}
			}
			else {
				stegoText.append(coverTextChar[j]);
			}
			i++;
			j++;
		}
		
		if (i == coverText.length()) {
			return stegoText.toString();
		} else {
			// Add the last ZWNJ or ZWJ to ignore the rest letters
			if (isIsolatedToNextLetter(coverTextChar[j - 1])) {
				// Add ZWNJ -> 'u200C' after ignored letters
				stegoText.append('\u200C');
			} else if (isConnectedToNextLetter(coverTextChar[j - 1])) {
				if (Character.isWhitespace(coverTextChar[j]))
					stegoText.append('\u200C');
				else
					// Add ZWN -> 'u200D' after ignored letters
					stegoText.append('\u200D');
			}
			stegoText.append(coverText.substring(j));
		}
		return stegoText.toString();
	}

	/**
	 * Determines if the specified letter isolated to the next letter.
	 * 
	 * @param c an Arabic letter
	 * @return true if the letter is isolated to the next letter; false otherwise.
	 */
	private static boolean isIsolatedToNextLetter(char c) {
		boolean isolated = false;
		switch (c) {
		case '\u0627':	// Ç
			isolated = true;
			break;
		case '\u0648':	// æ
			isolated = true;
			break;
		case '\u0631':	// Ñ
			isolated = true;
			break;
		case '\u0623':	// Ã
			isolated = true;
			break;
		case '\u062f':	// Ï
			isolated = true;
			break;
		case '\u0629':	// É
			isolated = true;
			break;
		case '\u0649':	// ì
			isolated = true;
			break;
		case '\u0625':	// Å
			isolated = true;
			break;
		case '\u0630':	// Ð
			isolated = true;
			break;
		case '\u0632':	// Ò
			isolated = true;
			break;
		case '\u0621':	// Á
			isolated = true;
			break;
		case '\u0622':	// Â
			isolated = true;
			break;
		case '\u0624':	// Ä
			isolated = true;
			break;
		case '\u0020':	// SPACE
			isolated = true;
			break;
		default:
			isolated = Character.isWhitespace(c);
			break;
		}
		return isolated;
	}

	/**
	 * Determines if the specified letter connected to the next letter.
	 * 
	 * @param c an Arabic letter
	 * @return true if the letter is connected to the next letter; false otherwise.
	 */
	private static boolean isConnectedToNextLetter(char c) {
		boolean connected = false;
		switch (c) {
		case '\u0644':	// á
			connected = true;
			break;
		case '\u0645':	// ã
			connected = true;
			break;
		case '\u0647':	// å
			connected = true;
			break;
		case '\u0639':	// Ú
			connected = true;
			break;
		case '\u0633':	// Ó
			connected = true;
			break;
		case '\u0643':	// ß
			connected = true;
			break;
		case '\u062d':	// Í
			connected = true;
			break;
		case '\u0635':	// Õ
			connected = true;
			break;
		case '\u0637':	// Ø
			connected = true;
			break;
		case '\u0626':	// Æ
			connected = true;
			break;
		case '\u0646':	// ä
			connected = true;
			break;
		case '\u064a':	// í
			connected = true;
			break;
		case '\u0628':	// È
			connected = true;
			break;
		case '\u0641':	// Ý
			connected = true;
			break;
		case '\u0642':	// Þ
			connected = true;
			break;
		case '\u062A':	// Ê
			connected = true;
			break;
		case '\u062c':	// Ì
			connected = true;
			break;
		case '\u062b':	// Ë
			connected = true;
			break;
		case '\u062e':	// Î
			connected = true;
			break;
		case '\u0634':	// Ô
			connected = true;
			break;
		case '\u0636':	// Ö
			connected = true;
			break;
		case '\u063a':	// Û
			connected = true;
			break;
		case '\u0638':	// Ù
			connected = true;
			break;
		case '\u0640':	// Ü TATWEEL
			connected = true;
			break;
		}
		return connected;
	}

	/**
	 * Remove the Unicode characters (ZWJ and ZWNJ) and letters after or between
	 * them.
	 * 
	 * @param stegoText an Arabic text with ZWJ and ZWNJ Unicode characters
	 * @return the secret text
	 */
	public static String removeZwjZwnj(String stegoText) {
		char[] temp = stegoText.toCharArray();
		StringBuilder secretText = new StringBuilder();

		int i = 0;
		while (i < stegoText.length()) {
			// ignore ZWJ == '\u200D' and ZWNJ == '\u200C' and any letters between or after them
			if (temp[i] == '\u200D' || temp[i] == '\u200C') {
				do {
					i++;
				} while (i < stegoText.length() && temp[i] != '\u200D' && temp[i] != '\u200C');
				
				i++;
			} else {
				secretText.append(temp[i]);
				i++;
			}
		}
		return secretText.toString();
	}

}
