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
import net.labymod.api.client.gui.screen.ScreenContext;
import net.labymod.api.client.gui.screen.activity.Link;
import net.labymod.api.client.gui.screen.state.ScreenCanvas;
import net.labymod.api.client.gui.screen.widget.widgets.DivWidget;
import net.labymod.api.client.gui.screen.widget.widgets.hud.HudWidgetWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.FlexibleContentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.renderer.IconWidget;
import net.labymod.api.client.render.ItemStackRenderer;
import net.labymod.api.client.render.matrix.Stack;
import net.labymod.api.util.Color;

@Link("bingo.lss")
public class BingoWidget extends WidgetHudWidget<HudWidgetConfig> {

  private static final int PADDING = 10;
  private static final int SPACING = 23;
  private static final int MAX_SIZE = 128;
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
  public void render(final ScreenContext context, final boolean isEditorContext, final HudSize size) {
    final Stack stack = context.stack();
    final ScreenCanvas canvas = context.canvas();

    final BingoCard card = this.bingo.getGame().getCard();
    final int minSize = this.bingo.getGame().getMinSize();

    int x = PADDING;
    int y = PADDING;

    for (final BingoItem item : card.getItems()) {
      if (item == null || item.getItemStack() == null || item.getItemStack().isAir()) {
        return;
      }

      if (this.bingo.highlight() && item.isHighlighted()) {
        canvas.submitRelativeRect(x - 1F, y - 1F, 18F, 18F, BEST_COLOR);
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
            canvas.submitRelativeRect(x - 3F, y - 3F, 22F, 2F, color);
            canvas.submitRelativeRect(x - 3F, y - 3F, 2F, 9F, color);
            canvas.submitRelativeRect(x + 17F, y - 3F, 2F, 9F, color);
            continue;
          }

          if (minSize == 3 || minSize == 4) {
            canvas.submitRelativeRect(x - 3F, y - 3F, 9F, 2F, color);
            canvas.submitRelativeRect(x - 3F, y - 3F, 2F, 9F, color);
            continue;
          }

          if (minSize == 8) {
            canvas.submitRelativeRect(x - 3F, y - 3F, 5F, 2F, color);
            canvas.submitRelativeRect(x - 3F, y - 3F, 2F, 5F, color);
            continue;
          }

          continue;
        }

        // Team Blue - 2/3/4/8 Teams
        if (identifier == BingoIdentifier.BLUE) {
          if (minSize == 2) {
            canvas.submitRelativeRect(x - 3F, y + 17F, 22F, 2F, color);
            canvas.submitRelativeRect(x - 3F, y + 10F, 2F, 9F, color);
            canvas.submitRelativeRect(x + 17F, y + 10F, 2F, 9F, color);
            continue;
          }

          if (minSize == 3 || minSize == 4) {
            canvas.submitRelativeRect(x + 10F, y - 3F, 9F, 2F, color);
            canvas.submitRelativeRect(x + 17F, y - 3F, 2F, 9F, color);
            continue;
          }

          if (minSize == 8) {
            canvas.submitRelativeRect(x + 4F, y - 3F, 8F, 2F, color);
            continue;
          }

          continue;
        }

        // Team Yellow - 3/4/8 Teams
        if (identifier == BingoIdentifier.YELLOW) {
          if (minSize == 3) {
            canvas.submitRelativeRect(x - 3F, y + 17F, 22F, 2F, color);
            canvas.submitRelativeRect(x - 3F, y + 10F, 2F, 9F, color);
            canvas.submitRelativeRect(x + 17F, y + 10F, 2F, 9F, color);
            continue;
          }

          if (minSize == 4) {
            canvas.submitRelativeRect(x - 3F, y + 17F, 9F, 2F, color);
            canvas.submitRelativeRect(x - 3F, y + 10F, 2F, 9F, color);
            continue;
          }

          if (minSize == 8) {
            canvas.submitRelativeRect(x + 14F, y - 3F, 5F, 2F, color);
            canvas.submitRelativeRect(x + 17F, y - 3F, 2F, 5F, color);
            continue;
          }

          continue;
        }

        // Team Green - 4/8 Teams
        if (identifier == BingoIdentifier.GREEN) {
          if (minSize == 4) {
            canvas.submitRelativeRect(x + 10F, y + 17F, 9F, 2F, color);
            canvas.submitRelativeRect(x + 17F, y + 10F, 2F, 9F, color);
            continue;
          }

          if (minSize == 8) {
            canvas.submitRelativeRect(x + 17F, y + 4F, 2F, 8F, color);
            continue;
          }

          continue;
        }

        // Team Pink - 8 Teams
        if (identifier == BingoIdentifier.PINK) {
          if (minSize == 8) {
            canvas.submitRelativeRect(x + 14F, y + 17F, 5F, 2F, color);
            canvas.submitRelativeRect(x + 17F, y + 14F, 2F, 5F, color);
            continue;
          }

          continue;
        }

        // Team Light Blue - 8 Teams
        if (identifier == BingoIdentifier.LIGHT_BLUE) {
          if (minSize == 8) {
            canvas.submitRelativeRect(x + 4F, y + 17F, 8F, 2F, color);
            continue;
          }

          continue;
        }

        // Team Orange - 8 Teams
        if (identifier == BingoIdentifier.ORANGE) {
          if (minSize == 8) {
            canvas.submitRelativeRect(x - 3F, y + 17F, 5F, 2F, color);
            canvas.submitRelativeRect(x - 3F, y + 14F, 2F, 5F, color);
            continue;
          }

          continue;
        }

        // Team Purple - 8 Teams
        if (identifier == BingoIdentifier.PURPLE) {
          if (minSize == 8) {
            canvas.submitRelativeRect(x - 3F, y + 4F, 2F, 8F, color);
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
