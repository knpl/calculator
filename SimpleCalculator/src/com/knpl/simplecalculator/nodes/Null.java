package com.knpl.simplecalculator.nodes;

import com.knpl.simplecalculator.SimpleCalculatorActivity;
import com.knpl.simplecalculator.visitors.Visitor;

public class Null extends Node {
	
	public static final Null NULL = new Null();
	
	public Null() {
		super();
	}
	
	@Override
	public void execute(SimpleCalculatorActivity calculator) throws Exception {
		calculator.print("...");
	}
	
	@Override
	public Object accept(Visitor v, Object info) throws Exception {
		return v.visit(this, null);
	}
}
