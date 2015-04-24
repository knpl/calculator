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
		new ErfDef(), new GammaDef(), new LogGammaDef()
	};
	
	public static class MinDef extends MVFuncDef {
		public static final String DESCRIPTION = "The minimum of a and b.";
		
		public MinDef() {
			super(new Signature("min", Arrays.asList(new Var("a"), new Var("b"))), DESCRIPTION);
		}
		
		@Override
		public Object accept(Visitor v) throws Exception {
			return v.visit(this);
		}

		@Override
		public Num numEvaluate(List<Num> args) throws Exception {
			return args.get(0).min(args.get(1));
		}
	}
	
	public static class MaxDef extends MVFuncDef {
		public static final String DESCRIPTION = "The maximum of a and b.";
		
		public MaxDef() {
			super(new Signature("max", Arrays.asList(new Var("a"), new Var("b"))), DESCRIPTION);
		}

		@Override
		public Object accept(Visitor v) throws Exception {
			return v.visit(this);
		}

		@Override
		public Num numEvaluate(List<Num> args) throws Exception {
			return args.get(0).max(args.get(1));
		}
	}
	
	public static class FloorDef extends SVFuncDef {
		public static final String DESCRIPTION = "The integer part of x.";
		
		public FloorDef() {
			super(new Signature("floor", Arrays.asList(new Var("x"))), DESCRIPTION);
		}

		@Override
		public Object accept(Visitor v) throws Exception {
			return v.visit(this);
		}

		@Override
		public Num numEvaluate(Num arg) throws Exception {
			return arg.floor();
		}
	}

	
	public static class CeilDef extends SVFuncDef {
		public static final String DESCRIPTION = "The nearest integer greater than or equal to x.";
		
		public CeilDef() {
			super(new Signature("ceil", Arrays.asList(new Var("x"))), DESCRIPTION);
		}

		@Override
		public Object accept(Visitor v) throws Exception {
			return v.visit(this);
		}

		@Override
		public Num numEvaluate(Num arg) throws Exception {
			return arg.ceil();
		}
	}
	
	public static class SqrtDef extends SVFuncDef {
		public static final String DESCRIPTION = "The square root of x.";
		
		public SqrtDef() {
			super(new Signature("sqrt", Arrays.asList(new Var("x"))), DESCRIPTION);
		}

		@Override
		public Object accept(Visitor v) throws Exception {
			return v.visit(this);
		}

		@Override
		public Num numEvaluate(Num arg) throws Exception {
			return arg.sqrt();
		}
	}
	
	public static class AbsDef extends SVFuncDef {
		public static final String DESCRIPTION = "The absolute value of x.";
		
		public AbsDef() {
			super(new Signature("abs", Arrays.asList(new Var("x"))), DESCRIPTION);
		}

		@Override
		public Signature getSignature() {
			return sig;
		}

		@Override
		public Object accept(Visitor v) throws Exception {
			return v.visit(this);
		}

		@Override
		public Num numEvaluate(Num arg) throws Exception {
			return arg.abs();
		}
	}
	
	public static class LogDef extends SVFuncDef {
		public static final String DESCRIPTION = "The natural (base e) logarithm of x.";
		
		public LogDef() {
			super(new Signature("log", Arrays.asList(new Var("x"))), DESCRIPTION);
		}

		@Override
		public Object accept(Visitor v) throws Exception {
			return v.visit(this);
		}

		@Override
		public Num numEvaluate(Num arg) throws Exception {
			return arg.log();
		}
	}
	
	public static class ExpDef extends SVFuncDef {
		public static final String DESCRIPTION = "The exponential (base e) of x.";
		
		public ExpDef() {
			super(new Signature("exp", Arrays.asList(new Var("x"))), DESCRIPTION);
		}

		@Override
		public Object accept(Visitor v) throws Exception {
			return v.visit(this);
		}

		@Override
		public Num numEvaluate(Num arg) throws Exception {
			return arg.exp();
		}
	}
	
	public static class SinhDef extends SVFuncDef {
		public static final String DESCRIPTION = "The hyperbolic sine of x.";
		
		public SinhDef() {
			super(new Signature("sinh", Arrays.asList(new Var("x"))), DESCRIPTION);
		}

		@Override
		public Object accept(Visitor v) throws Exception {
			return v.visit(this);
		}

		@Override
		public Num numEvaluate(Num arg) throws Exception {
			return arg.sinh();
		}
	}
	
	public static class CoshDef extends SVFuncDef {
		public static final String DESCRIPTION = "The hyperbolic cosine of x.";
		
		public CoshDef() {
			super(new Signature("cosh", Arrays.asList(new Var("x"))), DESCRIPTION);
		}

		@Override
		public Object accept(Visitor v) throws Exception {
			return v.visit(this);
		}

		@Override
		public Num numEvaluate(Num arg) throws Exception {
			return arg.cosh();
		}
	}
	
	public static class TanhDef extends SVFuncDef {
		public static final String DESCRIPTION = "The hyperbolic tangent of x.";
		
		public TanhDef() {
			super(new Signature("tanh", Arrays.asList(new Var("x"))), DESCRIPTION);
		}

		@Override
		public Object accept(Visitor v) throws Exception {
			return v.visit(this);
		}

		@Override
		public Num numEvaluate(Num arg) throws Exception {
			return arg.tanh();
		}
	}
	
	public static class SinDef extends SVFuncDef {
		public static final String DESCRIPTION = "The sine of x.";
		
		public SinDef() {
			super(new Signature("sin", Arrays.asList(new Var("x"))), DESCRIPTION);
		}

		@Override
		public Object accept(Visitor v) throws Exception {
			return v.visit(this);
		}

		@Override
		public Num numEvaluate(Num arg) throws Exception {
			return arg.sin();
		}
	}
	
	public static class CosDef extends SVFuncDef {
		public static final String DESCRIPTION = "The cosine of x.";
		
		public CosDef() {
			super(new Signature("cos", Arrays.asList(new Var("x"))), DESCRIPTION);
		}

		@Override
		public Object accept(Visitor v) throws Exception {
			return v.visit(this);
		}

		@Override
		public Num numEvaluate(Num arg) throws Exception {
			return arg.cos();
		}
	}
	
	public static class TanDef extends SVFuncDef {
		public static final String DESCRIPTION = "The tangent of x.";
		
		public TanDef() {
			super(new Signature("tan", Arrays.asList(new Var("x"))), DESCRIPTION);
		}

		@Override
		public Object accept(Visitor v) throws Exception {
			return v.visit(this);
		}

		@Override
		public Num numEvaluate(Num arg) throws Exception {
			return arg.tan();
		}
	}
	
	public static class AsinDef extends SVFuncDef {
		public static final String DESCRIPTION = "The inverse sine (arcsin) of x.";
		
		public AsinDef() {
			super(new Signature("asin", Arrays.asList(new Var("x"))), DESCRIPTION);
		}

		@Override
		public Object accept(Visitor v) throws Exception {
			return v.visit(this);
		}

		@Override
		public Num numEvaluate(Num arg) throws Exception {
			return arg.asin();
		}
	}
	
	public static class AcosDef extends SVFuncDef {
		public static final String DESCRIPTION = "The inverse cosine (arccos) of x.";
		
		public AcosDef() {
			super(new Signature("acos", Arrays.asList(new Var("x"))), DESCRIPTION);
		}

		@Override
		public Object accept(Visitor v) throws Exception {
			return v.visit(this);
		}

		@Override
		public Num numEvaluate(Num arg) throws Exception {
			return arg.acos();
		}
	}
	
	public static class AtanDef extends SVFuncDef {
		public static final String DESCRIPTION = "The inverse tangent (arctan) of x.";
		
		public AtanDef() {
			super(new Signature("atan", Arrays.asList(new Var("x"))), DESCRIPTION);
		}

		@Override
		public Object accept(Visitor v) throws Exception {
			return v.visit(this);
		}

		@Override
		public Num numEvaluate(Num arg) throws Exception {
			return arg.atan();
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
		public static final String DESCRIPTION = "The logarithm of the gamma function.";
		
		public LogGammaDef() {
			super(new Signature("log\u0393", Arrays.asList(new Var("x"))), DESCRIPTION);
		}

		@Override
		public Object accept(Visitor v) throws Exception {
			return v.visit(this);
		}

		@Override
		public Num numEvaluate(Num arg) throws Exception {
			return arg.loggamma();
		}
	}
}
