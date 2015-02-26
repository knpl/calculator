package com.knpl.simplecalculator.visitors;

import com.knpl.simplecalculator.nodes.*;
import com.knpl.simplecalculator.nodes.Builtins.*;

public class Visitor {
	public Object visit(Node node, Object info) throws Exception {
		throw new Exception("Not implemented");
	}
	
	public Object visit(Null node, Object info) throws Exception {
		return visit((Node) node, info);
	}
	
	public Object visit(Expr node, Object info) throws Exception {
		return visit((Node) node, info);
	}
	
	public Object visit(BinOp node, Object info) throws Exception {
		return visit((Expr) node, info);
	}
	
	public Object visit(MonOp node, Object info) throws Exception {
		return visit((Expr) node, info);
	}
	
	public Object visit(Func node, Object info) throws Exception {
		return visit((Expr) node, info);
	}
	
	public Object visit(UserFunc node, Object info) throws Exception {
		return visit((Func) node, info);
	}
	
	public Object visit(Call node, Object info) throws Exception {
		return visit((Expr) node, info);
	}
	
	public Object visit(Add node, Object info) throws Exception {
		return visit((BinOp) node, info);
	}
	
	public Object visit(Sub node, Object info) throws Exception {
		return visit((BinOp) node, info);
	}
	
	public Object visit(Mul node, Object info) throws Exception {
		return visit((BinOp) node, info);
	}
	
	public Object visit(Div node, Object info) throws Exception {
		return visit((BinOp) node, info);
	}
	
	public Object visit(Pow node, Object info) throws Exception {
		return visit((BinOp) node, info);
	}
	
	public Object visit(Minus node, Object info) throws Exception {
		return visit((MonOp) node, info);
	}
	
	public Object visit(Num node, Object info) throws Exception {
		return visit((Expr) node, info);
	}
	
	public Object visit(Var node, Object info) throws Exception {
		return visit((Expr) node, info);
	}
	
	public Object visit(FuncDefNode node, Object info) throws Exception {
		return visit((Node) node, info);
	}
	
	public Object visit(Signature node, Object info) throws Exception {
		return visit((Node) node, info);
	}
	
	public Object visit(Min node, Object info) throws Exception {
		return visit((Func) node, info);
	}
	
	public Object visit(Max node, Object info) throws Exception {
		return visit((Func) node, info);
	}
	
	public Object visit(Sqrt node, Object info) throws Exception {
		return visit((Func) node, info);
	}
	
	public Object visit(Abs node, Object info) throws Exception {
		return visit((Func) node, info);
	}
	
	public Object visit(Log node, Object info) throws Exception {
		return visit((Func) node, info);
	}
	
	public Object visit(Exp node, Object info) throws Exception {
		return visit((Func) node, info);
	}
	
	public Object visit(Sinh node, Object info) throws Exception {
		return visit((Func) node, info);
	}
	
	public Object visit(Cosh node, Object info) throws Exception {
		return visit((Func) node, info);
	}
	
	public Object visit(Sin node, Object info) throws Exception {
		return visit((Func) node, info);
	}
	
	public Object visit(Cos node, Object info) throws Exception {
		return visit((Func) node, info);
	}
	
	public Object visit(Tan node, Object info) throws Exception {
		return visit((Func) node, info);
	}
	
	public Object visit(Asin node, Object info) throws Exception {
		return visit((Func) node, info);
	}
	
	public Object visit(Acos node, Object info) throws Exception {
		return visit((Func) node, info);
	}
	
	public Object visit(Atan node, Object info) throws Exception {
		return visit((Func) node, info);
	}
}
