package hu.bme.aut.fox.robotvacuum.viewmodel;

import io.reactivex.subjects.BehaviorSubject;

import java.time.ZonedDateTime;
import java.util.Timer;
import java.util.TimerTask;

public class ClockViewModel {

	public final BehaviorSubject<Integer> hours = BehaviorSubject.create();
	public final BehaviorSubject<Integer> minutes = BehaviorSubject.create();
	public final BehaviorSubject<Integer> seconds = BehaviorSubject.create();

	private Timer timer;

	public void start() {
		if (timer == null) {
			timer = new Timer();
			timer.scheduleAtFixedRate(new TimerTask() {
				@Override
				public void run() {
					ZonedDateTime now = ZonedDateTime.now();
					hours.onNext(now.getHour());
					minutes.onNext(now.getMinute());
					seconds.onNext(now.getSecond());
				}
			}, 0, 1000);
		}
	}

	public void stop() {
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
	}
}
