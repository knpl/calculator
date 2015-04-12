package com.knpl.simplecalculator.nodes;

import java.util.List;

import com.knpl.simplecalculator.nodes.Expr;
import com.knpl.simplecalculator.nodes.MVFunc;
import com.knpl.simplecalculator.util.FuncDef;
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
		public Object accept(Visitor v) throws Exception {
			return v.visit(this);
		}

		@Override
		public Complex getComplex() {
			return new Complex(Math.PI, 0);
		}

		@Override
		public String getDescription() {
			return getName() + " = " + description;
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
		public Object accept(Visitor v) throws Exception {
			return v.visit(this);
		}

		@Override
		public Complex getComplex() {
			return new Complex(Math.E, 0);
		}

		@Override
		public String getDescription() {
			return getName() + " = " + description;
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
		public Object accept(Visitor v) throws Exception {
			return v.visit(this);
		}

		@Override
		public Complex getComplex() {
			return new Complex(0, 1);
		}

		@Override
		public String getDescription() {
			return getName() + " = " + description;
		}
	}
	
	/* Functions */
	public static class Min extends MVFunc {
		public Min(FuncDef definition, List<Expr> arguments) {
			super(definition, arguments);
		}
		
		@Override
		public Object accept(Visitor v) throws Exception {
			return v.visit(this);
		}
	}
	
	public static class Max extends MVFunc {
		public Max(FuncDef definition, List<Expr> arguments) {
			super(definition, arguments);
		}

		@Override
		public Object accept(Visitor v) throws Exception {
			return v.visit(this);
		}
	}
	
	public static class Floor extends SVFunc {
		
		public Floor(FuncDef definition, Expr argument) {
			super(definition, argument);
		}
		
		@Override
		public Object accept(Visitor v) throws Exception {
			return v.visit(this);
		}
	}
	
	public static class Ceil extends SVFunc {
		public Ceil(FuncDef definition, Expr argument) {
			super(definition, argument);
		}

		@Override
		public Object accept(Visitor v) throws Exception {
			return v.visit(this);
		}
	}
	
	public static class Sqrt extends SVFunc {
		public Sqrt(FuncDef definition, Expr argument) {
			super(definition, argument);
		}

		@Override
		public Object accept(Visitor v) throws Exception {
			return v.visit(this);
		}
	}
	
	public static class Abs extends SVFunc {
		public Abs(FuncDef definition, Expr argument) {
			super(definition, argument);
		}

		@Override
		public Object accept(Visitor v) throws Exception {
			return v.visit(this);
		}
	}
	
	public static class Log extends SVFunc {
		public Log(FuncDef definition, Expr argument) {
			super(definition, argument);
		}
		
		@Override
		public Object accept(Visitor v) throws Exception {
			return v.visit(this);
		}
	}
	
	public static class Exp extends SVFunc {
		public Exp(FuncDef definition, Expr argument) {
			super(definition, argument);
		}

		@Override
		public Object accept(Visitor v) throws Exception {
			return v.visit(this);
		}
	}
	
	public static class Sinh extends SVFunc {
		public Sinh(FuncDef definition, Expr argument) {
			super(definition, argument);
		}

		@Override
		public Object accept(Visitor v) throws Exception {
			return v.visit(this);
		}
	}
	
	public static class Cosh extends SVFunc {
		public Cosh(FuncDef definition, Expr argument) {
			super(definition, argument);
		}

		@Override
		public Object accept(Visitor v) throws Exception {
			return v.visit(this);
		}
	}
	
	public static class Tanh extends SVFunc {
		public Tanh(FuncDef definition, Expr argument) {
			super(definition, argument);
		}

		@Override
		public Object accept(Visitor v) throws Exception {
			return v.visit(this);
		}
	}
	
	public static class Sin extends SVFunc {
		public Sin(FuncDef definition, Expr argument) {
			super(definition, argument);
		}

		@Override
		public Object accept(Visitor v) throws Exception {
			return v.visit(this);
		}
	}
	
	public static class Cos extends SVFunc {
		public Cos(FuncDef definition, Expr argument) {
			super(definition, argument);
		}

		@Override
		public Object accept(Visitor v) throws Exception {
			return v.visit(this);
		}
	}
	
	public static class Tan extends SVFunc {
		public Tan(FuncDef definition, Expr argument) {
			super(definition, argument);
		}

		@Override
		public Object accept(Visitor v) throws Exception {
			return v.visit(this);
		}
	}
	
	public static class Asin extends SVFunc {
		public Asin(FuncDef definition, Expr argument) {
			super(definition, argument);
		}
		
		@Override
		public Object accept(Visitor v) throws Exception {
			return v.visit(this);
		}
	}
	
	public static class Acos extends SVFunc {
		public Acos(FuncDef definition, Expr argument) {
			super(definition, argument);
		}

		@Override
		public Object accept(Visitor v) throws Exception {
			return v.visit(this);
		}
	}
	
	public static class Atan extends SVFunc {
		public Atan(FuncDef definition, Expr argument) {
			super(definition, argument);
		}

		@Override
		public Object accept(Visitor v) throws Exception {
			return v.visit(this);
		}
	}
	
	public static class Erf extends SVFunc {
		public Erf(FuncDef definition, Expr argument) {
			super(definition, argument);
		}

		@Override
		public Object accept(Visitor v) throws Exception {
			return v.visit(this);
		}
	}
	
	public static class Gamma extends SVFunc {
		public Gamma(FuncDef definition, Expr argument) {
			super(definition, argument);
		}

		@Override
		public Object accept(Visitor v) throws Exception {
			return v.visit(this);
		}
	}
	
	public static class LogGamma extends SVFunc {
		public LogGamma(FuncDef definition, Expr argument) {
			super(definition, argument);
		}

		@Override
		public Object accept(Visitor v) throws Exception {
			return v.visit(this);
		}
	}
	
	public static class LogBeta extends MVFunc {
		public LogBeta(FuncDef definition, List<Expr> arguments) {
			super(definition, arguments);
		}
		
		@Override
		public Object accept(Visitor v) throws Exception {
			return v.visit(this);
		}
	}
}
