package com.knpl.simplecalculator.nodes;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.math3.special.Erf;
import org.apache.commons.math3.special.Gamma;

import com.knpl.simplecalculator.visitors.Visitor;

public class BuiltinFuncDefs {
	
	public static final FuncDef builtinFuncDefs[] = 
	{
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
		public static final String description = "The minimum of a and b.";
		
		public MinDef() {
			super(new Signature("min", Arrays.asList(new Var("a"), new Var("b"))));
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
	
	public static class MaxDef extends MVFuncDef {
		public static final String description = "The maximum of a and b.";
		
		public MaxDef() {
			super(new Signature("max", Arrays.asList(new Var("a"), new Var("b"))));
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
	
	public static class FloorDef extends SVFuncDef {
		public static final String description = "The integer part of x.";
		
		public FloorDef() {
			super(new Signature("floor", Arrays.asList(new Var("x"))));
		}

		@Override
		public String getDescription() {
			return sig + " = " + description;
		}

		@Override
		public Complex complexEvaluate(Complex z) throws Exception {
			if (z.im() != 0.0) {
				throw new Exception("floor only defined for real numbers");
			}
			return z.setRe(Math.floor(z.re()));
		}

		@Override
		public double evaluate(double x) throws Exception {
			return Math.floor(x);
		}
		
		@Override
		public Object accept(Visitor v) throws Exception {
			return v.visit(this);
		}
	}

	
	public static class CeilDef extends SVFuncDef {
		public static final String description = "The nearest integer greater than or equal to x.";
		
		public CeilDef() {
			super(new Signature("ceil", Arrays.asList(new Var("x"))));
		}

		@Override
		public String getDescription() {
			return sig + " = " + description;
		}

		@Override
		public Complex complexEvaluate(Complex z) throws Exception {
			if (z.im() != 0.0) {
				throw new Exception("ceil only defined for real numbers");
			}
			return z.setRe(Math.ceil(z.re()));
		}

		@Override
		public double evaluate(double x) throws Exception {
			return Math.ceil(x);
		}
		
		@Override
		public Object accept(Visitor v) throws Exception {
			return v.visit(this);
		}
	}
	
	public static class SqrtDef extends SVFuncDef {
		public static final String description = "The square root of x.";
		
		public SqrtDef() {
			super(new Signature("sqrt", Arrays.asList(new Var("x"))));
		}

		@Override
		public String getDescription() {
			return sig + " = " + description;
		}

		@Override
		public Complex complexEvaluate(Complex z) throws Exception {
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
		public double evaluate(double x) throws Exception {
			return Math.sqrt(x);
		}
		
		@Override
		public Object accept(Visitor v) throws Exception {
			return v.visit(this);
		}
	}
	
	public static class AbsDef extends SVFuncDef {
		public static final String description = "The absolute value of x.";
		
		public AbsDef() {
			super(new Signature("abs", Arrays.asList(new Var("x"))));
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
		public Complex complexEvaluate(Complex z) throws Exception {
			z.setRe(z.abs());
			z.setIm(0);
			return z;
		}

		@Override
		public double evaluate(double x) throws Exception {
			return Math.abs(x);
		}
		
		@Override
		public Object accept(Visitor v) throws Exception {
			return v.visit(this);
		}
	}
	
	public static class LogDef extends SVFuncDef {
		public static final String description = "The natural (base e) logarithm of x.";
		
		public LogDef() {
			super(new Signature("log", Arrays.asList(new Var("x"))));
		}

		@Override
		public String getDescription() {
			return sig + " = " + description;
		}

		@Override
		public Complex complexEvaluate(Complex z) throws Exception {
			return (z.re() >= 0.0 && z.im() == 0.0) ? z.setRe(Math.log(z.re()))
								   				    : z.log();
		}

		@Override
		public double evaluate(double x) throws Exception {
			return Math.log(x);
		}
		
		@Override
		public Object accept(Visitor v) throws Exception {
			return v.visit(this);
		}
	}
	
	public static class ExpDef extends SVFuncDef {
		public static final String description = "The exponential (base e) of x.";
		
		public ExpDef() {
			super(new Signature("exp", Arrays.asList(new Var("x"))));
		}

		@Override
		public String getDescription() {
			return sig + " = " + description;
		}

		@Override
		public Complex complexEvaluate(Complex z) throws Exception {
			return (z.im() == 0.0) ? z.setRe(Math.exp(z.re()))
								   : z.exp();
		}

		@Override
		public double evaluate(double x) throws Exception {
			return Math.exp(x);
		}
		
		@Override
		public Object accept(Visitor v) throws Exception {
			return v.visit(this);
		}
	}
	
	public static class SinhDef extends SVFuncDef {
		public static final String description = "The hyperbolic sine of x.";
		
		public SinhDef() {
			super(new Signature("sinh", Arrays.asList(new Var("x"))));
		}

		@Override
		public String getDescription() {
			return sig + " = " + description;
		}

		@Override
		public Complex complexEvaluate(Complex z) throws Exception {
			return (z.im() == 0.0) ? z.setRe(Math.sinh(z.re()))
								   : z.sinh();
		}

		@Override
		public double evaluate(double x) throws Exception {
			return Math.sinh(x);
		}
		
		@Override
		public Object accept(Visitor v) throws Exception {
			return v.visit(this);
		}
	}
	
	public static class CoshDef extends SVFuncDef {
		public static final String description = "The hyperbolic cosine of x.";
		
		public CoshDef() {
			super(new Signature("cosh", Arrays.asList(new Var("x"))));
		}

		@Override
		public String getDescription() {
			return sig + " = " + description;
		}

		@Override
		public Complex complexEvaluate(Complex z) throws Exception {
			return (z.im() == 0.0) ? z.setRe(Math.cosh(z.re()))
								   : z.cosh();
		}

		@Override
		public double evaluate(double x) throws Exception {
			return Math.cosh(x);
		}
		
		@Override
		public Object accept(Visitor v) throws Exception {
			return v.visit(this);
		}
	}
	
	public static class TanhDef extends SVFuncDef {
		public static final String description = "The hyperbolic tangent of x.";
		
		public TanhDef() {
			super(new Signature("tanh", Arrays.asList(new Var("x"))));
		}

		@Override
		public String getDescription() {
			return sig + " = " + description;
		}

		@Override
		public Complex complexEvaluate(Complex z) throws Exception {
			return (z.im() == 0.0) ? z.setRe(Math.tanh(z.re()))
								   : z.tanh();
		}

		@Override
		public double evaluate(double x) throws Exception {
			return Math.tanh(x);
		}
		
		@Override
		public Object accept(Visitor v) throws Exception {
			return v.visit(this);
		}
	}
	
	public static class SinDef extends SVFuncDef {
		public static final String description = "The sine of x.";
		
		public SinDef() {
			super(new Signature("sin", Arrays.asList(new Var("x"))));
		}

		@Override
		public String getDescription() {
			return sig + " = " + description;
		}

		@Override
		public Complex complexEvaluate(Complex z) throws Exception {
			return (z.im() == 0.0) ? z.setRe(Math.sin(z.re()))
								   : z.sin();
		}

		@Override
		public double evaluate(double x) throws Exception {
			return Math.sin(x);
		}
		
		@Override
		public Object accept(Visitor v) throws Exception {
			return v.visit(this);
		}
	}
	
	public static class CosDef extends SVFuncDef {
		public static final String description = "The cosine of x.";
		
		public CosDef() {
			super(new Signature("cos", Arrays.asList(new Var("x"))));
		}

		@Override
		public String getDescription() {
			return sig + " = " + description;
		}

		@Override
		public Complex complexEvaluate(Complex z) throws Exception {
			return (z.im() == 0.0) ? z.setRe(Math.cos(z.re()))
								   : z.cos();
		}

		@Override
		public double evaluate(double x) throws Exception {
			return Math.cos(x);
		}
		
		@Override
		public Object accept(Visitor v) throws Exception {
			return v.visit(this);
		}
	}
	
	public static class TanDef extends SVFuncDef {
		public static final String description = "The tangent of x.";
		
		public TanDef() {
			super(new Signature("tan", Arrays.asList(new Var("x"))));
		}

		@Override
		public String getDescription() {
			return sig + " = " + description;
		}

		@Override
		public Complex complexEvaluate(Complex z) throws Exception {
			return (z.im() == 0.0) ? z.setRe(Math.tan(z.re()))
								   : z.tan();
		}

		@Override
		public double evaluate(double x) throws Exception {
			return Math.tan(x);
		}
		
		@Override
		public Object accept(Visitor v) throws Exception {
			return v.visit(this);
		}
	}
	
	public static class AsinDef extends SVFuncDef {
		public static final String description = "The inverse sine (arcsin) of x.";
		
		public AsinDef() {
			super(new Signature("asin", Arrays.asList(new Var("x"))));
		}

		@Override
		public String getDescription() {
			return sig + " = " + description;
		}

		@Override
		public Complex complexEvaluate(Complex z) throws Exception {
			return (z.im() == 0.0) ? z.setRe(Math.asin(z.re()))
								   : z.asin();
		}

		@Override
		public double evaluate(double x) throws Exception {
			return Math.asin(x);
		}
		
		@Override
		public Object accept(Visitor v) throws Exception {
			return v.visit(this);
		}
	}
	
	public static class AcosDef extends SVFuncDef {
		public static final String description = "The inverse cosine (arccos) of x.";
		
		public AcosDef() {
			super(new Signature("acos", Arrays.asList(new Var("x"))));
		}

		@Override
		public String getDescription() {
			return sig + " = " + description;
		}

		@Override
		public Complex complexEvaluate(Complex z) throws Exception {
			return (z.im() == 0.0) ? z.setRe(Math.acos(z.re()))
								   : z.acos();
		}

		@Override
		public double evaluate(double x) throws Exception {
			return Math.acos(x);
		}
		
		@Override
		public Object accept(Visitor v) throws Exception {
			return v.visit(this);
		}
	}
	
	public static class AtanDef extends SVFuncDef {
		public static final String description = "The inverse tangent (arctan) of x.";
		
		public AtanDef() {
			super(new Signature("atan", Arrays.asList(new Var("x"))));
		}

		@Override
		public String getDescription() {
			return sig + " = " + description;
		}

		@Override
		public Complex complexEvaluate(Complex z) throws Exception {
			return (z.im() == 0.0) ? z.setRe(Math.atan(z.re()))
								   : z.atan();
		}

		@Override
		public double evaluate(double x) throws Exception {
			return Math.atan(x);
		}
		
		@Override
		public Object accept(Visitor v) throws Exception {
			return v.visit(this);
		}
	}
	
	public static class ErfDef extends SVFuncDef {
		public static final String description = "The error function.";
		
		public ErfDef() {
			super(new Signature("erf", Arrays.asList(new Var("x"))));
		}

		@Override
		public String getDescription() {
			return sig + " = " + description;
		}

		@Override
		public Complex complexEvaluate(Complex z) throws Exception {
			if (z.im() != 0.0) {
				throw new Exception("Complex erf not supported");
			}
			return z.setRe(Erf.erf(z.re()));
		}

		@Override
		public double evaluate(double x) throws Exception {
			return Erf.erf(x);
		}
		
		@Override
		public Object accept(Visitor v) throws Exception {
			return v.visit(this);
		}
	}
	
	public static class GammaDef extends SVFuncDef {
		public static final String description = "The gamma function.";
		
		public GammaDef() {
			super(new Signature("\u0393", Arrays.asList(new Var("x"))));
		}

		@Override
		public String getDescription() {
			return sig + " = " + description;
		}

		@Override
		public Complex complexEvaluate(Complex z) throws Exception {
			if (z.im() != 0.0) {
				throw new Exception("Complex gamma not supported");
			}
			return z.setRe(Gamma.gamma(z.re()));
		}

		@Override
		public double evaluate(double x) throws Exception {
			return Gamma.gamma(x);
		}
		
		@Override
		public Object accept(Visitor v) throws Exception {
			return v.visit(this);
		}
	}
	
	public static class LogGammaDef extends SVFuncDef {
		public static final String description = "The logarithm of the gamma function.";
		
		public LogGammaDef() {
			super(new Signature("log\u0393", Arrays.asList(new Var("x"))));
		}

		@Override
		public String getDescription() {
			return sig + " = " + description;
		}

		@Override
		public Complex complexEvaluate(Complex z) throws Exception {
			if (z.im() != 0.0) {
				throw new Exception("loggamma only defined for real numbers");
			}
			return z.setRe(Gamma.logGamma(z.re()));
		}

		@Override
		public double evaluate(double x) throws Exception {
			return Gamma.logGamma(x);
		}
		
		@Override
		public Object accept(Visitor v) throws Exception {
			return v.visit(this);
		}
	}
}
