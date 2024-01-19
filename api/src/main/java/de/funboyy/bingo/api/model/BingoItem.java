package de.funboyy.bingo.api.model;

import de.funboyy.bingo.api.Bingo;
import de.funboyy.bingo.api.BingoHelper;
import de.funboyy.bingo.api.enums.Difficulty;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.labymod.api.client.world.item.ItemStack;

public class BingoItem {

  private final ItemStack itemStack;
  private final String name;
  private final Difficulty difficulty;
  private final int customDifficulty;
  private final Set<BingoTeam> teams;
  private boolean highlighted = false;

  public BingoItem(final ItemStack itemStack) {
    this.itemStack = itemStack;

    final String displayName = BingoHelper.getPlainText(this.itemStack.getDisplayName());
    final Pattern pattern = Pattern.compile("^(.+) \\((.+)\\)$");
    final Matcher matcher = pattern.matcher(displayName);

    if (matcher.matches()) {
      this.name = matcher.group(1);
      this.difficulty = Difficulty.fromString(matcher.group(2));
    }

    else {
      this.name = displayName;
      this.difficulty = Difficulty.UNKNOWN;
    }

    this.customDifficulty = Bingo.get().getWebAPI()
        .getDifficulty(this.itemStack.getIdentifier().getPath(), this.difficulty);
    this.teams = new HashSet<>();
  }

  public ItemStack getItemStack() {
    return this.itemStack;
  }

  public String getName() {
    return this.name;
  }

  public int getDifficulty() {
    if (this.customDifficulty == -1) {
      return this.difficulty.getValue();
    }

    return this.customDifficulty;
  }

  public void found(final BingoTeam team) {
    if (this.teams.contains(team)) {
      return;
    }

    this.teams.add(team);
  }

  public Set<BingoTeam> getTeams() {
    return this.teams;
  }

  public void highlight() {
    this.highlighted = true;
  }

  public boolean isHighlighted() {
    return this.highlighted;
  }

}
