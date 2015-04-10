package com.knpl.simplecalculator.visitors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.knpl.simplecalculator.nodes.*;
import com.knpl.simplecalculator.util.FuncDef;
import com.knpl.simplecalculator.util.Globals;

public class ComplexEvaluate extends Visitor<Complex, Void> {

	private Map<String, Complex> map;
	
	public ComplexEvaluate() {
		map = new HashMap<String, Complex>();
	}
	
	public ComplexEvaluate(Map<String, Complex> map) {
		this.map = map;
	}
	
	@Override
	public Complex visit(Add node, Void info) throws Exception {
		Complex w = node.getLHS().accept(this, info);
		Complex z = node.getRHS().accept(this, info);
		return (w.im() == 0.0 && z.im() == 0.0) ? w.setRe(w.re() + z.re())
											    : w.add(z);
	}

	@Override
	public Complex visit(Sub node, Void info) throws Exception {
		Complex w = node.getLHS().accept(this, info);
		Complex z = node.getRHS().accept(this, info);
		return (w.im() == 0.0 && z.im() == 0.0) ? w.setRe(w.re() - z.re())
											    : w.sub(z);
	}

	@Override
	public Complex visit(Mul node, Void info) throws Exception {
		Complex w = node.getLHS().accept(this, info);
		Complex z = node.getRHS().accept(this, info);
		return (w.im() == 0.0 && z.im() == 0.0) ? w.setRe(w.re() * z.re())
											    : w.mul(z);
	}

	@Override
	public Complex visit(Div node, Void info) throws Exception {
		Complex w = node.getLHS().accept(this, info);
		Complex z = node.getRHS().accept(this, info);
		return (w.im() == 0.0 && z.im() == 0.0) ? w.setRe(w.re() / z.re())
											    : w.div(z);
	}

	@Override
	public Complex visit(Pow node, Void info) throws Exception {
		Complex w = node.getLHS().accept(this, info);
		Complex z = node.getRHS().accept(this, info);
		return (w.re() >= 0.0 && w.im() == 0.0 && z.im() == 0.0) ? w.setRe(Math.pow(w.re(),z.re()))
											  					 : w.pow(z);
	}

	@Override
	public Complex visit(Minus node, Void info) throws Exception {
		Complex z = node.getOp().accept(this, info);
		return (z.im() == 0.0) ? z.setRe(-z.re())
							   : z.neg();
	}

	@Override
	public Complex visit(Num node, Void info) throws Exception {
		return new Complex(node.getDouble(), 0.0);
	}
	
	@Override
	public Complex visit(Call node, Void info) throws Exception {
		FuncDef funcDef = Globals.getInstance().getFuncDef(node.getName());
		List<Expr> arguments = node.getArguments();
		List<Complex> args = new ArrayList<Complex>(arguments.size());
		for (Expr arg : arguments) {
			args.add(arg.accept(this, info));
		}
		return funcDef.complexEvaluate(args);
	}
	
	@Override
	public Complex visit(Func node, Void info) throws Exception {
		List<Expr> arguments = node.getArguments();
		List<Complex> args = new ArrayList<Complex>(arguments.size());
		for (Expr arg : arguments) {
			args.add(arg.accept(this, info));
		}
		return node.getDefinition().complexEvaluate(args);
	}

	@Override
	public Complex visit(Var node, Void info) throws Exception {
		Complex result = map.get(node.getName());
		if (result == null)
			throw new Exception("Undeclared variable "+node.getName());
		return new Complex(result);
	}

	@Override
	public Complex visit(ConstDef node, Void info) throws Exception {
		return node.getComplex();
	}
	
	@Override
	public Complex visit(Complex node, Void info) throws Exception {
		return new Complex(node);
	}
}
