package de.funboyy.bingo.api;

import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.flattener.ComponentFlattener;

public class BingoFlattener {

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
