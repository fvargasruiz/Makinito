package dad.makinito.hardware;

public abstract class ControlSignal {
	private String name;
	private String description;
	private Component component;

	public ControlSignal() {
		super();
	}
	
	public ControlSignal(String name, Component component, String description) {
		super();
		this.name = name;
		this.component = component;
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Component getComponent() {
		return component;
	}

	public void setComponent(Component component) {
		this.component = component;
	}

	public String gttDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public abstract void handleActivate();
	
	public void activate() {
		System.out.println(name + ": " + description + ".");
		handleActivate();
	}
	
	@Override
	public String toString() {
		return name; // "Señal de control " + name + " actúa sobre " + component.getName();
	}

}
