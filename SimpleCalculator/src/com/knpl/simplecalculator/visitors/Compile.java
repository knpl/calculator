package com.knpl.simplecalculator.visitors;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.knpl.simplecalculator.nodes.Builtins.*;
import com.knpl.simplecalculator.nodes.*;
import com.knpl.simplecalculator.util.ByteCodes;
import com.knpl.simplecalculator.util.Program;
import com.knpl.simplecalculator.util.UserFuncDef;

public class Compile extends Visitor {
	
	private ByteArrayOutputStream code;
	private int nbytes;
	
	private int maxStackSize;
	private int curStackSize;
	
	private List<Var> parameters;
	
	private Map<Double,Integer> constantMap;
	private Map<UserFunc, Integer> functionMap;
	private Map<UserFunc, Integer> newFunctionMap;
	
	private Program program;
	
	public Compile(List<Var> parameters) {
		code = new ByteArrayOutputStream();
		nbytes = 0;
		this.parameters = parameters;
		constantMap = new HashMap<Double, Integer>();
		functionMap = new HashMap<UserFunc, Integer>();
		newFunctionMap = new HashMap<UserFunc, Integer>();
		program = null;
		
		maxStackSize = curStackSize = 0;
	}
	
	public Compile() {
		code = new ByteArrayOutputStream();
		nbytes = 0;
		parameters = new ArrayList<Var>();
		constantMap = new HashMap<Double, Integer>();
		functionMap = new HashMap<UserFunc, Integer>();
		newFunctionMap = new HashMap<UserFunc, Integer>();
		program = null;
		
		maxStackSize = curStackSize = 0;
	}
	
	public Program getProgram() {
		return program;
	}
	
	private void write(int b) {
		code.write(b);
		nbytes+=1;
	}
	
	private void push() {
		curStackSize += 1;
		if (curStackSize > maxStackSize)
			maxStackSize = curStackSize;
	}
	
	private void push(int s) {
		curStackSize += s;
		if (curStackSize > maxStackSize)
			maxStackSize = curStackSize;
	}
	
	private void pop() {
		curStackSize -= 1;
	}
	
	private void pop(int s) {
		curStackSize -= s;
	}
	
	private void clear(ArrayList<UserFunc> list, int size) {
		list.clear();
		list.addAll(Collections.nCopies(size,(UserFunc) null));
	}
	
	@Override
	public Void visit(FuncDefNode node) throws Exception {
		Signature sig = node.getSignature();
		parameters = sig.getParameters();
		
		node.getExpression().accept(this);
		write(ByteCodes.RET);
		write(parameters.size());
		pop();
		
		ArrayList<UserFunc> newFunctions = new ArrayList<UserFunc>(newFunctionMap.size());
		clear(newFunctions, newFunctionMap.size());
		
		ArrayList<Integer> offsets = new ArrayList<Integer>();
		
		int nfunctions;
		while (!newFunctions.isEmpty()) {
			nfunctions = functionMap.size();
			functionMap.putAll(newFunctionMap);
			
			for (Map.Entry<UserFunc, Integer> e : newFunctionMap.entrySet()) {
				newFunctions.set(e.getValue()-nfunctions, e.getKey());
			}
			newFunctionMap.clear();
			
			List<Var> oldParameters = parameters;
			for (UserFunc f : newFunctions) {
				UserFuncDef ufd = (UserFuncDef) f.getFuncDef();
				parameters = ufd.getSignature().getParameters();
				offsets.add(nbytes);
				
				ufd.getExpression().accept(this);
				write(ByteCodes.RET);
				write(parameters.size());
				pop();
			}
			parameters = oldParameters;
			
			clear(newFunctions, newFunctionMap.size());
		}
		
		int nconstants = constantMap.size();
		ArrayList<Double> constants = new ArrayList<Double>(nconstants);
		constants.addAll(Collections.nCopies(nconstants, 0.0)); 
		for (Map.Entry<Double, Integer> e : constantMap.entrySet()) {
			constants.set(e.getValue(), e.getKey());
		}
		
		program = new Program(sig.getName(), code.toByteArray(), constants, offsets, parameters.size(), maxStackSize);
		
		return null;
	}
	
