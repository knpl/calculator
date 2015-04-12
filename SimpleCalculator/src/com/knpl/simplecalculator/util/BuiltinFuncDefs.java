package com.knpl.simplecalculator.util;

import java.util.Arrays;
import java.util.List;

import com.knpl.simplecalculator.nodes.Complex;
import com.knpl.simplecalculator.nodes.MVFunc;
import com.knpl.simplecalculator.nodes.Call;
import com.knpl.simplecalculator.nodes.Expr;
import com.knpl.simplecalculator.nodes.SVFunc;
import com.knpl.simplecalculator.nodes.Signature;
import com.knpl.simplecalculator.nodes.Var;
import com.knpl.simplecalculator.nodes.Builtins.*;

public class BuiltinFuncDefs {
	
	public static final FuncDef builtinFunctions[] = 
	{
		new MinDefinition(),
		new MaxDefinition(),
		new CeilDefinition(),
		new FloorDefinition(),
		new SqrtDefinition(),
		new AbsDefinition(),
		new LogDefinition(),
		new ExpDefinition(),
		new SinhDefinition(),
		new CoshDefinition(),
		new TanhDefinition(),
		new SinDefinition(),
		new CosDefinition(),
		new TanDefinition(),
		new AsinDefinition(),
		new AcosDefinition(),
		new AtanDefinition(),
		new ErfDefinition(),
		new GammaDefinition(),
		new LogGammaDefinition(),
		new LogBetaDefinition()
	};
	
	public static class MinDefinition extends FuncDef {
		public static final String description = "The minimum of a and b.";
		
		private final Signature sig;
		
		public MinDefinition() {
			sig = new Signature("min", Arrays.asList(new Var("a"), new Var("b")));
		}
		
		@Override
		public MVFunc createFunction(Call call) throws Exception {
			if (!call.match(sig))
				throw new Exception("Signature mismatch");
			return new Min(this, call.getArguments());
		}

		@Override
		public Signature getSignature() {
			return sig;
		}

		@Override
		public MVFunc createFunction(Signature sig, List<Expr> args) throws Exception {
			if (!this.sig.match(sig))
				throw new Exception("Signature mismatch");
			return new Min(this, args);
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
	}
	
	public static class MaxDefinition extends FuncDef {
		public static final String description = "The maximum of a and b.";
		
		private final Signature sig;
		
		public MaxDefinition() {
			sig = new Signature("max", Arrays.asList(new Var("a"), new Var("b")));
		}
		
		@Override
		public MVFunc createFunction(Call call) throws Exception {
			if (!call.match(sig))
				throw new Exception("Signature mismatch");
			return new Max(this, call.getArguments());
		}

		@Override
		public Signature getSignature() {
			return sig;
		}

		@Override
		public MVFunc createFunction(Signature sig, List<Expr> args) throws Exception {
			if (!this.sig.match(sig))
				throw new Exception("Signature mismatch");
			return new Max(this, args);
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
	}
	
	public static class FloorDefinition extends FuncDef {
		public static final String description = "The integer part of x.";
		
		private final Signature sig;
		
		public FloorDefinition() {
			sig = new Signature("floor", Arrays.asList(new Var("x")));
		}

		@Override
		public Signature getSignature() {
			return sig;
		}
		
		@Override
		public SVFunc createFunction(Call call) throws Exception {
			if (!call.match(sig))
				throw new Exception("Signature mismatch");
			return new Floor(this, call.getArguments().get(0));
		}

		@Override
		public SVFunc createFunction(Signature sig, List<Expr> args) throws Exception {
			if (!this.sig.match(sig))
				throw new Exception("Signature mismatch");
			return new Floor(this, args.get(0));
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
	}
	
	public static class CeilDefinition extends FuncDef {
		public static final String description = "The nearest integer greater than or equal to x.";
		
		private final Signature sig;
		
		public CeilDefinition() {
			sig = new Signature("ceil", Arrays.asList(new Var("x")));
		}

		@Override
		public Signature getSignature() {
			return sig;
		}
		
		@Override
		public SVFunc createFunction(Call call) throws Exception {
			if (!call.match(sig))
				throw new Exception("Signature mismatch");
			return new Ceil(this, call.getArguments().get(0));
		}

		@Override
		public SVFunc createFunction(Signature sig, List<Expr> args) throws Exception {
			if (!this.sig.match(sig))
				throw new Exception("Signature mismatch");
			return new Ceil(this, args.get(0));
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
	}
	
	public static class SqrtDefinition extends FuncDef {
		public static final String description = "The square root of x.";
		
		private final Signature sig;
		
		public SqrtDefinition() {
			sig = new Signature("sqrt", Arrays.asList(new Var("x")));
		}

