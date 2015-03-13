package com.knpl.simplecalculator.visitors;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import com.knpl.simplecalculator.nodes.*;
import com.knpl.simplecalculator.visitors.PrettyPrint.Info;

public class PrettyPrint extends Visitor<Void, Info> {
	
	private ByteArrayOutputStream baos;
	private PrintStream out;
	
	public PrettyPrint() {
		baos = new ByteArrayOutputStream();
		out = new PrintStream(baos);
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
	
	@Override
	public Void visit(Add node, Info info) throws Exception {
		boolean parens = parens(info, 0, true);
		
		if (parens) out.print("(");
		node.getLHS().accept(this, new Info(0, false));
		out.print('+');
		node.getRHS().accept(this, new Info(0, true));
		if (parens) out.print(")");
		return null;
	}
	
	@Override
	public Void visit(Sub node, Info info) throws  Exception {
		boolean parens = parens(info, 0, true);
		
		if (parens) out.print("(");
		node.getLHS().accept(this, new Info(0, false));
		out.print('-');
		node.getRHS().accept(this, new Info(0, true));
		if (parens) out.print(")");
		return null;
	}
	
	@Override
	public Void visit(Mul node, Info info) throws  Exception {
		boolean parens = parens(info, 1, true);
		
		if (parens) out.print("(");
		node.getLHS().accept(this, new Info(1, false));
		out.print('*');
		node.getRHS().accept(this, new Info(1, true));
		if (parens) out.print(")");
		return null;
	}
	
	@Override
	public Void visit(Div node, Info info) throws  Exception {
		boolean parens = parens(info, 1, true);
		
		if (parens) out.print("(");
		node.getLHS().accept(this, new Info(1, false));
		out.print('/');
		node.getRHS().accept(this, new Info(1, true));
		if (parens) out.print(")");
		return null;
	}
	
	@Override
	public Void visit(Pow node, Info info) throws  Exception {
		boolean parens = parens(info, 3, false);
		
		if (parens) out.print("(");
		node.getLHS().accept(this, new Info(3, false));
		out.print('^');
		node.getRHS().accept(this, new Info(3, true));
		if (parens) out.print(")");
		return null;
	}
	
	public Void visit(Minus node, Info info) throws Exception {
		boolean parens = parens((Info) info, 2, false);
		if (parens) out.print("(");
		out.print('-');
		node.getOp().accept(this, new Info(2, true));
		if (parens) out.print(")");
		return null;
	}
	
	@Override
	public Void visit(Func node, Info info) throws Exception {
		out.print(node.getName()+"(");
		List<Expr> args = node.getArguments();
		for (int i = 0; i < args.size()-1; ++i) {
			args.get(i).accept(this, null);
			out.print(",");
		}
		args.get(args.size()-1).accept(this, null);
		out.print(")");
		return null;
	}

	@Override
	public Void visit(Call node, Info info) throws Exception {
		out.print(node.getName()+"(");
		List<Expr> args = node.getArguments();
		for (int i = 0; i < args.size()-1; ++i) {
			args.get(i).accept(this, null);
			out.print(",");
		}
		args.get(args.size()-1).accept(this, null);
		out.print(")");
		return null;
	}

	@Override
	public Void visit(Num node, Info info) throws Exception {
		double val = node.getDouble();
		if (val == (long)val) {
			out.print((long) val);
		}
		else {
			out.print(val);
		}
		
		return null;
	}
	
	@Override
	public Void visit(Constant node, Info info) throws Exception {
		out.print(node.getName());
		return null;
	}

	@Override
	public Void visit(Var node, Info info) throws Exception {
		out.print(node.getName());
		return null;
	}

	@Override
	public Void visit(FuncDefNode node, Info info) throws Exception {
		node.getSignature().accept(this, null);
		out.print(" = ");
		node.getExpression().accept(this, null);
		return null;
	}
	
	@Override
	public Void visit(ConstDefNode node, Info info) throws Exception {
		out.print(node.getName() + " = ");
		node.getExpression().accept(this, null);
		return null;
	}
	
	@Override
	public Void visit(Signature node, Info info) throws Exception {
		out.print(node.getName()+"(");
		List<Var> params = node.getParameters();
		for (int i = 0; i < params.size()-1; ++i) {
			params.get(i).accept(this, null);
			out.print(",");
		}
		params.get(params.size()-1).accept(this, null);
		out.print(")");
		
		return null;
	}
	
	@Override
	public String toString() {
		return baos.toString();
	}

}
