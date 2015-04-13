package com.knpl.simplecalculator.parser;

import java.util.ArrayList;
import java.util.List;

import com.knpl.simplecalculator.nodes.*;
import static com.knpl.simplecalculator.parser.TokenType.*;

public class Parser {
	private Lexer lex;
	private Token tok;
	
	private Node result;
	
	private List<Object> mem;
	
	public Parser(Lexer lex) {
		this.lex = lex;
		nextToken();
		this.result = Null.NULL;
		this.mem = new ArrayList<Object>();
	}
	
	private boolean push(Object node) {
		mem.add(node);
		return true;
	}
	
	public Token nextToken() {
		return tok = lex.nextToken();
	}
	
	public boolean match(TokenType t) {
		return t == tok.type;
	}
	
	public boolean token(TokenType t) {
		if (t == tok.type) {
			tok = lex.nextToken();
			return true;
		}
		return false;
	}
	
	public boolean start() {
		if (token(EOF)) {
			return true;
		}
		else {
			return statement() && token(EOF);
		}
	}
	
	public boolean statement() {
		if (token(DEF)) {
			return definition();
		}
		else {
			return expr();
		}
	}
	
	private boolean identifier() {
		push(tok.token);
		return token(ID);
	}
	
	public boolean def() {
		return identifier() && params() && token(EQ) && expr(); 
	}
	
	public boolean params() {
		return token(LPAR) && token(ID) && idlist() && token(RPAR); 
	}
	
	public boolean idlist() {
		if (token(COMMA)) {
			return token(ID) && idlist();
		}
		return true;
	}

	public boolean definition() {
		if (!match(ID))
			return false;
		
		String id = tok.token;
		nextToken();
		
		Signature sig = null;
		if (match(LPAR)) {
			nextToken();
			
			List<Var> params = new ArrayList<Var>();
			while (match(ID)) {
				params.add(new Var(tok.toString()));
				nextToken();
				if (match(RPAR)) {
					nextToken();
					sig = new Signature(id, params);
					break;
				}
				else if (match(COMMA)) {
					nextToken();
				}
				else {
					return false;
				}
			}
		}
		
		if (!(token(EQ) && expr()))
			return false;
		
		result = (sig == null) ? new ConstDefNode(id, (Expr) result) 
							   : new FuncDefNode(sig, (Expr) result);
		
		return true;
	}
	
	public boolean constDef() {
		if (!match(ID))
			return false;
		
		String id = tok.toString();
		nextToken();
		
		if (!(token(EQ) && expr()))
			return false;
		
		result = new ConstDefNode(id, (Expr) result);
		
		return true;
	}
	
	public boolean funcDef() {
		if (!match(ID))
			return false;
		
		String id = tok.toString();
		nextToken();
		
		Signature sig = null;
		if (!token(LPAR))
			return false;
			
		List<Var> params = new ArrayList<Var>();
		while (match(ID)) {
			params.add(new Var(tok.toString()));
			nextToken();
			if (token(RPAR)) {
				sig = new Signature(id, params);
				break;
			}
			else if (match(COMMA)) {
				nextToken();
			}
			else {
				return false;
			}
		}
		
		if (!(token(EQ) && expr()))
			return false;
		
		result = new FuncDefNode(sig, (Expr) result);
		
		return true;
	}
	
	public boolean expr() {
		if (!term())
			return false;
		
		boolean plus;
		while ((plus = match(PLUS)) || match(MIN)) {
			nextToken();
			Expr last = (Expr) result;
			if (!term()) 
				return false;
			result = plus ? new Add(last, (Expr)result) : new Sub(last, (Expr)result);
		}
		return true;
	}
	
	public boolean term() {
		if (!prefix())
			return false;
		
		while (true) {
			Expr last = (Expr) result;
			switch (tok.type) {
			case MUL:
				token(MUL);
				if (!prefix())
					return false;
				result = new Mul(last, (Expr) result);
				break;
			case DIV:
				token(DIV);
				if (!prefix())
					return false;
				result = new Div(last, (Expr) result);
				break;
			case MOD:
				token(MOD);
				if (!prefix())
					return false;
				result = new Mod(last, (Expr) result);
				break;
			default:
				return true;
			}
		}
	}
	
	public boolean prefix() {
		if (token(MIN)) {
			if (!prefix())
				return false;
			result = new Minus((Expr) result);
			
			return true;
		}
		return postfix();
	}
	
	public boolean postfix() {
		return factor() && postfixrest();
	}
	
	public boolean postfixrest() {
		switch (tok.type) {
		case EXC:
			token(EXC);
			if (!postfixrest())
				return false;
			result = new Factorial((Expr) result);
			return true;
		case D2R:
			token(D2R);
			if (!postfixrest())
				return false;
			result = new DegToRad((Expr) result);
			return true;
		default:
			return true;
		}
	}
	
	public boolean factor() {		
		if (!terminal())
			return false;
		
		if (token(POW)){
			Expr lhs = (Expr) result;
			if (!prefix())
				return false;
			result = (Node) new Pow(lhs, (Expr) result);
		}
		
		return true;
	}
	
	public boolean terminal() {
		switch (tok.type) {
			
		case NUM:
			result = new Num(tok.token);
			nextToken();
			return true;
		
		case ID:
			String id = tok.token;
			nextToken();
			if (!match(LPAR)) {
				result = new Var(id);
				return true;
			}
			nextToken();
			
			List<Expr> args = new ArrayList<Expr>();
			while (expr()) {
				args.add((Expr) result);
				if (token(RPAR)) {
					result = new Call(id, args);
					return true;
				}
				if (!token(COMMA))
					return false;
			}

			return false;
		
		case LPAR:
			nextToken();
			return expr() && token(RPAR);
		
		default:
			return false;
		}
	}
	
	public Node getResult() {
		return result;
	}
}
