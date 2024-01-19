package de.funboyy.bingo.api.model;

public record BingoRow(BingoItem... items) {

  public int difficulty() {
    int difficulty = 0;

    for (final BingoItem item : this.items) {
      difficulty += item.getDifficulty();
    }

    return difficulty;
  }

}
