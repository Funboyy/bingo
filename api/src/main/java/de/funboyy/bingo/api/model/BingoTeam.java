package de.funboyy.bingo.api.model;

import java.util.Collection;
import java.util.HashSet;

public class BingoTeam {

  private final BingoIdentifier identifier;
  private final Collection<String> entries;

  public BingoTeam(final BingoIdentifier identifier) {
    this.identifier = identifier;
    this.entries = new HashSet<>();
  }

  public BingoIdentifier getIdentifier() {
    return this.identifier;
  }

  public Collection<String> getEntries() {
    return this.entries;
  }

  public boolean hasEntry(final String name) {
    return this.entries.contains(name);
  }

}
