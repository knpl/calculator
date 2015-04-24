package com.knpl.simplecalculator.util;

import com.knpl.simplecalculator.nodes.Complex;

public interface MyNumber {
	MyNumber add(MyNumber a);
	MyNumber sub(MyNumber a);
	MyNumber mul(MyNumber a);
	MyNumber div(MyNumber a);
	MyNumber mod(MyNumber a);
	MyNumber pow(MyNumber a);
	MyNumber neg();
	MyNumber deg2rad();
	MyNumber factorial();
	MyNumber max(MyNumber a);
	MyNumber min(MyNumber a);
	MyNumber floor();
	MyNumber ceil();
	MyNumber sqrt();
	MyNumber abs();
	MyNumber exp();
	MyNumber log();
	MyNumber sin();
	MyNumber cos();
	MyNumber tan();
	MyNumber asin();
	MyNumber acos();
	MyNumber atan();
	MyNumber sinh();
	MyNumber cosh();
	MyNumber tanh();
	MyNumber erf();
	MyNumber gamma();
	MyNumber loggamma();
	Complex toComplex();
}
