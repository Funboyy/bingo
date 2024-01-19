package de.funboyy.bingo.api.enums;

public enum State {

  OFFLINE(false),
  ONLINE(false),
  LOBBY(false),
  PREPARATION(true),
  PLAYING(true),
  FINISHED(true);

  private final boolean enabled;

  State(final boolean enabled) {
    this.enabled = enabled;
  }

  public boolean isEnabled() {
    return this.enabled;
  }

}
