package com.knpl.simplecalculator.parser;


public class Token {
	private TokenType type;
	private String token;
	
	public Token(TokenType type, String token) {
		this.type = type;
		this.token = token;
	}
	
	public TokenType getType() {
		return type;
	}
	
	public String toString() {
		return token;
	}
}
