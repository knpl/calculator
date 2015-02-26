package com.knpl.simplecalculator.visitors;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import com.knpl.simplecalculator.nodes.*;

public class PrettyPrint extends Visitor {
	
	private ByteArrayOutputStream baos;
	private PrintStream out;
	
	public PrettyPrint() {
		baos = new ByteArrayOutputStream();
		out = new PrintStream(baos);
	}
	
	private static class Info {
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
	
	@Override
	public Node visit(Add node, Object info) throws Exception {
		boolean parens = parens((Info)info, 0, true);
		
		if (parens) out.print("(");
		node.getLHS().accept(this, new Info(0, false));
		out.print('+');
		node.getRHS().accept(this, new Info(0, true));
		if (parens) out.print(")");
		return node;
	}
	
	@Override
	public Node visit(Sub node, Object info) throws  Exception {
		boolean parens = parens((Info)info, 0, true);
		
		if (parens) out.print("(");
		node.getLHS().accept(this, new Info(0, false));
		out.print('-');
		node.getRHS().accept(this, new Info(0, true));
		if (parens) out.print(")");
		return node;
	}
	
	@Override
	public Node visit(Mul node, Object info) throws  Exception {
		boolean parens = parens((Info)info, 1, true);
		
		if (parens) out.print("(");
		node.getLHS().accept(this, new Info(1, false));
		out.print('*');
		node.getRHS().accept(this, new Info(1, true));
		if (parens) out.print(")");
		return node;
	}
	
	@Override
	public Node visit(Div node, Object info) throws  Exception {
		boolean parens = parens((Info)info, 1, true);
		
		if (parens) out.print("(");
		node.getLHS().accept(this, new Info(1, false));
		out.print('/');
		node.getRHS().accept(this, new Info(1, true));
		if (parens) out.print(")");
		return node;
	}
	
	@Override
	public Node visit(Pow node, Object info) throws  Exception {
		boolean parens = parens((Info)info, 3, false);
		
		if (parens) out.print("(");
		node.getLHS().accept(this, new Info(3, false));
		out.print('^');
		node.getRHS().accept(this, new Info(3, true));
		if (parens) out.print(")");
		return node;
	}
	
	public Node visit(Minus node, Object info) throws Exception {
		boolean parens = parens((Info) info, 2, false);
		if (parens) out.print("(");
		out.print('-');
		node.getOp().accept(this, new Info(2, true));
		if (parens) out.print(")");
		return node;
	}
	
	@Override
	public Node visit(Func node, Object info) throws Exception {
		out.print(node.getName()+"(");
		List<Expr> args = node.getArguments();
		for (int i = 0; i < args.size()-1; ++i) {
			args.get(i).accept(this, info);
			out.print(",");
		}
		args.get(args.size()-1).accept(this, info);
		out.print(")");
		return node;
	}

	@Override
	public Node visit(Call node, Object info) throws Exception {
		out.print(node.getName()+"(");
		List<Expr> args = node.getArguments();
		for (int i = 0; i < args.size()-1; ++i) {
			args.get(i).accept(this, info);
			out.print(",");
		}
		args.get(args.size()-1).accept(this, info);
		out.print(")");
		return node;
	}

	@Override
	public Node visit(Num node, Object info) throws Exception {
		double val = node.getValue();
		if (val == (long)val) {
			out.print((long) val);
		}
		else {
			out.print(val);
		}
		
		return node;
	}

	@Override
	public Node visit(Var node, Object info) throws Exception {
		out.print(node.getName());
		return node;
	}

	@Override
	public Node visit(FuncDefNode node, Object info) throws Exception {
		node.getSignature().accept(this, info);
		out.print(" = ");
		node.getExpression().accept(this, info);
		return node;
	}
	
	@Override
	public Node visit(Signature node, Object info) throws Exception {
		out.print("def "+node.getName()+"(");
		List<Var> params = node.getParameters();
		for (int i = 0; i < params.size()-1; ++i) {
			params.get(i).accept(this, info);
			out.print(",");
		}
		params.get(params.size()-1).accept(this, info);
		out.print(")");
		
		return node;
	}
	
	@Override
	public String toString() {
		return baos.toString();
	}

}
