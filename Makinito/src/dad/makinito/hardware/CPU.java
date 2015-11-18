package dad.makinito.hardware;

public class CPU extends FunctionalUnit {
	private ALU alu;						// unidad aritmético-lógica
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