	@Override
	public Void visit(BinOp node) throws Exception {
		node.getLHS().accept(this);
		node.getRHS().accept(this);
		return null;
	}

	@Override
	public Void visit(Add node) throws Exception {
		visit((BinOp)node);
		write(ByteCodes.ADD);
		pop();
		return null;
	}

	@Override
	public Void visit(Sub node) throws Exception {
		visit((BinOp)node);
		write(ByteCodes.SUB);
		pop();
		return null;
	}

	@Override
	public Void visit(Mul node) throws Exception {
		visit((BinOp)node);
		write(ByteCodes.MUL);
		pop();
		return null;
	}

	@Override
	public Void visit(Div node) throws Exception {
		visit((BinOp)node);
		write(ByteCodes.DIV);
		pop();
		return null;
	}
	
	@Override
	public Void visit(Mod node) throws Exception {
		visit((BinOp)node);
		write(ByteCodes.MOD);
		pop();
		return null;
	}

	@Override
	public Void visit(Pow node) throws Exception {
		visit((BinOp)node);
		write(ByteCodes.POW);
		pop();
		return null;
	}
	
	@Override
	public Void visit(MonOp node) throws Exception {
		node.getOp().accept(this);
		return null;
	}

	@Override
	public Void visit(Minus node) throws Exception {
		visit((MonOp)node);
		write(ByteCodes.MINUS);
		return null;
	}
	
	@Override
	public Void visit(Factorial node) throws Exception {
		visit((MonOp)node);
		write(ByteCodes.INC);
		write(ByteCodes.GAMMA);
		return null;
	}
	
	@Override
	public Void visit(DegToRad node) throws Exception {
		visit((MonOp)node);
		write(ByteCodes.D2R);
		return null;
	}

	@Override
	public Void visit(Num node) throws Exception {
		Double val = node.getDouble();
		
		write(ByteCodes.LOADC);
		push();
		
		Integer index = constantMap.get(val);
		if (index == null) {
			index = (Integer) constantMap.size();
			constantMap.put(val, index);
		}
		write(index);
		
		return null;
	}
	
	@Override
	public Void visit(Complex node) throws Exception {
		if (node.im() != 0.0) {
			throw new Exception("Program can't do complex calculations");
		}
		Double val = node.re();
		
		write(ByteCodes.LOADC);
		push();
		
		Integer index = constantMap.get(val);
		if (index == null) {
			index = (Integer) constantMap.size();
			constantMap.put(val, index);
		}
		write(index);
		
		return null;
	}

	@Override
	public Void visit(ConstDef node) throws Exception {
		double val = node.getDouble();
		
		write(ByteCodes.LOADC);
		push();
		
		Integer index = constantMap.get(val);
		if (index == null) {
			index = (Integer) constantMap.size();
			constantMap.put(val, index);
		}
		write(index);
		
		return null;
	}

	@Override
	public Void visit(Var node) throws Exception {
		String name = node.getName();
		
		write(ByteCodes.LOADA);
		push();
		
		int n = parameters.size();
		for (int i = 0; i < n; ++i) {
			Var param = parameters.get(i);
			if (name.equals(param.getName())) {
				write(n - i);
				return null;
			}
		}
		
		throw new Exception("Unresolved variable: " + name);
	}
	
	@Override
	public Void visit(MVFunc node) throws Exception {
		List<Expr> args = node.getArguments();
		for (Expr arg : args) {
			arg.accept(this);
		}
		return null;
	}
	
	@Override
	public Void visit(SVFunc node) throws Exception {
		node.getArgument().accept(this);
		return null;
	}
	
