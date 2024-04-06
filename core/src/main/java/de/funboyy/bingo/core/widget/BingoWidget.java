package de.funboyy.bingo.core.widget;

import de.funboyy.bingo.api.Bingo;
import de.funboyy.bingo.api.BingoHelper;
import de.funboyy.bingo.api.enums.Textures;
import de.funboyy.bingo.api.model.BingoCard;
import de.funboyy.bingo.api.model.BingoGame;
import de.funboyy.bingo.api.model.BingoItem;
import de.funboyy.bingo.api.model.BingoTeam;
import net.labymod.api.Laby;
import net.labymod.api.client.gui.hud.hudwidget.HudWidgetConfig;
import net.labymod.api.client.gui.hud.hudwidget.widget.WidgetHudWidget;
import net.labymod.api.client.gui.hud.position.HudSize;
import net.labymod.api.client.gui.mouse.MutableMouse;
import net.labymod.api.client.gui.screen.activity.Link;
import net.labymod.api.client.gui.screen.widget.widgets.DivWidget;
import net.labymod.api.client.gui.screen.widget.widgets.hud.HudWidgetWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.FlexibleContentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.renderer.IconWidget;
import net.labymod.api.client.render.ItemStackRenderer;
import net.labymod.api.client.render.draw.RectangleRenderer;
import net.labymod.api.client.render.matrix.Stack;
import net.labymod.api.util.Color;
import net.labymod.api.util.bounds.Rectangle;

@Link("bingo.lss")
public class BingoWidget extends WidgetHudWidget<HudWidgetConfig> {

  private static final int PADDING = 10;
  private static final int SPACING = 23;
  private static final int MAX_SIZE = 128;
  private static final RectangleRenderer RECTANGLE_RENDERER = Laby.references()
      .renderPipeline().rectangleRenderer();
  private static final ItemStackRenderer ITEM_RENDERER = Laby.labyAPI()
      .minecraft().itemStackRenderer();

  private final Bingo<?> bingo;

  public BingoWidget(final Bingo<?> bingo) {
    super("bingoCard", HudWidgetConfig.class);
    this.bingo = bingo;
  }

  @Override
  public void load(final HudWidgetConfig config) {
    super.load(config);
    super.setIcon(Textures.ICON.getIcon());
  }

  @Override
  public void initialize(final HudWidgetWidget widget) {
    super.initialize(widget);

    final FlexibleContentWidget content = new FlexibleContentWidget();
    final DivWidget div = new DivWidget();
    final IconWidget icon = new IconWidget(Textures.BINGO_CARD_BACKGROUND.getIcon());
    icon.setCleanupOnDispose(true);
    div.addChild(icon);
    content.addContent(div);
    widget.addChild(content);
  }

  @Override
  public void render(final Stack stack, final MutableMouse mouse, final float partialTicks,
      final boolean isEditorContext, final HudSize size) {

    final BingoCard card = this.bingo.getGame().getCard();

    int x = PADDING;
    int y = PADDING;

    for (final BingoItem item : card.getItems()) {
      if (item == null || item.getItemStack() == null || item.getItemStack().isAir()) {
        return;
      }

      if (this.bingo.highlight() && item.isHighlighted()) {
        RECTANGLE_RENDERER.renderRectangle(stack,
            Rectangle.relative(x - 1, y - 1, 18, 18), BingoHelper.BEST.get());
      }

      ITEM_RENDERER.renderItemStack(stack, item.getItemStack(), x, y, false);

      for (final BingoTeam team : item.getTeams()) {
        final Color color = team.getColor();

        if (color == null) {
          continue;
        }

        if (color.equals(BingoHelper.RED)) {
          RECTANGLE_RENDERER.renderRectangle(stack, Rectangle.relative(x - 3,
              y - 3, 9, 2), color.get());
          RECTANGLE_RENDERER.renderRectangle(stack, Rectangle.relative(x - 3,
              y - 3, 2, 9), color.get());
          continue;
        }

        if (color.equals(BingoHelper.BLUE)) {
          RECTANGLE_RENDERER.renderRectangle(stack, Rectangle.relative(x + 10,
              y - 3, 9, 2), color.get());
          RECTANGLE_RENDERER.renderRectangle(stack, Rectangle.relative(x + 17,
              y - 3, 2, 9), color.get());
          continue;
        }

        if (color.equals(BingoHelper.GREEN)) {
          RECTANGLE_RENDERER.renderRectangle(stack, Rectangle.relative(x + 10,
              y + 17, 9, 2), color.get());
          RECTANGLE_RENDERER.renderRectangle(stack, Rectangle.relative(x + 17,
              y + 10, 2, 9), color.get());
          continue;
        }

        if (color.equals(BingoHelper.YELLOW)) {
          RECTANGLE_RENDERER.renderRectangle(stack, Rectangle.relative(x - 3,
              y + 17, 9, 2), color.get());
          RECTANGLE_RENDERER.renderRectangle(stack, Rectangle.relative(x - 3,
              y + 10, 2, 9), color.get());
        }
      }

      x += SPACING;

      if (x > MAX_SIZE - PADDING) {
        x = PADDING;
        y += SPACING;
      }

      if (y > MAX_SIZE - PADDING) {
        break;
      }
    }
  }

  @Override
  public boolean isVisibleInGame() {
    final BingoGame game = this.bingo.getGame();

    return this.bingo.isEnabled() && game.getState().isEnabled() && game.getCard().isFilled();
  }

}
