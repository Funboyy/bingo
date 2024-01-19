package de.funboyy.bingo.core;

import de.funboyy.bingo.api.Bingo;
import de.funboyy.bingo.api.enums.Textures;
import net.labymod.api.Laby;
import net.labymod.api.addon.AddonConfig;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.TextDecoration;
import net.labymod.api.client.gui.screen.key.Key;
import net.labymod.api.client.gui.screen.widget.widgets.input.ButtonWidget.ButtonSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.KeybindWidget.KeyBindSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.color.ColorPickerWidget.ColorPickerSetting;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import net.labymod.api.configuration.settings.Setting;
import net.labymod.api.configuration.settings.annotation.SettingSection;
import net.labymod.api.notification.Notification;
import net.labymod.api.util.Color;
import net.labymod.api.util.MethodOrder;

public class BingoConfiguration extends AddonConfig {

  @SwitchSetting
  private final ConfigProperty<Boolean> enabled = new ConfigProperty<>(true);

  @SettingSection("features")
  @SwitchSetting
  private final ConfigProperty<Boolean> highlight = new ConfigProperty<>(true);

  @SwitchSetting
  private final ConfigProperty<Boolean> autoGG = new ConfigProperty<>(true);

  @SettingSection("wayPoints")
  @SwitchSetting
  private final ConfigProperty<Boolean> wayPoints = new ConfigProperty<>(true);

  @SwitchSetting
  private final ConfigProperty<Boolean> showOthers = new ConfigProperty<>(true);

  @ColorPickerSetting
  private final ConfigProperty<Color> color = new ConfigProperty<>(Color.RED);

  @KeyBindSetting
  private final ConfigProperty<Key> createKey = new ConfigProperty<>(Key.NONE);

  @KeyBindSetting
  private final ConfigProperty<Key> manageKey = new ConfigProperty<>(Key.NONE);

  @MethodOrder(after = "autoGG")
  @ButtonSetting
  public void refreshButton(final Setting setting) {
    Bingo.get().getWebAPI().load();

    final Notification notification = Notification.builder()
        .icon(Textures.BINGO_CARD_ICON.getIcon())
        .title(Component.text("Bingo").decorate(TextDecoration.BOLD))
        .text(Component.translatable("bingo.settings.refreshButton.notification"))
        .build();

    Laby.labyAPI().notificationController().push(notification);
  }

  @Override
  public ConfigProperty<Boolean> enabled() {
    return this.enabled;
  }

  public ConfigProperty<Boolean> highlight() {
    return this.highlight;
  }

  public ConfigProperty<Boolean> autoGG() {
    return this.autoGG;
  }

  public ConfigProperty<Boolean> wayPoints() {
    return this.wayPoints;
  }

  public ConfigProperty<Boolean> showOthers() {
    return this.showOthers;
  }

  public ConfigProperty<Color> color() {
    return this.color;
  }

  public ConfigProperty<Key> createKey() {
    return this.createKey;
  }

  public ConfigProperty<Key> manageKey() {
    return this.manageKey;
  }

}
