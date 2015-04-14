package com.knpl.simplecalculator.visitors;

import com.knpl.simplecalculator.nodes.*;
import com.knpl.simplecalculator.nodes.BuiltinFuncDefs.*;
import com.knpl.simplecalculator.nodes.BuiltinConstDefs.*;

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
	
	public Object visit(FuncDef node) throws Exception {
		return visit((Node) node);
	}
	
	public Object visit(UserFuncDef node) throws Exception {
		return visit((FuncDef) node);
	}
	
	public Object visit(MinDef node) throws Exception {
		return visit((FuncDef) node);
	}
	
	public Object visit(MaxDef node) throws Exception {
		return visit((FuncDef) node);
	}
	
	public Object visit(FloorDef node) throws Exception {
		return visit((FuncDef) node);
	}
	
	public Object visit(CeilDef node) throws Exception {
		return visit((FuncDef) node);
	}
	
	public Object visit(SqrtDef node) throws Exception {
		return visit((FuncDef) node);
	}
	
	public Object visit(AbsDef node) throws Exception {
		return visit((FuncDef) node);
	}
	
	public Object visit(LogDef node) throws Exception {
		return visit((FuncDef) node);
	}
	
	public Object visit(ExpDef node) throws Exception {
		return visit((FuncDef) node);
	}
	
	public Object visit(SinhDef node) throws Exception {
		return visit((FuncDef) node);
	}
	
	public Object visit(CoshDef node) throws Exception {
		return visit((FuncDef) node);
	}
	
	public Object visit(TanhDef node) throws Exception {
		return visit((FuncDef) node);
	}
	
	public Object visit(SinDef node) throws Exception {
		return visit((FuncDef) node);
	}
	
	public Object visit(CosDef node) throws Exception {
		return visit((FuncDef) node);
	}
	
	public Object visit(TanDef node) throws Exception {
		return visit((FuncDef) node);
	}
	
	public Object visit(AsinDef node) throws Exception {
		return visit((FuncDef) node);
	}
	
	public Object visit(AcosDef node) throws Exception {
		return visit((FuncDef) node);
	}
	
	public Object visit(AtanDef node) throws Exception {
		return visit((FuncDef) node);
	}

	public Object visit(ErfDef node) throws Exception {
		return visit((FuncDef) node);
	}
	
	public Object visit(GammaDef node) throws Exception {
		return visit((FuncDef) node);
	}
	
	public Object visit(LogGammaDef node) throws Exception {
		return visit((FuncDef) node);
	}
	
	public Object visit(ConstDef node) throws Exception {
		return visit((Expr) node);
	}
	
	public Object visit(PiDef node) throws Exception {
		return visit((ConstDef) node);
	}
	
	public Object visit(EDef node) throws Exception {
		return visit((ConstDef) node);
	}
	
	public Object visit(IDef node) throws Exception {
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
