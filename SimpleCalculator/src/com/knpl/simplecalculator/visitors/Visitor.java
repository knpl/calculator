package com.knpl.simplecalculator.visitors;

import com.knpl.simplecalculator.nodes.*;
import com.knpl.simplecalculator.nodes.Builtins.*;

public class Visitor<O, I> {
	public O visit(Node node, I info) throws Exception {
		throw new Exception("Not implemented");
	}
	
	public O visit(Null node, I info) throws Exception {
		return visit((Node) node, info);
	}
	
	public O visit(Expr node, I info) throws Exception {
		return visit((Node) node, info);
	}
	
	public O visit(BinOp node, I info) throws Exception {
		return visit((Expr) node, info);
	}
	
	public O visit(MonOp node, I info) throws Exception {
		return visit((Expr) node, info);
	}
	
	public O visit(Func node, I info) throws Exception {
		return visit((Expr) node, info);
	}
	
	public O visit(UserFunc node, I info) throws Exception {
		return visit((Func) node, info);
	}
	
	public O visit(Call node, I info) throws Exception {
		return visit((Expr) node, info);
	}
	
	public O visit(Add node, I info) throws Exception {
		return visit((BinOp) node, info);
	}
	
	public O visit(Sub node, I info) throws Exception {
		return visit((BinOp) node, info);
	}
	
	public O visit(Mul node, I info) throws Exception {
		return visit((BinOp) node, info);
	}
	
	public O visit(Div node, I info) throws Exception {
		return visit((BinOp) node, info);
	}
	
	public O visit(Pow node, I info) throws Exception {
		return visit((BinOp) node, info);
	}
	
	public O visit(Minus node, I info) throws Exception {
		return visit((MonOp) node, info);
	}
	
	public O visit(Num node, I info) throws Exception {
		return visit((Expr) node, info);
	}
	
	public O visit(Var node, I info) throws Exception {
		return visit((Expr) node, info);
	}
	
	public O visit(FuncDefNode node, I info) throws Exception {
		return visit((Node) node, info);
	}
	
	public O visit(Signature node, I info) throws Exception {
		return visit((Node) node, info);
	}
	
	public O visit(Min node, I info) throws Exception {
		return visit((Func) node, info);
	}
	
	public O visit(Max node, I info) throws Exception {
		return visit((Func) node, info);
	}
	
	public O visit(Sqrt node, I info) throws Exception {
		return visit((Func) node, info);
	}
	
	public O visit(Abs node, I info) throws Exception {
		return visit((Func) node, info);
	}
	
	public O visit(Log node, I info) throws Exception {
		return visit((Func) node, info);
	}
	
	public O visit(Exp node, I info) throws Exception {
		return visit((Func) node, info);
	}
	
	public O visit(Sinh node, I info) throws Exception {
		return visit((Func) node, info);
	}
	
	public O visit(Cosh node, I info) throws Exception {
		return visit((Func) node, info);
	}
	
	public O visit(Sin node, I info) throws Exception {
		return visit((Func) node, info);
	}
	
	public O visit(Cos node, I info) throws Exception {
		return visit((Func) node, info);
	}
	
	public O visit(Tan node, I info) throws Exception {
		return visit((Func) node, info);
	}
	
	public O visit(Asin node, I info) throws Exception {
		return visit((Func) node, info);
	}
	
	public O visit(Acos node, I info) throws Exception {
		return visit((Func) node, info);
	}
	
	public O visit(Atan node, I info) throws Exception {
		return visit((Func) node, info);
	}
	
	public O visit(ConstDef node, I info) throws Exception {
		return visit((Expr) node, info);
	}
	
	public O visit(Pi node, I info) throws Exception {
		return visit((ConstDef) node, info);
	}
	
	public O visit(Euler node, I info) throws Exception {
		return visit((ConstDef) node, info);
	}
	
	public O visit(Im node, I info) throws Exception {
		return visit((ConstDef) node, info);
	}
	
	public O visit(ConstDefNode node, I info) throws Exception {
		return visit((Node) node, info);
	}
}
