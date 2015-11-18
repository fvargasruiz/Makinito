package dad.makinito.hardware.operators;

import dad.makinito.hardware.Operator;

public class AdditionOperator extends Operator {
	
	public AdditionOperator() {
		super("+");
	}

	@Override
	protected Integer handleOperate(Integer in1, Integer in2) {
		return in1 + in2;
	}

}
