package de.funboyy.bingo.core.waypoint.gui;

import de.funboyy.bingo.api.Bingo;
import de.funboyy.bingo.api.enums.Textures;
import de.funboyy.bingo.api.waypoint.WayPoint;
import net.labymod.api.client.gui.lss.property.annotation.AutoWidget;
import net.labymod.api.client.gui.screen.Parent;
import net.labymod.api.client.gui.screen.widget.SimpleWidget;
import net.labymod.api.client.gui.screen.widget.widgets.ComponentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.input.ButtonWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.list.VerticalListWidget;
import net.labymod.api.client.gui.screen.widget.widgets.renderer.IconWidget;

@AutoWidget
public class WayPointWidget extends SimpleWidget {

  private final WayPoint wayPoint;
  private final VerticalListWidget<WayPointWidget> list;

  public WayPointWidget(final WayPoint wayPoint, final VerticalListWidget<WayPointWidget> list) {
    this.wayPoint = wayPoint;
    this.list = list;
  }

  @Override
  public void initialize(final Parent parent) {
    super.initialize(parent);

    final IconWidget iconWidget = new IconWidget(Textures.WAY_POINT_ICON.getIcon());
    iconWidget.addId("icon");
    iconWidget.color().set(this.wayPoint.color());
    this.addChild(iconWidget);

    final ComponentWidget nameWidget = ComponentWidget.component(this.wayPoint.text());
    nameWidget.addId("name");
    this.addChild(nameWidget);

    final ButtonWidget buttonWidget = ButtonWidget.i18n("labymod.ui.button.remove");
    buttonWidget.addId("button");
    buttonWidget.setPressable(() -> {
      Bingo.get().getWayPointService().removeWayPoint(this.wayPoint);
      this.list.removeChild(this);
    });
    this.addChild(buttonWidget);
  }


}
