package com.knpl.simplecalculator.visitors;

import com.knpl.simplecalculator.nodes.*;
import com.knpl.simplecalculator.nodes.Builtins.*;

public class Visitor {
	public Object visit(Node node) throws Exception {
		throw new Exception("Not implemented");
	}
	
	public Object visit(Null node) throws Exception {
		return visit((Node) node);
	}
	
	public Object visit(Expr node) throws Exception {
		return visit((Node) node);
	}
	
	public Object visit(BinOp node) throws Exception {
		return visit((Expr) node);
	}
	
	public Object visit(MonOp node) throws Exception {
		return visit((Expr) node);
	}
	
	public Object visit(Call node) throws Exception {
		return visit((Expr) node);
	}
	
	public Object visit(Add node) throws Exception {
		return visit((BinOp) node);
	}
	
	public Object visit(Sub node) throws Exception {
		return visit((BinOp) node);
	}
	
	public Object visit(Mul node) throws Exception {
		return visit((BinOp) node);
	}
	
	public Object visit(Div node) throws Exception {
		return visit((BinOp) node);
	}
	
	public Object visit(Mod node) throws Exception {
		return visit((BinOp) node);
	}
	
	public Object visit(Pow node) throws Exception {
		return visit((BinOp) node);
	}
	
	public Object visit(Minus node) throws Exception {
		return visit((MonOp) node);
	}
	
	public Object visit(Factorial node) throws Exception {
		return visit((MonOp) node);
	}
	
	public Object visit(DegToRad node) throws Exception {
		return visit((MonOp) node);
	}
	
	public Object visit(Num node) throws Exception {
		return visit((Expr) node);
	}
	
	public Object visit(Var node) throws Exception {
		return visit((Expr) node);
	}
	
	public Object visit(FuncDefNode node) throws Exception {
		return visit((Node) node);
	}
	
	public Object visit(Signature node) throws Exception {
		return visit((Node) node);
	}

	public Object visit(Func node) throws Exception {
		return visit((Expr) node);
	}
	
	public Object visit(SVFunc node) throws Exception {
		return visit((Func) node);
	}
	
	public Object visit(MVFunc node) throws Exception {
		return visit((Func) node);
	}
	
	public Object visit(UserFunc node) throws Exception {
		return visit((MVFunc) node);
	}
	
	public Object visit(Min node) throws Exception {
		return visit((MVFunc) node);
	}
	
	
	public Object visit(Max node) throws Exception {
		return visit((MVFunc) node);
	}
	
	public Object visit(Floor node) throws Exception {
		return visit((SVFunc) node);
	}
	
	public Object visit(Ceil node) throws Exception {
		return visit((SVFunc) node);
	}
	
	public Object visit(Sqrt node) throws Exception {
		return visit((SVFunc) node);
	}
	
	public Object visit(Abs node) throws Exception {
		return visit((SVFunc) node);
	}
	
	public Object visit(Log node) throws Exception {
		return visit((SVFunc) node);
	}
	
	public Object visit(Exp node) throws Exception {
		return visit((SVFunc) node);
	}
	
	public Object visit(Sinh node) throws Exception {
		return visit((SVFunc) node);
	}
	
	public Object visit(Cosh node) throws Exception {
		return visit((SVFunc) node);
	}
	
	public Object visit(Tanh node) throws Exception {
		return visit((SVFunc) node);
	}
	
	public Object visit(Sin node) throws Exception {
		return visit((SVFunc) node);
	}
	
	public Object visit(Cos node) throws Exception {
		return visit((SVFunc) node);
	}
	
	public Object visit(Tan node) throws Exception {
		return visit((SVFunc) node);
	}
	
	public Object visit(Asin node) throws Exception {
		return visit((SVFunc) node);
	}
	
	public Object visit(Acos node) throws Exception {
		return visit((SVFunc) node);
	}
	
	public Object visit(Atan node) throws Exception {
		return visit((SVFunc) node);
	}

	public Object visit(Erf node) throws Exception {
		return visit((SVFunc) node);
	}
	
	public Object visit(Gamma node) throws Exception {
		return visit((SVFunc) node);
	}
	
	public Object visit(LogGamma node) throws Exception {
		return visit((SVFunc) node);
	}
	
	public Object visit(LogBeta node) throws Exception {
		return visit((MVFunc) node);
	}
	
	public Object visit(ConstDef node) throws Exception {
		return visit((Expr) node);
	}
	
	public Object visit(Pi node) throws Exception {
		return visit((ConstDef) node);
	}
	
	public Object visit(Euler node) throws Exception {
		return visit((ConstDef) node);
	}
	
	public Object visit(Im node) throws Exception {
		return visit((ConstDef) node);
	}
	
	public Object visit(ConstDefNode node) throws Exception {
		return visit((Node) node);
	}
	
	public Object visit(UserConstDef node) throws Exception {
		return visit((ConstDef) node);
	}
	
	public Object visit(Complex node) throws Exception {
		return visit((Expr) node);
	}
}
