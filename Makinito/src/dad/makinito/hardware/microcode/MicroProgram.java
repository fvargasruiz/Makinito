package dad.makinito.hardware.microcode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlType;

@XmlType
public class MicroProgram {

	private String name;
	private Boolean macro = false;
	private ArrayList<Parameter> parameters = new ArrayList<Parameter>();
	private ArrayList<MicroInstruction> microInstructions = new ArrayList<MicroInstruction>();
	private List<Condition> conditions = new ArrayList<Condition>();

	@XmlAttribute
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlAttribute
	public Boolean isMacro() {
		if (macro == false)
			return null;
		return macro;
	}

	public void setMacro(Boolean macro) {
		this.macro = macro;
	}

	@XmlElementWrapper(name = "microInstructions")
	@XmlElements( {
		@XmlElement(name="macroInstruction", type=MacroInstruction.class),
		@XmlElement(name="signalInstruction", type=SignalInstruction.class),
	})
	public List<MicroInstruction> getMicroInstructions() {
		return microInstructions;
	}

	@XmlElement(name = "parameter")
	@XmlElementWrapper(name = "parameters")
	public List<Parameter> getParameters() {
		return parameters;
	}

	@XmlElementWrapper(name="conditions")
	@XmlElement(name="condition")
	public List<Condition> getConditions() {
		return conditions;
	}
	
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(name + " ");
		int i = 0 ;
		for (Parameter p : parameters) {
			buffer.append(p + ((i < parameters.size() - 1) ? "," : ""));
			i++;
		}
		return buffer.toString();
	}
	
	private String translate(Map<String, String> parameters, String text) {
		for (String name : parameters.keySet()) {
			String value = parameters.get(name);
			text = text.replaceAll("\\$" + name, value);
		}
		return text;
	}
	
	private Map<String, String> parametersToMap(List<MicroParameter> microParameters) {
		Map<String, String> map = new HashMap<String, String>();
		for (MicroParameter mp : microParameters) {
			map.put(mp.getName(), mp.getValue());
		}
		return map;
	}
	
	public List<String> getSignals(Map<String, String> parameters, InstructionSet is) {
		List<String> signals = new ArrayList<String>();
		
		for (MicroInstruction mi : microInstructions) {
			
			if (mi instanceof SignalInstruction) {
				
				signals.add(translate(parameters, mi.getName()));
				
			} else if (mi instanceof MacroInstruction) {
				MacroInstruction macro = (MacroInstruction) mi;
				MicroProgram subMi = is.search(macro);

				Map<String, String> subMiParameters = parametersToMap(macro.getParameters());
				Map<String, String> translatedParameters = new HashMap<String, String>();
				for (String name : subMiParameters.keySet()) {
					String value = subMiParameters.get(name);
					translatedParameters.put(name, translate(parameters, value));
				}
				
				signals.addAll(subMi.getSignals(translatedParameters, is));
			}
			
		}
		
		return signals;
	}

}
