package com.knpl.simplecalculator.nodes;

import java.util.List;

import com.knpl.simplecalculator.visitors.Visitor;

public abstract class MVFuncDef extends FuncDef {
	public MVFuncDef(Signature sig) {
		super(sig);
	}
	
	public abstract Complex complexEvaluate(List<Complex> args) throws Exception;
	public abstract Double evaluate(List<Double> args) throws Exception;
	
	@Override
	public Object accept(Visitor v) throws Exception {
		return v.visit(this);
	}
}