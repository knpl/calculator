package com.knpl.calc.visitors;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import com.knpl.calc.nodes.*;
import com.knpl.calc.nodes.operators.*;
import com.knpl.calc.nodes.defs.*;
import com.knpl.calc.nodes.numbers.Num;
import com.knpl.calc.nodes.operators.DegToRad;

public class PrettyPrint extends Visitor {
	
	private ByteArrayOutputStream baos;
	private PrintStream out;
	
	public PrettyPrint() {
		baos = new ByteArrayOutputStream();
		out = new PrintStream(baos);
		info = null;
	}
	
	public static class Info {
		final boolean right;
		final int precedence;
		
		public Info(int precedence, boolean right) {
			this.precedence = precedence;
			this.right = right;
		}
	}
	
	private static boolean parens(Info info, int precedence, boolean leftAssoc) {
		if (info == null) {
			return false;
		}
		else if (info.precedence > precedence) {
			return true;
		}
		else if (info.precedence == precedence) {
			return leftAssoc == info.right;
		}
		else {
			return false;
		}
	}
	
	private Info info;
	
	private void setInfo(Info info) {
		this.info = info; 
	}
	
	private Info getInfo() {
		Info buf = info;
		info = null;
		return buf;
	}
	
	public static String printUserFuncDef(UserFuncDef ufd) throws Exception {
		PrettyPrint v = new PrettyPrint();
		ufd.accept(v);
		return v.toString();
	}
	
	public static String printUserConstDef(UserConstDef ucd) throws Exception {
		PrettyPrint v = new PrettyPrint();
		ucd.accept(v);
		return v.toString();
	}
	
	@Override
	public Node visitUserConstDef(UserConstDef node) throws Exception {
		getInfo();
		out.print(node.getName() + " = ");
		node.getExpression().accept(this);
		return node;
	}
	
	@Override
	public Node visitUserFuncDef(UserFuncDef node) throws Exception {
		getInfo();
		node.getSignature().accept(this);
		out.print(" = ");
		node.getExpression().accept(this);
		return node;
	}
	
	@Override
	public Node visitSignature(Signature node) throws Exception {
		getInfo();
		out.print(node.getName()+"(");
		List<Var> params = node.getParameters();
		for (int i = 0; i + 1 < params.size(); ++i) {
			params.get(i).accept(this);
			out.print(",");
		}
		params.get(params.size()-1).accept(this);
		out.print(")");
		
		return node;
	}
	
	@Override
	public Node visitAdd(Add node) throws Exception {
		boolean parens = parens(getInfo(), 0, true);
		
		if (parens) out.print("(");
		
		setInfo(new Info(0, false));
		node.getLHS().accept(this);
		
		out.print('+');
		
		setInfo(new Info(0, true));
		node.getRHS().accept(this);
		
		if (parens) out.print(")");
		return node;
	}
	
	@Override
	public Node visitSub(Sub node) throws  Exception {
		boolean parens = parens(getInfo(), 0, true);
		
		if (parens) out.print("(");
		
		setInfo(new Info(0, false));
		node.getLHS().accept(this);
		
		out.print('-');
		
		setInfo(new Info(0, true));
		node.getRHS().accept(this);
		
		if (parens) out.print(")");
		return node;
	}
	
	@Override
	public Node visitMul(Mul node) throws  Exception {
		boolean parens = parens(getInfo(), 1, true);
		
		if (parens) out.print("(");
		
		setInfo(new Info(1, false));
		node.getLHS().accept(this);
		
		out.print('*');
		
		setInfo(new Info(1, true));
		node.getRHS().accept(this);
		
		if (parens) out.print(")");
		return node;
	}
	
	@Override
	public Node visitDiv(Div node) throws  Exception {
		boolean parens = parens(getInfo(), 1, true);
		
		if (parens) out.print("(");
		
		setInfo(new Info(1, false));
		node.getLHS().accept(this);
		
		out.print('/');
		
		setInfo(new Info(1, true));
		node.getRHS().accept(this);
		
		if (parens) out.print(")");
		return node;
	}
	
	@Override
	public Node visitPow(Pow node) throws  Exception {
		boolean parens = parens(getInfo(), 4, false);
		
		if (parens) out.print("(");
		
		setInfo(new Info(4, false));
		node.getLHS().accept(this);
		
		out.print('^');
		
		setInfo(new Info(4, true));
		node.getRHS().accept(this);
		
		if (parens) out.print(")");
		return node;
	}
	
	public Node visitMinus(Minus node) throws Exception {
		boolean parens = parens(getInfo(), 2, false);
		if (parens) out.print("(");
		out.print('-');
		
		setInfo(new Info(2, true));
		node.getOp().accept(this);
		
		if (parens) out.print(")");
		return node;
	}
	
	public Node visitFactorial(Factorial node) throws Exception {
		boolean parens = parens(getInfo(), 3, false);
		if (parens) out.print("(");
		
		setInfo(new Info(3, true));
		node.getOp().accept(this);
		
		out.print('!');
		
		if (parens) out.print(")");
		return node;
	}
	
	public Node visitDegToRad(DegToRad node) throws Exception {
		boolean parens = parens(getInfo(), 3, false);
		if (parens) out.print("(");
		
		setInfo(new Info(3, true));
		node.getOp().accept(this);
		
		out.print('\u00B0');
		
		if (parens) out.print(")");
		return node;
	}
	
	@Override
	public Node visitFunc(Func node) throws Exception {
		getInfo();
		String name = node.getFuncDef().getSignature().getName();
		out.print(name+"(");
		List<Expr> args = node.getArguments();
		for (int i = 0; i < args.size()-1; ++i) {
			args.get(i).accept(this);
			out.print(",");
		}
		args.get(args.size()-1).accept(this);
		out.print(")");
		return node;
	}

	@Override
	public Node visitCall(Call node) throws Exception {
		getInfo();
		out.print(node.getName()+"(");
		List<Expr> args = node.getArguments();
		for (int i = 0; i < args.size()-1; ++i) {
			args.get(i).accept(this);
			out.print(",");
		}
		args.get(args.size()-1).accept(this);
		out.print(")");
		return node;
	}

	@Override
	public Node visitNumTok(NumTok node) throws Exception {
		getInfo();
		out.print(node);
		return node;
	}
	
	@Override
	public Node visitNum(Num node) throws Exception {
		getInfo();
		out.print(""+node);
		return node;
	}
	
	@Override
	public Node visitConst(Const node) throws Exception {
		getInfo();
		out.print(node.getConstDef().getName());
		return node;
	}

	@Override
	public Node visitVar(Var node) throws Exception {
		getInfo();
		out.print(node.getName());
		return node;
	}
	
	@Override
	public String toString() {
		return baos.toString();
	}
}
