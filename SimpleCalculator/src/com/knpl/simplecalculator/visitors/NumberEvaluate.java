package com.knpl.simplecalculator.visitors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.knpl.simplecalculator.util.MyNumber;

import org.apache.commons.math3.special.Gamma;

import com.knpl.simplecalculator.nodes.Add;
import com.knpl.simplecalculator.nodes.Complex;
import com.knpl.simplecalculator.nodes.ConstDef;
import com.knpl.simplecalculator.nodes.DegToRad;
import com.knpl.simplecalculator.nodes.Div;
import com.knpl.simplecalculator.nodes.Expr;
import com.knpl.simplecalculator.nodes.Factorial;
import com.knpl.simplecalculator.nodes.MVFunc;
import com.knpl.simplecalculator.nodes.Minus;
import com.knpl.simplecalculator.nodes.Mod;
import com.knpl.simplecalculator.nodes.Mul;
import com.knpl.simplecalculator.nodes.NumTok;
import com.knpl.simplecalculator.nodes.Pow;
import com.knpl.simplecalculator.nodes.SVFunc;
import com.knpl.simplecalculator.nodes.Sub;
import com.knpl.simplecalculator.nodes.Var;

public class NumberEvaluate extends Visitor {
	private Map<String, MyNumber> map;
	
	public NumberEvaluate() {
		map = new HashMap<String, MyNumber>();
	}
	
	public NumberEvaluate(Map<String, MyNumber> map) {
		this.map = map;
	}
	
	@Override
	public MyNumber visit(Add node) throws Exception {
		MyNumber a = (MyNumber) node.getLHS().accept(this);
		MyNumber b = (MyNumber) node.getRHS().accept(this);
		return a.add(b);
	}

	@Override
	public MyNumber visit(Sub node) throws Exception {
		MyNumber a = (MyNumber) node.getLHS().accept(this);
		MyNumber b = (MyNumber) node.getRHS().accept(this);
		return a.sub(b);
	}

	@Override
	public MyNumber visit(Mul node) throws Exception {
		MyNumber a = (MyNumber) node.getLHS().accept(this);
		MyNumber b = (MyNumber) node.getRHS().accept(this);
		return a.mul(b);
	}

	@Override
	public MyNumber visit(Div node) throws Exception {
		MyNumber a = (MyNumber) node.getLHS().accept(this);
		MyNumber b = (MyNumber) node.getRHS().accept(this);
		return a.div(b);
	}
	
	@Override
	public MyNumber visit(Mod node) throws Exception {
		MyNumber a = (MyNumber) node.getLHS().accept(this);
		MyNumber b = (MyNumber) node.getRHS().accept(this);
		return a.mod(b);
	}

	@Override
	public MyNumber visit(Pow node) throws Exception {
		MyNumber a = (MyNumber) node.getLHS().accept(this);
		MyNumber b = (MyNumber) node.getRHS().accept(this);
		return a.pow(b);
	}
	
	@Override
	public MyNumber visit(Minus node) throws Exception {
		return ((MyNumber) node.getOp().accept(this)).neg();
	}
	
	@Override
	public MyNumber visit(Factorial node) throws Exception {
		return ((MyNumber) node.getOp().accept(this)).factorial();
	}
	
	@Override
	public MyNumber visit(DegToRad node) throws Exception {
		return ((MyNumber) node.getOp().accept(this)).deg2rad();
	}

	@Override
	public MyNumber visit(NumTok node) throws Exception {
		return node.getRealDouble();
	}
	
	@Override
	public MyNumber visit(Var node) throws Exception {
		MyNumber result = map.get(node.getName());
		if (result == null) {
			throw new Exception("Undeclared variable "+node.getName());
		}
		return result;
	}
	
//	@Override
//	public MyNumber visit(SVFunc node) throws Exception {
//		return node.getSVFuncDef().evaluate((MyNumber) node.getArgument().accept(this));
//	}
//
//	@Override
//	public MyNumber visit(MVFunc node) throws Exception {
//		List<Double> args = new ArrayList<Double>();
//		for (Expr e : node.getArguments()) {
//			args.add((MyNumber)e.accept(this));
//		}
//		return node.getMVFuncDef().evaluate(args);
//	}
//	
//	@Override
//	public MyNumber visit(ConstDef node) throws Exception {
//		return node.getDouble();
//	}
//	
//	@Override
//	public MyNumber visit(Complex node) throws Exception {
//		return node.im() == 0.0 ? node.re() : Double.NaN;
//	} 
}
