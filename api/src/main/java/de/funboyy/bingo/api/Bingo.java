package de.funboyy.bingo.api;

import de.funboyy.bingo.api.controller.BingoController;
import de.funboyy.bingo.api.model.BingoGame;
import de.funboyy.bingo.api.waypoint.WayPointService;
import de.funboyy.bingo.api.web.BingoWebAPI;
import net.labymod.api.addon.AddonConfig;
import net.labymod.api.addon.LabyAddon;
import net.labymod.api.client.gui.screen.key.Key;
import net.labymod.api.util.Color;

public abstract class Bingo<T extends AddonConfig> extends LabyAddon<T> {

  protected static Bingo<?> instance;

  public static Bingo<?> get() {
    return instance;
  }

  public abstract boolean isEnabled();

  public abstract boolean autoGG();

  public abstract boolean highlight();

  public abstract boolean wayPoints();

  public abstract boolean showOthers();

  public abstract Color color();

  public abstract Key createKey();

  public abstract Key manageKey();

  public abstract Key lastDeathKey();

  public abstract WayPointService getWayPointService();

  public abstract BingoWebAPI getWebAPI();

  public abstract BingoController getController();

  public abstract BingoGame getGame();

  public abstract void setGame(final BingoGame game);

}
