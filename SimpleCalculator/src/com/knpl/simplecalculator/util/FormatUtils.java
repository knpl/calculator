package com.knpl.simplecalculator.util;

import java.util.Arrays;

import com.knpl.simplecalculator.nodes.Complex;

public class FormatUtils {
	public static int flog(int base, double x) {
		return (int) Math.floor(Math.log(x)/Math.log(base));
	}
	
	public static String zeroes(int n) {
		char[] zeroes = new char[n];
		Arrays.fill(zeroes, '0');
		return new String(zeroes);
	}
	
	public static String format(Complex z, int n, int base, boolean polar) {
		if (polar) {
			double mod = z.mod();
			double arg = z.arg();

			if (arg == 0.0) {
				return format(mod, n, base);
			}
			else if (mod == 0.0) {
				return "0";
			}
			else if (mod == 1.0) {
				return "e^"+format(arg, n, base)+"*i";
			}
			else {
				return format(mod, n, base) + "*e^" + format(arg, n, base)+"*i";
			}
		}
		else {
			// Cartesian coordinates
			if (z.im() == 0.0) {
				return format(z.re(), n, base);
			}
			else if (z.re() == 0.0) {
				if (z.im() == 1.0)
					return "i";
				return format(z.im(), n, base)+"*i";
			}
			else {
				String im = "";
				if (z.im() != 1.0)
					im = format(z.im(), n, base)+"*";
				return format(z.re(), n, base) + " + " + im + "i";
			}
		}
	}
	
	public static String format(double x, int n, int base) {
		boolean sign = x < 0;
		if (x == 0.0 || x == -0.0) {
			return "0";
		}
		if (Double.isNaN(x)) {
			return "undefined";
		}
		if (Double.isInfinite(x)) {
			return (sign ? "-" : "") + "\u221E";
		}
		x = Math.abs(x);
		
		// Get the n most significant digits of x and store them in frac.
		// Round the least significant digit up.
		int stop = flog(base, x);
		int start = stop - n + 1;
		long frac = (long) Math.round(x * Math.pow(base, -start));
		
		// Skip over trailing zeroes
		long quo, rem;
		while (true) {
			rem = frac % base;
			frac = frac / base;
			if (rem != 0) {
				break;
			}
			start += 1;
		}
		// This only occurs when frac was rounded up from 99...9 to 100...0 (in case base = 10)
		if (start > stop) {
			stop += 1;
		}
		
		int len = stop - start + 1;
		int decp = -1;
		String prefix = "";
		String postfix = "";
		if (stop > n-1 || start < -n+1) {
			if (start < stop) {
				len += 1;
				decp = 1;
			}
			postfix = "e"+stop;
		}
		else if (stop >= 0 && start < 0) {
			len += 1; 
			decp = stop + 1;
		}
		else if (stop < 0) {
			prefix = "0."+zeroes(-stop-1);
		}
		else if (start >= 0) {
			postfix = zeroes(start);
		}
		else {
			return "undefined";
		}
		
		char[] buf = new char[len];
		buf[len - 1] = (char) ('0' + rem);
		for (int i = len - 2; i >= 0; i -= 1) {
			quo = frac / base;
			rem = frac % base;
			frac = quo;
			if (i == decp) {
				buf[i] = '.';
				i -= 1;
			}
			buf[i] = (char) ('0' + rem);
		}
	
		return (sign ? "-" : "") + prefix + new String(buf) + postfix;
	}
}
