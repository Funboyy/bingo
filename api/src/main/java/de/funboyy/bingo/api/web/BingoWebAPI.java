package de.funboyy.bingo.api.web;

import de.funboyy.bingo.api.Bingo;
import de.funboyy.bingo.api.enums.Difficulty;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import net.labymod.api.util.io.web.request.Request;
import net.labymod.api.util.io.web.request.WebResolver;

public class BingoWebAPI {

  private final Request<BingoResponse> request = Request.ofGson(BingoResponse.class)
      .url("https://funboyy.de/bingo/api/items.json");

  private final Map<String, Difficulty> missingItems = new HashMap<>();
  private BingoResponse response;

  public void load() {
    WebResolver.resolve(this.request, response -> this.response = response.getNullable());
  }

  public int getDifficulty(final String identifier, final Difficulty difficulty) {
    if (this.response == null || this.response.getItems() == null) {
      return -1;
    }

    if (!this.response.getItems().containsKey(identifier)) {
      this.missingItems.put(identifier, difficulty);
      return -1;
    }

    return this.response.getItems().get(identifier);
  }

  public void displayWarning() {
    if (this.missingItems.isEmpty()) {
      return;
    }

    final StringBuilder builder = new StringBuilder("Missing custom difficulties for ");

    for (final Entry<String, Difficulty> entry : this.missingItems.entrySet()) {
      builder.append(String.format("%s (%s), ", entry.getKey(), entry.getValue()));
    }

    builder.setLength(builder.length() - 2);

    Bingo.get().logger().warn(builder.toString());

    this.missingItems.clear();
  }

}
