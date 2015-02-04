package com.knpl.simplecalculator;

import java.util.Arrays;
import java.util.List;

import com.knpl.simplecalculator.nodes.Func;
import com.knpl.simplecalculator.nodes.Call;
import com.knpl.simplecalculator.nodes.Expr;
import com.knpl.simplecalculator.nodes.Signature;
import com.knpl.simplecalculator.nodes.Var;
import com.knpl.simplecalculator.nodes.Builtins.*;

public class BuiltinFuncDefs {
	
	public static final FunctionDefinition builtinFunctions[] = 
	{
		new MinDefinition(),
		new MaxDefinition(),
		new SqrtDefinition(),
		new AbsDefinition(),
		new LogDefinition(),
		new ExpDefinition(),
		new SinhDefinition(),
		new CoshDefinition(),
		new SinDefinition(),
		new CosDefinition(),
		new TanDefinition(),
		new AsinDefinition(),
		new AcosDefinition(),
		new AtanDefinition()
	};
	
	public static class MinDefinition implements FunctionDefinition {
		private final Signature sig;
		
		public MinDefinition() {
			sig = new Signature("min", Arrays.asList(new Var("a"), new Var("b")));
		}
		
		@Override
		public Func createFunction(Call call) throws Exception {
			if (!call.getName().equals("min")) {
				throw new Exception("Function name mismatch");
			}
			if (call.getArguments().size() != 2) {
				throw new Exception("Argument mismatch");
			}
			List<Expr> args = call.getArguments();
			return new Min(this, args.get(0), args.get(1));
		}

		@Override
		public Signature getSignature() {
			return sig;
		}
	}
	
	public static class MaxDefinition implements FunctionDefinition {
		private final Signature sig;
		
		public MaxDefinition() {
			sig = new Signature("max", Arrays.asList(new Var("a"), new Var("b")));
		}
		
		@Override
		public Func createFunction(Call call) throws Exception {
			if (!call.getName().equals("max")) {
				throw new Exception("Function name mismatch");
			}
			if (call.getArguments().size() != 2) {
				throw new Exception("Argument mismatch");
			}
			List<Expr> args = call.getArguments();
			return new Max(this, args.get(0), args.get(1));
		}

		@Override
		public Signature getSignature() {
			return sig;
		}
	}
	
	public static class SqrtDefinition implements FunctionDefinition {
		private final Signature sig;
		
		public SqrtDefinition() {
			sig = new Signature("sqrt", Arrays.asList(new Var("x")));
		}

		@Override
		public Signature getSignature() {
			return sig;
		}
		
		@Override
		public Func createFunction(Call call) throws Exception {
			if (!call.getName().equals("sqrt")) {
				throw new Exception("Function name mismatch");
			}
			if (call.getArguments().size() != 1) {
				throw new Exception("Argument mismatch");
			}
			return new Sqrt(this, call.getArguments().get(0));
		}
	}
	
	public static class AbsDefinition implements FunctionDefinition {
		private final Signature sig;
		
		public AbsDefinition() {
			sig = new Signature("abs", Arrays.asList(new Var("x")));
		}

		@Override
		public Signature getSignature() {
			return sig;
		}
		
		@Override
		public Func createFunction(Call call) throws Exception {
			if (!call.getName().equals("abs")) {
				throw new Exception("Function name mismatch");
			}
			if (call.getArguments().size() != 1) {
				throw new Exception("Argument mismatch");
			}
			return new Abs(this, call.getArguments().get(0));
		}
	}
	
	public static class LogDefinition implements FunctionDefinition {
		private final Signature sig;
		
		public LogDefinition() {
			sig = new Signature("log", Arrays.asList(new Var("x")));
		}

		@Override
		public Signature getSignature() {
			return sig;
		}
		
		@Override
		public Func createFunction(Call call) throws Exception {
			if (!call.getName().equals("log")) {
				throw new Exception("Function name mismatch");
			}
			if (call.getArguments().size() != 1) {
				throw new Exception("Argument mismatch");
			}
			return new Log(this, call.getArguments().get(0));
		}
	}
	
	public static class ExpDefinition implements FunctionDefinition {
		private final Signature sig;
		
		public ExpDefinition() {
			sig = new Signature("exp", Arrays.asList(new Var("x")));
		}

		@Override
		public Signature getSignature() {
			return sig;
		}
		@Override
		public Func createFunction(Call call) throws Exception {
			if (!call.getName().equals("exp")) {
				throw new Exception("Function name mismatch");
			}
			if (call.getArguments().size() != 1) {
				throw new Exception("Argument mismatch");
			}
			return new Exp(this, call.getArguments().get(0));
		}
	}
	
