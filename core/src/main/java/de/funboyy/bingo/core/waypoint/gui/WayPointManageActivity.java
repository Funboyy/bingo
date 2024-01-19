package de.funboyy.bingo.core.waypoint.gui;

import de.funboyy.bingo.api.waypoint.WayPoint;
import java.util.Set;
import net.labymod.api.client.gui.screen.Parent;
import net.labymod.api.client.gui.screen.activity.Activity;
import net.labymod.api.client.gui.screen.activity.AutoActivity;
import net.labymod.api.client.gui.screen.activity.Link;
import net.labymod.api.client.gui.screen.key.InputType;
import net.labymod.api.client.gui.screen.key.Key;
import net.labymod.api.client.gui.screen.widget.widgets.ComponentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.DivWidget;
import net.labymod.api.client.gui.screen.widget.widgets.input.ButtonWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.FlexibleContentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.ScrollWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.list.VerticalListWidget;

@AutoActivity
@Link("waypoint-manage.lss")
public class WayPointManageActivity extends Activity {

  private final Set<WayPoint> wayPoints;

  public WayPointManageActivity(final Set<WayPoint> wayPoints) {
    this.wayPoints = wayPoints;
  }

  @Override
  public void initialize(final Parent parent) {
    super.initialize(parent);

    final DivWidget wrapper = new DivWidget();
    wrapper.addId("wrapper");

    final FlexibleContentWidget container = new FlexibleContentWidget();
    container.addId("container");

    final ComponentWidget title = ComponentWidget.i18n("bingo.activity.wayPoint.manage");
    title.addId("title");
    container.addContent(title);

    final VerticalListWidget<WayPointWidget> wayPointList = new VerticalListWidget<>();
    wayPointList.addId("list");

    for (final WayPoint wayPoint : this.wayPoints) {
      wayPointList.addChild(new WayPointWidget(wayPoint, wayPointList));
    }

    container.addFlexibleContent(new ScrollWidget(wayPointList));

    final DivWidget buttonWrapper = new DivWidget();
    buttonWrapper.addId("buttonWrapper");

    final ButtonWidget button = ButtonWidget.i18n("labymod.ui.button.done");
    button.addId("button");
    button.setPressable(super::displayPreviousScreen);

    buttonWrapper.addChild(button);
    container.addContent(buttonWrapper);
    wrapper.addChild(container);
    this.document.addChild(wrapper);
  }

  @Override
  public boolean keyPressed(final Key key, final InputType type) {
    if (key == Key.ESCAPE) {
      return super.displayPreviousScreen();
    }

    return super.keyPressed(key, type);
  }

}
