package com.knpl.simplecalculator.nodes;

import com.knpl.simplecalculator.util.MyNumber;
import com.knpl.simplecalculator.visitors.Visitor;

public abstract class ConstDef extends Expr {
	
	protected final String name,
						   description;
	
	public ConstDef(String name, String description) {
		this.name = name;
		this.description = description;
	}
	
	public String getName() {
		return name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public abstract double getDouble();
	public abstract Complex getComplex();
	
	public abstract MyNumber getNumber();
	
	@Override
	public String toString() {
		return getDescription();
	}
	
	@Override
	public Object accept(Visitor v) throws Exception {
		return v.visit(this);
	}
}