		@Override
		public Signature getSignature() {
			return sig;
		}
		
		@Override
		public SVFunc createFunction(Call call) throws Exception {
			if (!call.match(sig))
				throw new Exception("Signature mismatch");
			return new Sqrt(this, call.getArguments().get(0));
		}

		@Override
		public SVFunc createFunction(Signature sig, List<Expr> args) throws Exception {
			if (!this.sig.match(sig))
				throw new Exception("Signature mismatch");
			return new Sqrt(this, args.get(0));
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
	}
	
	public static class AbsDefinition extends FuncDef {
		public static final String description = "The absolute value of x.";
		
		private final Signature sig;
		
		public AbsDefinition() {
			sig = new Signature("abs", Arrays.asList(new Var("x")));
		}

		@Override
		public Signature getSignature() {
			return sig;
		}
		
		@Override
		public SVFunc createFunction(Call call) throws Exception {
			if (!call.match(sig))
				throw new Exception("Signature mismatch");
			return new Abs(this, call.getArguments().get(0));
		}

		@Override
		public SVFunc createFunction(Signature sig, List<Expr> args) throws Exception {
			if (!this.sig.match(sig))
				throw new Exception("Signature mismatch");
			return new Abs(this, args.get(0));
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
	}
	
	public static class LogDefinition extends FuncDef {
		public static final String description = "The natural (base e) logarithm of x.";
		
		private final Signature sig;
		
		public LogDefinition() {
			sig = new Signature("log", Arrays.asList(new Var("x")));
		}

		@Override
		public Signature getSignature() {
			return sig;
		}
		
		@Override
		public SVFunc createFunction(Call call) throws Exception {
			if (!call.match(sig))
				throw new Exception("Signature mismatch");
			return new Log(this, call.getArguments().get(0));
		}

		@Override
		public SVFunc createFunction(Signature sig, List<Expr> args) throws Exception {
			if (!this.sig.match(sig))
				throw new Exception("Signature mismatch");
			return new Log(this, args.get(0));
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
	}
	
	public static class ExpDefinition extends FuncDef {
		public static final String description = "The exponential (base e) of x.";
		
		private final Signature sig;
		
		public ExpDefinition() {
			sig = new Signature("exp", Arrays.asList(new Var("x")));
		}

		@Override
		public Signature getSignature() {
			return sig;
		}
		@Override
		public SVFunc createFunction(Call call) throws Exception {
			if (!call.match(sig))
				throw new Exception("Signature mismatch");
			return new Exp(this, call.getArguments().get(0));
		}

		@Override
		public SVFunc createFunction(Signature sig, List<Expr> args) throws Exception {
			if (!this.sig.match(sig))
				throw new Exception("Signature mismatch");
			return new Exp(this, args.get(0));
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
	}
	
	public static class SinhDefinition extends FuncDef {
		public static final String description = "The hyperbolic sine of x.";
		
		private final Signature sig;
		
		public SinhDefinition() {
			sig = new Signature("sinh", Arrays.asList(new Var("x")));
		}

		@Override
		public Signature getSignature() {
			return sig;
		}
		@Override
		public SVFunc createFunction(Call call) throws Exception {
			if (!call.match(sig))
				throw new Exception("Signature mismatch");
			return new Sinh(this, call.getArguments().get(0));
		}
		
		@Override
		public SVFunc createFunction(Signature sig, List<Expr> args) throws Exception {
			if (!this.sig.match(sig))
				throw new Exception("Signature mismatch");
			return new Sinh(this, args.get(0));
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
	}
	
	public static class CoshDefinition extends FuncDef {
		public static final String description = "The hyperbolic cosine of x.";
		
		private final Signature sig;
		
		public CoshDefinition() {
			sig = new Signature("cosh", Arrays.asList(new Var("x")));
		}

		@Override
		public Signature getSignature() {
			return sig;
		}
		
		@Override
		public SVFunc createFunction(Call call) throws Exception {
			if (!call.match(sig))
				throw new Exception("Signature mismatch");
			return new Cosh(this, call.getArguments().get(0));
		}

		@Override
		public SVFunc createFunction(Signature sig, List<Expr> args) throws Exception {
			if (!this.sig.match(sig))
				throw new Exception("Signature mismatch");
			return new Cosh(this, args.get(0));
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
	}
	
	public static class TanhDefinition extends FuncDef {
		public static final String description = "The hyperbolic tangent of x.";
		
		private final Signature sig;
		
		public TanhDefinition() {
			sig = new Signature("tanh", Arrays.asList(new Var("x")));
		}

		@Override
		public Signature getSignature() {
			return sig;
		}
		
