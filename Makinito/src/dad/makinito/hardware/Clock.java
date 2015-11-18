package dad.makinito.hardware;

public class Clock {
	private Integer frequency;
	private Sequencer sequencer;

	public Clock(Integer frequency, Sequencer sequencer) {
		super();
		this.frequency = frequency;
		this.sequencer = sequencer;
	}
	
	public Integer getFrequency() {
		return frequency;
	}

	public void setFrequency(Integer frequency) {
		this.frequency = frequency;
	}

	public Sequencer getSequencer() {
		return sequencer;
	}

	public void setSequencer(Sequencer sequencer) {
		this.sequencer = sequencer;
	}

	public void pulse() {
		sequencer.execute();
	}

}
