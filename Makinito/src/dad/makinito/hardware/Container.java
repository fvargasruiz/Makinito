package dad.makinito.hardware;

import java.util.ArrayList;
import java.util.List;

public abstract class Container extends Component {
	private List<Component> components = new ArrayList<Component>();

	public final List<Component> getComponents() {
		return components;
	}
	
	public String toString(String tabs) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(tabs + getName() + " {\n");
		for (Component component : components) {
			if (component instanceof Container)
				buffer.append(((Container) component).toString(tabs + "\t") + "\n");
			else 
				buffer.append(component.toString(tabs + "\t") + "\n");
		}
		buffer.append(tabs + "}");
		return buffer.toString();
	}
	
	@Override
	public String toString() {
		return toString("");
	}

}
