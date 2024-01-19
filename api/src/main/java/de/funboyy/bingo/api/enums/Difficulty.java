package de.funboyy.bingo.api.enums;

public enum Difficulty {

  VERY_EASY("Sehr einfach", 0),
  EASY("Einfach", 25),
  NORMAL("Normal", 50),
  HARD("Schwer", 75),
  VERY_HARD("Sehr schwer", 100),
  UNKNOWN("Unbekannt", -1);

  private final String displayName;
  private final int value;

  Difficulty(final String displayName, final int value) {
    this.displayName = displayName;
    this.value = value;
  }

  public String getDisplayName() {
    return this.displayName;
  }

  public int getValue() {
    return this.value;
  }

  public static Difficulty fromString(final String name) {
    for (final Difficulty difficulty : values()) {
      if (difficulty.getDisplayName().equals(name)) {
        return difficulty;
      }
    }

    return UNKNOWN;
  }

}
