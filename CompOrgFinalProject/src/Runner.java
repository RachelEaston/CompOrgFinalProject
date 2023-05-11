import java.util.Scanner;
import java.lang.Math;


public class Runner {

	static final int k = 4; //number of bits in exp
	static final int BIAS = (int) (Math.pow(2, k-1) - 1);
	
	public static void main(String[] args) {
		Scanner console = new Scanner(System.in);
		
		while (true) {
			
			System.out.println("""
					Enter 0 to convert 8-bit floating point number to decimal
					1 to convert decimal to 8-bit floating point number
					2 to quit""");
			switch (console.nextInt()) {
				case 0 -> {
					System.out.println("Enter your floating point number(8 bits long)");
					binaryToDecimal(console.next());
				}
				case 1 -> {
					System.out.println("Enter your decimal number");
					decimalToBinary(console.nextDouble());
				}
				case 2 -> {
					System.out.println("Thank for playing. Goodbye! :)");
					return;
				}
				default -> System.out.println("Not a valid choice");
			}
		}
	}
	public static void binaryToDecimal(String binary) {
		// Replace with the 8-bit binary number to convert
		float decimal;
		int sign;
		int exponent = 0;
		float mantissa = 1.0f;

		// Determine the sign bit
		sign = (binary.charAt(0) == 1) ? -1 : 1;
		// Determine the exponent bits
		for (int i = 1; i <= 4; i++) {
			exponent += (binary.charAt(i) - '0') * Math.pow(2, 4 - i);
		}
		// Determine the mantissa bits
		for (int i = 5; i <= 7; i++) {
			mantissa += (binary.charAt(i) - '0') * Math.pow(2, (4 - i));
		}
		// Check if the number is denormalized, normalized, NaN, or infinity
		if (exponent == 0) { // denormalized
			mantissa--;
			decimal = sign * mantissa * (float) Math.pow(2, (1 - 7));
		} else if (exponent == 15 && mantissa == 1.0f) { // infinity
			decimal = sign * Float.POSITIVE_INFINITY;
		} else if (exponent == 15 && mantissa != 1.0f) { // NaN
			decimal = Float.NaN;
		} else { // normalized
			decimal = sign * mantissa * (float) Math.pow(2, exponent - 7);
		}

		System.out.println("your number \"" + binary + "\" to decimal -> " + decimal);
	}
	public static void decimalToBinary(double input) {
		String binary;
		int sign;
		int exponent;
		float mantissa;
		String decimalString = toBinary(input, 9);
		
		float decimal = Float.parseFloat(decimalString);
		
		sign = (decimal < 0) ? 1 : 0;
		
		decimal = Math.abs(decimal);
		
		// Check if the number is zero
		if (decimal == 0.0f) binary = "00000000";
		else {
			// Determine the exponent and mantissa
			exponent = (String.format("%.9f", decimal).split("\\.")[0].contains("1")) ? String.format("%.9f", decimal).split("\\.")[0].length() - 1 : -(String.format("%.9f", decimal).split("\\.")[1].indexOf("1") + 1);
			
			mantissa = (exponent < -6) ? Float.parseFloat(String.format("%.4f", decimal * (float)Math.pow(10, 6))) : Float.parseFloat(String.format("%.4f", decimal * (float)Math.pow(10, -exponent)));
			
			String substring = String.format("%.3f", mantissa).substring(Float.toString(mantissa).indexOf('.') + 1);
			binary = (exponent < -6) ? sign + "0000" + substring : sign + toBinary(exponent + BIAS, 4).split("\\.")[0] + substring;
		}

		System.out.println("your number \"" + input + "\" to 8-bit floating point -> " + binary);
	}
	
	public static String toBinary(double d, int precision) {
		long wholePart = (long) d;
		return wholeToBinary(wholePart) + '.' + fractionalToBinary(d - wholePart, precision);
	}
	
	private static String wholeToBinary(long l) {
		return Long.toBinaryString(l);
	}
	
	private static String fractionalToBinary(double num, int precision) {
		StringBuilder binary = new StringBuilder();
		while (num > 0 && binary.length() < precision) {
			double r = num * 2;
			if (r >= 1) {
				binary.append(1);
				num = r - 1;
			} else {
				binary.append(0);
				num = r;
			}
		}
		return binary.toString();
	}
}


