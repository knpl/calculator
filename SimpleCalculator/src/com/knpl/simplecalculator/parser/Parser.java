package com.knpl.simplecalculator.parser;

import java.util.ArrayList;
import java.util.List;

import com.knpl.simplecalculator.nodes.*;

public class Parser {
	private Lexer lex;
	private Token tok;
	
	private Node result;
	
	public Parser(Lexer lex) {
		this.lex = lex;
		nextToken();
		this.result = Null.NULL;
	}
	
	public Token nextToken() {
		return tok = lex.nextToken();
	}
	
	public boolean match(TokenType t) {
		return t == tok.getType();
	}
	
	public boolean start() {
		if (match(TokenType.EOF)) return true;
		if (!statement()) return false;
		if (!match(TokenType.EOF)) return false;
		return true;
	}
	
	public boolean statement() {
		if (match(TokenType.DEF)) {
			nextToken();
			if (!definition()) return false;
		}
		else if (!expr()) return false;
		return true;
	}
	
	public boolean definition() {
		if (!signature()) return false;
		Signature sig = (Signature) result;
		
		if (!match(TokenType.EQ)) return false;
		nextToken();
		
		if (!expr()) return false;
		
		result = new FuncDefNode(sig, (Expr)result);
		
		return true;
	}
	
	public boolean signature() {
		if (!match(TokenType.ID)) return false;
		
		String id = tok.toString();
		nextToken();
		
		ArrayList<Var> params; 
		
		if (!match(TokenType.LPAR)) {
			return false;
		}
		nextToken();
		
		params = new ArrayList<Var>();
		
		if (match(TokenType.RPAR)) {
			nextToken();
		}
		else {
			while (match(TokenType.ID)) {
				params.add(new Var(tok.toString()));
				nextToken();
				if (match(TokenType.RPAR)) {
					nextToken();
				}
				else if (match(TokenType.COMMA)) {
					nextToken();
				}
				else {
					return false;
				}
			}
		}
		
		result = new Signature(id, params);
		
		return true;
	}
	
	public boolean expr() {
		if (!term()) return false;
		
		boolean plus;
		while ((plus = match(TokenType.PLUS)) || match(TokenType.MIN)) {
			nextToken();
			Expr last = (Expr) result;
			if (!term()) return false;
			result = plus ? new Add(last, (Expr)result) : new Sub(last, (Expr)result);
		}
		return true;
	}
	
	public boolean term() {
		if (!factor()) return false;
		
		while (true) {
			Expr last = (Expr) result;
			if (match(TokenType.MUL)){
				nextToken();
				if	(!factor()) return false;
				result = new Mul(last, (Expr) result);
			}
			else if (match(TokenType.DIV)) {
				nextToken();
				if	(!factor()) return false;
				result = new Div(last, (Expr) result);
			}
			else if (noMinusFactor()) {
				result = new Mul(last, (Expr) result);
			}
			else {
				return true;
			}
		}
	}
	
	public boolean factor() {
		boolean minus = false;
		if (minus = match(TokenType.MIN)) {
			nextToken();
		}
		
		if (!terminal()) {
			return false;
		}
		if (!match(TokenType.POW)){
			if (minus) {
				result = new Minus((Expr)result);
			}
			return true;
		}
		nextToken();
		
		Expr lhs = (Expr)result;
		if (!factor()) {
			return false;
		}
		result = (Node)new Pow(lhs, (Expr)result);
		if (minus) {
			result = new Minus((Expr)result);
		}
		return true;
	}

	public boolean noMinusFactor() {
		if (!terminal()) {
			return false;
		}
		if (!match(TokenType.POW)){
			return true;
		}
		nextToken();
		
		Expr lhs = (Expr)result;
		if (!factor()) {
			return false;
		}
		result = (Node)new Pow(lhs, (Expr)result);
		return true;
	}
	 	
	public boolean terminal() {
		if (match(TokenType.NUM)) {
			result = new Num(Double.parseDouble(tok.toString()));
			nextToken();
			return true;
		}
		else if (match(TokenType.ID)) {
			String id = tok.toString();
			nextToken();
			if (!match(TokenType.LPAR)) {
				result = new Var(id);
				return true;
			}
			nextToken();
			
			List<Expr> args = new ArrayList<Expr>();
			
			while (expr()) {
				args.add((Expr) result);
				if (match(TokenType.RPAR)) {
					nextToken();
					result = new Call(id, args);
					return true;
				}
				else if (match(TokenType.COMMA)) {
					nextToken();
				}
				else {
					return false;
				}
			}

			return false;
		}
		else if (match(TokenType.LPAR)){
			nextToken();
			if (!expr()) return false;
			if (!match(TokenType.RPAR)) return false;
			nextToken();
			return true;
		}
		else {
			return false;
		}
	}
	
	public Node getResult() {
		return result;
	}
}
