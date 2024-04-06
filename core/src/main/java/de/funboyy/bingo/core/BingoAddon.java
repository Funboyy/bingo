package de.funboyy.bingo.core;

import de.funboyy.bingo.api.Bingo;
import de.funboyy.bingo.api.BingoServer;
import de.funboyy.bingo.api.controller.BingoController;
import de.funboyy.bingo.api.model.BingoGame;
import de.funboyy.bingo.api.waypoint.WayPointService;
import de.funboyy.bingo.api.web.BingoWebAPI;
import de.funboyy.bingo.core.generated.DefaultReferenceStorage;
import de.funboyy.bingo.core.listener.BingoListener;
import de.funboyy.bingo.core.waypoint.DefaultWayPointService;
import de.funboyy.bingo.core.widget.BingoWidget;
import net.labymod.api.client.gui.screen.key.Key;
import net.labymod.api.models.addon.annotation.AddonMain;
import net.labymod.api.util.Color;

@AddonMain
public class BingoAddon extends Bingo<BingoConfiguration> {

  private BingoGame game;
  private BingoWebAPI webAPI;
  private WayPointService wayPointService;

  public BingoAddon() {
    instance = this;
  }

  @Override
  protected void enable() {
    this.webAPI = new BingoWebAPI();
    this.wayPointService = new DefaultWayPointService(this);

    this.webAPI.load();

    this.setGame(new BingoGame(this));
    this.registerSettingCategory();
    this.registerListener(this.wayPointService);
    this.registerListener(new BingoListener(this));
    this.labyAPI().hudWidgetRegistry().register(new BingoWidget(this));
    this.labyAPI().serverController().registerServer(new BingoServer(this));
  }

  @Override
  protected Class<BingoConfiguration> configurationClass() {
    return BingoConfiguration.class;
  }

  @Override
  public boolean isEnabled() {
    return this.configuration().enabled().get();
  }

  @Override
  public boolean autoGG() {
    return this.isEnabled() && this.configuration().autoGG().get();
  }

  @Override
  public boolean highlight() {
    return this.isEnabled() && this.configuration().highlight().get();
  }

  @Override
  public boolean wayPoints() {
    return this.isEnabled() && this.configuration().wayPoints().get();
  }

  @Override
  public boolean showOthers() {
    return this.isEnabled() && this.configuration().showOthers().get();
  }

  @Override
  public Color color() {
    return this.configuration().color().get();
  }

  @Override
  public Key createKey() {
    return this.configuration().createKey().get();
  }

  @Override
  public Key manageKey() {
    return this.configuration().manageKey().get();
  }

  @Override
  public Key lastDeathKey() {
    return this.configuration().lastDeathKey().get();
  }

  @Override
  public WayPointService getWayPointService() {
    return this.wayPointService;
  }

  @Override
  public BingoWebAPI getWebAPI() {
    return this.webAPI;
  }

  @Override
  public BingoController getController() {
    final DefaultReferenceStorage referenceStorage = this.referenceStorageAccessor();
    return referenceStorage.getBingoController();
  }

  @Override
  public BingoGame getGame() {
    return this.game;
  }

  @Override
  public void setGame(final BingoGame game) {
    this.wayPointService.removeWayPoints();
    this.game = game;
  }

}
