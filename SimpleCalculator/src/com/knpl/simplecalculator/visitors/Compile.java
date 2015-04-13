package com.knpl.simplecalculator.visitors;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.knpl.simplecalculator.nodes.*;
import com.knpl.simplecalculator.nodes.BuiltinFuncDefs.*;
import com.knpl.simplecalculator.util.ByteCodes;
import com.knpl.simplecalculator.util.Program;

public class Compile extends Visitor {
	
	private ByteArrayOutputStream code;
	private int nbytes;
	
	private int maxStackSize;
	private int curStackSize;
	
	private List<Var> parameters;
	
	private Map<Double,Integer> constantMap;
	private Map<UserFuncDef, Integer> functionMap;
	private Map<UserFuncDef, Integer> newFunctionMap;
	
	private Program program;
	
	public Compile(List<Var> parameters) {
		code = new ByteArrayOutputStream();
		nbytes = 0;
		this.parameters = parameters;
		constantMap = new HashMap<Double, Integer>();
		functionMap = new HashMap<UserFuncDef, Integer>();
		newFunctionMap = new HashMap<UserFuncDef, Integer>();
		program = null;
		
		maxStackSize = curStackSize = 0;
	}
	
	public Compile() {
		code = new ByteArrayOutputStream();
		nbytes = 0;
		parameters = new ArrayList<Var>();
		constantMap = new HashMap<Double, Integer>();
		functionMap = new HashMap<UserFuncDef, Integer>();
		newFunctionMap = new HashMap<UserFuncDef, Integer>();
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
	
	private void clear(ArrayList<UserFuncDef> list, int size) {
		list.clear();
		list.addAll(Collections.nCopies(size,(UserFuncDef) null));
	}
	
	@Override
	public Void visit(FuncDefNode node) throws Exception {
		Signature sig = node.getSignature();
		parameters = sig.getParameters();
		
		node.getExpression().accept(this);
		write(ByteCodes.RET);
		write(parameters.size());
		pop();
		
		ArrayList<UserFuncDef> newFunctions = new ArrayList<UserFuncDef>(newFunctionMap.size());
		clear(newFunctions, newFunctionMap.size());
		
		ArrayList<Integer> offsets = new ArrayList<Integer>();
		
		int nfunctions;
		while (!newFunctions.isEmpty()) {
			nfunctions = functionMap.size();
			functionMap.putAll(newFunctionMap);
			
			for (Map.Entry<UserFuncDef, Integer> e : newFunctionMap.entrySet()) {
				newFunctions.set(e.getValue()-nfunctions, e.getKey());
			}
			newFunctionMap.clear();
			
			List<Var> oldParameters = parameters;
			for (UserFuncDef ufd : newFunctions) {
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
		node.getFuncDef().accept(this);
		pop(args.size() - 1);
		return null;
	}
	
	@Override
	public Void visit(SVFunc node) throws Exception {
		node.getArgument().accept(this);
		node.getFuncDef().accept(this);
		return null;
	}
	
	@Override
	public Void visit(UserFuncDef node) throws Exception {
		write(ByteCodes.CALL);
		int pStackSize = node.getProgram().getStackSize();
		push(pStackSize);
		pop(pStackSize);
		
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
	public Void visit(MinDef node) throws Exception {
		write(ByteCodes.MIN);
		return null;
	}
	
	@Override
	public Void visit(MaxDef node) throws Exception {
		write(ByteCodes.MAX);
		return null;
	}
	
	@Override
	public Void visit(FloorDef node) throws Exception {
		write(ByteCodes.FLOOR);
		return null;
	}
	
	@Override
	public Void visit(CeilDef node) throws Exception {
		write(ByteCodes.CEIL);
		return null;
	}
	
	@Override
	public Void visit(SqrtDef node) throws Exception {
		write(ByteCodes.SQRT);
		return null;
	}
	
	@Override
	public Void visit(AbsDef node) throws Exception {
		write(ByteCodes.ABS);
		return null;
	}
	
	@Override
	public Void visit(LogDef node) throws Exception {
		write(ByteCodes.LOG);
		return null;
	}
	
	@Override
	public Void visit(ExpDef node) throws Exception {
		write(ByteCodes.EXP);
		return null;
	}
	
	@Override
	public Void visit(SinhDef node) throws Exception {
		write(ByteCodes.SINH);
		return null;
	}
	
	@Override
	public Void visit(CoshDef node) throws Exception {
		write(ByteCodes.COSH);
		return null;
	}
	
	@Override
	public Void visit(TanhDef node) throws Exception {
		write(ByteCodes.TANH);
		return null;
	}
	
	@Override
	public Void visit(SinDef node) throws Exception {
		write(ByteCodes.SIN);
		return null;
	}
	
	@Override
	public Void visit(CosDef node) throws Exception {
		write(ByteCodes.COS);
		return null;
	}
	
	@Override
	public Void visit(TanDef node) throws Exception {
		write(ByteCodes.TAN);
		return null;
	}
	
	@Override
	public Void visit(AsinDef node) throws Exception {
		write(ByteCodes.ASIN);
		return null;
	}
	
	@Override
	public Void visit(AcosDef node) throws Exception {
		write(ByteCodes.ACOS);
		return null;
	}
	
	@Override
	public Void visit(AtanDef node) throws Exception {
		write(ByteCodes.ATAN);
		return null;
	}
	
	@Override
	public Void visit(ErfDef node) throws Exception {
		write(ByteCodes.ERF);
		return null;
	}
	
	@Override
	public Void visit(GammaDef node) throws Exception {
		write(ByteCodes.GAMMA);
		return null;
	}
	
	@Override
	public Void visit(LogGammaDef node) throws Exception {
		write(ByteCodes.LOGGAMMA);
		return null;
	}
}
