package com.knpl.calc.visitors;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.knpl.calc.nodes.*;
import com.knpl.calc.nodes.BuiltinFuncDefs.*;
import com.knpl.calc.util.ByteCodes;
import com.knpl.calc.util.Program;

public class Compile extends Visitor {
	
	private ByteArrayOutputStream code;
	private int nbytes;
	
	private int maxStackSize;
	private int curStackSize;
	
	private List<Var> parameters;
	
	private Map<Double,Integer> constantMap;
	private Map<UserFuncDef, Integer> functionMap;
	private Map<UserFuncDef, Integer> newFunctionMap;
	
	public Compile() {
		code = new ByteArrayOutputStream();
		nbytes = 0;
		parameters = new ArrayList<Var>();
		constantMap = new HashMap<Double, Integer>();
		functionMap = new HashMap<UserFuncDef, Integer>();
		newFunctionMap = new HashMap<UserFuncDef, Integer>();
		maxStackSize = curStackSize = 0;
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
	
	public static Program compileUserFuncDef(UserFuncDef userFuncDef) throws Exception {
		Compile compile = new Compile();
		return compile.compile(userFuncDef);
	}
	
	private Program compile(UserFuncDef userFuncDef) throws Exception {
		Signature sig = userFuncDef.getSignature();
		parameters = sig.getParameters();
		
		userFuncDef.getExpression().accept(this);
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
		constants.addAll(Collections.nCopies(nconstants, 0.)); 
		for (Map.Entry<Double, Integer> e : constantMap.entrySet()) {
			constants.set(e.getValue(), e.getKey());
		}
		
		return new Program(sig.getName(), code.toByteArray(),
						   constants, offsets,
						   parameters.size(), maxStackSize);
	}
	
	private void clear(ArrayList<UserFuncDef> list, int size) {
		list.clear();
		list.addAll(Collections.nCopies(size,(UserFuncDef) null));
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
		pop();
		return node;
	}

	@Override
	public Node visit(Sub node) throws Exception {
		visit((BinOp)node);
		write(ByteCodes.SUB);
		pop();
		return node;
	}

	@Override
	public Node visit(Mul node) throws Exception {
		visit((BinOp)node);
		write(ByteCodes.MUL);
		pop();
		return node;
	}

	@Override
	public Node visit(Div node) throws Exception {
		visit((BinOp)node);
		write(ByteCodes.DIV);
		pop();
		return node;
	}
	
	@Override
	public Node visit(Mod node) throws Exception {
		visit((BinOp)node);
		write(ByteCodes.MOD);
		pop();
		return node;
	}

	@Override
	public Node visit(Pow node) throws Exception {
		visit((BinOp)node);
		write(ByteCodes.POW);
		pop();
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
	public Node visit(Factorial node) throws Exception {
		visit((MonOp)node);
		write(ByteCodes.INC);
		write(ByteCodes.GAMMA);
		return node;
	}
	
	@Override
	public Node visit(DegToRad node) throws Exception {
		visit((MonOp)node);
		write(ByteCodes.D2R);
		return node;
	}

	@Override
	public Node visit(NumTok node) throws Exception {
		Double val = node.getRealDouble().getValue();
		
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
	public Node visit(RealDouble node) throws Exception {
		write(ByteCodes.LOADC);
		push();
		
		double val = node.getValue();
		
		Integer index = constantMap.get(val);
		if (index == null) {
			index = (Integer) constantMap.size();
			constantMap.put(val, index);
		}
		write(index);
		
		return node;
	}

	@Override
	public Node visit(Const node) throws Exception {
		Num num = node.getConstDef().getNum();
		if (num instanceof Complex) {
			throw new Exception("Program can't do complex calculations");
		}
		double val = ((RealDouble) num).getValue();
		
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
	public Node visit(Var node) throws Exception {
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
	public Node visit(Func node) throws Exception {
		List<Expr> args = node.getArguments();
		for (Expr arg : args) {
			arg.accept(this);
		}
		node.getFuncDef().accept(this);
		pop(args.size() - 1);
		return node;
	}
	
	@Override
	public Node visit(UserFuncDef node) throws Exception {
		write(ByteCodes.CALL);
		
		Integer offset = functionMap.get(this);
		if (offset == null) {
			offset = newFunctionMap.get(this);
			if (offset == null) {
				offset = functionMap.size() + newFunctionMap.size();
				newFunctionMap.put(node, offset);
			}
		}
		
		int pStackSize = node.getProgram().getStackSize();
		write(offset);
		push(pStackSize);
		pop(pStackSize);
		
		return node;
	}
	
	@Override
	public Node visit(MinDef node) throws Exception {
		write(ByteCodes.MIN);
		return node;
	}
	
	@Override
	public Node visit(MaxDef node) throws Exception {
		write(ByteCodes.MAX);
		return node;
	}
	
	@Override
	public Node visit(FloorDef node) throws Exception {
		write(ByteCodes.FLOOR);
		return node;
	}
	
	@Override
	public Node visit(CeilDef node) throws Exception {
		write(ByteCodes.CEIL);
		return node;
	}
	
	@Override
	public Node visit(SqrtDef node) throws Exception {
		write(ByteCodes.SQRT);
		return node;
	}
	
	@Override
	public Node visit(AbsDef node) throws Exception {
		write(ByteCodes.ABS);
		return node;
	}
	
	@Override
	public Node visit(LogDef node) throws Exception {
		write(ByteCodes.LOG);
		return node;
	}
	
	@Override
	public Node visit(ExpDef node) throws Exception {
		write(ByteCodes.EXP);
		return node;
	}
	
	@Override
	public Node visit(SinhDef node) throws Exception {
		write(ByteCodes.SINH);
		return node;
	}
	
	@Override
	public Node visit(CoshDef node) throws Exception {
		write(ByteCodes.COSH);
		return node;
	}
	
	@Override
	public Node visit(TanhDef node) throws Exception {
		write(ByteCodes.TANH);
		return node;
	}
	
	@Override
	public Node visit(SinDef node) throws Exception {
		write(ByteCodes.SIN);
		return node;
	}
	
	@Override
	public Node visit(CosDef node) throws Exception {
		write(ByteCodes.COS);
		return node;
	}
	
	@Override
	public Node visit(TanDef node) throws Exception {
		write(ByteCodes.TAN);
		return node;
	}
	
	@Override
	public Node visit(AsinDef node) throws Exception {
		write(ByteCodes.ASIN);
		return node;
	}
	
	@Override
	public Node visit(AcosDef node) throws Exception {
		write(ByteCodes.ACOS);
		return node;
	}
	
	@Override
	public Node visit(AtanDef node) throws Exception {
		write(ByteCodes.ATAN);
		return node;
	}
	
	@Override
	public Node visit(ErfDef node) throws Exception {
		write(ByteCodes.ERF);
		return node;
	}
	
	@Override
	public Node visit(GammaDef node) throws Exception {
		write(ByteCodes.GAMMA);
		return node;
	}
	
	@Override
	public Node visit(LogGammaDef node) throws Exception {
		write(ByteCodes.LOGGAMMA);
		return node;
	}
}
