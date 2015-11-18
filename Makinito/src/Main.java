import java.io.File;
import java.io.IOException;

import dad.makinito.hardware.Makinito;
import dad.makinito.hardware.MakinitoException;
import dad.makinito.software.parser.ParserException;

public class Main {
	
	public static final String EJEMPLO1 = "ejemplos/ejemplo1-multiplicar.maki";
	public static final String EJEMPLO2 = "ejemplos/ejemplo2-factorial.maki";
	public static final String EJEMPLO3 = "ejemplos/ejemplo3-salto-incondicional.maki";

	public static void main(String[] args) throws IOException, MakinitoException {
		try {
			Makinito makinito = new Makinito();
			makinito.getClock().setFrequency(0);
			makinito.loadProgram(new File(EJEMPLO2));
			makinito.start(false);
		} catch (ParserException e) {
			e.printStackTrace();
		}
	}

}
