package com.knpl.simplecalculator.nodes;

import com.knpl.simplecalculator.SimpleCalculatorActivity;
import com.knpl.simplecalculator.visitors.Visitor;

public class Null extends Node {
	
	public static final Null NULL = new Null();
	
	@Override
	public void execute(SimpleCalculatorActivity calculator) throws Exception {
		calculator.print("...");
	}
	
	@Override
	public <O, I> O accept(Visitor<O, I> v, I info) throws Exception {
		return v.visit(this, info);
	}
}
