package com.knpl.calc.visitors;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import com.knpl.calc.nodes.*;
import com.knpl.calc.nodes.defs.*;
import com.knpl.calc.nodes.defs.BuiltinFuncDefs.*;
import com.knpl.calc.nodes.numbers.*;
import com.knpl.calc.nodes.operators.*;
import com.knpl.calc.util.ByteCodes;
import com.knpl.calc.util.Program;

public class Compile extends Visitor {
	
	private ByteArrayOutputStream code;
	
	private List<Var> parameters;
	private List<Num> constants;
	private List<UserFuncDef> functions;
	
	private Compile() {
		code = new ByteArrayOutputStream();
		constants = new ArrayList<Num>();
		functions = new ArrayList<UserFuncDef>();
	}
	
	public static Program compileUserFuncDef(UserFuncDef userFuncDef) throws Exception {
		Compile compile = new Compile();
		return compile.compile(userFuncDef);
	}
	
	private Program compile(UserFuncDef ufd) throws Exception {
		Signature sig = ufd.getSignature();
		final String name = sig.getName();
		final int paramcnt  = sig.getParameters().size();
		ArrayList<Short> offsets = new ArrayList<Short>();
		
		parameters = ufd.getSignature().getParameters();
		ufd.getExpression().accept(this);
		code.write(ByteCodes.RET);
		for (int i = 0; i < functions.size(); ++i) {
			offsets.add((short)code.size());
			
			ufd = functions.get(i);
			parameters = ufd.getSignature().getParameters();
			ufd.fold();
			ufd.getExpression().accept(this);
			code.write(ByteCodes.RET);
		}
		
		short[] offs = new short[offsets.size()];
		for (int i = 0; i < offs.length; ++i) {
			offs[i] = offsets.get(i);
		}
		
		Num[] consts = new Num[constants.size()];
		for (int i = 0; i < consts.length; ++i) {
			consts[i] = constants.get(i);
		}
		
		return new Program(name, paramcnt, code.toByteArray(), consts, offs);
	}
	
	@Override
	public Node visitNum(Num node) {
		int index = constants.indexOf(node);
		if (index < 0) {
			index = constants.size();
			constants.add(node);
		}
		code.write(ByteCodes.LOADC);
		code.write(index);
		return node;
	}

	@Override
	public Node visitVar(Var node) throws Exception {
		int index = parameters.indexOf(node);
		if (index < 0) {
			throw new Exception("Unresolved variable: " + node.getName());
		}
		code.write(ByteCodes.LOADA);
		code.write(index);
		return node;
	}

	@Override
	public Node visitAdd(Add node) throws Exception {
		node.getLHS().accept(this);
		node.getRHS().accept(this);
		code.write(ByteCodes.ADD);
		return node;
	}

	@Override
	public Node visitSub(Sub node) throws Exception {
		node.getLHS().accept(this);
		node.getRHS().accept(this);
		code.write(ByteCodes.SUB);
		return node;
	}

	@Override
	public Node visitMul(Mul node) throws Exception {
		node.getLHS().accept(this);
		node.getRHS().accept(this);
		code.write(ByteCodes.MUL);
		return node;
	}

	@Override
	public Node visitDiv(Div node) throws Exception {
		node.getLHS().accept(this);
		node.getRHS().accept(this);
		code.write(ByteCodes.DIV);
		return node;
	}
	
	@Override
	public Node visitMod(Mod node) throws Exception {
		node.getLHS().accept(this);
		node.getRHS().accept(this);
		code.write(ByteCodes.MOD);
		return node;
	}

	@Override
	public Node visitPow(Pow node) throws Exception {
		node.getLHS().accept(this);
		node.getRHS().accept(this);
		code.write(ByteCodes.POW);
		return node;
	}
	
	@Override
	public Node visitMinus(Minus node) throws Exception {
		node.getOp().accept(this);
		code.write(ByteCodes.MINUS);
		return node;
	}
	
	@Override
	public Node visitFactorial(Factorial node) throws Exception {
		node.getOp().accept(this);
		code.write(ByteCodes.INC);
		code.write(ByteCodes.GAMMA);
		return node;
	}
	
