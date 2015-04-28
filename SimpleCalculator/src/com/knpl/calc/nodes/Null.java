package com.knpl.calc.nodes;

import com.knpl.calc.SimpleCalculatorActivity;
import com.knpl.calc.visitors.Visitor;

public class Null extends Node {
	
	public static final Null NULL = new Null();
	
	@Override
	public void execute(SimpleCalculatorActivity calculator) throws Exception {
		calculator.print("...");
	}
	
	@Override
	public Object accept(Visitor v) throws Exception {
		return v.visit(this);
	}
}