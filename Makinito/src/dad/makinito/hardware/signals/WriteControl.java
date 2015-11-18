package dad.makinito.hardware.signals;

import dad.makinito.hardware.Memory;
import dad.makinito.hardware.ControlSignal;

public class WriteControl extends ControlSignal {
	
	public WriteControl(Memory memory) {
		super("ESCR", memory, "Almacena en la dirección memoria contenida en RD el contenido del registro RM");
	}

	@Override
	public void handleActivate() {
		((Memory) getComponent()).write();
	}

}
