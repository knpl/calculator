package com.knpl.simplecalculator.nodes;

import com.knpl.simplecalculator.visitors.Visitor;

public abstract class FuncDef extends Node {
	
	public final Signature sig;
	public final String description;
	
	public FuncDef(Signature sig, String description) {
		this.sig = sig;
		this.description = description;
	}
	
	public Signature getSignature() {
		return sig;
	}
	
	public String getDescription() {
		return description;
	}
	
	@Override
	public String toString() {
		return getDescription();
	}
	
	@Override
	public Object accept(Visitor v) throws Exception {
		return v.visit(this);
	}
}
