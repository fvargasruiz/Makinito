package dad.makinito.hardware.signals;

import dad.makinito.hardware.ControlSignal;
import dad.makinito.hardware.Memory;

public class ReadControl extends ControlSignal {
	
	public ReadControl(Memory memory) {
		super("LECT", memory, "Recupera la información (dato o instrucción) que hay en la dirección contenida en RD y lo almacena en RM");
	}
	
	@Override
	public void handleActivate() {
		((Memory) getComponent()).read();
	}

}
