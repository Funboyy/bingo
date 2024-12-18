package de.funboyy.bingo.api;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Scheduler {

  private static Scheduler instance;

  public static Scheduler getInstance() {
    if (instance == null) {
      instance = new Scheduler();
    }

    return instance;
  }

  private final ScheduledExecutorService service = Executors.newScheduledThreadPool(5);

  public void schedule(final Runnable runnable, final int delay) {
    this.service.schedule(runnable, delay, TimeUnit.MILLISECONDS);
  }

}
