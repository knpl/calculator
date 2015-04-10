package com.knpl.simplecalculator.nodes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.knpl.simplecalculator.visitors.Visitor;

public class Signature extends Node implements Serializable {
	
	private static final long serialVersionUID = 9021859215633531622L;
	
	private final String name;
	private final List<Var> parameters;
	
	public Signature(String name, List<Var> parameters) {
		this.name = name;
		this.parameters = Collections.unmodifiableList(new ArrayList<Var>(parameters));
	}
	
	public Signature(String name) {
		this.name = name;
		this.parameters = Collections.unmodifiableList(new ArrayList<Var>());
	}
	
	public String getName() {
		return name;
	}
	
	public List<Var> getParameters() {
		return parameters;
	}
	
	public boolean match(Signature sig) {
		return name.equals(sig.name) && parameters.size() == sig.parameters.size(); 
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
