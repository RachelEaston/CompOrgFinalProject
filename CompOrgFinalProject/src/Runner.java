import java.util.Scanner;
import java.lang.Math;


public class Runner {
	
	public static void main(String[] args) {
		Scanner console = new Scanner(System.in);
		
		System.out.println("Enter 0 to convert 8-bit floating point number to decimal\n1 to convert decimal to 8-bit "
				+ "floating point number\n2 to quit");
		switch (console.nextInt()) {
			case 0:
				System.out.println("Enter your floating point number(8 bits long)");
				binaryToDecimal(console.next());
				break;
			case 1:
				System.out.println("Enter your decimal number");
				decimalToBinary(console.nextFloat());
				break;
			case 2:
				System.out.println("Thank for playing. Goodbye! :)");
				return;
			default: System.out.println("Not a valid choice");
		}
	}
	public static void binaryToDecimal(String binary) {
		//String binary = "10101010"; // Replace with the 8-bit binary number to convert
		float decimal = 0.0f;
		int sign;
		int exponent = 0;
		float mantissa = 1.0f;

		// Determine the sign bit
		if (binary.charAt(0) == 1)
			sign = -1;
		else
			sign = 1;

		// Determine the exponent bits
		for (int i = 1; i <= 3; i++) {
			exponent += (binary.charAt(i) - '0') * Math.pow(2, 3 - i);
		}

		// Determine the mantissa bits
		for (int i = 4; i <= 7; i++) {
			mantissa += (binary.charAt(i) - '0') * Math.pow(2, -(i - 3));
		}

		// Check if the number is denormalized, normalized, NaN, or infinity
		if (exponent == 0) { // denormalized
			decimal = sign * mantissa * (float) Math.pow(2, -(7 - 1 - 3));
		} else if (exponent == 7 && mantissa == 0.0f) { // infinity
			decimal = sign * Float.POSITIVE_INFINITY;
		} else if (exponent == 7 && mantissa != 0.0f) { // NaN
			decimal = Float.NaN;
		} else { // normalized
			decimal = sign * mantissa * (float) Math.pow(2, exponent - 3);
		}

		System.out.println("your number \"" + binary + "\" to decimal -> " + decimal);
	}
	public static void decimalToBinary(float decimal) {
		//float decimal = 13.625f; // Replace with the decimal number to convert
		String binary = "";
		int sign;
		int exponent = 0;
		float mantissa = 0.0f;
		
		if (decimal < 0)
			sign = 1;
		else
			sign = 0;

		decimal = Math.abs(decimal);

		// Check if the number is zero
		if (decimal == 0.0f) {
			binary = "00000000";
		} 
		else {
			// Determine the exponent and mantissa
			exponent = (int) Math.floor(Math.log(decimal) / Math.log(2));
			mantissa = (float) (decimal / Math.pow(2, exponent)) - 1.0f;

			// Normalize the mantissa
			while (mantissa < 0.5f) {
				mantissa *= 2.0f;
				exponent--;
			}

			// Check if the number is denormalized, normalized, NaN, or infinity
			if (exponent < -3) { // denormalized
				exponent += 3;
				mantissa *= (float) Math.pow(2, -exponent);
			} 
			else if (exponent > 3) { // infinity
				binary = sign + "1110000" + "0000";
			} 
			else if (Float.isNaN(decimal)) { // NaN
				binary = sign + "1110000" + "0001";
			} 
			else { // normalized
				exponent += 3;
				mantissa -= 1.0f;
			}

			// Convert the exponent and mantissa to binary
			String exponentBinary = Integer.toBinaryString(exponent);
			while (exponentBinary.length() < 3) {
				exponentBinary = "0" + exponentBinary;
			}
			String mantissaBinary = "";
			for (int i = 0; i < 4; i++) {
				mantissa *= 2.0f;
				if (mantissa >= 1.0f) {
					mantissaBinary += "1";
					mantissa -= 1.0f;
				} else {
					mantissaBinary += "0";
				}
			}

			binary = sign + exponentBinary + mantissaBinary;
		}

		System.out.println("your number \"" + decimal + "\" to 8-bit floating point -> " + binary);
	}

}


