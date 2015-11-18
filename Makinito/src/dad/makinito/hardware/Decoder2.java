package dad.makinito.hardware;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import dad.makinito.software.Instruction;
import dad.makinito.software.Operand;

public class Decoder2 extends Component {

	private FlagRegister flagsRegister;						// registro de estado
	private Register decoderInstructionRegister;			// registro de instrucción del decodificador
	private Sequencer sequencer;
	private Map<String, ControlSignal> allSignals;
	private Makinito makinito;

	public Decoder2(Sequencer sequencer, Makinito makinito) {
		super();
		setName("Decodificador");
		decoderInstructionRegister = new Register("RID");
		this.sequencer = sequencer;
		this.makinito = makinito;
	}
	
	public void init(Map<String, ControlSignal> signals) {
		this.allSignals = signals;
	}

	public Register getInstructionRegister() {
		return decoderInstructionRegister;
	}
	
	public void setFlagRegister(FlagRegister flagsRegister) {
		this.flagsRegister = flagsRegister;
	}

	private List<ControlSignal> decode(Instruction instruction) {
		List<ControlSignal> signals = new ArrayList<ControlSignal>();
		
		if ("MOVER".equals(instruction.getOpCode())) {
			fromOperandToBus(signals, instruction.getOperands().get(0), 1, true); // true = data bus (BD) / false = address bus
			fromDataBusToTarget(signals, instruction.getOperands().get(1), 2);
			signals.add(allSignals.get("ICP")); 
		} else if ("SUMAR".equals(instruction.getOpCode())) {
			fromOperandsToALU(signals, instruction.getOperands());
			signals.add(allSignals.get("UAL-OP(+)"));
			signals.add(allSignals.get("ICP")); 
		} else if ("RESTAR".equals(instruction.getOpCode())) {
			fromOperandsToALU(signals, instruction.getOperands());
			signals.add(allSignals.get("UAL-OP(-)"));
			signals.add(allSignals.get("ICP")); 
		} else if ("MULTIPLICAR".equals(instruction.getOpCode())) {
			fromOperandsToALU(signals, instruction.getOperands());
			signals.add(allSignals.get("UAL-OP(*)"));
			signals.add(allSignals.get("ICP")); 
		} else if ("DIVIDIR".equals(instruction.getOpCode())) {
			fromOperandsToALU(signals, instruction.getOperands());
			signals.add(allSignals.get("UAL-OP(/)"));
			signals.add(allSignals.get("ICP")); 
		} else if ("TERMINAR".equals(instruction.getOpCode())) {
			signals.add(allSignals.get("TERM"));
			signals.add(allSignals.get("ICP")); 
		} else if ("COMPARAR".equals(instruction.getOpCode())) {
			fromOperandsToALU(signals, instruction.getOperands());
			signals.add(allSignals.get("UAL-OP(=)"));
			signals.add(allSignals.get("ICP")); 
		} else if (	("SALTAR".equals(instruction.getOpCode())) ||														// salto incondicional
					("SALTAR-SI-IG".equals(instruction.getOpCode()) 		&& 	flagsRegister.isZero()) ||								// salta si Z=1
					("SALTAR-SI-NIG".equals(instruction.getOpCode()) 		&& 	!flagsRegister.isZero()) ||							// salta si Z=0
					("SALTAR-SI-MA".equals(instruction.getOpCode()) 		&& 	!flagsRegister.isNegative() && !flagsRegister.isZero()) ||		// salta si N=0 y Z=0
					("SALTAR-SI-MAI".equals(instruction.getOpCode()) 		&& 	(!flagsRegister.isNegative() || flagsRegister.isZero())) ||		// salta si N=0 o Z=1
					("SALTAR-SI-ME".equals(instruction.getOpCode()) 		&& 	flagsRegister.isNegative()) ||							// salta si N=1
					("SALTAR-SI-MEI".equals(instruction.getOpCode()) 		&& 	(flagsRegister.isNegative() || flagsRegister.isZero())))			// salta si N=1 o Z=0 
		{	
			fromOperandToBus(signals, instruction.getOperands().get(0), 1, false);
			signals.add(allSignals.get("BDI-CP"));
		} else {
			signals.add(allSignals.get("ICP")); 
		}

		return signals;
	}

