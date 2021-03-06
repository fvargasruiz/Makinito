package dad.makinito.hardware;

/**
 * El cerebro de Makinito (el microprocesador), contiene la unidad de control y 
 * la unidad aritm�tico-l�gica.
 * 
 * @author Francisco Vargas
 */
public class CPU extends FunctionalUnit {
	private ALU alu;						// unidad aritm�tico-l�gica
	private ControlUnit controlUnit;		// unidad de control
	
	public CPU(Makinito makinito) {
		setName("CPU");
		alu = new ALU();
		controlUnit = new ControlUnit(makinito);
		getComponents().add(alu);
		getComponents().add(controlUnit);
	}

	public ALU getALU() {
		return alu;
	}

	public ControlUnit getControlUnit() {
		return controlUnit;
	}

	@Override
	public void reset() {
		alu.reset();
		controlUnit.reset();
	}

}
