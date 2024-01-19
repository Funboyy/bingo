package de.funboyy.bingo.api.enums;

import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.resources.ResourceLocation;

public enum Textures {

  BINGO_CARD_ICON("bingo-card/icon.png"),
  BINGO_CARD_BACKGROUND("bingo-card/background.png"),
  WAY_POINT_ICON("waypoint/icon.png");

  private final Icon icon;

  Textures(final String path) {
    this.icon = Icon.texture(ResourceLocation.create("bingo", "textures/" + path))
        .resolution(128, 128);
  }

  public Icon getIcon() {
    return this.icon;
  }

}
