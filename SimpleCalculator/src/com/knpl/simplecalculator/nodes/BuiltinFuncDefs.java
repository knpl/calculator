package com.knpl.simplecalculator.nodes;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.math3.special.Erf;
import org.apache.commons.math3.special.Gamma;

import com.knpl.simplecalculator.visitors.Visitor;

public class BuiltinFuncDefs {
	
	public static final FuncDef builtinFuncDefs[] = 
	{
		new MinDef(),
		new MaxDef(),
		new CeilDef(),
		new FloorDef(),
		new SqrtDef(),
		new AbsDef(),
		new LogDef(),
		new ExpDef(),
		new SinhDef(),
		new CoshDef(),
		new TanhDef(),
		new SinDef(),
		new CosDef(),
		new TanDef(),
		new AsinDef(),
		new AcosDef(),
		new AtanDef(),
		new ErfDef(),
		new GammaDef(),
		new LogGammaDef()
	};
	
	public static class MinDef extends FuncDef {
		public static final String description = "The minimum of a and b.";
		
		private final Signature sig;
		
		public MinDef() {
			sig = new Signature("min", Arrays.asList(new Var("a"), new Var("b")));
		}

		@Override
		public Signature getSignature() {
			return sig;
		}

		@Override
		public String getDescription() {
			return sig + " = " + description;
		}

		@Override
		public Complex complexEvaluate(List<Complex> args) throws Exception {
			Complex a = args.get(0);
			if (a.im() != 0.0) {
				throw new Exception("min only defined for real numbers");
			}
			Complex b = args.get(1);
			if (b.im() != 0.0) {
				throw new Exception("min only defined for real numbers");
			}
			return (b.re() < a.re()) ? b : a;
		}
		
		@Override
		public Double evaluate(List<Double> args) throws Exception {
			return Math.min(args.get(0), args.get(1));
		}
		
		@Override
		public Object accept(Visitor v) throws Exception {
			return v.visit(this);
		}
	}
	
	public static class MaxDef extends FuncDef {
		public static final String description = "The maximum of a and b.";
		
		private final Signature sig;
		
		public MaxDef() {
			sig = new Signature("max", Arrays.asList(new Var("a"), new Var("b")));
		}
		
		@Override
		public Signature getSignature() {
			return sig;
		}
		
		@Override
		public String getDescription() {
			return sig + " = " + description;
		}

		@Override
		public Complex complexEvaluate(List<Complex> args) throws Exception {
			Complex a = args.get(0);
			if (a.im() != 0.0) {
				throw new Exception("max only defined for real numbers");
			}
			Complex b = args.get(1);
			if (b.im() != 0.0) {
				throw new Exception("max only defined for real numbers");
			}
			return (b.re() > a.re()) ? b : a;
		}

		@Override
		public Double evaluate(List<Double> args) throws Exception {
			return Math.max(args.get(0), args.get(1));
		}
		
		@Override
		public Object accept(Visitor v) throws Exception {
			return v.visit(this);
		}
	}
	
	public static class FloorDef extends FuncDef {
		public static final String description = "The integer part of x.";
		
		private final Signature sig;
		
		public FloorDef() {
			sig = new Signature("floor", Arrays.asList(new Var("x")));
		}

		@Override
		public Signature getSignature() {
			return sig;
		}

		@Override
		public String getDescription() {
			return sig + " = " + description;
		}

		@Override
		public Complex complexEvaluate(List<Complex> args) throws Exception {
			Complex z = args.get(0);
			if (z.im() != 0.0) {
				throw new Exception("floor only defined for real numbers");
			}
			return z.setRe(Math.floor(z.re()));
		}

		@Override
		public Double evaluate(List<Double> args) throws Exception {
			return Math.floor(args.get(0));
		}
		
		@Override
		public Object accept(Visitor v) throws Exception {
			return v.visit(this);
		}
	}
	
	public static class CeilDef extends FuncDef {
		public static final String description = "The nearest integer greater than or equal to x.";
		
		private final Signature sig;
		
		public CeilDef() {
			sig = new Signature("ceil", Arrays.asList(new Var("x")));
		}

		@Override
		public Signature getSignature() {
			return sig;
		}

		@Override
		public String getDescription() {
			return sig + " = " + description;
		}

		@Override
		public Complex complexEvaluate(List<Complex> args) throws Exception {
			Complex z = args.get(0);
			if (z.im() != 0.0) {
				throw new Exception("ceil only defined for real numbers");
			}
			return z.setRe(Math.ceil(z.re()));
		}

		@Override
		public Double evaluate(List<Double> args) throws Exception {
			return Math.ceil(args.get(0));
		}
		
