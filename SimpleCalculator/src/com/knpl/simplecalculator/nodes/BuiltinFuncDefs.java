package com.knpl.simplecalculator.nodes;

import java.util.Arrays;
import java.util.List;

import com.knpl.simplecalculator.visitors.Visitor;

public class BuiltinFuncDefs {
	
	public static final FuncDef builtinFuncDefs[] = {
		new MinDef(), new MaxDef(),
		new CeilDef(), new FloorDef(),
		new SqrtDef(), new AbsDef(),
		new LogDef(), new ExpDef(),
		new SinhDef(), new CoshDef(), new TanhDef(),
		new SinDef(), new CosDef(), new TanDef(),
		new AsinDef(), new AcosDef(), new AtanDef(),
		new ErfDef(), new GammaDef(), new LogGammaDef(),
		new ReDef(), new ImDef(), new ArgDef(), new ModDef(), new ConjDef()
	};
	
	public static class MinDef extends MVFuncDef {
		public static final String DESCRIPTION = "The minimum of a and b.";
		
		public MinDef() {
			super(new Signature("min", Arrays.asList(new Var("a"), new Var("b"))), DESCRIPTION);
		}
		
		@Override
		public Num numEvaluate(List<Num> args) throws Exception {
			return args.get(0).min(args.get(1));
		}

		@Override
		public Object accept(Visitor v) throws Exception {
			return v.visit(this);
		}
	}
	
	public static class MaxDef extends MVFuncDef {
		public static final String DESCRIPTION = "The maximum of a and b.";
		
		public MaxDef() {
			super(new Signature("max", Arrays.asList(new Var("a"), new Var("b"))), DESCRIPTION);
		}

		@Override
		public Num numEvaluate(List<Num> args) throws Exception {
			return args.get(0).max(args.get(1));
		}

		@Override
		public Object accept(Visitor v) throws Exception {
			return v.visit(this);
		}
	}
	
	public static class FloorDef extends SVFuncDef {
		public static final String DESCRIPTION = "The integer part of x.";
		
		public FloorDef() {
			super(new Signature("floor", Arrays.asList(new Var("x"))), DESCRIPTION);
		}

		@Override
		public Num numEvaluate(Num arg) throws Exception {
			return arg.floor();
		}

		@Override
		public Object accept(Visitor v) throws Exception {
			return v.visit(this);
		}
	}

	public static class CeilDef extends SVFuncDef {
		public static final String DESCRIPTION = "The nearest integer greater than or equal to x.";
		
		public CeilDef() {
			super(new Signature("ceil", Arrays.asList(new Var("x"))), DESCRIPTION);
		}

		@Override
		public Num numEvaluate(Num arg) throws Exception {
			return arg.ceil();
		}

		@Override
		public Object accept(Visitor v) throws Exception {
			return v.visit(this);
		}
	}
	
	public static class SqrtDef extends SVFuncDef {
		public static final String DESCRIPTION = "The square root.";
		
		public SqrtDef() {
			super(new Signature("sqrt", Arrays.asList(new Var("x"))), DESCRIPTION);
		}

		@Override
		public Num numEvaluate(Num arg) throws Exception {
			return arg.sqrt();
		}

		@Override
		public Object accept(Visitor v) throws Exception {
			return v.visit(this);
		}
	}
	
	public static class AbsDef extends SVFuncDef {
		public static final String DESCRIPTION = "The absolute value.";
		
		public AbsDef() {
			super(new Signature("abs", Arrays.asList(new Var("x"))), DESCRIPTION);
		}

		@Override
		public Signature getSignature() {
			return sig;
		}

		@Override
		public Num numEvaluate(Num arg) throws Exception {
			return arg.abs();
		}

		@Override
		public Object accept(Visitor v) throws Exception {
			return v.visit(this);
		}
	}
	
	public static class LogDef extends SVFuncDef {
		public static final String DESCRIPTION = "The natural logarithm.";
		
		public LogDef() {
			super(new Signature("log", Arrays.asList(new Var("x"))), DESCRIPTION);
		}

		@Override
		public Num numEvaluate(Num arg) throws Exception {
			return arg.log();
		}

