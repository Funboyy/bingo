package de.funboyy.bingo.api.model;

public class BingoCard {

  private final BingoItem[] items;

  public BingoCard() {
    this.items = new BingoItem[25];
  }

  public void highlight() {
    if (!this.isFilled()) {
      return;
    }

    BingoRow row = null;
    int difficulty = Integer.MAX_VALUE;

    for (int i = 0; i < 5; i++) {
      final BingoRow horizontal = new BingoRow(this.items[5 * i], this.items[5 * i + 1],
          this.items[5 * i + 2], this.items[5 * i + 3], this.items[5 * i + 4]);

      if (horizontal.difficulty() < difficulty) {
        difficulty = horizontal.difficulty();
        row = horizontal;
      }

      final BingoRow vertical = new BingoRow(this.items[i], this.items[i + 5],
          this.items[i + 10], this.items[i + 15], this.items[i + 20]);

      if (vertical.difficulty() < difficulty) {
        difficulty = vertical.difficulty();
        row = vertical;
      }
    }

    final BingoRow leftDiagonal = new BingoRow(this.items[0], this.items[6],
        this.items[12], this.items[18], this.items[24]);

    if (leftDiagonal.difficulty() < difficulty) {
      difficulty = leftDiagonal.difficulty();
      row = leftDiagonal;
    }

    final BingoRow rightDiagonal = new BingoRow(this.items[4], this.items[8],
        this.items[12], this.items[16], this.items[20]);

    if (rightDiagonal.difficulty() < difficulty) {
      row = rightDiagonal;
    }

    if (row == null) {
      return;
    }

    for (final BingoItem item : row.items()) {
      item.highlight();
    }
  }

  public boolean isFilled() {
    for (final BingoItem item : this.items) {
      if (item == null) {
        return false;
      }
    }

    return true;
  }

  public BingoItem[] getItems() {
    return this.items;
  }

  public BingoItem getItem(final String name) {
    for (final BingoItem item : this.items) {
      if (item == null) {
        continue;
      }

      if (item.getName().equals(name)) {
        return item;
      }
    }

    return null;
  }

}
