package com.knpl.calc.nodes;

import java.util.List;

import com.knpl.calc.visitors.Visitor;

public abstract class FuncDef extends Node {
	
	public final Signature sig;
	public String description;
	
	public FuncDef(Signature sig, String description) {
		this.sig = sig;
		this.description = description;
	}
	
	public FuncDef(Signature sig) {
		this(sig, "");
	}
	
	public Signature getSignature() {
		return sig;
	}
	
	public String getDescription() {
		return description;
	}
	
	public FuncDef setDescription(String description) {
		this.description = description;
		return this;
	}
	
	public abstract Num numEvaluate(List<Num> args) throws Exception;
	
	@Override
	public String toString() {
		return sig + " = " + getDescription();
	}
	
	@Override
	public Object accept(Visitor v) throws Exception {
		return v.visit(this);
	}
}
