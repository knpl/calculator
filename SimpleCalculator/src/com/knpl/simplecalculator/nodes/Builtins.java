package com.knpl.simplecalculator.nodes;

import java.util.ArrayList;
import java.util.List;


import com.knpl.simplecalculator.nodes.Expr;
import com.knpl.simplecalculator.nodes.Func;
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
		public <O, I> O accept(Visitor<O, I> v, I info) throws Exception {
			return v.visit(this, info);
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
		public <O, I> O accept(Visitor<O, I> v, I info) throws Exception {
			return v.visit(this, info);
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
		public <O, I> O accept(Visitor<O, I> v, I info) throws Exception {
			return v.visit(this, info);
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
	public static class Min extends Func {
		private Expr arguments[];
		
		public Min(FuncDef definition, Expr arg1, Expr arg2) {
			this.definition = definition;
			this.arguments = new Expr[2];
			arguments[0] = arg1;
			arguments[1] = arg2;
		}
		
		@Override
		public List<Expr> getArguments() {
			List<Expr> al = new ArrayList<Expr>(2);
			al.add(arguments[0]);
			al.add(arguments[1]);
			return al;
		}
		
		@Override
		public <O, I> O accept(Visitor<O, I> v, I info) throws Exception {
			return v.visit(this, info);
		}

		@Override
		public Expr getArg(int i) {
			return arguments[i];
		}
	}
	
	public static class Max extends Func {
		private Expr arguments[];
		
		public Max(FuncDef definition, Expr arg1, Expr arg2) {
			this.definition = definition;
			this.arguments = new Expr[2];
			arguments[0] = arg1;
			arguments[1] = arg2;
		}
		
		@Override
		public List<Expr> getArguments() {
			List<Expr> al = new ArrayList<Expr>(2);
			al.add(arguments[0]);
			al.add(arguments[1]);
			return al;
		}
		
		@Override
		public <O, I> O accept(Visitor<O, I> v, I info) throws Exception {
			return v.visit(this, info);
		}
		
		@Override
		public Expr getArg(int i) {
			return arguments[i];
		}
	}
	
	public static class Floor extends Func {
		private Expr argument;
		
		public Floor(FuncDef definition, Expr argument) {
			this.definition = definition;
			this.argument = argument;
		}

		@Override
		public List<Expr> getArguments() {
			List<Expr> al = new ArrayList<Expr>(1);
			al.add(argument);
			return al;
		}
		
		@Override
		public <O, I> O accept(Visitor<O, I> v, I info) throws Exception {
			return v.visit(this, info);
		}

		@Override
		public Expr getArg(int i) {
			if (i != 0)
				throw new IndexOutOfBoundsException();
			return argument;
		}
	}
	
	public static class Ceil extends Func {
		private Expr argument;
		
		public Ceil(FuncDef definition, Expr argument) {
			this.definition = definition;
			this.argument = argument;
		}

		@Override
		public List<Expr> getArguments() {
			List<Expr> al = new ArrayList<Expr>(1);
			al.add(argument);
			return al;
		}
		
		@Override
		public <O, I> O accept(Visitor<O, I> v, I info) throws Exception {
			return v.visit(this, info);
		}

		@Override
		public Expr getArg(int i) {
			if (i != 0)
				throw new IndexOutOfBoundsException();
			return argument;
		}
	}
	
	public static class Sqrt extends Func {
		private Expr argument;
		
		public Sqrt(FuncDef definition, Expr argument) {
			this.definition = definition;
			this.argument = argument;
		}

		@Override
		public List<Expr> getArguments() {
			List<Expr> al = new ArrayList<Expr>(1);
			al.add(argument);
			return al;
		}
		
		@Override
		public <O, I> O accept(Visitor<O, I> v, I info) throws Exception {
			return v.visit(this, info);
		}

		@Override
		public Expr getArg(int i) {
			if (i != 0)
				throw new IndexOutOfBoundsException();
			return argument;
		}
	}
	
	public static class Abs extends Func {
		private Expr argument;
		
		public Abs(FuncDef definition, Expr argument) {
			this.definition = definition;
			this.argument = argument;
		}

		@Override
		public List<Expr> getArguments() {
			List<Expr> al = new ArrayList<Expr>(1);
			al.add(argument);
			return al;
		}
		
		@Override
		public <O, I> O accept(Visitor<O, I> v, I info) throws Exception {
			return v.visit(this, info);
		}

		@Override
		public Expr getArg(int i) {
			if (i != 0)
				throw new IndexOutOfBoundsException();
			return argument;
		}
	}
	
	public static class Log extends Func {
		private Expr argument;
		
		public Log(FuncDef definition, Expr argument) {
			this.definition = definition;
			this.argument = argument;
		}
		
		@Override
		public List<Expr> getArguments() {
			List<Expr> al = new ArrayList<Expr>(1);
			al.add(argument);
			return al;
		}
		
		@Override
		public <O, I> O accept(Visitor<O, I> v, I info) throws Exception {
			return v.visit(this, info);
		}

		@Override
		public Expr getArg(int i) {
			if (i != 0)
				throw new IndexOutOfBoundsException();
			return argument;
		}
	}
	
	public static class Exp extends Func {
		private Expr argument;
		
		public Exp(FuncDef definition, Expr argument) {
			this.definition = definition;
			this.argument = argument;
		}

		@Override
		public List<Expr> getArguments() {
			List<Expr> al = new ArrayList<Expr>(1);
			al.add(argument);
			return al;
		}
		
		@Override
		public <O, I> O accept(Visitor<O, I> v, I info) throws Exception {
			return v.visit(this, info);
		}

		@Override
		public Expr getArg(int i) {
			if (i != 0)
				throw new IndexOutOfBoundsException();
			return argument;
		}
	}
	
	public static class Sinh extends Func {
		private Expr argument;
		
		public Sinh(FuncDef definition, Expr argument) {
			this.definition = definition;
			this.argument = argument;
		}

		@Override
		public List<Expr> getArguments() {
			List<Expr> al = new ArrayList<Expr>(1);
			al.add(argument);
			return al;
		}
		
		@Override
		public <O, I> O accept(Visitor<O, I> v, I info) throws Exception {
			return v.visit(this, info);
		}

		@Override
		public Expr getArg(int i) {
			if (i != 0)
				throw new IndexOutOfBoundsException();
			return argument;
		}
	}
	
	public static class Cosh extends Func {
		private Expr argument;
		
		public Cosh(FuncDef definition, Expr argument) {
			this.definition = definition;
			this.argument = argument;
		}

		@Override
		public List<Expr> getArguments() {
			List<Expr> al = new ArrayList<Expr>(1);
			al.add(argument);
			return al;
		}
		
		@Override
		public <O, I> O accept(Visitor<O, I> v, I info) throws Exception {
			return v.visit(this, info);
		}

		@Override
		public Expr getArg(int i) {
			if (i != 0)
				throw new IndexOutOfBoundsException();
			return argument;
		}
	}
	
	public static class Tanh extends Func {
		private Expr argument;
		
		public Tanh(FuncDef definition, Expr argument) {
			this.definition = definition;
			this.argument = argument;
		}

		@Override
		public List<Expr> getArguments() {
			List<Expr> al = new ArrayList<Expr>(1);
			al.add(argument);
			return al;
		}
		
		@Override
		public <O, I> O accept(Visitor<O, I> v, I info) throws Exception {
			return v.visit(this, info);
		}

		@Override
		public Expr getArg(int i) {
			if (i != 0)
				throw new IndexOutOfBoundsException();
			return argument;
		}
	}
	
	public static class Sin extends Func {
		private Expr argument;
		
		public Sin(FuncDef definition, Expr argument) {
			this.definition = definition;
			this.argument = argument;
		}

		@Override
		public List<Expr> getArguments() {
			List<Expr> al = new ArrayList<Expr>(1);
			al.add(argument);
			return al;
		}
		
		@Override
		public <O, I> O accept(Visitor<O, I> v, I info) throws Exception {
			return v.visit(this, info);
		}

		@Override
		public Expr getArg(int i) {
			if (i != 0)
				throw new IndexOutOfBoundsException();
			return argument;
		}
	}
	
	public static class Cos extends Func {
		private Expr argument;
		
		public Cos(FuncDef definition, Expr argument) {
			this.definition = definition;
			this.argument = argument;
		}

		@Override
		public List<Expr> getArguments() {
			List<Expr> al = new ArrayList<Expr>(1);
			al.add(argument);
			return al;
		}
		
		@Override
		public <O, I> O accept(Visitor<O, I> v, I info) throws Exception {
			return v.visit(this, info);
		}

		@Override
		public Expr getArg(int i) {
			if (i != 0)
				throw new IndexOutOfBoundsException();
			return argument;
		}
	}
	
	public static class Tan extends Func {
		private Expr argument;
		
		public Tan(FuncDef definition, Expr argument) {
			this.definition = definition;
			this.argument = argument;
		}

		@Override
		public List<Expr> getArguments() {
			List<Expr> al = new ArrayList<Expr>(1);
			al.add(argument);
			return al;
		}
		
		@Override
		public <O, I> O accept(Visitor<O, I> v, I info) throws Exception {
			return v.visit(this, info);
		}

		@Override
		public Expr getArg(int i) {
			if (i != 0)
				throw new IndexOutOfBoundsException();
			return argument;
		}
	}
	
	public static class Asin extends Func {
		private Expr argument;
		
		public Asin(FuncDef definition, Expr argument) {
			this.definition = definition;
			this.argument = argument;
		}
		
		@Override
		public List<Expr> getArguments() {
			List<Expr> al = new ArrayList<Expr>(1);
			al.add(argument);
			return al;
		}
		
		@Override
		public <O, I> O accept(Visitor<O, I> v, I info) throws Exception {
			return v.visit(this, info);
		}

		@Override
		public Expr getArg(int i) {
			if (i != 0)
				throw new IndexOutOfBoundsException();
			return argument;
		}
	}
	
	public static class Acos extends Func {
		private Expr argument;
		
		public Acos(FuncDef definition, Expr argument) {
			this.definition = definition;
			this.argument = argument;
		}

		@Override
		public List<Expr> getArguments() {
			List<Expr> al = new ArrayList<Expr>(1);
			al.add(argument);
			return al;
		}
		
		@Override
		public <O, I> O accept(Visitor<O, I> v, I info) throws Exception {
			return v.visit(this, info);
		}

		@Override
		public Expr getArg(int i) {
			if (i != 0)
				throw new IndexOutOfBoundsException();
			return argument;
		}
	}
	
	public static class Atan extends Func {
		private Expr argument;
		
		public Atan(FuncDef definition, Expr argument) {
			this.definition = definition;
			this.argument = argument;
		}

		@Override
		public List<Expr> getArguments() {
			List<Expr> al = new ArrayList<Expr>(1);
			al.add(argument);
			return al;
		}

		@Override
		public Expr getArg(int i) {
			if (i != 0)
				throw new IndexOutOfBoundsException();
			return argument;
		}
		
		@Override
		public <O, I> O accept(Visitor<O, I> v, I info) throws Exception {
			return v.visit(this, info);
		}
	}
	
	public static class Erf extends Func {
		private Expr argument;
		
		public Erf(FuncDef definition, Expr argument) {
			this.definition = definition;
			this.argument = argument;
		}

		@Override
		public List<Expr> getArguments() {
			List<Expr> al = new ArrayList<Expr>(1);
			al.add(argument);
			return al;
		}
		
		@Override
		public <O, I> O accept(Visitor<O, I> v, I info) throws Exception {
			return v.visit(this, info);
		}

		@Override
		public Expr getArg(int i) {
			if (i != 0)
				throw new IndexOutOfBoundsException();
			return argument;
		}
	}
	
	public static class Gamma extends Func {
		private Expr argument;
		
		public Gamma(FuncDef definition, Expr argument) {
			this.definition = definition;
			this.argument = argument;
		}

		@Override
		public List<Expr> getArguments() {
			List<Expr> al = new ArrayList<Expr>(1);
			al.add(argument);
			return al;
		}
		
		@Override
		public <O, I> O accept(Visitor<O, I> v, I info) throws Exception {
			return v.visit(this, info);
		}

		@Override
		public Expr getArg(int i) {
			if (i != 0)
				throw new IndexOutOfBoundsException();
			return argument;
		}
	}
	
	public static class LogGamma extends Func {
		private Expr argument;
		
		public LogGamma(FuncDef definition, Expr argument) {
			this.definition = definition;
			this.argument = argument;
		}

		@Override
		public List<Expr> getArguments() {
			List<Expr> al = new ArrayList<Expr>(1);
			al.add(argument);
			return al;
		}
		
		@Override
		public <O, I> O accept(Visitor<O, I> v, I info) throws Exception {
			return v.visit(this, info);
		}

		@Override
		public Expr getArg(int i) {
			if (i != 0)
				throw new IndexOutOfBoundsException();
			return argument;
		}
	}
	
	public static class LogBeta extends Func {
		private List<Expr> arguments;
		
		public LogBeta(FuncDef definition, Expr arg1, Expr arg2) {
			this.definition = definition;
			arguments = new ArrayList<Expr>(2);
			arguments.add(arg1);
			arguments.add(arg2);
		}

		@Override
		public List<Expr> getArguments() {
			return arguments;
		}
		
		@Override
		public <O, I> O accept(Visitor<O, I> v, I info) throws Exception {
			return v.visit(this, info);
		}

		@Override
		public Expr getArg(int i) {
			return arguments.get(i);
		}
	}
}
