package de.funboyy.bingo.api;

import java.util.Timer;
import java.util.TimerTask;

public class Scheduler {

  private static Scheduler instance;

  public static Scheduler getInstance() {
    if (instance == null) {
      instance = new Scheduler();
    }

    return instance;
  }

  public void schedule(final Runnable runnable, final int delay) {
    new Timer().schedule(new TimerTask() {
      @Override
      public void run() {
        try {
          runnable.run();
        } catch (final Exception exception) {
          throw new RuntimeException(exception);
        }
      }
    }, delay);
  }

}
