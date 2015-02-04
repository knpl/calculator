package com.knpl.simplecalculator.nodes;

import java.util.ArrayList;
import java.util.List;


import com.knpl.simplecalculator.FunctionDefinition;
import com.knpl.simplecalculator.nodes.Expr;
import com.knpl.simplecalculator.nodes.Func;
import com.knpl.simplecalculator.visitors.Visitor;

public class Builtins {

	public static class Min extends Func {
		private Expr arguments[];
		
		public Min(FunctionDefinition definition, Expr arg1, Expr arg2) {
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
		public Object accept(Visitor v) throws Exception {
			return v.visit(this);
		}

		@Override
		public Expr getArg(int i) {
			return arguments[i];
		}
	}
	
	public static class Max extends Func {
		private Expr arguments[];
		
		public Max(FunctionDefinition definition, Expr arg1, Expr arg2) {
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
		public Object accept(Visitor v) throws Exception {
			return v.visit(this);
		}

		@Override
		public Expr getArg(int i) {
			return arguments[i];
		}
	}
	
	public static class Sqrt extends Func {
		private Expr argument;
		
		public Sqrt(FunctionDefinition definition, Expr argument) {
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
		public Object accept(Visitor v) throws Exception {
			return v.visit(this);
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
		
		public Abs(FunctionDefinition definition, Expr argument) {
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
		public Object accept(Visitor v) throws Exception {
			return v.visit(this);
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
		
		public Log(FunctionDefinition definition, Expr argument) {
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
		public Object accept(Visitor v) throws Exception {
			return v.visit(this);
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
		
		public Exp(FunctionDefinition definition, Expr argument) {
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
		public Object accept(Visitor v) throws Exception {
			return v.visit(this);
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
		
		public Sinh(FunctionDefinition definition, Expr argument) {
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
		public Object accept(Visitor v) throws Exception {
			return v.visit(this);
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
		
		public Cosh(FunctionDefinition definition, Expr argument) {
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
		public Object accept(Visitor v) throws Exception {
			return v.visit(this);
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
		
		public Sin(FunctionDefinition definition, Expr argument) {
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
		public Object accept(Visitor v) throws Exception {
			return v.visit(this);
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
		
		public Cos(FunctionDefinition definition, Expr argument) {
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
		public Object accept(Visitor v) throws Exception {
			return v.visit(this);
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
		
		public Tan(FunctionDefinition definition, Expr argument) {
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
		public Object accept(Visitor v) throws Exception {
			return v.visit(this);
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
		
		public Asin(FunctionDefinition definition, Expr argument) {
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
		public Object accept(Visitor v) throws Exception {
			return v.visit(this);
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
		
		public Acos(FunctionDefinition definition, Expr argument) {
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
		public Object accept(Visitor v) throws Exception {
			return v.visit(this);
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
		
		public Atan(FunctionDefinition definition, Expr argument) {
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
		public Object accept(Visitor v) throws Exception {
			return v.visit(this);
		}

		@Override
		public Expr getArg(int i) {
			if (i != 0)
				throw new IndexOutOfBoundsException();
			return argument;
		}
	}
}