	public static class SinhDefinition implements FunctionDefinition {
		private final Signature sig;
		
		public SinhDefinition() {
			sig = new Signature("sinh", Arrays.asList(new Var("x")));
		}

		@Override
		public Signature getSignature() {
			return sig;
		}
		@Override
		public Func createFunction(Call call) throws Exception {
			if (!call.getName().equals("sinh")) {
				throw new Exception("Function name mismatch");
			}
			if (call.getArguments().size() != 1) {
				throw new Exception("Argument mismatch");
			}
			return new Sinh(this, call.getArguments().get(0));
		}
	}
	
	public static class CoshDefinition implements FunctionDefinition {
		private final Signature sig;
		
		public CoshDefinition() {
			sig = new Signature("cosh", Arrays.asList(new Var("x")));
		}

		@Override
		public Signature getSignature() {
			return sig;
		}
		
		@Override
		public Func createFunction(Call call) throws Exception {
			if (!call.getName().equals("cosh")) {
				throw new Exception("Function name mismatch");
			}
			if (call.getArguments().size() != 1) {
				throw new Exception("Argument mismatch");
			}
			return new Cosh(this, call.getArguments().get(0));
		}
	}
	
	public static class SinDefinition implements FunctionDefinition {
		private final Signature sig;
		
		public SinDefinition() {
			sig = new Signature("sin", Arrays.asList(new Var("x")));
		}

		@Override
		public Signature getSignature() {
			return sig;
		}
		
		@Override
		public Func createFunction(Call call) throws Exception {
			if (!call.getName().equals("sin")) {
				throw new Exception("Function name mismatch");
			}
			if (call.getArguments().size() != 1) {
				throw new Exception("Argument mismatch");
			}
			return new Sin(this, call.getArguments().get(0));
		}
	}
	
	public static class CosDefinition implements FunctionDefinition {
		private final Signature sig;
		
		public CosDefinition() {
			sig = new Signature("cos", Arrays.asList(new Var("x")));
		}

		@Override
		public Signature getSignature() {
			return sig;
		}
		
		@Override
		public Func createFunction(Call call) throws Exception {
			if (!call.getName().equals("cos")) {
				throw new Exception("Function name mismatch");
			}
			if (call.getArguments().size() != 1) {
				throw new Exception("Argument mismatch");
			}
			return new Cos(this, call.getArguments().get(0));
		}
	}
	
	public static class TanDefinition implements FunctionDefinition {
		private final Signature sig;
		
		public TanDefinition() {
			sig = new Signature("tan", Arrays.asList(new Var("x")));
		}

		@Override
		public Signature getSignature() {
			return sig;
		}
		
		@Override
		public Func createFunction(Call call) throws Exception {
			if (!call.getName().equals("tan")) {
				throw new Exception("Function name mismatch");
			}
			if (call.getArguments().size() != 1) {
				throw new Exception("Argument mismatch");
			}
			return new Tan(this, call.getArguments().get(0));
		}
	}
	
	public static class AsinDefinition implements FunctionDefinition {
		private final Signature sig;
		
		public AsinDefinition() {
			sig = new Signature("asin", Arrays.asList(new Var("x")));
		}

		@Override
		public Signature getSignature() {
			return sig;
		}
		
		@Override
		public Func createFunction(Call call) throws Exception {
			if (!call.getName().equals("asin")) {
				throw new Exception("Function name mismatch");
			}
			if (call.getArguments().size() != 1) {
				throw new Exception("Argument mismatch");
			}
			return new Asin(this, call.getArguments().get(0));
		}
	}
	
	public static class AcosDefinition implements FunctionDefinition {
		private final Signature sig;
		
		public AcosDefinition() {
			sig = new Signature("acos", Arrays.asList(new Var("x")));
		}

		@Override
		public Signature getSignature() {
			return sig;
		}
		
		@Override
		public Func createFunction(Call call) throws Exception {
			if (!call.getName().equals("acos")) {
				throw new Exception("Function name mismatch");
			}
			if (call.getArguments().size() != 1) {
				throw new Exception("Argument mismatch");
			}
			return new Acos(this, call.getArguments().get(0));
		}
	}
	
	public static class AtanDefinition implements FunctionDefinition {
		private final Signature sig;
		
		public AtanDefinition() {
			sig = new Signature("atan", Arrays.asList(new Var("x")));
		}

		@Override
		public Signature getSignature() {
			return sig;
		}
		
		@Override
		public Func createFunction(Call call) throws Exception {
			if (!call.getName().equals("atan")) {
				throw new Exception("Function name mismatch");
			}
			if (call.getArguments().size() != 1) {
				throw new Exception("Argument mismatch");
			}
			return new Atan(this, call.getArguments().get(0));
		}
	}
}
