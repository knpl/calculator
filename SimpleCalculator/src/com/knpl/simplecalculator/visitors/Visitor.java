package com.knpl.simplecalculator.visitors;

import com.knpl.simplecalculator.nodes.*;
import com.knpl.simplecalculator.nodes.Builtins.*;
import com.knpl.simplecalculator.util.FuncDef;
import com.knpl.simplecalculator.util.UserFuncDef;
import com.knpl.simplecalculator.util.BuiltinFuncDefs.*;

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
	
	public Object visit(MinDefinition node) throws Exception {
		return visit((FuncDef) node);
	}
	
	public Object visit(MaxDefinition node) throws Exception {
		return visit((FuncDef) node);
	}
	
	public Object visit(FloorDefinition node) throws Exception {
		return visit((FuncDef) node);
	}
	
	public Object visit(CeilDefinition node) throws Exception {
		return visit((FuncDef) node);
	}
	
	public Object visit(SqrtDefinition node) throws Exception {
		return visit((FuncDef) node);
	}
	
	public Object visit(AbsDefinition node) throws Exception {
		return visit((FuncDef) node);
	}
	
	public Object visit(LogDefinition node) throws Exception {
		return visit((FuncDef) node);
	}
	
	public Object visit(ExpDefinition node) throws Exception {
		return visit((FuncDef) node);
	}
	
	public Object visit(SinhDefinition node) throws Exception {
		return visit((FuncDef) node);
	}
	
	public Object visit(CoshDefinition node) throws Exception {
		return visit((FuncDef) node);
	}
	
	public Object visit(TanhDefinition node) throws Exception {
		return visit((FuncDef) node);
	}
	
	public Object visit(SinDefinition node) throws Exception {
		return visit((FuncDef) node);
	}
	
	public Object visit(CosDefinition node) throws Exception {
		return visit((FuncDef) node);
	}
	
	public Object visit(TanDefinition node) throws Exception {
		return visit((FuncDef) node);
	}
	
	public Object visit(AsinDefinition node) throws Exception {
		return visit((FuncDef) node);
	}
	
	public Object visit(AcosDefinition node) throws Exception {
		return visit((FuncDef) node);
	}
	
	public Object visit(AtanDefinition node) throws Exception {
		return visit((FuncDef) node);
	}

	public Object visit(ErfDefinition node) throws Exception {
		return visit((FuncDef) node);
	}
	
	public Object visit(GammaDefinition node) throws Exception {
		return visit((FuncDef) node);
	}
	
	public Object visit(LogGammaDefinition node) throws Exception {
		return visit((FuncDef) node);
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
