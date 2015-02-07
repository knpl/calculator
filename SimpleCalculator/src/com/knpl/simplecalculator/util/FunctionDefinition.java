package com.knpl.simplecalculator.util;

import com.knpl.simplecalculator.nodes.Call;
import com.knpl.simplecalculator.nodes.Func;
import com.knpl.simplecalculator.nodes.Signature;

public interface FunctionDefinition {
	public Signature getSignature();
	public Func createFunction(Call call) throws Exception;
}
