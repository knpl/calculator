package com.knpl.simplecalculator.nodes;

import java.util.ArrayList;
import java.util.List;

import com.knpl.simplecalculator.visitors.Visitor;

public class Call extends Expr {

	private final String name;
	private final List<Expr> arguments;
	
	public Call(String name, List<Expr> args) {
		this.name = name;
		this.arguments = new ArrayList<Expr>(args);
	}

	public String getName() {
		return name;
	}
	
	public List<Expr> getArguments() {
		return arguments;
	}
	
	@Override
	public <O, I> O accept(Visitor<O, I> v, I info) throws Exception {
		return v.visit(this, info);
	}
}
