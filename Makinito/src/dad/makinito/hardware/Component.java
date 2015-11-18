package dad.makinito.hardware;

public abstract class Component {
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return toString("");
	}

	public String toString(String tabs) {
		return tabs + name;
	}
	
	public abstract void reset();

}
