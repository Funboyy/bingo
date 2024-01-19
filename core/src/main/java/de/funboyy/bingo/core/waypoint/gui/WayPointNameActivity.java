package de.funboyy.bingo.core.waypoint.gui;

import java.util.function.Consumer;
import net.labymod.api.client.gui.screen.Parent;
import net.labymod.api.client.gui.screen.activity.Activity;
import net.labymod.api.client.gui.screen.activity.AutoActivity;
import net.labymod.api.client.gui.screen.activity.Link;
import net.labymod.api.client.gui.screen.key.InputType;
import net.labymod.api.client.gui.screen.key.Key;
import net.labymod.api.client.gui.screen.key.KeyHandler;
import net.labymod.api.client.gui.screen.widget.Widget;
import net.labymod.api.client.gui.screen.widget.widgets.ComponentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.DivWidget;
import net.labymod.api.client.gui.screen.widget.widgets.input.ButtonWidget;
import net.labymod.api.client.gui.screen.widget.widgets.input.TextFieldWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.list.VerticalListWidget;

@AutoActivity
@Link("waypoint-name.lss")
public class WayPointNameActivity extends Activity {

  private String value;
  private final Consumer<String> nameConsumer;

  public WayPointNameActivity(final Consumer<String> nameConsumer) {
    this.nameConsumer = nameConsumer;
    this.value = "";
  }

  @Override
  public void initialize(final Parent parent) {
    super.initialize(parent);

    final DivWidget wrapper = new DivWidget();
    wrapper.addId("wrapper");

    final VerticalListWidget<Widget> container = new VerticalListWidget<>();
    container.addId("container");

    final ComponentWidget title = ComponentWidget.i18n("bingo.activity.wayPoint.name");
    title.addId("title");
    container.addChild(title);

    final TextFieldWidget textField = new TextFieldWidget();
    textField.addId("input");
    textField.setFocused(true);
    textField.setText(this.value, true);
    textField.updateListener(text -> this.value = text.trim());
    textField.maximalLength(32);
    container.addChild(textField);

    final DivWidget buttonWrapper = new DivWidget();
    buttonWrapper.addId("buttonWrapper");

    final ButtonWidget button = ButtonWidget.i18n("labymod.ui.button.done");
    button.addId("button");
    button.setPressable(() -> {
      if (!this.value.isEmpty()) {
        this.nameConsumer.accept(this.value);
      }

      super.displayPreviousScreen();
    });

    buttonWrapper.addChild(button);
    container.addChild(buttonWrapper);
    wrapper.addChild(container);
    this.document.addChild(wrapper);
  }

  @Override
  public boolean keyPressed(final Key key, final InputType type) {
    if (KeyHandler.isEnter(key)) {
      if (!this.value.isEmpty()) {
        this.nameConsumer.accept(this.value);
      }

      return super.displayPreviousScreen();
    }

    if (key == Key.ESCAPE) {
      return super.displayPreviousScreen();
    }

    return super.keyPressed(key, type);
  }

}
