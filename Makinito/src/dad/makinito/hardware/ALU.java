package dad.makinito.hardware;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ALU extends FunctionalUnit {

	private Register accumulator;				// registro acumulador
	private Register temporaryRegister;			// registro temporal
	private FlagRegister flagsRegister;			// registro de estado
	private Map<String, Operator> operators = new HashMap<String, Operator>();
	
	public ALU() {
		setName("UAL");
		accumulator = new Register("AC");
		temporaryRegister = new Register("RT");
		flagsRegister = new FlagRegister();
		
		getComponents().add(accumulator);
		getComponents().add(temporaryRegister);
		getComponents().add(flagsRegister);
	}
	
	public Register getAccumulator() {
		return accumulator;
	}

	public Register getTemporaryRegister() {
		return temporaryRegister;
	}

	public FlagRegister getFlagsRegister() {
		return flagsRegister;
	}

	public final Collection<Operator> getOperators() {
		return operators.values();
	}

	public Operator getOperador(String name) {
		return operators.get(name);
	}

	public void addOperator(Operator operator) {
		operator.setIn1(accumulator);
		operator.setIn2(temporaryRegister);
		operator.setOut(accumulator);
		operator.setFlagRegister(flagsRegister);
		operators.put(operator.getName(), operator);
//		getComponentes().add(operador);
	}

	@Override
	public void reset() {
		accumulator.reset();
		temporaryRegister.reset();
		flagsRegister.reset();
	}

}