		@Override
		public Object accept(Visitor v) throws Exception {
			return v.visit(this);
		}
	}
	
	public static class ExpDef extends SVFuncDef {
		public static final String DESCRIPTION = "The base e exponential function.";
		
		public ExpDef() {
			super(new Signature("exp", Arrays.asList(new Var("x"))), DESCRIPTION);
		}

		@Override
		public Num numEvaluate(Num arg) throws Exception {
			return arg.exp();
		}

		@Override
		public Object accept(Visitor v) throws Exception {
			return v.visit(this);
		}
	}
	
	public static class SinhDef extends SVFuncDef {
		public static final String DESCRIPTION = "The hyperbolic sine.";
		
		public SinhDef() {
			super(new Signature("sinh", Arrays.asList(new Var("x"))), DESCRIPTION);
		}

		@Override
		public Num numEvaluate(Num arg) throws Exception {
			return arg.sinh();
		}

		@Override
		public Object accept(Visitor v) throws Exception {
			return v.visit(this);
		}
	}
	
	public static class CoshDef extends SVFuncDef {
		public static final String DESCRIPTION = "The hyperbolic cosine.";
		
		public CoshDef() {
			super(new Signature("cosh", Arrays.asList(new Var("x"))), DESCRIPTION);
		}

		@Override
		public Num numEvaluate(Num arg) throws Exception {
			return arg.cosh();
		}

		@Override
		public Object accept(Visitor v) throws Exception {
			return v.visit(this);
		}
	}
	
	public static class TanhDef extends SVFuncDef {
		public static final String DESCRIPTION = "The hyperbolic tangent.";
		
		public TanhDef() {
			super(new Signature("tanh", Arrays.asList(new Var("x"))), DESCRIPTION);
		}

		@Override
		public Num numEvaluate(Num arg) throws Exception {
			return arg.tanh();
		}

		@Override
		public Object accept(Visitor v) throws Exception {
			return v.visit(this);
		}
	}
	
	public static class SinDef extends SVFuncDef {
		public static final String DESCRIPTION = "The sine function.";
		
		public SinDef() {
			super(new Signature("sin", Arrays.asList(new Var("x"))), DESCRIPTION);
		}

		@Override
		public Num numEvaluate(Num arg) throws Exception {
			return arg.sin();
		}

		@Override
		public Object accept(Visitor v) throws Exception {
			return v.visit(this);
		}
	}
	
	public static class CosDef extends SVFuncDef {
		public static final String DESCRIPTION = "The cosine function.";
		
		public CosDef() {
			super(new Signature("cos", Arrays.asList(new Var("x"))), DESCRIPTION);
		}

		@Override
		public Num numEvaluate(Num arg) throws Exception {
			return arg.cos();
		}

		@Override
		public Object accept(Visitor v) throws Exception {
			return v.visit(this);
		}
	}
	
	public static class TanDef extends SVFuncDef {
		public static final String DESCRIPTION = "The tangent function.";
		
		public TanDef() {
			super(new Signature("tan", Arrays.asList(new Var("x"))), DESCRIPTION);
		}

		@Override
		public Num numEvaluate(Num arg) throws Exception {
			return arg.tan();
		}

		@Override
		public Object accept(Visitor v) throws Exception {
			return v.visit(this);
		}
	}
	
	public static class AsinDef extends SVFuncDef {
		public static final String DESCRIPTION = "The inverse sine (arcsin).";
		
		public AsinDef() {
			super(new Signature("asin", Arrays.asList(new Var("x"))), DESCRIPTION);
		}

		@Override
		public Num numEvaluate(Num arg) throws Exception {
			return arg.asin();
		}

		@Override
		public Object accept(Visitor v) throws Exception {
			return v.visit(this);
		}
	}
	
	public static class AcosDef extends SVFuncDef {
		public static final String DESCRIPTION = "The inverse cosine (arccos).";
		
		public AcosDef() {
			super(new Signature("acos", Arrays.asList(new Var("x"))), DESCRIPTION);
		}

		@Override
		public Num numEvaluate(Num arg) throws Exception {
			return arg.acos();
		}

		@Override
		public Object accept(Visitor v) throws Exception {
			return v.visit(this);
		}
	}
	
