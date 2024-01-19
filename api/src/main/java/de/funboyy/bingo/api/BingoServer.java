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
    if (phase == LoginPhase.LOGIN) {
      this.bingo.getGame().setState(State.ONLINE);
    }
  }

  @Override
  public void disconnect(final Phase phase) {
    this.bingo.setGame(new BingoGame(this.bingo));
  }

}
