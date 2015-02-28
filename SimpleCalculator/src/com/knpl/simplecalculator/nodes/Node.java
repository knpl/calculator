package com.knpl.simplecalculator.nodes;


import com.knpl.simplecalculator.SimpleCalculatorActivity;
import com.knpl.simplecalculator.visitors.Visitor;

public abstract class Node {
	public void execute(SimpleCalculatorActivity calculator) throws Exception {
		throw new Exception("Node doesn't implement execute");
	}
	
	public <O, I> O accept(Visitor<O, I> v, I info) throws Exception {
		return v.visit(this, info);
	}
}
