package de.funboyy.bingo.api.model;

import net.labymod.api.util.Color;

public enum BingoIdentifier {

  RED("Rot", Color.ofRGB(233, 1, 1).get(), 2), // #E90101
  BLUE("Blau", Color.ofRGB(56, 56, 255).get(), 2), // #3838FF
  YELLOW("Gelb", Color.ofRGB(255, 215, 0).get(), 3), // #FFD700
  GREEN("Grün", Color.ofRGB(25, 220, 15).get(), 4), // #19DC0F
  PINK("Rosa", Color.ofRGB(255, 105, 180).get(), 8), // #FF69B4
  LIGHT_BLUE("Hellblau", Color.ofRGB(0, 191, 255).get(), 8), // #00BFFF
  ORANGE("Orange", Color.ofRGB(255, 91, 3).get(), 8), // #FF5B03
  PURPLE("Violett", Color.ofRGB(116, 1, 223).get(), 8); // #7401DF

  private final String name;
  private final int color;
  private final int minSize;

  BingoIdentifier(final String name, final int color, final int minSize) {
    this.name = name;
    this.color = color;
    this.minSize = minSize;
  }

  public String getName() {
    return this.name;
  }

  public int getColor() {
    return this.color;
  }

  public int getMinSize() {
    return this.minSize;
  }

}
