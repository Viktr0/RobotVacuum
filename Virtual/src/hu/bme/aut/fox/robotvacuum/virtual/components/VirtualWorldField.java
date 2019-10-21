package hu.bme.aut.fox.robotvacuum.virtual.components;

public class VirtualWorldField {
	public Status status;

	public VirtualWorldField(Status s) {
		status = s;
	}

	public enum Status {
		CLEAN,
		DIRTY,
		NOTEMPTY
	}
}
