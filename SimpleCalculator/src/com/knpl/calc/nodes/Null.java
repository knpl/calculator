package com.knpl.calc.nodes;

import com.knpl.calc.visitors.Visitor;

public class Null extends Node {
	
	public static final Null NULL = new Null();
	
	@Override
	public Object accept(Visitor v) throws Exception {
		return v.visitNull(this);
	}
}