		@Override
		public Object accept(Visitor v) throws Exception {
			return v.visit(this);
		}
	}
	
	public static class SqrtDef extends FuncDef {
		public static final String description = "The square root of x.";
		
		private final Signature sig;
		
		public SqrtDef() {
			sig = new Signature("sqrt", Arrays.asList(new Var("x")));
		}

		@Override
		public Signature getSignature() {
			return sig;
		}

		@Override
		public String getDescription() {
			return sig + " = " + description;
		}

		@Override
		public Complex complexEvaluate(List<Complex> args) throws Exception {
			Complex z = args.get(0);
			if (z.im() == 0.0) {
				double re = z.re();
				if (re < 0.0) {
					z.setRe(0.0);
					z.setIm(Math.sqrt(-re));
				}
				else {
					z.setRe(Math.sqrt(re));
				}
				return z;
			}
			return z.sqrt();
		}

		@Override
		public Double evaluate(List<Double> args) throws Exception {
			return Math.sqrt(args.get(0));
		}
		
		@Override
		public Object accept(Visitor v) throws Exception {
			return v.visit(this);
		}
	}
	
	public static class AbsDef extends FuncDef {
		public static final String description = "The absolute value of x.";
		
		private final Signature sig;
		
		public AbsDef() {
			sig = new Signature("abs", Arrays.asList(new Var("x")));
		}

		@Override
		public Signature getSignature() {
			return sig;
		}

		@Override
		public String getDescription() {
			return sig + " = " + description;
		}

		@Override
		public Complex complexEvaluate(List<Complex> args) throws Exception {
			return new Complex(args.get(0).abs(), 0.0);
		}

		@Override
		public Double evaluate(List<Double> args) throws Exception {
			return Math.abs(args.get(0));
		}
		
		@Override
		public Object accept(Visitor v) throws Exception {
			return v.visit(this);
		}
	}
	
	public static class LogDef extends FuncDef {
		public static final String description = "The natural (base e) logarithm of x.";
		
		private final Signature sig;
		
		public LogDef() {
			sig = new Signature("log", Arrays.asList(new Var("x")));
		}

		@Override
		public Signature getSignature() {
			return sig;
		}

		@Override
		public String getDescription() {
			return sig + " = " + description;
		}

		@Override
		public Complex complexEvaluate(List<Complex> args) throws Exception {
			Complex z = args.get(0);
			return (z.re() >= 0.0 && z.im() == 0.0) ? z.setRe(Math.log(z.re()))
								   				    : z.log();
		}

		@Override
		public Double evaluate(List<Double> args) throws Exception {
			return Math.log(args.get(0));
		}
		
		@Override
		public Object accept(Visitor v) throws Exception {
			return v.visit(this);
		}
	}
	
	public static class ExpDef extends FuncDef {
		public static final String description = "The exponential (base e) of x.";
		
		private final Signature sig;
		
		public ExpDef() {
			sig = new Signature("exp", Arrays.asList(new Var("x")));
		}

		@Override
		public Signature getSignature() {
			return sig;
		}

		@Override
		public String getDescription() {
			return sig + " = " + description;
		}

		@Override
		public Complex complexEvaluate(List<Complex> args) throws Exception {
			Complex z = args.get(0);
			return (z.im() == 0.0) ? z.setRe(Math.exp(z.re()))
								   : z.exp();
		}

		@Override
		public Double evaluate(List<Double> args) throws Exception {
			return Math.exp(args.get(0));
		}
		
		@Override
		public Object accept(Visitor v) throws Exception {
			return v.visit(this);
		}
	}
	
	public static class SinhDef extends FuncDef {
		public static final String description = "The hyperbolic sine of x.";
		
		private final Signature sig;
		
		public SinhDef() {
			sig = new Signature("sinh", Arrays.asList(new Var("x")));
		}

		@Override
		public Signature getSignature() {
			return sig;
		}

		@Override
		public String getDescription() {
			return sig + " = " + description;
		}

		@Override
		public Complex complexEvaluate(List<Complex> args) throws Exception {
			Complex z = args.get(0);
			return (z.im() == 0.0) ? z.setRe(Math.sinh(z.re()))
								   : z.sinh();
		}

		@Override
		public Double evaluate(List<Double> args) throws Exception {
			return Math.sinh(args.get(0));
		}
		
		@Override
		public Object accept(Visitor v) throws Exception {
			return v.visit(this);
		}
	}
	
	public static class CoshDef extends FuncDef {
		public static final String description = "The hyperbolic cosine of x.";
		
		private final Signature sig;
		
