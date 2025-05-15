package de.funboyy.bingo.api.model;

import de.funboyy.bingo.api.Bingo;
import de.funboyy.bingo.api.BingoFlattener;
import de.funboyy.bingo.api.Scheduler;
import de.funboyy.bingo.api.enums.State;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.labymod.api.Laby;
import net.labymod.api.client.Minecraft;
import net.labymod.api.client.entity.player.ClientPlayer;
import net.labymod.api.client.network.ClientPacketListener;
import net.labymod.api.client.network.NetworkPlayerInfo;
import net.labymod.api.client.scoreboard.ScoreboardTeam;
import net.labymod.api.client.world.ClientWorld;
import net.labymod.api.util.I18n;

public class BingoGame {

  private final Bingo<?> bingo;
  private final Set<BingoTeam> teams = new HashSet<>();
  private final BingoCard card = new BingoCard();
  private BingoDeath death = null;
  private State state = State.OFFLINE;
  private boolean openedCard = false;
  private int minSize = 0;

  public BingoGame(final Bingo<?> bingo) {
    this.bingo = bingo;
  }

  public void handleMessage(final String message) {
    if (this.state == State.LOBBY) {
      if (message.matches("^\\[Bingo] Die Runde beginnt in 1 Sekunde$")) {
        this.setState(State.PREPARATION);
      }

      return;
    }

    if (this.state == State.PREPARATION) {
      if (message.matches("^\\[Bingo] Die Schutzzeit ist vorbei! Lasst die Kämpfe beginnen!$")) {
        this.setState(State.PLAYING);

        for (final BingoIdentifier identifier : BingoIdentifier.values()) {
          this.teams.add(new BingoTeam(identifier));
        }

        this.addPlayersToTeams();
      }

      return;
    }

    if (this.state == State.PLAYING) {
      final Pattern pattern = Pattern.compile("^\\[Bingo] (.+) hat das Item (.+) gefunden ?(?:\\(\\+\\d+ XP\\))?$");
      final Matcher matcher = pattern.matcher(message);

      if (matcher.matches()) {
        final String playerName = matcher.group(1);
        final String itemName = matcher.group(2);

        final BingoTeam team = this.getTeam(playerName);
        final BingoItem item = this.card.getItem(itemName);

        if (team == null || item == null) {
          return;
        }

        item.found(team);
        return;
      }

      if (message.matches("^\\[Bingo] .+ hat Bingo gewonnen ?(?:\\(\\+\\d+ XP\\))?$")) {
        this.setState(State.FINISHED);

        if (this.bingo.autoGG()) {
          final int delay = (new Random().nextInt(100) * 10) + 250;
          Scheduler.getInstance().schedule(() -> Laby.references().chatExecutor().chat("gg"), delay);
        }
      }
    }
  }

  public void handleUpdateInventory() {
    if (this.openedCard) {
      return;
    }

    this.openedCard = true;

    final int delay = (new Random().nextInt(100) * 10) + 3_000;
    Scheduler.getInstance().schedule(() -> Laby.references().chatExecutor().chat("/bingo"), delay);
  }

  public void handleDeath() {
    if (this.state != State.PLAYING) {
      return;
    }

    final Minecraft minecraft = this.bingo.labyAPI().minecraft();
    final ClientPlayer player = minecraft.getClientPlayer();
    final ClientWorld world = minecraft.clientWorld();

    if (player == null || world == null) {
      return;
    }

    this.death = new BingoDeath(I18n.translate("bingo.settings.lastDeathKey.text"),
        player.position().toDoubleVector3(), world.dimension().getPath());
  }

  public void resetDeath() {
    this.death = null;
  }

  public void addPlayersToTeams() {
    final ClientPacketListener packetListener = this.bingo.labyAPI().minecraft().getClientPacketListener();

    if (packetListener == null) {
      return;
    }

    for (final NetworkPlayerInfo playerInfo : packetListener.getNetworkPlayerInfos()) {
      if (playerInfo == null) {
        continue;
      }

      final ScoreboardTeam scoreboardTeam = playerInfo.getTeam();

      if (scoreboardTeam == null) {
        continue;
      }

      final String name = playerInfo.profile().getUsername();
      final String prefix = BingoFlattener.getPlainText(scoreboardTeam.getPrefix());

      for (final BingoTeam team : this.teams) {
        final BingoIdentifier identifier = team.getIdentifier();

        if (prefix.startsWith(identifier.getName()) && !team.hasEntry(name)) {
          team.getEntries().add(name);

          if (identifier.getMinSize() > this.minSize) {
            this.minSize = identifier.getMinSize();
          }
        }
      }
    }
  }

  public BingoTeam getTeam(final String name) {
    return this.getTeam(name, false);
  }

  public BingoTeam getTeam(final String name, final boolean added) {
    for (final BingoTeam team : this.teams) {
      if (team.hasEntry(name)) {
        return team;
      }
    }

    if (!added) {
      this.addPlayersToTeams();
      return this.getTeam(name, true);
    }

    return null;
  }

  public BingoCard getCard() {
    return this.card;
  }

  public BingoDeath getDeath() {
    return this.death;
  }

  public State getState() {
    return this.state;
  }

  public void setState(final State state) {
    this.bingo.getWayPointService().removeWayPoints();
    this.state = state;
  }

  public int getMinSize() {
    return this.minSize;
  }

}
