package com.knpl.simplecalculator.nodes;

import java.util.ArrayList;
import java.util.List;

import com.knpl.simplecalculator.visitors.Visitor;

public class Call extends Expr {

	private String name;
	private List<Expr> arguments;
	
	public Call(String name, List<Expr> args) {
		this.name = name;
		this.arguments = new ArrayList<Expr>(args.size());
		for (Expr arg : args) {
			this.arguments.add(arg);
		}
	}

	public String getName() {
		return name;
	}
	
	public List<Expr> getArguments() {
		return arguments;
	}
	
	@Override
	public Object accept(Visitor v) throws Exception {
		return v.visit(this);
	}

}
