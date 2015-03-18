package com.knpl.simplecalculator.visitors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.knpl.simplecalculator.nodes.*;
import com.knpl.simplecalculator.nodes.Builtins.*;
import com.knpl.simplecalculator.numbers.Complex;

public class ComplexEvaluate extends Visitor<Complex, Void> {

	private Map<String, Complex> map;
	
	public ComplexEvaluate() {
		map = new HashMap<String, Complex>();
	}
	
	public ComplexEvaluate(Map<String, Complex> map) {
		this.map = map;
	}
	
	@Override
	public Complex visit(Min node, Void info) throws Exception {
		Complex a = node.getArg(0).accept(this, info);
		if (a.im() != 0.0) {
			throw new Exception("min only defined for real numbers");
		}
		Complex b = node.getArg(1).accept(this, info);
		if (b.im() != 0.0) {
			throw new Exception("min only defined for real numbers");
		}
		return (b.re() < a.re()) ? b : a;
	}
	
	@Override
	public Complex visit(Max node, Void info) throws Exception {
		Complex a = node.getArg(0).accept(this, info);
		if (a.im() != 0.0) {
			throw new Exception("max only defined for real numbers");
		}
		Complex b = node.getArg(1).accept(this, info);
		if (b.im() != 0.0) {
			throw new Exception("max only defined for real numbers");
		}
		return (b.re() > a.re()) ? b : a;
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
	public Complex visit(UserFunc node, Void info) throws Exception {
		List<Expr> arguments = node.getArguments();
		List<Complex> args = new ArrayList<Complex>(arguments.size());
		for (Expr arg : arguments) {
			args.add(arg.accept(this, info));
		}
		return node.complexEvaluate(args);
	}

	@Override
	public Complex visit(Var node, Void info) throws Exception {
		Complex result = map.get(node.getName());
		if (result == null)
			throw new Exception("No mapping for variable "+node.getName());
		return new Complex(result);
	}
	
	@Override
	public Complex visit(Sqrt node, Void info) throws Exception {
		Complex z = node.getArg(0).accept(this, info);
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
	public Complex visit(Abs node, Void info) throws Exception {
		return new Complex(node.getArg(0).accept(this, info).abs(), 0.0);
	}

	@Override
	public Complex visit(Log node, Void info) throws Exception {
		Complex z = node.getArg(0).accept(this, info);
		return (z.re() >= 0.0 && z.im() == 0.0) ? z.setRe(Math.log(z.re()))
							   				    : z.log();
	}

	@Override
	public Complex visit(Exp node, Void info) throws Exception {
		Complex z = node.getArg(0).accept(this, info);
		return (z.im() == 0.0) ? z.setRe(Math.exp(z.re()))
							   : z.exp();
	}

	@Override
	public Complex visit(Sinh node, Void info) throws Exception {
		Complex z = node.getArg(0).accept(this, info);
		return (z.im() == 0.0) ? z.setRe(Math.sinh(z.re()))
							   : z.sinh();
	}

	@Override
	public Complex visit(Cosh node, Void info) throws Exception {
		Complex z = node.getArg(0).accept(this, info);
		return (z.im() == 0.0) ? z.setRe(Math.cosh(z.re()))
							   : z.cosh();
	}

	@Override
	public Complex visit(Sin node, Void info) throws Exception {
		Complex z = node.getArg(0).accept(this, info);
		return (z.im() == 0.0) ? z.setRe(Math.sin(z.re()))
							   : z.sin();
	}

	@Override
	public Complex visit(Cos node, Void info) throws Exception {
		Complex z = node.getArg(0).accept(this, info);
		return (z.im() == 0.0) ? z.setRe(Math.cos(z.re()))
							   : z.cos();
	}

	@Override
	public Complex visit(Tan node, Void info) throws Exception {
		Complex z = node.getArg(0).accept(this, info);
		return (z.im() == 0.0) ? z.setRe(Math.tan(z.re()))
							   : z.tan();
	}

	@Override
	public Complex visit(Asin node, Void info) throws Exception {
		Complex z = node.getArg(0).accept(this, info);
		return (z.im() == 0.0) ? z.setRe(Math.asin(z.re()))
							   : z.asin();
	}

	@Override
	public Complex visit(Acos node, Void info) throws Exception {
		Complex z = node.getArg(0).accept(this, info);
		return (z.im() == 0.0) ? z.setRe(Math.acos(z.re()))
							   : z.acos();
	}

	@Override
	public Complex visit(Atan node, Void info) throws Exception {
		Complex z = node.getArg(0).accept(this, info);
		return (z.im() == 0.0) ? z.setRe(Math.atan(z.re()))
							   : z.atan();
	}

	@Override
	public Complex visit(ConstDef node, Void info) throws Exception {
		return node.getComplex();
	}
}
