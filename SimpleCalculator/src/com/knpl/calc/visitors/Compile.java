package com.knpl.calc.visitors;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.knpl.calc.nodes.*;
import com.knpl.calc.nodes.defs.*;
import com.knpl.calc.nodes.defs.BuiltinFuncDefs.*;
import com.knpl.calc.nodes.numbers.*;
import com.knpl.calc.nodes.operators.*;
import com.knpl.calc.util.ByteCodes;
import com.knpl.calc.util.Program;

public class Compile extends Visitor {
	
	private ByteArrayOutputStream code;
	private int nbytes;
	
	private int maxStackSize;
	private int curStackSize;
	
	private List<Var> parameters;
	
	private Map<Num, Integer> constantMap;
	private Map<UserFuncDef, Integer> functionMap;
	private Map<UserFuncDef, Integer> newFunctionMap;
	
	public Compile() {
		code = new ByteArrayOutputStream();
		nbytes = 0;
		parameters = new ArrayList<Var>();
		constantMap = new HashMap<Num, Integer>();
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
		
		Num[] constants = new Num[constantMap.size()];
		for (Map.Entry<Num, Integer> e : constantMap.entrySet()) {
			constants[e.getValue()] = e.getKey();
		}
		
		short[] offs = new short[offsets.size()];
		for (int i = 0; i < offs.length; ++i) {
			offs[i] = (short) (int) offsets.get(i);
		}
		return new Program(sig.getName(), code.toByteArray(),
						   constants, offs, parameters.size(), maxStackSize);
	}
	
	private void clear(ArrayList<UserFuncDef> list, int size) {
		list.clear();
		list.addAll(Collections.nCopies(size,(UserFuncDef) null));
	}
	
	@Override
	public Node visitBinOp(BinOp node) throws Exception {
		node.getLHS().accept(this);
		node.getRHS().accept(this);
		return node;
	}

	@Override
	public Node visitAdd(Add node) throws Exception {
		visitBinOp((BinOp)node);
		write(ByteCodes.ADD);
		pop();
		return node;
	}

	@Override
	public Node visitSub(Sub node) throws Exception {
		visitBinOp((BinOp)node);
		write(ByteCodes.SUB);
		pop();
		return node;
	}

	@Override
	public Node visitMul(Mul node) throws Exception {
		visitBinOp((BinOp)node);
		write(ByteCodes.MUL);
		pop();
		return node;
	}

	@Override
	public Node visitDiv(Div node) throws Exception {
		visitBinOp((BinOp)node);
		write(ByteCodes.DIV);
		pop();
		return node;
	}
	
	@Override
	public Node visitMod(Mod node) throws Exception {
		visitBinOp((BinOp)node);
		write(ByteCodes.MOD);
		pop();
		return node;
	}

	@Override
	public Node visitPow(Pow node) throws Exception {
		visitBinOp((BinOp)node);
		write(ByteCodes.POW);
		pop();
		return node;
	}
	
	@Override
	public Node visitMonOp(MonOp node) throws Exception {
		node.getOp().accept(this);
		return node;
	}

	@Override
	public Node visitMinus(Minus node) throws Exception {
		visitMonOp((MonOp)node);
		write(ByteCodes.MINUS);
		return node;
	}
	
	@Override
	public Node visitFactorial(Factorial node) throws Exception {
		visitMonOp((MonOp)node);
		write(ByteCodes.INC);
		write(ByteCodes.GAMMA);
		return node;
	}
	
	@Override
	public Node visitDegToRad(DegToRad node) throws Exception {
		visitMonOp((MonOp)node);
		write(ByteCodes.D2R);
		return node;
	}
	
	@Override
	public Node visitNum(Num node) throws Exception {
		write(ByteCodes.LOADC);
		push();
		
		Integer index = constantMap.get(node);
		if (index == null) {
			index = (Integer) constantMap.size();
			constantMap.put(node, index);
		}
		write(index);
		
		return node;
	}

	@Override
	public Node visitVar(Var node) throws Exception {
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
	public Node visitFunc(Func node) throws Exception {
		List<Expr> args = node.getArguments();
		for (Expr arg : args) {
			arg.accept(this);
		}
		node.getFuncDef().accept(this);
		pop(args.size() - 1);
		return node;
	}
	
	@Override
	public Node visitUserFuncDef(UserFuncDef node) throws Exception {
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
	public Node visitMinDef(MinDef node) throws Exception {
		write(ByteCodes.MIN);
		return node;
	}
	
	@Override
	public Node visitMaxDef(MaxDef node) throws Exception {
		write(ByteCodes.MAX);
		return node;
	}
	
	@Override
	public Node visitFloorDef(FloorDef node) throws Exception {
		write(ByteCodes.FLOOR);
		return node;
	}
	
	@Override
	public Node visitCeilDef(CeilDef node) throws Exception {
		write(ByteCodes.CEIL);
		return node;
	}
	
	@Override
	public Node visitSqrtDef(SqrtDef node) throws Exception {
		write(ByteCodes.SQRT);
		return node;
	}
	
	@Override
	public Node visitAbsDef(AbsDef node) throws Exception {
		write(ByteCodes.ABS);
		return node;
	}
	
	@Override
	public Node visitLogDef(LogDef node) throws Exception {
		write(ByteCodes.LOG);
		return node;
	}
	
	@Override
	public Node visitExpDef(ExpDef node) throws Exception {
		write(ByteCodes.EXP);
		return node;
	}
	
	@Override
	public Node visitSinhDef(SinhDef node) throws Exception {
		write(ByteCodes.SINH);
		return node;
	}
	
	@Override
	public Node visitCoshDef(CoshDef node) throws Exception {
		write(ByteCodes.COSH);
		return node;
	}
	
	@Override
	public Node visitTanhDef(TanhDef node) throws Exception {
		write(ByteCodes.TANH);
		return node;
	}
	
	@Override
	public Node visitSinDef(SinDef node) throws Exception {
		write(ByteCodes.SIN);
		return node;
	}
	
	@Override
	public Node visitCosDef(CosDef node) throws Exception {
		write(ByteCodes.COS);
		return node;
	}
	
	@Override
	public Node visitTanDef(TanDef node) throws Exception {
		write(ByteCodes.TAN);
		return node;
	}
	
	@Override
	public Node visitAsinDef(AsinDef node) throws Exception {
		write(ByteCodes.ASIN);
		return node;
	}
	
	@Override
	public Node visitAcosDef(AcosDef node) throws Exception {
		write(ByteCodes.ACOS);
		return node;
	}
	
	@Override
	public Node visitAtanDef(AtanDef node) throws Exception {
		write(ByteCodes.ATAN);
		return node;
	}
	
	@Override
	public Node visitErfDef(ErfDef node) throws Exception {
		write(ByteCodes.ERF);
		return node;
	}
	
	@Override
	public Node visitGammaDef(GammaDef node) throws Exception {
		write(ByteCodes.GAMMA);
		return node;
	}
	
	@Override
	public Node visitLogGammaDef(LogGammaDef node) throws Exception {
		write(ByteCodes.LOGGAMMA);
		return node;
	}
}