	public static class AtanDef extends SVFuncDef {
		public static final String DESCRIPTION = "The inverse tangent (arctan).";
		
		public AtanDef() {
			super(new Signature("atan", Arrays.asList(new Var("x"))), DESCRIPTION);
		}

		@Override
		public Num numEvaluate(Num arg) throws Exception {
			return arg.atan();
		}

		@Override
		public Object accept(Visitor v) throws Exception {
			return v.visit(this);
		}
	}
	
	public static class ErfDef extends SVFuncDef {
		public static final String DESCRIPTION = "The error function.";
		
		public ErfDef() {
			super(new Signature("erf", Arrays.asList(new Var("x"))), DESCRIPTION);
		}

		@Override
		public Object accept(Visitor v) throws Exception {
			return v.visit(this);
		}

		@Override
		public Num numEvaluate(Num arg) throws Exception {
			return arg.erf();
		}
	}
	
	public static class GammaDef extends SVFuncDef {
		public static final String DESCRIPTION = "The gamma function.";
		
		public GammaDef() {
			super(new Signature("\u0393", Arrays.asList(new Var("x"))), DESCRIPTION);
		}

		@Override
		public Object accept(Visitor v) throws Exception {
			return v.visit(this);
		}

		@Override
		public Num numEvaluate(Num arg) throws Exception {
			return arg.gamma();
		}
	}
	
	public static class LogGammaDef extends SVFuncDef {
		public static final String DESCRIPTION = "The log of the gamma.";
		
		public LogGammaDef() {
			super(new Signature("log\u0393", Arrays.asList(new Var("x"))), DESCRIPTION);
		}

		@Override
		public Num numEvaluate(Num arg) throws Exception {
			return arg.loggamma();
		}

		@Override
		public Object accept(Visitor v) throws Exception {
			return v.visit(this);
		}
	}
	
	public static class ReDef extends SVFuncDef {
		public static final String DESCRIPTION = "The real part of z.";
	
		public ReDef() {
			super(new Signature("re", Arrays.asList(new Var("z"))), DESCRIPTION);
		}
	
		@Override
		public Num numEvaluate(Num arg) throws Exception {
			return arg.re();
		}

		@Override
		public Object accept(Visitor v) throws Exception {
			return v.visit(this);
		}
	}
	
	public static class ImDef extends SVFuncDef {
		public static final String DESCRIPTION = "The imaginary part of z.";
	
		public ImDef() {
			super(new Signature("im", Arrays.asList(new Var("z"))), DESCRIPTION);
		}
	
		@Override
		public Num numEvaluate(Num arg) throws Exception {
			return arg.im();
		}

		@Override
		public Object accept(Visitor v) throws Exception {
			return v.visit(this);
		}
	}
	
	public static class ModDef extends SVFuncDef {
		public static final String DESCRIPTION = "The modulus of z.";
	
		public ModDef() {
			super(new Signature("mod", Arrays.asList(new Var("z"))), DESCRIPTION);
		}
	
		@Override
		public Num numEvaluate(Num arg) throws Exception {
			return arg.mod();
		}

		@Override
		public Object accept(Visitor v) throws Exception {
			return v.visit(this);
		}
	}
	
	public static class ArgDef extends SVFuncDef {
		public static final String DESCRIPTION = "The argument of z.";
	
		public ArgDef() {
			super(new Signature("arg", Arrays.asList(new Var("z"))), DESCRIPTION);
		}
	
		@Override
		public Num numEvaluate(Num arg) throws Exception {
			return arg.arg();
		}

		@Override
		public Object accept(Visitor v) throws Exception {
			return v.visit(this);
		}
	}
	
	public static class ConjDef extends SVFuncDef {
		public static final String DESCRIPTION = "The complex conjugate of z.";
	
		public ConjDef() {
			super(new Signature("conj", Arrays.asList(new Var("z"))), DESCRIPTION);
		}
	
		@Override
		public Num numEvaluate(Num arg) throws Exception {
			return arg.conj();
		}

		@Override
		public Object accept(Visitor v) throws Exception {
			return v.visit(this);
		}
	}
}