	private void fromDataBusToTarget(List<ControlSignal> signals, Operand op, int position) {
		switch (op.getAddressingMode()) {
		case IMMEDIATE:
			throw new MakinitoException("El destino de la instrucción no puede ser un valor inmediato: " + getInstructionRegister().getInstruction());
		case DIRECT:
			signals.add(allSignals.get("CDE" + position + "-BDI"));
			signals.add(allSignals.get("BDI-RD"));
			signals.add(allSignals.get("BD-RM"));
			signals.add(allSignals.get("ESCR"));
			break;
		case INDIRECT:
			signals.add(allSignals.get("CDE" + position + "-BDI"));
			signals.add(allSignals.get("BDI-RD"));
			signals.add(allSignals.get("LECT"));
			signals.add(allSignals.get("RM-BDI"));
			signals.add(allSignals.get("BDI-RD")); 
			signals.add(allSignals.get("BD-RM"));
			signals.add(allSignals.get("ESCR"));
			break;
		case REGISTER:
			String registerName = makinito.decodeRegister(op.getEffectiveAddressField().getValue());
			signals.add(allSignals.get("BD-" + registerName));
			break;
		default:
			break;
		}	
	}

	private void fromOperandsToALU(List<ControlSignal> signals, List<Operand> operands) {
		if (operands.size() == 2) {
			fromOperandToRegister(signals, operands.get(0), 1, "AC");
			fromOperandToRegister(signals, operands.get(1), 2, "RT");
		} 
		else if (operands.size() == 1) {
			fromOperandToRegister(signals, operands.get(0), 1, "AC");			
		}
	}

	private void fromOperandToRegister(List<ControlSignal> signals, Operand op, int position, String targetRegister) {
		fromOperandToBus(signals, op, position, true);
		signals.add(allSignals.get("BD-" + targetRegister.toUpperCase()));		
	}

	private void fromOperandToBus(List<ControlSignal> signals, Operand op, int position, boolean data) {
		switch (op.getAddressingMode()) {
		case IMMEDIATE:
			signals.add(allSignals.get("CDE" + position + "-BD" + (data ? "" : "I")));
			break;
		case DIRECT:
			signals.add(allSignals.get("CDE" + position + "-BDI"));
			signals.add(allSignals.get("BDI-RD"));
			signals.add(allSignals.get("LECT"));
			signals.add(allSignals.get("RM-BD" + (data ? "" : "I")));
			break;
		case INDIRECT:
			signals.add(allSignals.get("CDE" + position + "-BDI"));
			signals.add(allSignals.get("BDI-RD"));
			signals.add(allSignals.get("LECT"));
			signals.add(allSignals.get("RM-BDI"));
			signals.add(allSignals.get("BDI-RD"));
			signals.add(allSignals.get("LECT"));
			signals.add(allSignals.get("RM-BD" + (data ? "" : "I")));
			break;
		case REGISTER:
			String registerName = makinito.decodeRegister(op.getEffectiveAddressField().getValue());
			signals.add(allSignals.get(registerName + "-BD" + (data ? "" : "I")));
			break;
		default:
			break;
		}
	}

	public void decode() {
		Instruction instruction = (Instruction) getInstructionRegister().getContent();
		List<ControlSignal> signals = decode(instruction);
		sequencer.getExecution().clear();
		sequencer.getExecution().addAll(signals);
	}
	
	@Override
	public String toString(String tabs) {
		return 
				tabs + getName() + "{\n" +
				tabs + "\t" + decoderInstructionRegister.toString() + "\n" + 
				tabs + "}";
	}

	@Override
	public void reset() {
		flagsRegister.reset();
		decoderInstructionRegister.reset();
	}

}
