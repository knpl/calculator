package com.knpl.calc.nodes;

import com.knpl.calc.visitors.Visitor;

public class Var extends Expr {
	private final String name;

	public Var(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		else if (o == null || !(o instanceof Var)) {
			return false;
		}
		else {
			Var that = (Var) o;
			return name.equals(that.name);
		}
	}
	
	@Override
	public int hashCode() {
		return name.hashCode();
	}
	
	@Override
	public Object accept(Visitor v) throws Exception {
		return v.visitVar(this);
	}
}
