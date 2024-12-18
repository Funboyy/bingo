package de.funboyy.bingo.core.widget;

import de.funboyy.bingo.api.Bingo;
import de.funboyy.bingo.api.enums.Textures;
import de.funboyy.bingo.api.model.BingoCard;
import de.funboyy.bingo.api.model.BingoGame;
import de.funboyy.bingo.api.model.BingoIdentifier;
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
  private static final int BEST_COLOR = Color.ofRGB(200, 250, 100).get();

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
    final int minSize = this.bingo.getGame().getMinSize();

    int x = PADDING;
    int y = PADDING;

    for (final BingoItem item : card.getItems()) {
      if (item == null || item.getItemStack() == null || item.getItemStack().isAir()) {
        return;
      }

      if (this.bingo.highlight() && item.isHighlighted()) {
        RECTANGLE_RENDERER.renderRectangle(stack,
            Rectangle.relative(x - 1, y - 1, 18, 18), BEST_COLOR);
      }

      ITEM_RENDERER.renderItemStack(stack, item.getItemStack(), x, y, false);

      for (final BingoTeam team : item.getTeams()) {
        final BingoIdentifier identifier = team.getIdentifier();
        final int color = identifier.getColor();

        if (identifier.getMinSize() > minSize) {
          continue;
        }

        // Team Red - 2/3/4/8 Teams
        if (identifier == BingoIdentifier.RED) {
          if (minSize == 2) {
            RECTANGLE_RENDERER.renderRectangle(stack, Rectangle.relative(x - 3,
                y - 3, 22, 2), color);
            RECTANGLE_RENDERER.renderRectangle(stack, Rectangle.relative(x - 3,
                y - 3, 2, 9), color);
            RECTANGLE_RENDERER.renderRectangle(stack, Rectangle.relative(x + 17,
                y - 3, 2, 9), color);
            continue;
          }

          if (minSize == 3 || minSize == 4) {
            RECTANGLE_RENDERER.renderRectangle(stack, Rectangle.relative(x - 3,
                y - 3, 9, 2), color);
            RECTANGLE_RENDERER.renderRectangle(stack, Rectangle.relative(x - 3,
                y - 3, 2, 9), color);
            continue;
          }

          if (minSize == 8) {
            RECTANGLE_RENDERER.renderRectangle(stack, Rectangle.relative(x - 3,
                y - 3, 5, 2), color);
            RECTANGLE_RENDERER.renderRectangle(stack, Rectangle.relative(x - 3,
                y - 3, 2, 5), color);
            continue;
          }

          continue;
        }

        // Team Blue - 2/3/4/8 Teams
        if (identifier == BingoIdentifier.BLUE) {
          if (minSize == 2) {
            RECTANGLE_RENDERER.renderRectangle(stack, Rectangle.relative(x - 3,
                y + 17, 22, 2), color);
            RECTANGLE_RENDERER.renderRectangle(stack, Rectangle.relative(x - 3,
                y + 10, 2, 9), color);
            RECTANGLE_RENDERER.renderRectangle(stack, Rectangle.relative(x + 17,
                y + 10, 2, 9), color);
            continue;
          }

          if (minSize == 3 || minSize == 4) {
            RECTANGLE_RENDERER.renderRectangle(stack, Rectangle.relative(x + 10,
                y - 3, 9, 2), color);
            RECTANGLE_RENDERER.renderRectangle(stack, Rectangle.relative(x + 17,
                y - 3, 2, 9), color);
            continue;
          }

          if (minSize == 8) {
            RECTANGLE_RENDERER.renderRectangle(stack, Rectangle.relative(x + 4,
                y - 3, 8, 2), color);
            continue;
          }

          continue;
        }

        // Team Yellow - 3/4/8 Teams
        if (identifier == BingoIdentifier.YELLOW) {
          if (minSize == 3) {
            RECTANGLE_RENDERER.renderRectangle(stack, Rectangle.relative(x - 3,
                y + 17, 22, 2), color);
            RECTANGLE_RENDERER.renderRectangle(stack, Rectangle.relative(x - 3,
                y + 10, 2, 9), color);
            RECTANGLE_RENDERER.renderRectangle(stack, Rectangle.relative(x + 17,
                y + 10, 2, 9), color);
            continue;
          }

          if (minSize == 4) {
            RECTANGLE_RENDERER.renderRectangle(stack, Rectangle.relative(x - 3,
                y + 17, 9, 2), color);
            RECTANGLE_RENDERER.renderRectangle(stack, Rectangle.relative(x - 3,
                y + 10, 2, 9), color);
            continue;
          }

          if (minSize == 8) {
            RECTANGLE_RENDERER.renderRectangle(stack, Rectangle.relative(x + 14,
                y - 3, 5, 2), color);
            RECTANGLE_RENDERER.renderRectangle(stack, Rectangle.relative(x + 17,
                y - 3, 2, 5), color);
            continue;
          }

          continue;
        }

        // Team Green - 4/8 Teams
        if (identifier == BingoIdentifier.GREEN) {
          if (minSize == 4) {
            RECTANGLE_RENDERER.renderRectangle(stack, Rectangle.relative(x + 10,
                y + 17, 9, 2), color);
            RECTANGLE_RENDERER.renderRectangle(stack, Rectangle.relative(x + 17,
                y + 10, 2, 9), color);
            continue;
          }

          if (minSize == 8) {
            RECTANGLE_RENDERER.renderRectangle(stack, Rectangle.relative(x + 17,
                y + 4, 2, 8), color);
            continue;
          }

          continue;
        }

        // Team Pink - 8 Teams
        if (identifier == BingoIdentifier.PINK) {
          if (minSize == 8) {
            RECTANGLE_RENDERER.renderRectangle(stack, Rectangle.relative(x + 14,
                y + 17, 5, 2), color);
            RECTANGLE_RENDERER.renderRectangle(stack, Rectangle.relative(x + 17,
                y + 14, 2, 5), color);
            continue;
          }

          continue;
        }

        // Team Light Blue - 8 Teams
        if (identifier == BingoIdentifier.LIGHT_BLUE) {
          if (minSize == 8) {
            RECTANGLE_RENDERER.renderRectangle(stack, Rectangle.relative(x + 4,
                y + 17, 8, 2), color);
            continue;
          }

          continue;
        }

        // Team Orange - 8 Teams
        if (identifier == BingoIdentifier.ORANGE) {
          if (minSize == 8) {
            RECTANGLE_RENDERER.renderRectangle(stack, Rectangle.relative(x - 3,
                y + 17, 5, 2), color);
            RECTANGLE_RENDERER.renderRectangle(stack, Rectangle.relative(x - 3,
                y + 14, 2, 5), color);
            continue;
          }

          continue;
        }

        // Team Purple - 8 Teams
        if (identifier == BingoIdentifier.PURPLE) {
          if (minSize == 8) {
            RECTANGLE_RENDERER.renderRectangle(stack, Rectangle.relative(x - 3,
                y + 4, 2, 8), color);
          }
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
