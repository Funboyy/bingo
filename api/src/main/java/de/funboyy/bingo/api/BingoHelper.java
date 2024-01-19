package de.funboyy.bingo.api;

import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.flattener.ComponentFlattener;
import net.labymod.api.util.Color;

public class BingoHelper {

  public static final Color RED = Color.ofRGB(215, 0, 0);
  public static final Color BLUE = Color.ofRGB(64, 64, 255);
  public static final Color YELLOW = Color.ofRGB(225, 225, 50);
  public static final Color GREEN = Color.ofRGB(0, 215, 50);
  public static final Color BEST = Color.ofRGB(200, 250, 100);

  public static String getPlainText(final Object object) {
    final Component component;

    if (object instanceof Component) {
      component = (Component) object;
    } else {
      component = Laby.labyAPI().minecraft().componentMapper().fromMinecraftComponent(object);
    }

    final ComponentFlattener flattener = Laby.references().componentRenderer().getColorStrippingFlattener();
    final StringBuilder builder = new StringBuilder();
    flattener.flatten(component, consumer -> {}, builder::append);

    return builder.toString();
  }

}
