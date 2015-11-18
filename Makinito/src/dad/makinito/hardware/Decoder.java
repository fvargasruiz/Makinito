package dad.makinito.hardware;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dad.makinito.hardware.microcode.Condition;
import dad.makinito.hardware.microcode.InstructionSet;
import dad.makinito.hardware.microcode.MicroProgram;
import dad.makinito.hardware.microcode.Parameter;
import dad.makinito.software.AddressingMode;
import dad.makinito.software.Instruction;
import dad.makinito.software.Operand;

public class Decoder extends Component {
	
	private static final String INSTRUCTION_SET_FILE = "/dad/makinito/hardware/microcode/microcode.xml";

	private FlagRegister flagsRegister;						// registro de estado
	private Register decoderInstructionRegister;			// registro de instrucción del decodificador
	private Sequencer sequencer;
	private Map<String, ControlSignal> allSignals;
	private Makinito makinito;
	private InstructionSet instructionSet;

	public Decoder(Sequencer sequencer, Makinito makinito) {
		super();
		setName("Decodificador");
		decoderInstructionRegister = new Register("RID");
		this.sequencer = sequencer;
		this.makinito = makinito;
		try {
			this.instructionSet = InstructionSet.read(getClass().getResource(INSTRUCTION_SET_FILE));
		} catch (Exception e) {
			throw new MakinitoException("No ha sido posible cargar los microprogramas en el decodificador desde " + INSTRUCTION_SET_FILE, e);
		}
	}
	
	public void init(Map<String, ControlSignal> signals) {
		this.allSignals = signals;
	}

	public Register getDecoderInstructionRegister() {
		return decoderInstructionRegister;
	}
	
	public void setFlagRegister(FlagRegister flagsRegister) {
		this.flagsRegister = flagsRegister;
	}
	
	private Map<String, String> operandsToMap(List<Operand> operands, List<Parameter> parameters) {
		Map<String, String> map = new HashMap<String, String>();
		for (int i = 0; i < parameters.size(); i++) {
			Parameter parameter = parameters.get(i);
			if (parameter.getName() != null) {
				Operand operand = operands.get(i);
				map.put(parameter.getName(), operand.getRegisterName());
			}
		}
		return map;
	}
	
	private boolean checkConditions(List<Condition> conditions) {
		for (Condition condition : conditions) {
			if (checkCondition(condition)) return true;
		}
		return false;
	}
	
	private boolean checkCondition(Condition condition) {
		if (condition == null) return true;
		
		// si hay carry, comprobamos si es igual al del registro de estados 
		if (condition.isCarry() != null && condition.isCarry() != flagsRegister.isCarry()) {
			return false;
		}

		// si hay carry, comprobamos si es igual al del registro de estados 
		if (condition.isNegative() != null && condition.isNegative() != flagsRegister.isNegative()) {
			return false;
		}

		// si hay carry, comprobamos si es igual al del registro de estados 
		if (condition.isOverflow() != null && condition.isOverflow() != flagsRegister.isOverflow()) {
			return false;
		}

		// si hay carry, comprobamos si es igual al del registro de estados 
		if (condition.isZero() != null && condition.isZero() != flagsRegister.isZero()) {
			return false;
		}

		
		return true;
	}
	
	private List<ControlSignal> decode(Instruction instruction) {
		List<ControlSignal> controlSignals = new ArrayList<ControlSignal>();
		
		MicroProgram mp = instructionSet.search(instruction, makinito);
		
		if (mp == null || (mp.isMacro() != null && mp.isMacro())) {
			throw new MakinitoException("Instrucción no válida: " + instruction);
		}
		
		if (checkConditions(mp.getConditions())) {
			
			Map<String, String> parameters = operandsToMap(instruction.getOperands(), mp.getParameters());
			List<String> signals = mp.getSignals(parameters, instructionSet);
			for (String signal : signals) {
				controlSignals.add(allSignals.get(signal));
			}

		} else {
			controlSignals.add(allSignals.get("ICP"));
		}
		
		return optimize(controlSignals);
	}

	private List<ControlSignal> optimize(List<ControlSignal> controlSignals) {
		List<ControlSignal> optimized = new ArrayList<ControlSignal>(controlSignals);
		ControlSignal previous = null;
		for (int i = 0; i < controlSignals.size(); i++) {
			ControlSignal signal = controlSignals.get(i); 
			if (previous != null) {
				
				if (previous.getName().contains("-") && signal.getName().contains("-")) {
					String [] prev = previous.getName().split("-");
					String [] curr = signal.getName().split("-");
					if (prev[0].equals(curr[1]) && prev[1].equals(curr[0])) {
						optimized.remove(signal);
						optimized.remove(previous);
					}
				}
			}
			previous = signal;
		}
		return optimized;
	}

	public void decode() {
		Instruction instruction = (Instruction) getDecoderInstructionRegister().getContent();
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

	public static void main(String[] args) {
		Makinito makinito = new Makinito();
		Decoder decoder = makinito.getCPU().getControlUnit().getDecoder();
		
		Instruction i = new Instruction("SUMAR");
		i.getOperands().add(new Operand(AddressingMode.IMMEDIATE, 5, null));
//		i.getOperands().add(new Operand(AddressingMode.REGISTER, makinito.encodeRegister("AC"), "AC"));
//		i.getOperands().add(new Operand(AddressingMode.DIRECT, 12, null));
//		i.getOperands().add(new Operand(AddressingMode.DIRECT, 12, null));
//		i.getOperands().add(new Operand(AddressingMode.INDIRECT, 9, null));
//		i.getOperands().add(new Operand(AddressingMode.INDIRECT, 9, null));
		i.getOperands().add(new Operand(AddressingMode.REGISTER, makinito.encodeRegister("RT"), "RT"));
//		i.getOperands().add(new Operand(AddressingMode.IMMEDIATE, 5, null));
		
		System.out.println(i);
		
		List<ControlSignal> signals = decoder.decode(i);
		
		System.out.println(signals);
	}
	
}
