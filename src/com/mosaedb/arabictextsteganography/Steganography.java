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
			case '\u0627':	// �
			case '\u0644':	// �
			case '\u0645':	// �
			case '\u0648':	// �
			case '\u0647':	// �
			case '\u0631':	// �
			case '\u0639':	// �
			case '\u0623':	// �
			case '\u062f':	// �
			case '\u0633':	// �
			case '\u0643':	// �
			case '\u062d':	// �
			case '\u0649':	// �
			case '\u0635':	// �
			case '\u0625':	// �
			case '\u0637':	// �
			case '\u0621':	// �
			case '\u0626':	// �
			case '\u0622':	// �
			case '\u0624':	// �
				zeroOneMask[i] = 1;
				break;
			case '\u0646':	// �
			case '\u064a':	// �
			case '\u0628':	// �
			case '\u0641':	// �
			case '\u0642':	// �
			case '\u062A':	// �
			case '\u0629':	// �
			case '\u062c':	// �
			case '\u0630':	// �
			case '\u062b':	// �
			case '\u062e':	// �
			case '\u0634':	// �
			case '\u0632':	// �
			case '\u0636':	// �
			case '\u063a':	// �
			case '\u0638':	// �
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
		case '\u0627':	// �
			isolated = true;
			break;
		case '\u0648':	// �
			isolated = true;
			break;
		case '\u0631':	// �
			isolated = true;
			break;
		case '\u0623':	// �
			isolated = true;
			break;
		case '\u062f':	// �
			isolated = true;
			break;
		case '\u0629':	// �
			isolated = true;
			break;
		case '\u0649':	// �
			isolated = true;
			break;
		case '\u0625':	// �
			isolated = true;
			break;
		case '\u0630':	// �
			isolated = true;
			break;
		case '\u0632':	// �
			isolated = true;
			break;
		case '\u0621':	// �
			isolated = true;
			break;
		case '\u0622':	// �
			isolated = true;
			break;
		case '\u0624':	// �
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
		case '\u0644':	// �
			connected = true;
			break;
		case '\u0645':	// �
			connected = true;
			break;
		case '\u0647':	// �
			connected = true;
			break;
		case '\u0639':	// �
			connected = true;
			break;
		case '\u0633':	// �
			connected = true;
			break;
		case '\u0643':	// �
			connected = true;
			break;
		case '\u062d':	// �
			connected = true;
			break;
		case '\u0635':	// �
			connected = true;
			break;
		case '\u0637':	// �
			connected = true;
			break;
		case '\u0626':	// �
			connected = true;
			break;
		case '\u0646':	// �
			connected = true;
			break;
		case '\u064a':	// �
			connected = true;
			break;
		case '\u0628':	// �
			connected = true;
			break;
		case '\u0641':	// �
			connected = true;
			break;
		case '\u0642':	// �
			connected = true;
			break;
		case '\u062A':	// �
			connected = true;
			break;
		case '\u062c':	// �
			connected = true;
			break;
		case '\u062b':	// �
			connected = true;
			break;
		case '\u062e':	// �
			connected = true;
			break;
		case '\u0634':	// �
			connected = true;
			break;
		case '\u0636':	// �
			connected = true;
			break;
		case '\u063a':	// �
			connected = true;
			break;
		case '\u0638':	// �
			connected = true;
			break;
		case '\u0640':	// � TATWEEL
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
