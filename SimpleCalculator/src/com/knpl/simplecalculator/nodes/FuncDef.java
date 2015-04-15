package com.knpl.simplecalculator.nodes;

import com.knpl.simplecalculator.visitors.Visitor;

public abstract class FuncDef extends Node {
	
	public final Signature sig;
	
	public FuncDef(Signature sig) {
		this.sig = sig;
	}
	
	public Signature getSignature() {
		return sig;
	}
	
	public abstract String getDescription();
	
	@Override
	public String toString() {
		return getDescription();
	}
	
	@Override
	public Object accept(Visitor v) throws Exception {
		return v.visit(this);
	}
}
