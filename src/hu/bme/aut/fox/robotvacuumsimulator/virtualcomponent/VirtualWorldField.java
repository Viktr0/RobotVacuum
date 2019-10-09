package hu.bme.aut.fox.robotvacuumsimulator.virtualcomponent;

public class VirtualWorldField {
	public Status status;
	public enum Status {
		CLEAN,
		DIRTY,
		NOTEMPTY
	}
}
