package hu.bme.aut.fox.robotvacuum.components.world;

import java.util.Date;

public class InterpretedWorldField {
		private Status status;
		private long found; // ms
		private long cleaned; // ms;


		public InterpretedWorldField(Status s) {
			status = s;
			found = status.isKnown() ? new Date().getTime() : -1;
			cleaned = status == Status.CLEANED ? found : -1;
		}

		InterpretedWorldField(Status s, long c, long f) {
			status = s;
			cleaned = c;
			found = f;
		}

		public void clean() {
			status = Status.CLEANED;
			this.cleaned = new Date().getTime();
		}

		public void find(final boolean empty) {
			status = empty ? Status.DIRTY : Status.NOTEMPTY;
			found = new Date().getTime();
		}

		public Status getStatus() {
			return status;
		}

		public long getFound() {
			return found;
		}

		public long getCleaned() {
			return cleaned;
		}

	public enum Status {
		UNKNOWN(false, false),
		CLEANED(true, true),
		DIRTY(true, true),
		NOTEMPTY(false, true);

		private final boolean empty;
		private final boolean known;

		Status(final boolean isEmpty, final boolean known) {
			this.empty = isEmpty;
			this.known = known;
		}

		public boolean isEmpty() {
			return this.empty;
		}

		public boolean isKnown() {
			return this.known;
		}
	}
}
