package dad.makinito.hardware.operators;

import dad.makinito.hardware.Operator;

public class DivisionOperator extends Operator {

	public DivisionOperator() {
		super("/");
	}

	@Override
	protected Integer handleOperate(Integer in1, Integer in2) {
		return in1 / in2;
	}

}