		@Override
		public SVFunc createFunction(Call call) throws Exception {
			if (!call.match(sig))
				throw new Exception("Signature mismatch");
			return new Tanh(this, call.getArguments().get(0));
		}

		@Override
		public SVFunc createFunction(Signature sig, List<Expr> args) throws Exception {
			if (!this.sig.match(sig))
				throw new Exception("Signature mismatch");
			return new Tanh(this, args.get(0));
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
	}
	
	public static class SinDefinition extends FuncDef {
		public static final String description = "The sine of x.";
		
		private final Signature sig;
		
		public SinDefinition() {
			sig = new Signature("sin", Arrays.asList(new Var("x")));
		}

		@Override
		public Signature getSignature() {
			return sig;
		}
		
		@Override
		public SVFunc createFunction(Call call) throws Exception {
			if (!call.match(sig))
				throw new Exception("Signature mismatch");
			return new Sin(this, call.getArguments().get(0));
		}
		
		@Override
		public SVFunc createFunction(Signature sig, List<Expr> args) throws Exception {
			if (!this.sig.match(sig))
				throw new Exception("Signature mismatch");
			return new Sin(this, args.get(0));
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
	}
	
	public static class CosDefinition extends FuncDef {
		public static final String description = "The cosine of x.";
		
		private final Signature sig;
		
		public CosDefinition() {
			sig = new Signature("cos", Arrays.asList(new Var("x")));
		}

		@Override
		public Signature getSignature() {
			return sig;
		}
		
		@Override
		public SVFunc createFunction(Call call) throws Exception {
			if (!call.match(sig))
				throw new Exception("Signature mismatch");
			return new Cos(this, call.getArguments().get(0));
		}

		@Override
		public SVFunc createFunction(Signature sig, List<Expr> args) throws Exception {
			if (!this.sig.match(sig))
				throw new Exception("Signature mismatch");
			return new Cos(this, args.get(0));
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
	}
	
	public static class TanDefinition extends FuncDef {
		public static final String description = "The tangent of x.";
		
		private final Signature sig;
		
		public TanDefinition() {
			sig = new Signature("tan", Arrays.asList(new Var("x")));
		}

		@Override
		public Signature getSignature() {
			return sig;
		}
		
		@Override
		public SVFunc createFunction(Call call) throws Exception {
			if (!call.match(sig))
				throw new Exception("Signature mismatch");
			return new Tan(this, call.getArguments().get(0));
		}

		@Override
		public SVFunc createFunction(Signature sig, List<Expr> args) throws Exception {
			if (!this.sig.match(sig))
				throw new Exception("Signature mismatch");
			return new Tan(this, args.get(0));
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
	}
	
	public static class AsinDefinition extends FuncDef {
		public static final String description = "The inverse sine (arcsin) of x.";
		
		private final Signature sig;
		
		public AsinDefinition() {
			sig = new Signature("asin", Arrays.asList(new Var("x")));
		}

		@Override
		public Signature getSignature() {
			return sig;
		}
		
		@Override
		public SVFunc createFunction(Call call) throws Exception {
			if (!call.match(sig))
				throw new Exception("Signature mismatch");
			return new Asin(this, call.getArguments().get(0));
		}

		@Override
		public SVFunc createFunction(Signature sig, List<Expr> args) throws Exception {
			if (!this.sig.match(sig))
				throw new Exception("Signature mismatch");
			return new Asin(this, args.get(0));
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
	}
	
	public static class AcosDefinition extends FuncDef {
		public static final String description = "The inverse cosine (arccos) of x.";
		
		private final Signature sig;
		
		public AcosDefinition() {
			sig = new Signature("acos", Arrays.asList(new Var("x")));
		}

		@Override
		public Signature getSignature() {
			return sig;
		}
		
		@Override
		public SVFunc createFunction(Call call) throws Exception {
			if (!call.match(sig))
				throw new Exception("Signature mismatch");
			return new Acos(this, call.getArguments().get(0));
		}
		
		@Override
		public SVFunc createFunction(Signature sig, List<Expr> args) throws Exception {
			if (!this.sig.match(sig))
				throw new Exception("Signature mismatch");
			return new Acos(this, args.get(0));
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
	}
	
	public static class AtanDefinition extends FuncDef {
		public static final String description = "The inverse tangent (arctan) of x.";
		
		private final Signature sig;
		
		public AtanDefinition() {
			sig = new Signature("atan", Arrays.asList(new Var("x")));
		}

		@Override
		public Signature getSignature() {
			return sig;
		}
		
		@Override
		public SVFunc createFunction(Call call) throws Exception {
			if (!call.match(sig))
				throw new Exception("Signature mismatch");
			return new Atan(this, call.getArguments().get(0));
		}
		
