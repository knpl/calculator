package com.knpl.simplecalculator.nodes;

import java.util.Collections;
import java.util.List;

import com.knpl.simplecalculator.visitors.Visitor;

public class Signature extends Node {
	private final String name;
	private final List<Var> parameters;
	
	public Signature(String name, List<Var> parameters) {
		this.name = name;
		this.parameters = Collections.unmodifiableList(parameters);
	}
	
	public String getName() {
		return name;
	}
	
	public List<Var> getParameters() {
		return parameters;
	}
	
	@Override
	public Object accept(Visitor v) throws Exception {
		return v.visit(this);
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer(name+"(");
		int size = parameters.size();
		for (int i = 0; i < size - 1; ++i) {
			sb.append(parameters.get(i).getName()+", ");
		}
		sb.append(parameters.get(size-1).getName()+")");
		
		return sb.toString();
	}
}