	@Override
	public Void visit(UserFunc node) throws Exception {
		visit((MVFunc)node);
		
		UserFuncDef definition = node.getUserFuncDef();
		
		write(ByteCodes.CALL);
		int pStackSize = definition.getProgram().getStackSize();
		int nargs = definition.getSignature().getParameters().size();
		push(pStackSize);
		pop(pStackSize + nargs - 1);
		
		Integer offset = functionMap.get(this);
		if (offset == null) {
			offset = newFunctionMap.get(this);
			if (offset == null) {
				offset = functionMap.size() + newFunctionMap.size();
				newFunctionMap.put(node, offset);
			}
		}
		write(offset);

		return null;
	}
	
	@Override
	public Void visit(Min node) throws Exception {
		visit((MVFunc)node);
		write(ByteCodes.MIN);
		pop();
		return null;
	}
	
	@Override
	public Void visit(Max node) throws Exception {
		visit((MVFunc)node);
		write(ByteCodes.MAX);
		pop();
		return null;
	}
	
	@Override
	public Void visit(Floor node) throws Exception {
		visit((SVFunc)node);
		write(ByteCodes.FLOOR);
		return null;
	}
	
	@Override
	public Void visit(Ceil node) throws Exception {
		visit((SVFunc)node);
		write(ByteCodes.CEIL);
		return null;
	}
	
	@Override
	public Void visit(Sqrt node) throws Exception {
		visit((SVFunc)node);
		write(ByteCodes.SQRT);
		return null;
	}
	
	@Override
	public Void visit(Abs node) throws Exception {
		visit((SVFunc)node);
		write(ByteCodes.ABS);
		return null;
	}
	
	@Override
	public Void visit(Log node) throws Exception {
		visit((SVFunc)node);
		write(ByteCodes.LOG);
		return null;
	}
	
	@Override
	public Void visit(Exp node) throws Exception {
		visit((SVFunc)node);
		write(ByteCodes.EXP);
		return null;
	}
	
	@Override
	public Void visit(Sinh node) throws Exception {
		visit((SVFunc)node);
		write(ByteCodes.SINH);
		return null;
	}
	
	@Override
	public Void visit(Cosh node) throws Exception {
		visit((SVFunc)node);
		write(ByteCodes.COSH);
		return null;
	}
	
	@Override
	public Void visit(Tanh node) throws Exception {
		visit((SVFunc)node);
		write(ByteCodes.TANH);
		return null;
	}
	
	@Override
	public Void visit(Sin node) throws Exception {
		visit((SVFunc)node);
		write(ByteCodes.SIN);
		return null;
	}
	
	@Override
	public Void visit(Cos node) throws Exception {
		visit((SVFunc)node);
		write(ByteCodes.COS);
		return null;
	}
	
	@Override
	public Void visit(Tan node) throws Exception {
		visit((SVFunc)node);
		write(ByteCodes.TAN);
		return null;
	}
	
	@Override
	public Void visit(Asin node) throws Exception {
		visit((SVFunc)node);
		write(ByteCodes.ASIN);
		return null;
	}
	
	@Override
	public Void visit(Acos node) throws Exception {
		visit((SVFunc)node);
		write(ByteCodes.ACOS);
		return null;
	}
	
	@Override
	public Void visit(Atan node) throws Exception {
		visit((SVFunc)node);
		write(ByteCodes.ATAN);
		return null;
	}
	
	@Override
	public Void visit(Erf node) throws Exception {
		visit((SVFunc)node);
		write(ByteCodes.ERF);
		return null;
	}
	
	@Override
	public Void visit(Gamma node) throws Exception {
		visit((SVFunc)node);
		write(ByteCodes.GAMMA);
		return null;
	}
	
	@Override
	public Void visit(LogGamma node) throws Exception {
		visit((SVFunc)node);
		write(ByteCodes.LOGGAMMA);
		return null;
	}
	
	@Override
	public Void visit(LogBeta node) throws Exception {
		visit((MVFunc)node);
		write(ByteCodes.LOGBETA);
		pop();
		return null;
	}
}
