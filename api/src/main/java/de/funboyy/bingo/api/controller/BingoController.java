package de.funboyy.bingo.api.controller;

import de.funboyy.bingo.api.Bingo;
import de.funboyy.bingo.api.enums.State;
import de.funboyy.bingo.api.model.BingoCard;
import de.funboyy.bingo.api.model.BingoGame;
import de.funboyy.bingo.api.model.BingoItem;
import java.util.List;
import net.labymod.api.client.world.item.ItemStack;
import net.labymod.api.reference.annotation.Referenceable;
import org.jetbrains.annotations.Nullable;

@Nullable
@Referenceable
public interface BingoController {

  void registerItems();

  default void handleItems(final BingoGame game, final String title, final List<?> items) {
    if (!title.matches("^Bingokarte von .+$")) {
      return;
    }

    final BingoCard card = game.getCard();

    if (game.getState() != State.PREPARATION) {
      return;
    }

    int index = 0;
    for (int slot = 0; slot < items.size(); slot++) {
      if (slot % 9 < 2 || slot % 9 > 6 || slot > 45) {
        continue;
      }

      if (!(items.get(slot) instanceof ItemStack item)) {
        continue;
      }

      card.getItems()[index] = new BingoItem(item);
      index++;
    }

    if (card.isFilled()) {
      Bingo.get().getWebAPI().displayWarning();
      card.highlight();
    }
  }

}