		@Override
		public SVFunc createFunction(Signature sig, List<Expr> args) throws Exception {
			if (!this.sig.match(sig))
				throw new Exception("Signature mismatch");
			return new Atan(this, args.get(0));
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
	}
	
	public static class ErfDefinition extends FuncDef {
		public static final String description = "The error function.";
		
		private final Signature sig;
		
		public ErfDefinition() {
			sig = new Signature("erf", Arrays.asList(new Var("x")));
		}

		@Override
		public Signature getSignature() {
			return sig;
		}
		
		@Override
		public SVFunc createFunction(Call call) throws Exception {
			if (!call.match(sig))
				throw new Exception("Signature mismatch");
			return new Erf(this, call.getArguments().get(0));
		}
		
		@Override
		public SVFunc createFunction(Signature sig, List<Expr> args) throws Exception {
			if (!this.sig.match(sig))
				throw new Exception("Signature mismatch");
			return new Erf(this, args.get(0));
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
			return z.setRe(org.apache.commons.math3.special.Erf.erf(z.re()));
		}

		@Override
		public Double evaluate(List<Double> args) throws Exception {
			return org.apache.commons.math3.special.Erf.erf(args.get(0));
		}
	}
	
	public static class GammaDefinition extends FuncDef {
		public static final String description = "The gamma function.";
		
		private final Signature sig;
		
		public GammaDefinition() {
			sig = new Signature("\u0393", Arrays.asList(new Var("x")));
		}

		@Override
		public Signature getSignature() {
			return sig;
		}
		
		@Override
		public SVFunc createFunction(Call call) throws Exception {
			if (!call.match(sig))
				throw new Exception("Signature mismatch");
			return new Gamma(this, call.getArguments().get(0));
		}
		
		@Override
		public SVFunc createFunction(Signature sig, List<Expr> args) throws Exception {
			if (!this.sig.match(sig))
				throw new Exception("Signature mismatch");
			return new Gamma(this, args.get(0));
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
			return z.setRe(org.apache.commons.math3.special.Gamma.gamma(z.re()));
		}

		@Override
		public Double evaluate(List<Double> args) throws Exception {
			return org.apache.commons.math3.special.Gamma.gamma(args.get(0));
		}
	}
	
	public static class LogGammaDefinition extends FuncDef {
		public static final String description = "The logarithm of the gamma function.";
		
		private final Signature sig;
		
		public LogGammaDefinition() {
			sig = new Signature("log\u0393", Arrays.asList(new Var("x")));
		}

		@Override
		public Signature getSignature() {
			return sig;
		}
		
		@Override
		public SVFunc createFunction(Call call) throws Exception {
			if (!call.match(sig))
				throw new Exception("Signature mismatch");
			return new LogGamma(this, call.getArguments().get(0));
		}
		
		@Override
		public SVFunc createFunction(Signature sig, List<Expr> args) throws Exception {
			if (!this.sig.match(sig))
				throw new Exception("Signature mismatch");
			return new LogGamma(this, args.get(0));
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
			return z.setRe(org.apache.commons.math3.special.Gamma.logGamma(z.re()));
		}

		@Override
		public Double evaluate(List<Double> args) throws Exception {
			return org.apache.commons.math3.special.Gamma.logGamma(args.get(0));
		}
	}
	
	public static class LogBetaDefinition extends FuncDef {
		public static final String description = "The logarithm of the beta function.";
		
		private final Signature sig;
		
		public LogBetaDefinition() {
			sig = new Signature("log\u03B2", Arrays.asList(new Var("x"), new Var("y")));
		}

		@Override
		public Signature getSignature() {
			return sig;
		}
		
		@Override
		public MVFunc createFunction(Call call) throws Exception {
			if (!call.match(sig))
				throw new Exception("Signature mismatch");
			return new LogBeta(this, call.getArguments());
		}
		
		@Override
		public MVFunc createFunction(Signature sig, List<Expr> args) throws Exception {
			if (!this.sig.match(sig))
				throw new Exception("Signature mismatch");
			return new LogBeta(this, args);
		}

		@Override
		public String getDescription() {
			return sig + " = " + description;
		}

		@Override
		public Complex complexEvaluate(List<Complex> args) throws Exception {
			Complex a = args.get(0);
			if (a.im() != 0.0) {
				throw new Exception("logbeta only defined for real numbers");
			}
			Complex b = args.get(1);
			if (b.im() != 0.0) {
				throw new Exception("logbeta only defined for real numbers");
			}
			return a.setRe(org.apache.commons.math3.special.Beta.logBeta(a.re(), b.re()));
		}

		@Override
		public Double evaluate(List<Double> args) throws Exception {
			return org.apache.commons.math3.special.Beta.logBeta(args.get(0), args.get(1));
		}
	}
}
