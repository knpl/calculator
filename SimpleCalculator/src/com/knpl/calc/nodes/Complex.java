package com.knpl.calc.nodes;

import com.knpl.calc.util.FormatUtils;
import com.knpl.calc.visitors.Visitor;

public class Complex extends Num {
	
	private double re,
				   im;
	
	public Complex(double re, double im) {
		this.re = re;
		this.im = im;
	}
	
	public Complex(Complex that) {
		this(that.re, that.im);
	}
	
	public Complex copy() {
		return new Complex(this);
	}
	
	public static Complex fromPolar(double mod, double arg) {
		return new Complex(mod * Math.cos(arg), mod * Math.sin(arg));
	}
	
	public Complex setRe(double re) {
		this.re = re;
		return this;
	}
	
	public Complex setIm(double im) {
		this.im = im;
		return this;
	}
	
	public double imz() {
		return im;
	}
	
	public double rez() {
		return re;
	}
	
	public double argz() {
		return Math.atan2(im, re);
	}
	
	public double modz() {
		return Math.hypot(re, im);
	}
	
	@Override
	public Complex add(Num a) {
		Complex z = (a instanceof Complex) ? (Complex) a : a.toComplex();
		re += z.re;
		im += z.im;
		return this;
	}

	@Override
	public Complex sub(Num a) {
		Complex z = (a instanceof Complex) ? (Complex) a : a.toComplex();
		re -= z.re;
		im -= z.im;
		return this;
	}

	@Override
	public Complex mul(Num a) {
		Complex z = (a instanceof Complex) ? (Complex) a : a.toComplex();
		double oldre = re;
		re = oldre*z.re - im*z.im;
		im = oldre*z.im + im*z.re;
		return this;
	}

	@Override
	public Complex div(Num a) {
		Complex z = (a instanceof Complex) ? (Complex) a : a.toComplex();
		double mod = z.re*z.re + z.im*z.im,
			   oldre = re;
		re = (oldre*z.re + im*z.im)/mod;
		im = (im*z.re - oldre*z.im)/mod;
		return this;
	}
	
	@Override
	public Complex pow(Num a) {
		Complex z = (a instanceof Complex) ? (Complex) a : a.toComplex();
		return log().mul(z).exp(); /* exp(z*log(x)) */
	}
	
	@Override
	public Complex neg() {
		re = -re;
		im = -im;
		return this;
	}
	
	@Override
	public Complex exp() {
		double r = Math.exp(re);
		re = r*Math.cos(im);
		im = r*Math.sin(im);
		return this;
	}

	@Override
	public Complex log() {
		double mod = modz(),
			   arg = argz();
		re = Math.log(mod);
		im = arg;
		return this;
	}

	@Override
	public Complex sqrt() {
		double mod = Math.sqrt(modz()),
			   arg = 0.5*argz();
		re = mod*Math.cos(arg);
		im = mod*Math.sin(arg);
		return this;
	}
	
	@Override
	public RealDouble abs() {
		return new RealDouble(Math.hypot(re, im));
	}
	
	@Override
	public Complex sinh() {
		double ea 	 = Math.exp(re),
			   eainv = 1/ea;
		re = 0.5*(ea - eainv)*Math.cos(im);
		im = 0.5*(ea + eainv)*Math.sin(im);
		return this;
	}
	
	@Override
	public Complex cosh() {
		double ea	 = Math.exp(re),
			   eainv = 1/ea;
		re = 0.5*(ea + eainv)*Math.cos(im);
		im = 0.5*(ea - eainv)*Math.sin(im);
		return this;
	}
	
	// tanh = -i*tan(i*z)
	@Override
	public Complex tanh() {
		double tmp;
		
		// multiply by i
		tmp = im;
		im = re;
		re = -tmp;
		
		tan();
		
		// multiply by -i
		tmp = im;
		im = -re;
		re = tmp;
		
		return this;
	}
	
	@Override
	public Complex sin() {
		double eb 	 = Math.exp(im),
			   ebinv = 1/eb;
		im = 0.5*(eb - ebinv)*Math.cos(re);
		re = 0.5*(eb + ebinv)*Math.sin(re);
		return this;
	}

