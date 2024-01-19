package de.funboyy.bingo.api.web;

import java.util.Map;

public class BingoResponse {

  private final Map<String, Integer> items;

  public BingoResponse(final Map<String, Integer> items) {
    this.items = items;
  }

  public Map<String, Integer> getItems() {
    return this.items;
  }

}
