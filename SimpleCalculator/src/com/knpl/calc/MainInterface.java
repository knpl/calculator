package com.knpl.calc;

import java.util.ArrayList;

import com.knpl.calc.nodes.numbers.Num;
import com.knpl.calc.plot.Mapper;

public interface MainInterface {
	void print(Num n) throws Exception;
	void print(String s, boolean left) throws Exception;
	void plot(ArrayList<Mapper> mappers) throws Exception;
}