	@Override
	public Complex cos() {
		double eb 	 = Math.exp(im),
			   ebinv = 1/eb;
		im = 0.5*(ebinv - eb)*Math.sin(re);
		re = 0.5*(eb + ebinv)*Math.cos(re);
		return this;
	}

	@Override
	public Complex tan() {
		double e2b 		= Math.exp(2*im),
			   e2binv	= 1/e2b,
			   dsin2a 	= 2*Math.sin(2*re),
			   dcos2a 	= 2*Math.cos(2*re),
			   denom 	= dcos2a + e2b + e2binv;
		
		re = dsin2a/denom;
		im = (e2b - e2binv)/denom;
		return this;
	}

	@Override
	public Complex asin() {
		double oldre = re,
			   oldim = im;
		
		re = -oldim;
		im = oldre;
		
		mul(this); 
		re += 1;
		sqrt();
		re -= oldim;
		im += oldre;
		log();
		
		oldre = re;
		re = im;
		im = -oldre;

		return this;
	}

	@Override
	public Complex acos() {
		double oldre = re,
			   oldim = im;
		
		mul(this);
		re -= 1;
		sqrt();
		re += oldre;
		im += oldim;
		log();
		
		oldre = re;
		re = im;
		im = -oldre;
		
		return this;
	}

	@Override
	public Complex atan() {
		double oldre = re,
			   oldim = im;
		
		re = 1+oldim;
		im = -oldre;
		div(new Complex(1-oldim, oldre)).log();
		
		oldre = re;
		re = -0.5*im;
		im = 0.5*oldre;
		
		return this;
	}
	
	public static Complex add(Complex w, Complex z) {
		return new Complex(w.re + z.re, w.im + z.im);
	}
	
	public static Complex sub(Complex w, Complex z) {
		return new Complex(w.re - z.re, w.im - z.im);
	}
	
	public static Complex mul(Complex w, Complex z) {
		return new Complex(w.re*z.re - w.im*z.im,
						   w.re*z.im + w.im*z.re);
	}
	
	public static Complex div(Complex w, Complex z) {
		double mod2 = z.re*z.re + z.im*z.im;
		return new Complex((w.re*z.re + w.im*z.im)/mod2,
						   (w.im*z.re - w.re*z.im)/mod2);
	}
	
	public static Complex pow(Complex w, Complex z) {
		return (new Complex(w)).log().mul(z).exp();
	}
	
	public static double abs(Complex z) {
		return Math.hypot(z.re, z.im);
	}
	
	public static Complex sqrt(Complex z) {
		return fromPolar(Math.sqrt(modz(z)), argz(z)/2);
	}
	
	public static Complex neg(Complex z) {
		return new Complex(-z.re, -z.im);
	}
	
	public static double modz(Complex z) {
		return Math.hypot(z.re, z.im);
	}
	
	public static double argz(Complex z) {
		return Math.atan2(z.im, z.re);
	}
	
	public static Complex log(Complex z) {
		return new Complex(Math.log(modz(z)), argz(z));
	}
	
	public static Complex sinh(Complex z) {
		double ea 	 = Math.exp(z.re),
			   eainv = 1/ea;
		return new Complex(0.5*(ea - eainv)*Math.cos(z.im),
						   0.5*(ea + eainv)*Math.sin(z.im));
	}
	
	public static Complex cosh(Complex z) {
		double ea 	 = Math.exp(z.re),
			   eainv = 1/ea;
		return new Complex(0.5*(ea + eainv)*Math.cos(z.im),
						   0.5*(ea - eainv)*Math.sin(z.im));
	}
	
	public static Complex tanh(Complex z) {
		Complex copy = new Complex(z);
		return copy.tanh();
	}
	
	public static Complex sin(Complex z) {
		double eb 	 = Math.exp(z.im),
			   ebinv = 1/eb;
		return new Complex(0.5*(eb + ebinv)*Math.sin(z.re),
						   0.5*(eb - ebinv)*Math.cos(z.re));
	}
	
