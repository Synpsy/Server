package net.run;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class Countdown {

	public Integer Seconds, finalSeconds, Task;
	Runnable runnable;
	Plugin Plugin;
	
	public interface Count {
		public void onCount(Integer Seconds);
	}
	
	public Countdown(Plugin Plugin, Integer Seconds, Runnable runnable) {
		this.Seconds = Seconds; this.runnable = runnable; this.Seconds += 1; this.finalSeconds = Seconds; this.Plugin = Plugin;
	}
	
	public void start(Count Count) {
		Task = Bukkit.getScheduler().scheduleSyncRepeatingTask(Plugin, new Runnable() {
			
			@Override
			public void run() {
				Seconds--;
				Count.onCount(Seconds);
				if(Seconds == 0) {
					runnable.run();
					Bukkit.getScheduler().cancelTask(Task);
				}
			}
			
		}, 0, 20L);
	}
	
	public void setSeconds(Integer Seconds) {
		this.Seconds = Seconds;
	}
	
	public void stop() {
		Bukkit.getScheduler().cancelTask(Task);
	}
	
}

