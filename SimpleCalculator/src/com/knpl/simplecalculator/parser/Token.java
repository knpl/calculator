package com.knpl.simplecalculator.parser;

public class Token {
	public final TokenType type;
	public final String token;
	
	public Token(TokenType type, String token) {
		this.type = type;
		this.token = token;
	}
	
	public String toString() {
		return token;
	}
}
