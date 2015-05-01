package com.knpl.calc.nodes;


import com.knpl.calc.MainInterface;
import com.knpl.calc.visitors.Visitor;

public abstract class Node {
	public void execute(MainInterface interfaze) throws Exception {
		throw new Exception("Node doesn't implement execute");
	}
	
	public Object accept(Visitor v) throws Exception {
		return v.visitNode(this);
	}
}