package com.knpl.calc.visitors;

import com.knpl.calc.nodes.*;
import com.knpl.calc.nodes.operators.*;
import com.knpl.calc.nodes.defs.*;
import com.knpl.calc.nodes.defs.BuiltinConstDefs.*;
import com.knpl.calc.nodes.defs.BuiltinFuncDefs.*;
import com.knpl.calc.nodes.numbers.Complex;
import com.knpl.calc.nodes.numbers.Num;
import com.knpl.calc.nodes.numbers.RealDouble;
import com.knpl.calc.nodes.operators.DegToRad;

public class Visitor {
	public Object visitNode(Node node) throws Exception {
		throw new Exception("Not implemented");
	}
	public Object visitNull(Null node) throws Exception {
		return visitNode((Node) node);
	}
	public Object visitExpr(Expr node) throws Exception {
		return visitNode((Node) node);
	}
	public Object visitBinOp(BinOp node) throws Exception {
		return visitExpr((Expr) node);
	}
	public Object visitMonOp(MonOp node) throws Exception {
		return visitExpr((Expr) node);
	}
	public Object visitCall(Call node) throws Exception {
		return visitExpr((Expr) node);
	}
	public Object visitAdd(Add node) throws Exception {
		return visitBinOp((BinOp) node);
	}
	public Object visitSub(Sub node) throws Exception {
		return visitBinOp((BinOp) node);
	}
	public Object visitMul(Mul node) throws Exception {
		return visitBinOp((BinOp) node);
	}
	public Object visitDiv(Div node) throws Exception {
		return visitBinOp((BinOp) node);
	}
	public Object visitMod(Mod node) throws Exception {
		return visitBinOp((BinOp) node);
	}
	public Object visitPow(Pow node) throws Exception {
		return visitBinOp((BinOp) node);
	}
	public Object visitMinus(Minus node) throws Exception {
		return visitMonOp((MonOp) node);
	}
	public Object visitFactorial(Factorial node) throws Exception {
		return visitMonOp((MonOp) node);
	}
	public Object visitDegToRad(DegToRad node) throws Exception {
		return visitMonOp((MonOp) node);
	}
	public Object visitNumTok(NumTok node) throws Exception {
		return visitExpr((Expr) node);
	}
	public Object visitVar(Var node) throws Exception {
		return visitExpr((Expr) node);
	}
	public Object visitSignature(Signature node) throws Exception {
		return visitNode((Node) node);
	}
	public Object visitFunc(Func node) throws Exception {
		return visitExpr((Expr) node);
	}
	public Object visitFuncDef(FuncDef node) throws Exception {
		return visitNode((Node) node);
	}
	public Object visitUserFuncDef(UserFuncDef node) throws Exception {
		return visitFuncDef((FuncDef) node);
	}
	public Object visitMinDef(MinDef node) throws Exception {
		return visitFuncDef((FuncDef) node);
	}
	public Object visitMaxDef(MaxDef node) throws Exception {
		return visitFuncDef((FuncDef) node);
	}
	public Object visitFloorDef(FloorDef node) throws Exception {
		return visitFuncDef((FuncDef) node);
	}
	public Object visitCeilDef(CeilDef node) throws Exception {
		return visitFuncDef((FuncDef) node);
	}
	public Object visitSqrtDef(SqrtDef node) throws Exception {
		return visitFuncDef((FuncDef) node);
	}
	public Object visitAbsDef(AbsDef node) throws Exception {
		return visitFuncDef((FuncDef) node);
	}
	public Object visitLogDef(LogDef node) throws Exception {
		return visitFuncDef((FuncDef) node);
	}
	public Object visitExpDef(ExpDef node) throws Exception {
		return visitFuncDef((FuncDef) node);
	}
	public Object visitSinhDef(SinhDef node) throws Exception {
		return visitFuncDef((FuncDef) node);
	}
	public Object visitCoshDef(CoshDef node) throws Exception {
		return visitFuncDef((FuncDef) node);
	}
	public Object visitTanhDef(TanhDef node) throws Exception {
		return visitFuncDef((FuncDef) node);
	}
	public Object visitSinDef(SinDef node) throws Exception {
		return visitFuncDef((FuncDef) node);
	}
	public Object visitCosDef(CosDef node) throws Exception {
		return visitFuncDef((FuncDef) node);
	}
	public Object visitTanDef(TanDef node) throws Exception {
		return visitFuncDef((FuncDef) node);
	}
	public Object visitAsinDef(AsinDef node) throws Exception {
		return visitFuncDef((FuncDef) node);
	}
	public Object visitAcosDef(AcosDef node) throws Exception {
		return visitFuncDef((FuncDef) node);
	}
	public Object visitAtanDef(AtanDef node) throws Exception {
		return visitFuncDef((FuncDef) node);
	}
	public Object visitErfDef(ErfDef node) throws Exception {
		return visitFuncDef((FuncDef) node);
	}
	public Object visitGammaDef(GammaDef node) throws Exception {
		return visitFuncDef((FuncDef) node);
	}
	public Object visitLogGammaDef(LogGammaDef node) throws Exception {
		return visitFuncDef((FuncDef) node);
	}
	public Object visitReDef(ReDef node) throws Exception {
		return visitFuncDef((FuncDef) node);
	}
	public Object visitImDef(ImDef node) throws Exception {
		return visitFuncDef((FuncDef) node);
	}
	public Object visitArgDef(ArgDef node) throws Exception {
		return visitFuncDef((FuncDef) node);
	}
	public Object visitModDef(ModDef node) throws Exception {
		return visitFuncDef((FuncDef) node);
	}
	public Object visitConjDef(ConjDef node) throws Exception {
		return visitFuncDef((FuncDef) node);
	}
	public Object visitConst(Const node) throws Exception {
		return visitExpr((Expr) node);
	}
	public Object visitConstDef(ConstDef node) throws Exception {
		return visitNode((Node) node);
	}
	public Object visitUserConstDef(UserConstDef node) throws Exception {
		return visitConstDef((ConstDef) node);
	}
	public Object visitPiDef(PiDef node) throws Exception {
		return visitConstDef((ConstDef) node);
	}
	public Object visitEDef(EDef node) throws Exception {
		return visitConstDef((ConstDef) node);
	}
	public Object visitIDef(IDef node) throws Exception {
		return visitConstDef((ConstDef) node);
	}
	public Object visitNum(Num node) throws Exception {
		return visitExpr((Expr) node);
	}
	public Object visitRealDouble(RealDouble node) throws Exception {
		return visitNum((Num) node);
	}
	public Object visitComplex(Complex node) throws Exception {
		return visitNum((Num) node);
	}
}