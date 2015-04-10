package com.knpl.simplecalculator.util;

import java.util.List;

import com.knpl.simplecalculator.nodes.Call;
import com.knpl.simplecalculator.nodes.Complex;
import com.knpl.simplecalculator.nodes.Expr;
import com.knpl.simplecalculator.nodes.Func;
import com.knpl.simplecalculator.nodes.Signature;

public abstract class FuncDef {
	public abstract Signature getSignature();
	public abstract String getDescription();
	public abstract Func createFunction(Call call) throws Exception;
	public abstract Func createFunction(Signature sig, List<Expr> args) throws Exception;
	public abstract Complex complexEvaluate(List<Complex> args) throws Exception;
	public abstract Double evaluate(List<Double> args) throws Exception;
	
	@Override
	public String toString() {
		return getDescription();
	}
}