		public CoshDef() {
			sig = new Signature("cosh", Arrays.asList(new Var("x")));
		}

		@Override
		public Signature getSignature() {
			return sig;
		}

		@Override
		public String getDescription() {
			return sig + " = " + description;
		}

		@Override
		public Complex complexEvaluate(List<Complex> args) throws Exception {
			Complex z = args.get(0);
			return (z.im() == 0.0) ? z.setRe(Math.cosh(z.re()))
								   : z.cosh();
		}

		@Override
		public Double evaluate(List<Double> args) throws Exception {
			return Math.cosh(args.get(0));
		}
		
		@Override
		public Object accept(Visitor v) throws Exception {
			return v.visit(this);
		}
	}
	
	public static class TanhDef extends FuncDef {
		public static final String description = "The hyperbolic tangent of x.";
		
		private final Signature sig;
		
		public TanhDef() {
			sig = new Signature("tanh", Arrays.asList(new Var("x")));
		}

		@Override
		public Signature getSignature() {
			return sig;
		}

		@Override
		public String getDescription() {
			return sig + " = " + description;
		}

		@Override
		public Complex complexEvaluate(List<Complex> args) throws Exception {
			Complex z = args.get(0);
			return (z.im() == 0.0) ? z.setRe(Math.tanh(z.re()))
								   : z.tanh();
		}

		@Override
		public Double evaluate(List<Double> args) throws Exception {
			return Math.tanh(args.get(0));
		}
		
		@Override
		public Object accept(Visitor v) throws Exception {
			return v.visit(this);
		}
	}
	
	public static class SinDef extends FuncDef {
		public static final String description = "The sine of x.";
		
		private final Signature sig;
		
		public SinDef() {
			sig = new Signature("sin", Arrays.asList(new Var("x")));
		}

		@Override
		public Signature getSignature() {
			return sig;
		}

		@Override
		public String getDescription() {
			return sig + " = " + description;
		}

		@Override
		public Complex complexEvaluate(List<Complex> args) throws Exception {
			Complex z = args.get(0);
			return (z.im() == 0.0) ? z.setRe(Math.sin(z.re()))
								   : z.sin();
		}

		@Override
		public Double evaluate(List<Double> args) throws Exception {
			return Math.sin(args.get(0));
		}
		
		@Override
		public Object accept(Visitor v) throws Exception {
			return v.visit(this);
		}
	}
	
	public static class CosDef extends FuncDef {
		public static final String description = "The cosine of x.";
		
		private final Signature sig;
		
		public CosDef() {
			sig = new Signature("cos", Arrays.asList(new Var("x")));
		}

		@Override
		public Signature getSignature() {
			return sig;
		}

		@Override
		public String getDescription() {
			return sig + " = " + description;
		}

		@Override
		public Complex complexEvaluate(List<Complex> args) throws Exception {
			Complex z = args.get(0);
			return (z.im() == 0.0) ? z.setRe(Math.cos(z.re()))
								   : z.cos();
		}

		@Override
		public Double evaluate(List<Double> args) throws Exception {
			return Math.cos(args.get(0));
		}
		
		@Override
		public Object accept(Visitor v) throws Exception {
			return v.visit(this);
		}
	}
	
	public static class TanDef extends FuncDef {
		public static final String description = "The tangent of x.";
		
		private final Signature sig;
		
		public TanDef() {
			sig = new Signature("tan", Arrays.asList(new Var("x")));
		}

		@Override
		public Signature getSignature() {
			return sig;
		}

		@Override
		public String getDescription() {
			return sig + " = " + description;
		}

		@Override
		public Complex complexEvaluate(List<Complex> args) throws Exception {
			Complex z = args.get(0);
			return (z.im() == 0.0) ? z.setRe(Math.tan(z.re()))
								   : z.tan();
		}

		@Override
		public Double evaluate(List<Double> args) throws Exception {
			return Math.tan(args.get(0));
		}
		
		@Override
		public Object accept(Visitor v) throws Exception {
			return v.visit(this);
		}
	}
	
	public static class AsinDef extends FuncDef {
		public static final String description = "The inverse sine (arcsin) of x.";
		
		private final Signature sig;
		
		public AsinDef() {
			sig = new Signature("asin", Arrays.asList(new Var("x")));
		}

		@Override
		public Signature getSignature() {
			return sig;
		}

		@Override
		public String getDescription() {
			return sig + " = " + description;
		}

		@Override
		public Complex complexEvaluate(List<Complex> args) throws Exception {
			Complex z = args.get(0);
			return (z.im() == 0.0) ? z.setRe(Math.asin(z.re()))
								   : z.asin();
		}

