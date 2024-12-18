package de.funboyy.bingo.v1_21_3;

import de.funboyy.bingo.api.Bingo;
import de.funboyy.bingo.api.BingoFlattener;
import de.funboyy.bingo.api.controller.BingoController;
import javax.inject.Singleton;
import net.labymod.api.models.Implements;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

@Singleton
@Implements(BingoController.class)
public class VersionedBingoController implements BingoController {

  @Override
  public void registerItems() {
    final LocalPlayer player = Minecraft.getInstance().player;
    final Screen screen = Minecraft.getInstance().screen;

    if (player == null || screen == null) {
      return;
    }

    final AbstractContainerMenu containerMenu = player.containerMenu;

    if (containerMenu.getType() != MenuType.GENERIC_9x5) {
      return;
    }

    if (!(screen instanceof ContainerScreen)) {
      return;
    }

    this.handleItems(
        Bingo.get().getGame(),
        BingoFlattener.getPlainText(screen.getTitle()),
        containerMenu.getItems()
    );
  }

}
