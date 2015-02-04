package com.knpl.simplecalculator.visitors;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.knpl.simplecalculator.ByteCodes;
import com.knpl.simplecalculator.Program;
import com.knpl.simplecalculator.UserFuncDef;
import com.knpl.simplecalculator.nodes.Builtins.*;
import com.knpl.simplecalculator.nodes.*;

public class Compile extends Visitor {
	
	private ByteArrayOutputStream code;
	private int nbytes;
	
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
	}
	
	public Compile() {
		code = new ByteArrayOutputStream();
		nbytes = 0;
		parameters = new ArrayList<Var>();
		constantMap = new HashMap<Double, Integer>();
		functionMap = new HashMap<UserFunc, Integer>();
		newFunctionMap = new HashMap<UserFunc, Integer>();
		program = null;
	}
	
	public Program getProgram() {
		return program;
	}
	
	private void write(int b) {
		code.write(b);
		nbytes+=1;
	}
	
	private void clear(ArrayList<UserFunc> list, int size) {
		list.clear();
		list.addAll(Collections.nCopies(size,(UserFunc) null));
	}
	
	@Override
	public Node visit(FuncDefNode node) throws Exception {
		if (nbytes != 0) {
			throw new Exception("Recursive function definition encountered");
		}
		
		Signature sig = node.getSignature();
		parameters = sig.getParameters();
		
		node.getExpression().accept(this);
		write(ByteCodes.RET);
		write(parameters.size());
		
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
				ufd.getExpression().accept(this);
				write(ByteCodes.RET);
				write(parameters.size());
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
		
		program = new Program(sig.getName(), code.toByteArray(), constants, offsets, parameters.size());
		
		return node;
	}
	
	@Override
	public Node visit(BinOp node) throws Exception {
		node.getLHS().accept(this);
		node.getRHS().accept(this);
		return node;
	}

	@Override
	public Node visit(Add node) throws Exception {
		visit((BinOp)node);
		write(ByteCodes.ADD);
		return node;
	}

	@Override
	public Node visit(Sub node) throws Exception {
		visit((BinOp)node);
		write(ByteCodes.SUB);
		return node;
	}

	@Override
	public Node visit(Mul node) throws Exception {
		visit((BinOp)node);
		write(ByteCodes.MUL);
		return node;
	}

	@Override
	public Node visit(Div node) throws Exception {
		visit((BinOp)node);
		write(ByteCodes.DIV);
		return node;
	}

	@Override
	public Node visit(Pow node) throws Exception {
		visit((BinOp)node);
		write(ByteCodes.POW);
		return node;
	}
	
	@Override
	public Node visit(MonOp node) throws Exception {
		node.getOp().accept(this);
		return node;
	}

	@Override
	public Node visit(Minus node) throws Exception {
		visit((MonOp)node);
		write(ByteCodes.MINUS);
		return node;
	}

	@Override
	public Node visit(Num node) throws Exception {
		Double val = node.getValue();
		
		write(ByteCodes.LOADC);
		Integer index = constantMap.get(val);
		if (index == null) {
			index = (Integer) constantMap.size();
			constantMap.put(val, index);
		}
		write(index);
		
		return node;
	}

	@Override
	public Node visit(Var node) throws Exception {
		String name = node.getName();
		write(ByteCodes.LOADA);
		
		for (int i = 0; i < parameters.size(); ++i) {
			Var param = parameters.get(i);
			if (name.equals(param.getName())) {
				write(i);
				return node;
			}
		}
		
		throw new Exception("Unresolved variable: " + name);
	}
	
	@Override
	public Node visit(Func node) throws Exception {
		List<Expr> args = node.getArguments();
		for (Expr arg : args) {
			arg.accept(this);
		}
		return node;
	}
	
	@Override
	public Node visit(UserFunc node) throws Exception {
		visit((Func)node);
		
		write(ByteCodes.CALL);
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
	public Node visit(Min node) throws Exception {
		visit((Func)node);
		write(ByteCodes.MIN);
		return node;
	}
	
	@Override
	public Node visit(Max node) throws Exception {
		visit((Func)node);
		write(ByteCodes.MAX);
		return node;
	}
	
	@Override
	public Node visit(Sqrt node) throws Exception {
		visit((Func)node);
		write(ByteCodes.SQRT);
		return node;
	}
	
	@Override
	public Node visit(Abs node) throws Exception {
		visit((Func)node);
		write(ByteCodes.ABS);
		return node;	}
	
	@Override
	public Node visit(Log node) throws Exception {
		visit((Func)node);
		write(ByteCodes.LOG);
		return node;
	}
	
	@Override
	public Node visit(Exp node) throws Exception {
		visit((Func)node);
		write(ByteCodes.EXP);
		return node;
	}
	
	@Override
	public Node visit(Sinh node) throws Exception {
		visit((Func)node);
		write(ByteCodes.SINH);
		return node;
	}
	
	@Override
	public Node visit(Cosh node) throws Exception {
		visit((Func)node);
		write(ByteCodes.COSH);
		return node;
	}
	
	@Override
	public Node visit(Sin node) throws Exception {
		visit((Func)node);
		write(ByteCodes.SIN);
		return node;
	}
	
	@Override
	public Node visit(Cos node) throws Exception {
		visit((Func)node);
		write(ByteCodes.COS);
		return node;
	}
	
	@Override
	public Node visit(Tan node) throws Exception {
		visit((Func)node);
		write(ByteCodes.TAN);
		return node;
	}
	
	@Override
	public Node visit(Asin node) throws Exception {
		visit((Func)node);
		write(ByteCodes.ASIN);
		return node;
	}
	
	@Override
	public Node visit(Acos node) throws Exception {
		visit((Func)node);
		write(ByteCodes.ACOS);
		return node;
	}
	
	@Override
	public Node visit(Atan node) throws Exception {
		visit((Func)node);
		write(ByteCodes.ATAN);
		return node;
	}
}