		@Override
		public Double evaluate(List<Double> args) throws Exception {
			return Math.asin(args.get(0));
		}
		
		@Override
		public Object accept(Visitor v) throws Exception {
			return v.visit(this);
		}
	}
	
	public static class AcosDef extends FuncDef {
		public static final String description = "The inverse cosine (arccos) of x.";
		
		private final Signature sig;
		
		public AcosDef() {
			sig = new Signature("acos", Arrays.asList(new Var("x")));
		}

		@Override
		public Signature getSignature() {
			return sig;
		}

		@Override
		public String getDescription() {
			return sig + " = " + description;
		}

		@Override
		public Complex complexEvaluate(List<Complex> args) throws Exception {
			Complex z = args.get(0);
			return (z.im() == 0.0) ? z.setRe(Math.acos(z.re()))
								   : z.acos();
		}

		@Override
		public Double evaluate(List<Double> args) throws Exception {
			return Math.acos(args.get(0));
		}
		
		@Override
		public Object accept(Visitor v) throws Exception {
			return v.visit(this);
		}
	}
	
	public static class AtanDef extends FuncDef {
		public static final String description = "The inverse tangent (arctan) of x.";
		
		private final Signature sig;
		
		public AtanDef() {
			sig = new Signature("atan", Arrays.asList(new Var("x")));
		}

		@Override
		public Signature getSignature() {
			return sig;
		}

		@Override
		public String getDescription() {
			return sig + " = " + description;
		}

		@Override
		public Complex complexEvaluate(List<Complex> args) throws Exception {
			Complex z = args.get(0);
			return (z.im() == 0.0) ? z.setRe(Math.atan(z.re()))
								   : z.atan();
		}

		@Override
		public Double evaluate(List<Double> args) throws Exception {
			return Math.atan(args.get(0));
		}
		
		@Override
		public Object accept(Visitor v) throws Exception {
			return v.visit(this);
		}
	}
	
	public static class ErfDef extends FuncDef {
		public static final String description = "The error function.";
		
		private final Signature sig;
		
		public ErfDef() {
			sig = new Signature("erf", Arrays.asList(new Var("x")));
		}

		@Override
		public Signature getSignature() {
			return sig;
		}

		@Override
		public String getDescription() {
			return sig + " = " + description;
		}

		@Override
		public Complex complexEvaluate(List<Complex> args) throws Exception {
			Complex z = args.get(0);
			if (z.im() != 0.0) {
				throw new Exception("erf only defined for real numbers");
			}
			return z.setRe(Erf.erf(z.re()));
		}

		@Override
		public Double evaluate(List<Double> args) throws Exception {
			return Erf.erf(args.get(0));
		}
		
		@Override
		public Object accept(Visitor v) throws Exception {
			return v.visit(this);
		}
	}
	
	public static class GammaDef extends FuncDef {
		public static final String description = "The gamma function.";
		
		private final Signature sig;
		
		public GammaDef() {
			sig = new Signature("\u0393", Arrays.asList(new Var("x")));
		}

		@Override
		public Signature getSignature() {
			return sig;
		}

		@Override
		public String getDescription() {
			return sig + " = " + description;
		}

		@Override
		public Complex complexEvaluate(List<Complex> args) throws Exception {
			Complex z = args.get(0);
			if (z.im() != 0.0) {
				throw new Exception("gamma only defined for real numbers");
			}
			return z.setRe(Gamma.gamma(z.re()));
		}

		@Override
		public Double evaluate(List<Double> args) throws Exception {
			return Gamma.gamma(args.get(0));
		}
		
		@Override
		public Object accept(Visitor v) throws Exception {
			return v.visit(this);
		}
	}
	
	public static class LogGammaDef extends FuncDef {
		public static final String description = "The logarithm of the gamma function.";
		
		private final Signature sig;
		
		public LogGammaDef() {
			sig = new Signature("log\u0393", Arrays.asList(new Var("x")));
		}

		@Override
		public Signature getSignature() {
			return sig;
		}

		@Override
		public String getDescription() {
			return sig + " = " + description;
		}

		@Override
		public Complex complexEvaluate(List<Complex> args) throws Exception {
			Complex z = args.get(0);
			if (z.im() != 0.0) {
				throw new Exception("loggamma only defined for real numbers");
			}
			return z.setRe(Gamma.logGamma(z.re()));
		}

		@Override
		public Double evaluate(List<Double> args) throws Exception {
			return Gamma.logGamma(args.get(0));
		}
		
		@Override
		public Object accept(Visitor v) throws Exception {
			return v.visit(this);
		}
	}
}
