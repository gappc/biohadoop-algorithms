package at.ac.uibk.dps.biohadoop.functions;

import at.ac.uibk.dps.biohadoop.functions.Function;

public class FunctionProvider {

	public static Function function;

	public static Function getFunction() {
		return function;
	}

	public static void setFunction(Function function) {
		FunctionProvider.function = function;
	}
	
}
