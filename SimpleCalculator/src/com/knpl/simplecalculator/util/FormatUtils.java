package com.knpl.simplecalculator.util;

import java.util.Arrays;

import com.knpl.simplecalculator.nodes.Complex;
import com.knpl.simplecalculator.nodes.Num;
import com.knpl.simplecalculator.nodes.RealDouble;

public class FormatUtils {
	
	public static String zeroes(int n) {
		char[] zeroes = new char[n];
		Arrays.fill(zeroes, '0');
		return new String(zeroes);
	}
	
	public static String format(Num a, int n, boolean polar) {
		if (a instanceof Complex) {
			Complex z = (Complex) a;
			return polar ? polarFormat(z, n) : cartesianFormat(z, n);
		}
		else if (a instanceof RealDouble) {
			return format(((RealDouble)a).getValue(), n);
		}
		return "What kind of number is this?";
	}
	
	public static String format(Complex z, int n, boolean polar) {
		return polar ? polarFormat(z, n) : cartesianFormat(z, n);
	}
	
	public static String cartesianFormat(Complex z, int n) {
		if (z.im() == 0.0) {
			return format(z.re(), n);
		}
		else if (z.re() == 0.0) {
			if (z.im() == 1.0)
				return "i";
			return format(z.im(), n)+"*i";
		}
		else {
			String im = "";
			if (z.im() != 1.0)
				im = format(z.im(), n)+"*";
			return format(z.re(), n) + " + " + im + "i";
		}
	}
	
	public static String polarFormat(Complex z, int n) {
		double mod = z.mod();
		double arg = z.arg();

		if (arg == 0.0) {
			return format(mod, n);
		}
		else if (mod == 0.0) {
			return "0";
		}
		else if (mod == 1.0) {
			return "e^("+format(arg, n)+" * i)";
		}
		else {
			return format(mod, n) + " * e^(" + format(arg, n)+" * i)";
		}
	}
	
	public static String format(double x, int n) {
		boolean sign = x < 0;
		if (sign)
			x = -x;
		if (x == 0.0)
			return "0";
		if (Double.isNaN(x))
			return "undefined";
		if (Double.isInfinite(x))
			return (sign ? "-" : "") + "\u221E";
		
		StringBuffer sb = new StringBuffer();
		if (sign)
			sb.append('-');
		
		// f is the order of the final printed digit.
		// i is the order of the initial printed digit.
		int f = (int) Math.floor(Math.log10(x));
		int i = f - n + 1;
		long frac = Math.round((x * Math.pow(10, -i)));
		
		// The n-digit approximation of x: x = frac * 10^f
		
		// Eliminate trailing zeroes.
		while ((frac % 10) == 0) {
			frac /= 10;
			i += 1;
		}
		// This only happens when 9..9 is rounded to 10..0
		if (i > f)
			f += 1; 
		
		// The n-digit approximation of x is still: x = frac * 10^f
		// Except that frac now does not have a single factor of 10 in it. 
		
		// exp is the value of the exponent of 10 in scientific notation.
		// It only shows up when the number to format has a most significant 
		// digit with order greater than n - 1 or smaller than -1. 
		int exp = 0;
		if (f > n - 1 || (i < 1 - n && f < -1)) {
			exp = f;
			i -= f;
			f = 0;
		}
		else if (i > 0) {
			frac *= (long)Math.pow(10, i);
			i = 0;
		}
		else if (f < 0) {
			f = 0;
		}
		
		// Format number. Add a space every three orders of magnitude,
		// and place a decimal point in the appropriate spot.
		final int len = f - i + 1;
		char[] buf = new char[len + len/3 + 3]; // On the safe side.
		int p = buf.length - 1; // Position variable.
		
		long digit;
		for (int k = i; k <= f; ++k) {
			digit = frac % 10;
			frac  = frac / 10;
			if (k != i) {
				if (k == 0)
					buf[p--] = '.';
				else if ((k % 3) == 0)
					buf[p--] = ' ';
			}
			buf[p--] = (char) ('0' + digit);
		}
		p++;
		
		sb.append(buf, p, buf.length - p);
		if (exp != 0)
			sb.append(" E").append(exp);
		return sb.toString();
	}
}
