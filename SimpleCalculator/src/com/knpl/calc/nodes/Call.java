package com.knpl.calc.nodes;

import java.util.ArrayList;
import java.util.List;

import com.knpl.calc.nodes.defs.Signature;
import com.knpl.calc.visitors.Visitor;

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
	
	public boolean match(Signature sig) {
		return name.equals(sig.getName()) && sig.getParameters().size() == arguments.size();
	}
	
	@Override
	public Object accept(Visitor v) throws Exception {
		return v.visitCall(this);
	}
}
