package com.knpl.simplecalculator.parser;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {
	
	private static final Pattern tokenPattern = Pattern.compile(
		"	 (def) 			 |	#Match definition\n" +
		"	 ([A-Za-z_]\\w*) |	#Match identifier\n" +
		"	 ( (?: (?: 0|[1-9]\\d* )(?: \\.\\d* )? | (?: \\.\\d+ ) ) (?: [Ee]-?\\d+ )? )" 
		, Pattern.COMMENTS);
	
	private int i;
	private final String input;
	private final int len;
	private final Matcher matcher;
	
	public Lexer(String input) {
		i = 0;
		this.input = input;
		this.len = input.length();
		this.matcher = tokenPattern.matcher(input);
	}
	
	public void print(PrintStream out) {
		Token cur;
		TokenType type;
		do { 
			cur = nextToken();
			type = cur.type;
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
				result = patternToken();
			}
		}
		
		return (result == null) ? new Token(TokenType.EOF, "EOF") : result;
	}
	
	private Token patternToken() {
		Token result = null;
		
		if (!matcher.find(i)) {
			result = new Token(TokenType.INVALID, input.substring(i));
			i += 1;
			return result;
		}
		
		boolean match = false;
		String token;
		for (int group = 1; group <= matcher.groupCount(); ++group) {
			token = matcher.group(group);
			if (token != null) { /* found a match */
				match = true;
				
				switch (group) {
				case 1:
					result = new Token(TokenType.DEF, token);
					break;
				case 2:
					result = new Token(TokenType.ID, token);
					break;
				case 3:
					result = new Token(TokenType.NUM, token);
					break;
				default:
					result = new Token(TokenType.INVALID, token);
				}
				
				i += token.length();
				
				break;
			}
		}
		
		if (!match) {
			token = matcher.group(0);
			result = new Token(TokenType.INVALID, token);
			i += token.length();
		}
		
		return result;
	}
}