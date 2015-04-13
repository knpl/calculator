package com.knpl.simplecalculator.util;

import java.util.List;

import com.knpl.simplecalculator.nodes.Complex;
import com.knpl.simplecalculator.nodes.Node;
import com.knpl.simplecalculator.nodes.Signature;
import com.knpl.simplecalculator.visitors.Visitor;

public abstract class FuncDef extends Node {
	public abstract Signature getSignature();
	public abstract String getDescription();
	public abstract Complex complexEvaluate(List<Complex> args) throws Exception;
	public abstract Double evaluate(List<Double> args) throws Exception;
	
	@Override
	public String toString() {
		return getDescription();
	}
	
	@Override
	public Object accept(Visitor v) throws Exception {
		return v.visit(this);
	}
}
