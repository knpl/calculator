package com.knpl.simplecalculator.parser;

import java.io.PrintStream;


public class Lexer {
	private int i;
	private char[] text;
	
	public Lexer(char[] text) {
		i = 0;
		this.text = text;
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
	
	public Token nextToken() {
		Token result = null;
		
		while (result==null && i<text.length) {
			switch (text[i]) {
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
					result = definition();
					if (result == null) result = numeric();
					if (result == null) result = identifier();
					if (result == null) {
						result = new Token(TokenType.INVALID, ""+text[i]);
						i+=1;
					}
			}
		}
		
		return result == null ? new Token(TokenType.EOF, "EOF") : result;
	}
	
	private Token definition() {
		Token result = null;
		if (i+2 < text.length && text[i] == 'd' && text[i+1] == 'e' && text[i+2] == 'f') {
			result = new Token(TokenType.DEF, "def");
			i+=3;
		}
		return result;
	}
	
	private Token numeric() {
		Token result = null;
		int start = i;
		
		if (i   < text.length && text[i] == '.' &&
			i+1 < text.length && '0' <= text[i+1] && text[i+1] <= '9') {
			
			i+=2;
			while (i < text.length && '0' <= text[i] && text[i] <= '9') {
				i+=1;
			}
		}
		else {
			if (i < text.length && text[i] == '0') {
				i+=1;
			}
			else if (i < text.length && '1' <= text[i] && text[i] <= '9') {
				i+=1;
				while (i < text.length && '0' <= text[i] && text[i] <= '9') {
					i+=1;
				}
			}
			
			if (i > start && i < text.length && text[i] == '.') {
				i+=1;
				while (i < text.length && '0' <= text[i] && text[i] <= '9') {
					i+=1;
				}
			}
		}
		
		if (i > start) {
			result = new Token(TokenType.NUM, new String(text, start, i-start));
		}
		
		return result;
	}
	
	private Token identifier() {
		Token result = null;
		int start = i;
		
		if (i < text.length && isIdentifierStart(text[i])) {
			i+=1;
			while (i < text.length && isIdentifier(text[i])) {
				i+=1;
			}
			result = new Token(TokenType.ID, new String(text, start, i-start));
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