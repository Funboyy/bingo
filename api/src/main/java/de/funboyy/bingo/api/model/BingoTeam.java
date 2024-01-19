package de.funboyy.bingo.api.model;

import java.util.Collection;
import java.util.HashSet;
import net.labymod.api.util.Color;

public class BingoTeam {

  private final String name;
  private final Color color;
  private final Collection<String> entries;

  public BingoTeam(final String name, final Color color) {
    this.name = name;
    this.color = color;
    this.entries = new HashSet<>();
  }

  public String getName() {
    return this.name;
  }

  public Color getColor() {
    return this.color;
  }

  public Collection<String> getEntries() {
    return this.entries;
  }

  public boolean hasEntry(final String name) {
    return this.entries.contains(name);
  }

}
