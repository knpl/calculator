package com.knpl.simplecalculator.nodes;

import com.knpl.simplecalculator.visitors.Visitor;

public class BuiltinConstDefs {
	
	public static final ConstDef[] builtinConstDefs = {
		new PiDef(), new EDef(), new IDef()
	};
	
	/* Constants */
	public static class PiDef extends ConstDef {
		public static final String 
			NAME = "\u03C0",
			DESCRIPTION = "The ratio of a " +
						  "circle's circumference to its diameter.";
		
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
			return getName() + " = " + DESCRIPTION;
		}
		
		@Override
		public Object accept(Visitor v) throws Exception {
			return v.visit(this);
		}
	}
	
	public static class EDef extends ConstDef {
		public static final String 
			NAME = "e",
			DESCRIPTION = "Euler's constant. The base of " +
						  "the natural logarithm and exponential function.";

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
			return getName() + " = " + DESCRIPTION;
		}

		@Override
		public Object accept(Visitor v) throws Exception {
			return v.visit(this);
		}
	}
	
	public static class IDef extends ConstDef {
		public static final String 
			NAME = "i",
			DESCRIPTION = "The imaginary unit. The square root of -1.";

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
			return getName() + " = " + DESCRIPTION;
		}

		@Override
		public Object accept(Visitor v) throws Exception {
			return v.visit(this);
		}
	}
}
