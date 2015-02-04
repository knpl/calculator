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
	@Override
	public Node visit(BinOp node) throws Exception {
		out.print("(");
		node.getLHS().accept(this);
		out.print(node.getOpString());
		node.getRHS().accept(this);
		out.print(")");
		return node;
	}

	@Override
	public Node visit(MonOp node) throws Exception {
		out.print(node.getOpString());
		out.print("(");
		node.getOp().accept(this);
		out.print(")");
		return node;
	}
	
	@Override
	public Node visit(Func node) throws Exception {
		out.print("!"+node.getName()+"(");
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
	public Node visit(Call node) throws Exception {
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
	public Node visit(Num node) throws Exception {
		out.print(node.getValue());
		return node;
	}

	@Override
	public Node visit(Var node) throws Exception {
		out.print(node.getName());
		return node;
	}

	@Override
	public Node visit(FuncDefNode node) throws Exception {
		node.getSignature().accept(this);
		out.print(" = ");
		node.getExpression().accept(this);
		return node;
	}
	
	@Override
	public Node visit(Signature node) throws Exception {
		out.print("def "+node.getName()+"(");
		List<Var> params = node.getParameters();
		for (int i = 0; i < params.size()-1; ++i) {
			params.get(i).accept(this);
			out.print(",");
		}
		params.get(params.size()-1).accept(this);
		out.print(")");
		
		return node;
	}
	
	@Override
	public String toString() {
		return baos.toString();
	}

}
