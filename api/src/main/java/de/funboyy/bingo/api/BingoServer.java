package de.funboyy.bingo.api;

import de.funboyy.bingo.api.enums.State;
import de.funboyy.bingo.api.model.BingoGame;
import net.labymod.api.client.network.server.AbstractServer;
import net.labymod.api.event.Phase;

public class BingoServer extends AbstractServer {

  private final Bingo<?> bingo;

  public BingoServer(final Bingo<?> bingo) {
    super("gommehd");
    this.bingo = bingo;
  }

  @Override
  public void loginOrSwitch(final LoginPhase phase) {
    final BingoGame game = this.bingo.getGame();

    if (phase == LoginPhase.LOGIN) {
      game.setState(State.ONLINE);
      return;
    }

    if (game.getState() == State.ONLINE) {
      return;
    }

    // the payload is received before this is triggered,
    // so we need to check if it was recently or not
    if (game.getState() == State.LOBBY && !game.shouldUpdateState()) {
      return;
    }

    final BingoGame newGame = new BingoGame(this.bingo);
    newGame.setState(State.ONLINE);

    this.bingo.setGame(newGame);
  }

  @Override
  public void disconnect(final Phase phase) {
    this.bingo.setGame(new BingoGame(this.bingo));
  }

}
