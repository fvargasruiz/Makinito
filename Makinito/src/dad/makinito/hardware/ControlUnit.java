package dad.makinito.hardware;

import java.util.Map;

public class ControlUnit extends FunctionalUnit {
	private Sequencer sequencer;
	private Decoder decoder;
	
	private Register instructionRegister;	// registro de instrucción
	private Register programCounter;		// contador de programa
	
	public ControlUnit(Makinito makinito) {
		setName("UC");
		
		sequencer = new Sequencer();
		decoder = new Decoder(sequencer, makinito);
		instructionRegister = new Register("RI");
		programCounter = new Register("CP");
		
		getComponents().add(sequencer);
		getComponents().add(decoder);
		getComponents().add(instructionRegister);
		getComponents().add(programCounter);
	}
	
	public void init(Map<String, ControlSignal> signals) {
		sequencer.init(signals);
		decoder.init(signals);
	}

	public Sequencer getSequencer() {
		return sequencer;
	}

	public Decoder getDecoder() {
		return decoder;
	}

	public Register getInstructionRegister() {
		return instructionRegister;
	}

	public Register getProgramCounter() {
		return programCounter;
	}

	@Override
	public void reset() {
		instructionRegister.reset();
		programCounter.reset();
		sequencer.reset();
		decoder.reset();
	}
	
}