	@Override
	public Node visitDegToRad(DegToRad node) throws Exception {
		node.getOp().accept(this);
		code.write(ByteCodes.D2R);
		return node;
	}
	
	@Override
	public Node visitFunc(Func node) throws Exception {
		for (Expr arg : node.getArguments()) {
			arg.accept(this);
		}
		node.getFuncDef().accept(this);
		return node;
	}
	
	@Override
	public Node visitUserFuncDef(UserFuncDef node) {
		int index = functions.indexOf(node);
		if (index < 0) {
			index = functions.size();
			functions.add(node);
		}
		code.write(ByteCodes.CALL);
		code.write(index);
		code.write(node.getSignature().getParameters().size());
		return node;
	}
	
	@Override
	public Node visitMinDef(MinDef node) {
		code.write(ByteCodes.MIN);
		return node;
	}
	
	@Override
	public Node visitMaxDef(MaxDef node) {
		code.write(ByteCodes.MAX);
		return node;
	}
	
	@Override
	public Node visitFloorDef(FloorDef node) {
		code.write(ByteCodes.FLOOR);
		return node;
	}
	
	@Override
	public Node visitCeilDef(CeilDef node) {
		code.write(ByteCodes.CEIL);
		return node;
	}
	
	@Override
	public Node visitSqrtDef(SqrtDef node) {
		code.write(ByteCodes.SQRT);
		return node;
	}
	
	@Override
	public Node visitAbsDef(AbsDef node) {
		code.write(ByteCodes.ABS);
		return node;
	}
	
	@Override
	public Node visitLogDef(LogDef node) {
		code.write(ByteCodes.LOG);
		return node;
	}
	
	@Override
	public Node visitExpDef(ExpDef node) {
		code.write(ByteCodes.EXP);
		return node;
	}
	
	@Override
	public Node visitSinhDef(SinhDef node) {
		code.write(ByteCodes.SINH);
		return node;
	}
	
	@Override
	public Node visitCoshDef(CoshDef node) {
		code.write(ByteCodes.COSH);
		return node;
	}
	
	@Override
	public Node visitTanhDef(TanhDef node) {
		code.write(ByteCodes.TANH);
		return node;
	}
	
	@Override
	public Node visitSinDef(SinDef node) {
		code.write(ByteCodes.SIN);
		return node;
	}
	
	@Override
	public Node visitCosDef(CosDef node) {
		code.write(ByteCodes.COS);
		return node;
	}
	
	@Override
	public Node visitTanDef(TanDef node) {
		code.write(ByteCodes.TAN);
		return node;
	}
	
	@Override
	public Node visitAsinDef(AsinDef node) {
		code.write(ByteCodes.ASIN);
		return node;
	}
	
	@Override
	public Node visitAcosDef(AcosDef node)  {
		code.write(ByteCodes.ACOS);
		return node;
	}
	
	@Override
	public Node visitAtanDef(AtanDef node) {
		code.write(ByteCodes.ATAN);
		return node;
	}
	
	@Override
	public Node visitErfDef(ErfDef node) {
		code.write(ByteCodes.ERF);
		return node;
	}
	
	@Override
	public Node visitGammaDef(GammaDef node) {
		code.write(ByteCodes.GAMMA);
		return node;
	}
	
	@Override
	public Node visitLogGammaDef(LogGammaDef node) {
		code.write(ByteCodes.LOGGAMMA);
		return node;
	}
	
	@Override
	public Node visitReDef(ReDef node) {
		code.write(ByteCodes.RE);
		return node;
	}
	
	@Override
	public Node visitImDef(ImDef node) {
		code.write(ByteCodes.IM);
		return node;
	}
	
	@Override
	public Node visitArgDef(ArgDef node) {
		code.write(ByteCodes.ARG);
		return node;
	}
	
	@Override
	public Node visitModDef(ModDef node) {
		code.write(ByteCodes.MODUL);
		return node;
	}
	
	@Override
	public Node visitConjDef(ConjDef node) {
		code.write(ByteCodes.CONJ);
		return node;
	}
}
