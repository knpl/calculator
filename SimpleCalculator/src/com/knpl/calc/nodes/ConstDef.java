package com.knpl.calc.nodes;

import com.knpl.calc.visitors.Visitor;

public abstract class ConstDef extends Node {
	
	protected final String name;
	protected String description;
	
	public ConstDef(String name, String description) {
		this.name = name;
		this.description = description;
	}
	
	public ConstDef(String name) {
		this(name, "");
	}
	
	public String getName() {
		return name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public abstract Num getNum() throws Exception;
	
	@Override
	public String toString() {
		return name+" = "+getDescription();
	}
	
	@Override
	public Object accept(Visitor v) throws Exception {
		return v.visit(this);
	}
}