	public static Complex cos(Complex z) {
		double eb 	 = Math.exp(z.im),
			   ebinv = 1/eb;
		return new Complex(0.5*(eb + ebinv)*Math.cos(z.re),
						   0.5*(ebinv - eb)*Math.sin(z.re));
	}
	
	public static Complex tan(Complex z) {
		double e2b 		= Math.exp(2*z.im),
			   e2binv	= 1/e2b,
			   dsin2a = 2*Math.sin(2*z.re),
			   dcos2a = 2*Math.cos(2*z.re),
			   denom = dcos2a + e2b + e2binv;
		
		return new Complex(dsin2a/denom,
						   (e2b - e2binv)/denom);
	}
	
	/* asin(z) = -i*log(i*z + sqrt(1 - z*z)) */
	public static Complex asin(Complex z) {
		double tmp;
		Complex w = new Complex(-z.im, z.re); 
		w.mul(w); 
		w.re += 1;
		w.sqrt();
		w.re -= z.im;
		w.im += z.re;
		w.log();
		
		tmp = w.re;
		w.re = w.im;
		w.im = -tmp;
		
		return w;
	}
	
	/* acos(z) = -i*log(z + sqrt(z*z - 1)) */
	public static Complex acos(Complex z) {
		double tmp;
		Complex w = new Complex(z);
		w.mul(w);
		w.re -= 1;
		w.sqrt();
		w.re += z.re;
		w.im += z.im;
		w.log();
		
		tmp = w.re;
		w.re = w.im;
		w.im = -tmp;
		
		return w;
	}
	
	/* atan(z) = 0.5*i*log((1-z*i)/(1+z*i)) */
	public static Complex atan(Complex z) {
		double tmp;
		Complex w = new Complex(1+z.im, -z.re);
		w.div(new Complex(1-z.im, z.re)).log();
		
		tmp = w.re;
		w.re = -0.5*w.im;
		w.im = 0.5*tmp;
		
		return w;
	}
	
	@Override
	public RealDouble re() {
		return new RealDouble(re);
	}
	
	@Override
	public RealDouble im() {
		return new RealDouble(im);
	}
	
	@Override
	public RealDouble mod() {
		return new RealDouble(modz());
	}
	
	@Override
	public RealDouble arg() {
		return new RealDouble(argz());
	}
	
	@Override
	public Complex conj() {
		im = -im;
		return this;
	}

	@Override
	public Complex toComplex() {
		return this;
	}
	
	@Override
	public String toString() {
		if (im == 0)
			return ""+re;
		else if (im < 0)
			return re+"-"+(-im)+"*i";
		else
			return re+"+"+im+"*i";
	}
	
	@Override
	public String format(int decimalcount, boolean polar) {
		return polar ? polarFormat(decimalcount) 
					 : cartesianFormat(decimalcount);
	}
	
	private String polarFormat(int decimalcount) {
		double mod = modz(),
			   arg = argz();
		
		if (mod == 0) {
			return "0";
		}
		else if (arg == 0) {
			return FormatUtils.format(mod, decimalcount);
		}
		else {
			StringBuffer sb = new StringBuffer();
			if (mod != 1)
				sb.append(FormatUtils.format(mod, decimalcount)).append(" * ");
			sb.append("e^(").append(FormatUtils.format(arg, decimalcount));
			sb.append(" * i)");
			return sb.toString();
		}
	}
	
	private String cartesianFormat(int decimalcount) {
		if (im == 0) {
			return FormatUtils.format(re, decimalcount);
		}
		else if (re == 0) {
			if (im == 1)
				return "i";
			return FormatUtils.format(im, decimalcount)+" * i";
		}
		else {
			StringBuffer sb = new StringBuffer();
			sb.append(FormatUtils.format(re, decimalcount));
			if (im < 0) {
				sb.append(" - ");
				if (im == -1)
					sb.append('i');
				else
					sb.append(FormatUtils.format(-im, decimalcount)).append(" * i");
			}
			else {
				sb.append(" + ");
				if (im == 1)
					sb.append('i');
				else 
					sb.append(FormatUtils.format(im, decimalcount)).append(" * i");
			}
			return sb.toString();
		}
	}
	
	@Override
	public Object accept(Visitor v) throws Exception {
		return v.visit(this);
	}
}