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
		if (curStackSize > maxStackSize) {
			maxStackSize = curStackSize;
		}
	}
	
	private void push(int s) {
		curStackSize += s;
		if (curStackSize > maxStackSize) {
			maxStackSize = curStackSize;
		}
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
	public Node visit(FuncDefNode node, Object info) throws Exception {
		if (nbytes != 0) {
			throw new Exception("Recursive function definition encountered");
		}
		
		Signature sig = node.getSignature();
		parameters = sig.getParameters();
		
		node.getExpression().accept(this, info);
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
				UserFuncDef ufd = (UserFuncDef) f.getDefinition();
				parameters = ufd.getSignature().getParameters();
				offsets.add(nbytes);
				
				ufd.getExpression().accept(this, info);
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
		
		return node;
	}
	
	@Override
	public Node visit(BinOp node, Object info) throws Exception {
		node.getLHS().accept(this, info);
		node.getRHS().accept(this, info);
		return node;
	}

	@Override
	public Node visit(Add node, Object info) throws Exception {
		visit((BinOp)node, info);
		write(ByteCodes.ADD);
		pop();
		return node;
	}

	@Override
	public Node visit(Sub node, Object info) throws Exception {
		visit((BinOp)node, info);
		write(ByteCodes.SUB);
		pop();
		return node;
	}

	@Override
	public Node visit(Mul node, Object info) throws Exception {
		visit((BinOp)node, info);
		write(ByteCodes.MUL);
		pop();
		return node;
	}

	@Override
	public Node visit(Div node, Object info) throws Exception {
		visit((BinOp)node, info);
		write(ByteCodes.DIV);
		pop();
		return node;
	}

	@Override
	public Node visit(Pow node, Object info) throws Exception {
		visit((BinOp)node, info);
		write(ByteCodes.POW);
		pop();
		return node;
	}
	
	@Override
	public Node visit(MonOp node, Object info) throws Exception {
		node.getOp().accept(this, info);
		return node;
	}

	@Override
	public Node visit(Minus node, Object info) throws Exception {
		visit((MonOp)node, info);
		write(ByteCodes.MINUS);
		return node;
	}

	@Override
	public Node visit(Num node, Object info) throws Exception {
		Double val = node.getValue();
		
		write(ByteCodes.LOADC);
		push();
		
		Integer index = constantMap.get(val);
		if (index == null) {
			index = (Integer) constantMap.size();
			constantMap.put(val, index);
		}
		write(index);
		
		return node;
	}

	@Override
	public Node visit(Var node, Object info) throws Exception {
		String name = node.getName();
		
		write(ByteCodes.LOADA);
		push();
		
		int n = parameters.size();
		for (int i = 0; i < n; ++i) {
			Var param = parameters.get(i);
			if (name.equals(param.getName())) {
				write(n - i);
				return node;
			}
		}
		
		throw new Exception("Unresolved variable: " + name);
	}
	
	@Override
	public Node visit(Func node, Object info) throws Exception {
		List<Expr> args = node.getArguments();
		for (Expr arg : args) {
			arg.accept(this, info);
		}
		
		return node;
	}
	
	@Override
	public Node visit(UserFunc node, Object info) throws Exception {
		visit((Func)node, null);
		
		UserFuncDef definition = node.getDefinition();
		
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

		return node;
	}
	
	@Override
	public Node visit(Min node, Object info) throws Exception {
		visit((Func)node, null);
		write(ByteCodes.MIN);
		pop();
		return node;
	}
	
	@Override
	public Node visit(Max node, Object info) throws Exception {
		visit((Func)node, null);
		write(ByteCodes.MAX);
		pop();
		return node;
	}
	
	@Override
	public Node visit(Sqrt node, Object info) throws Exception {
		visit((Func)node, null);
		write(ByteCodes.SQRT);
		return node;
	}
	
	@Override
	public Node visit(Abs node, Object info) throws Exception {
		visit((Func)node, null);
		write(ByteCodes.ABS);
		return node;	}
	
	@Override
	public Node visit(Log node, Object info) throws Exception {
		visit((Func)node, null);
		write(ByteCodes.LOG);
		return node;
	}
	
	@Override
	public Node visit(Exp node, Object info) throws Exception {
		visit((Func)node, null);
		write(ByteCodes.EXP);
		return node;
	}
	
	@Override
	public Node visit(Sinh node, Object info) throws Exception {
		visit((Func)node, null);
		write(ByteCodes.SINH);
		return node;
	}
	
	@Override
	public Node visit(Cosh node, Object info) throws Exception {
		visit((Func)node, null);
		write(ByteCodes.COSH);
		return node;
	}
	
	@Override
	public Node visit(Sin node, Object info) throws Exception {
		visit((Func)node, null);
		write(ByteCodes.SIN);
		return node;
	}
	
	@Override
	public Node visit(Cos node, Object info) throws Exception {
		visit((Func)node, null);
		write(ByteCodes.COS);
		return node;
	}
	
	@Override
	public Node visit(Tan node, Object info) throws Exception {
		visit((Func)node, null);
		write(ByteCodes.TAN);
		return node;
	}
	
	@Override
	public Node visit(Asin node, Object info) throws Exception {
		visit((Func)node, null);
		write(ByteCodes.ASIN);
		return node;
	}
	
	@Override
	public Node visit(Acos node, Object info) throws Exception {
		visit((Func)node, null);
		write(ByteCodes.ACOS);
		return node;
	}
	
	@Override
	public Node visit(Atan node, Object info) throws Exception {
		visit((Func)node, null);
		write(ByteCodes.ATAN);
		return node;
	}
}
