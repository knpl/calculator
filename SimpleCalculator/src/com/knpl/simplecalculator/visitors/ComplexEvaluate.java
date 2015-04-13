package com.knpl.simplecalculator.visitors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.math3.special.Gamma;

import com.knpl.simplecalculator.nodes.*;
import com.knpl.simplecalculator.util.FuncDef;
import com.knpl.simplecalculator.util.Globals;

public class ComplexEvaluate extends Visitor {

	private Map<String, Complex> map;
	
	public ComplexEvaluate() {
		map = new HashMap<String, Complex>();
	}
	
	public ComplexEvaluate(Map<String, Complex> map) {
		this.map = map;
	}
	
	@Override
	public Complex visit(Add node) throws Exception {
		Complex w = (Complex) node.getLHS().accept(this);
		Complex z = (Complex) node.getRHS().accept(this);
		return (w.im() == 0.0 && z.im() == 0.0) ? w.setRe(w.re() + z.re())
											    : w.add(z);
	}

	@Override
	public Complex visit(Sub node) throws Exception {
		Complex w = (Complex) node.getLHS().accept(this);
		Complex z = (Complex) node.getRHS().accept(this);
		return (w.im() == 0.0 && z.im() == 0.0) ? w.setRe(w.re() - z.re())
											    : w.sub(z);
	}

	@Override
	public Complex visit(Mul node) throws Exception {
		Complex w = (Complex) node.getLHS().accept(this);
		Complex z = (Complex) node.getRHS().accept(this);
		return (w.im() == 0.0 && z.im() == 0.0) ? w.setRe(w.re() * z.re())
											    : w.mul(z);
	}

	@Override
	public Complex visit(Div node) throws Exception {
		Complex w = (Complex) node.getLHS().accept(this);
		Complex z = (Complex) node.getRHS().accept(this);
		return (w.im() == 0.0 && z.im() == 0.0) ? w.setRe(w.re() / z.re())
											    : w.div(z);
	}
	
	@Override
	public Complex visit(Mod node) throws Exception {
		Complex a = (Complex) node.getLHS().accept(this);
		if (a.im() != 0.0) {
			throw new Exception("operator % does not support complex arguments");
		}
		Complex b = (Complex) node.getRHS().accept(this);
		if (b.im() != 0.0) {
			throw new Exception("operator % does not support complex arguments");
		}
		return a.setRe(a.re() - b.re()*Math.floor(a.re()/b.re()));
	}

	@Override
	public Complex visit(Pow node) throws Exception {
		Complex w = (Complex) node.getLHS().accept(this);
		Complex z = (Complex) node.getRHS().accept(this);
		return (w.re() >= 0.0 && w.im() == 0.0 && z.im() == 0.0) ? w.setRe(Math.pow(w.re(),z.re()))
											  					 : w.pow(z);
	}

	@Override
	public Complex visit(Minus node) throws Exception {
		Complex z = (Complex) node.getOp().accept(this);
		return (z.im() == 0.0) ? z.setRe(-z.re())
							   : z.neg();
	}
	
	@Override
	public Complex visit(Factorial node) throws Exception {
		Complex z = (Complex) node.getOp().accept(this);
		if (z.im() == 0.0) {
			return z.setRe(Gamma.gamma(1 + z.re()));
		}
		z.setRe(Double.NaN);
		z.setIm(Double.NaN);
		return z;
	}
	
	@Override
	public Complex visit(DegToRad node) throws Exception {
		Complex z = (Complex) node.getOp().accept(this);
		return z.mul(new Complex(Math.PI/180, 0));
	}

	@Override
	public Complex visit(Num node) throws Exception {
		return new Complex(node.getDouble(), 0.0);
	}
	
	@Override
	public Complex visit(Call node) throws Exception {
		FuncDef funcDef = Globals.getInstance().getFuncDef(node.getName());
		List<Expr> arguments = node.getArguments();
		List<Complex> args = new ArrayList<Complex>(arguments.size());
		for (Expr arg : arguments) {
			args.add((Complex) arg.accept(this));
		}
		return funcDef.complexEvaluate(args);
	}
	
	@Override
	public Complex visit(SVFunc node) throws Exception {
		return node.getFuncDef().complexEvaluate(
				Arrays.asList((Complex) node.getArgument().accept(this))
		);
	}
	
	@Override
	public Complex visit(MVFunc node) throws Exception {
		List<Expr> arguments = node.getArguments();
		List<Complex> args = new ArrayList<Complex>(arguments.size());
		for (Expr arg : arguments) {
			args.add((Complex) arg.accept(this));
		}
		return node.getFuncDef().complexEvaluate(args);
	}

	@Override
	public Complex visit(Var node) throws Exception {
		Complex result = map.get(node.getName());
		if (result == null)
			throw new Exception("Undeclared variable "+node.getName());
		return new Complex(result);
	}

	@Override
	public Complex visit(ConstDef node) throws Exception {
		return node.getComplex();
	}
	
	@Override
	public Complex visit(Complex node) throws Exception {
		return new Complex(node);
	}
}
