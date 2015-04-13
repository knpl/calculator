package com.knpl.simplecalculator.nodes;

import com.knpl.simplecalculator.visitors.Visitor;

public class Builtins {
	
	/* Constants */
	public static class Pi extends ConstDef {
		public static final String NAME = "\u03C0";
		public static final String description = 
				"The ratio of a circle's circumference to its diameter.";
		
		@Override
		public String getName() {
			return NAME;
		}

		@Override
		public double getDouble() {
			return Math.PI;
		}

		@Override
		public Complex getComplex() {
			return new Complex(Math.PI, 0);
		}

		@Override
		public String getDescription() {
			return getName() + " = " + description;
		}
		
		@Override
		public Object accept(Visitor v) throws Exception {
			return v.visit(this);
		}
	}
	
	public static class Euler extends ConstDef {
		public static final String NAME = "e";
		public static final String description = 
				"Euler's constant. The base of the natural logarithm and exponential function.";

		@Override
		public String getName() {
			return NAME;
		}

		@Override
		public double getDouble() {
			return Math.E;
		}

		@Override
		public Complex getComplex() {
			return new Complex(Math.E, 0);
		}

		@Override
		public String getDescription() {
			return getName() + " = " + description;
		}

		@Override
		public Object accept(Visitor v) throws Exception {
			return v.visit(this);
		}
	}
	
	public static class Im extends ConstDef {
		public static final String NAME = "i";
		public static final String description = 
				"The imaginary unit. The square root of -1.";

		@Override
		public String getName() {
			return NAME;
		}

		@Override
		public double getDouble() {
			return Double.NaN;
		}

		@Override
		public Complex getComplex() {
			return new Complex(0, 1);
		}

		@Override
		public String getDescription() {
			return getName() + " = " + description;
		}

		@Override
		public Object accept(Visitor v) throws Exception {
			return v.visit(this);
		}
	}
}
