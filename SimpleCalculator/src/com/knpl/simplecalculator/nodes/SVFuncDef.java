package com.knpl.simplecalculator.nodes;

import com.knpl.simplecalculator.visitors.Visitor;

public abstract class SVFuncDef extends FuncDef {
	public SVFuncDef(Signature sig) {
		super(sig);
	}
	
	public abstract Num numEvaluate(Num arg) throws Exception;
	
	@Override
	public Object accept(Visitor v) throws Exception {
		return v.visit(this);
	}
}
