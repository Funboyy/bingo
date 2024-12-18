package de.funboyy.bingo.core.listener;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.funboyy.bingo.api.Bingo;
import de.funboyy.bingo.api.Scheduler;
import de.funboyy.bingo.api.enums.State;
import de.funboyy.bingo.api.model.BingoGame;
import de.funboyy.bingo.api.model.GoMod;
import de.funboyy.bingo.api.model.GoMod.Data;
import net.labymod.api.client.resources.ResourceLocation;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.chat.ChatMessageSendEvent;
import net.labymod.api.event.client.chat.ChatReceiveEvent;
import net.labymod.api.event.client.entity.player.inventory.InventorySetSlotEvent;
import net.labymod.api.event.client.network.server.NetworkPayloadEvent;
import net.labymod.api.event.client.network.server.NetworkPayloadEvent.Side;
import net.labymod.serverapi.api.payload.io.PayloadReader;

public class BingoListener {

  private static final Gson GSON = new GsonBuilder().create();

  private final Bingo<?> bingo;

  public BingoListener(final Bingo<?> bingo) {
    this.bingo = bingo;
  }

  @Subscribe
  public void onChatReceive(final ChatReceiveEvent event) {
    if (!this.bingo.isEnabled()) {
      return;
    }

    final BingoGame game = this.bingo.getGame();

    if (game.getState() == State.OFFLINE) {
      return;
    }

    game.handleMessage(event.chatMessage().getPlainText());
  }

  @Subscribe
  public void onChatSend(final ChatMessageSendEvent event) {
    if (!this.bingo.isEnabled()) {
      return;
    }

    final BingoGame game = this.bingo.getGame();

    if (game.getState() != State.PREPARATION && game.getState() != State.PLAYING) {
      return;
    }

    if (!event.getMessage().equalsIgnoreCase("/bingo")) {
      return;
    }

    if (game.getCard().isFilled()) {
      return;
    }

    Scheduler.getInstance().schedule(() -> this.bingo.getController().registerItems(), 500);
  }

  @Subscribe
  public void onInventoryUpdate(final InventorySetSlotEvent event) {
    if (!this.bingo.isEnabled()) {
      return;
    }

    final BingoGame game = this.bingo.getGame();

    if (game.getState() != State.PREPARATION) {
      return;
    }

    game.handleUpdateInventory();
  }

  @Subscribe
  public void onPayload(final NetworkPayloadEvent event) {
    if (!this.bingo.isEnabled()) {
      return;
    }

    if (event.side() != Side.RECEIVE) {
      return;
    }

    final ResourceLocation identifier = event.identifier();

    if (!identifier.equals(GoMod.IDENTIFIER)) {
      return;
    }

    final BingoGame game = this.bingo.getGame();

    if (game.getState() == State.OFFLINE) {
      return;
    }

    final PayloadReader reader = new PayloadReader(event.getPayload());
    final GoMod goMod = GSON.fromJson(reader.readString(), GoMod.class);

    if (!goMod.getAction().equals("JOIN_SERVER") || goMod.getData() == null) {
      return;
    }

    final Data data = goMod.getData();

    if (!data.getCloudType().equals("BINGO")) {
      if (game.getState() == State.ONLINE) {
        return;
      }

      final BingoGame newGame = new BingoGame(this.bingo);
      newGame.setState(State.ONLINE);

      this.bingo.setGame(newGame);
      return;
    }

    final BingoGame newGame = new BingoGame(this.bingo);
    newGame.setState(State.LOBBY);

    this.bingo.setGame(newGame);
  }

}
