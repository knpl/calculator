package com.knpl.simplecalculator.parser;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;


public class Lexer {
	private int i;
	final String input;
	final int len;
	
	public Lexer(String input) {
		i = 0;
		this.input = input;
		this.len = input.length();
	}
	
	public void print(PrintStream out) {
		Token cur;
		TokenType type;
		do { 
			cur = nextToken();
			type = cur.getType();
			out.printf("(%s,\"%s\") ", type, cur);
		}
		while (type != TokenType.EOF && type != TokenType.INVALID);
	}
	
	@Override
	public String toString() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream out = new PrintStream(baos);
		print(out);
		out.close();
		return baos.toString();
	}
	
	public Token nextToken() {
		Token result = null;
		
		while (result==null && i < len) {
			switch (input.charAt(i)) {
				case ' ': case '\r': case '\n': case '\t':
					i+=1;
					break;
				case '+': 
					result = new Token(TokenType.PLUS, "+"); 
					i+=1; 
					break;
				case '-': 
					result = new Token(TokenType.MIN, "-"); 
					i+=1; 
					break;
				case '*': 
					result = new Token(TokenType.MUL, "*"); 
					i+=1; 
					break;
				case '/': 
					result = new Token(TokenType.DIV, "/"); 
					i+=1; 
					break;
				case '%': 
					result = new Token(TokenType.MOD, "%"); 
					i+=1; 
					break;
				case '(': 
					result = new Token(TokenType.LPAR, "("); 
					i+=1; 
					break;
				case ')': 
					result = new Token(TokenType.RPAR, ")"); 
					i+=1; 
					break;
				case ',':
					result = new Token(TokenType.COMMA, ",");
					i+=1;
					break;
				case '^': 
					result = new Token(TokenType.POW, "^"); 
					i+=1; 
					break;
				case '=':
					result = new Token(TokenType.EQ, "=");
					i+=1;
					break;
				default:
					try {
						result = definition();
						if (result == null) result = numeric();
						if (result == null) result = identifier();
						if (result == null) {
							result = new Token(TokenType.INVALID, ""+input.charAt(i));
							i += 1;
						}
					}
					catch (IndexOutOfBoundsException e) {
						String s  = input.substring(i);
						result = new Token(TokenType.INVALID, s);
						i += s.length();
					}
			}
		}
		
		return (result == null) ? new Token(TokenType.EOF, "EOF") : result;
	}
	
	private Token definition() {
		Token result = null;
		if (input.startsWith("def", i)) {
			result = new Token(TokenType.DEF, "def");
			i+=3;
		}
		return result;
	}
	
	private Token numeric() {
		Token result = null;
		int start = i;
		
		char c = input.charAt(i);
		
		if (c == '.') {
			if (i+1 >= len) {
				return null;
			}
			
			c = input.charAt(i+1);
			if (!('0' <= c && c <= '9')) {
				return null;
			}
					
			i += 2;
			while (i < len) {
				c = input.charAt(i);
				if (!('0' <= c && c <= '9')) {
					break;
				}
				i += 1;
			}
			
		}
		else {
			if (c == '0') { /* Must immediately be followed by dot */
				i+=1;
			}
			else if ('1' <= c && c <= '9') { /* Can contain more digits before dot */
				
				i += 1;
				while (i < len) {
					c = input.charAt(i);
					if (!('0' <= c && c <= '9')) {
						break;
					}
					i += 1;
				}
				
			}
			
			if (i > start && i < len && input.charAt(i) == '.') {
				i += 1;
				while (i < len) {
					c = input.charAt(i);
					if (!('0' <= c && c <= '9')) {
						break;
					}
					i += 1;
				}
			}
		}
		
		if (i > start) {
			result = new Token(TokenType.NUM, input.substring(start, i));
		}
		
		return result;
	}
	
	private Token identifier() {
		Token result = null;
		char c;
		int start = i;
		
		c = input.charAt(i);
		if (isIdentifierStart(c)) {
			i+=1;
			while (i < len && isIdentifier(input.charAt(i))) {
				i+=1;
			}
			result = new Token(TokenType.ID, input.substring(start, i));
		}
		
		return result;
	}
	
	private boolean isIdentifierStart(char c) {
		return ('A' <= c && c <= 'Z') ||
			   ('a' <= c && c <= 'z') ||
			   ('_' == c);
	}
	
	private boolean isIdentifier(char c) {
		return ('0' <= c && c <= '9') ||
			   ('A' <= c && c <= 'Z') ||
			   ('a' <= c && c <= 'z') ||
			   ('_' == c);
	}
}