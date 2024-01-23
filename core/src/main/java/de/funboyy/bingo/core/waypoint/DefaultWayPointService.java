package de.funboyy.bingo.core.waypoint;

import de.funboyy.bingo.api.Bingo;
import de.funboyy.bingo.api.model.BingoTeam;
import de.funboyy.bingo.api.waypoint.WayPoint;
import de.funboyy.bingo.api.waypoint.WayPointService;
import de.funboyy.bingo.core.waypoint.gui.WayPointManageActivity;
import de.funboyy.bingo.core.waypoint.gui.WayPointNameActivity;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import net.labymod.api.Laby;
import net.labymod.api.LabyAPI;
import net.labymod.api.client.Minecraft;
import net.labymod.api.client.entity.player.ClientPlayer;
import net.labymod.api.client.gui.screen.key.Key;
import net.labymod.api.client.network.ClientPacketListener;
import net.labymod.api.client.network.NetworkPlayerInfo;
import net.labymod.api.client.world.ClientWorld;
import net.labymod.api.client.world.object.WorldObjectRegistry;
import net.labymod.api.event.Phase;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.input.KeyEvent;
import net.labymod.api.event.client.input.KeyEvent.State;
import net.labymod.api.event.client.render.GameRenderEvent;
import net.labymod.api.labyconnect.LabyConnectSession;
import net.labymod.api.user.GameUser;
import net.labymod.api.user.GameUserService;
import net.labymod.api.util.ThreadSafe;
import net.labymod.api.util.math.vector.FloatVector3;
import net.labymod.core.event.labymod.PacketAddonDevelopmentEvent;
import net.labymod.core.labyconnect.protocol.packets.PacketAddonDevelopment;
import net.labymod.serverapi.protocol.payload.io.PayloadReader;
import net.labymod.serverapi.protocol.payload.io.PayloadWriter;

public class DefaultWayPointService implements WayPointService {

  private static final String LABY_CONNECT_KEY = "bingo:waypoint";
  private static final float DEFAULT_SIZE = 1f;
  private static final float TARGET_DISTANCE = 75f;

  private final Bingo<?> bingo;
  private final LabyAPI labyAPI;
  private final Minecraft minecraft;
  private final WorldObjectRegistry worldObjectRegistry;
  private final Set<WayPoint> wayPoints;

  public DefaultWayPointService(final Bingo<?> bingo) {
    this.bingo = bingo;
    this.labyAPI = Laby.labyAPI();
    this.minecraft = this.labyAPI.minecraft();
    this.worldObjectRegistry = Laby.references().worldObjectRegistry();
    this.wayPoints = new HashSet<>();
  }

  @Subscribe
  public void onWayPointKey(final KeyEvent event) {
    if (event.state() != State.UNPRESSED) {
      return;
    }

    if (!this.bingo.wayPoints()) {
      return;
    }

    if (this.minecraft.minecraftWindow().isScreenOpened()) {
      return;
    }

    if (!this.minecraft.isIngame()) {
      return;
    }

    if (this.bingo.getGame().getState() != de.funboyy.bingo.api.enums.State.PLAYING) {
      return;
    }

    final Key createKey = this.bingo.createKey();
    final Key manageKey = this.bingo.manageKey();

    if (createKey != Key.NONE && event.key() == createKey) {
      final WayPointNameActivity nameActivity = new WayPointNameActivity(this::placeWayPoint);
      this.minecraft.minecraftWindow().displayScreen(nameActivity);
      return;
    }

    if (manageKey != Key.NONE && event.key() == manageKey) {
      final WayPointManageActivity manageActivity = new WayPointManageActivity(this.wayPoints);
      this.minecraft.minecraftWindow().displayScreen(manageActivity);
    }
  }

  @Subscribe
  public void onTick(final GameRenderEvent event) {
    final ClientPlayer player = this.minecraft.getClientPlayer();

    if (player == null || this.wayPoints.isEmpty()) {
      return;
    }

    if (event.phase() != Phase.PRE) {
      return;
    }

    if (!this.minecraft.isIngame()) {
      return;
    }

    if (!this.bingo.wayPoints()) {
      return;
    }

    final FloatVector3 playerPosition = player.position();

    for (final WayPoint wayPoint : this.wayPoints) {
      if (!wayPoint.isOwn() && !this.bingo.showOthers()) {
        continue;
      }

      final FloatVector3 distanceVector = new FloatVector3(
          playerPosition.getX() - wayPoint.position().getX(),
          playerPosition.getY() - wayPoint.position().getY(),
          playerPosition.getZ() - wayPoint.position().getZ()
      );

      final float distance = (float) distanceVector.length();

      if (distance == wayPoint.distance()) {
        continue;
      }

      wayPoint.distance(distance);

      if (distance <= TARGET_DISTANCE) {
        wayPoint.scale(4f * (distance / TARGET_DISTANCE) + DEFAULT_SIZE);
        wayPoint.location().set(wayPoint.position());
        continue;
      }

      wayPoint.scale(5f);

      final float normalizationFactor = TARGET_DISTANCE / distance;
      final FloatVector3 newPosition = new FloatVector3(
          playerPosition.getX() - (distanceVector.getX() * normalizationFactor),
          playerPosition.getY() - (distanceVector.getY() * normalizationFactor),
          playerPosition.getZ() - (distanceVector.getZ() * normalizationFactor)
      );

      wayPoint.location().set(newPosition);
    }
  }

  @Subscribe
  public void onLabyConnectWayPoint(final PacketAddonDevelopmentEvent event) {
    final PacketAddonDevelopment packet = event.packet();
    final ClientPlayer self = this.minecraft.getClientPlayer();
    final ClientWorld world = this.minecraft.clientWorld();

    if (!packet.getKey().equals(LABY_CONNECT_KEY)) {
      return;
    }

    if (self == null || world == null) {
      return;
    }

    if (this.bingo.getGame().getState() != de.funboyy.bingo.api.enums.State.PLAYING) {
      return;
    }

    final PayloadReader reader = new PayloadReader(packet.getData());
    final String name = reader.readString();
    final int color = reader.readVarInt();
    final String dimension = reader.readString();
    final float x = reader.readVarInt() + 0.5f;
    final float y = reader.readVarInt() + 0.5f;
    final float z = reader.readVarInt() + 0.5f;

    final WayPoint wayPoint = new DefaultWayPoint(name, color, new FloatVector3(x, y, z), dimension);
    this.displayWayPoint(wayPoint);
  }

  @Override
  public void removeWayPoint(final WayPoint wayPoint) {
    this.worldObjectRegistry.unregister(v -> v.getValue() == wayPoint);
    this.wayPoints.remove(wayPoint);
  }

  @Override
  public void removeWayPoints() {
    for (final WayPoint wayPoint : this.wayPoints) {
      this.worldObjectRegistry.unregister(v -> v.getValue() == wayPoint);
      this.wayPoints.remove(wayPoint);
    }
  }

  private void displayWayPoint(final WayPoint wayPoint) {
    this.wayPoints.add(wayPoint);

    Laby.labyAPI().minecraft().executeOnRenderThread(() ->
        this.worldObjectRegistry.register(wayPoint));
  }

  private void placeWayPoint(final String name) {
    ThreadSafe.ensureRenderThread();

    final WayPoint wayPoint = this.createWayPoint(name);

    if (wayPoint != null) {
      this.displayWayPoint(wayPoint);
      this.sendLabyConnect(wayPoint);
    }
  }

  private void sendLabyConnect(final WayPoint wayPoint) {
    final ClientPlayer player = this.minecraft.getClientPlayer();
    final ClientWorld world = this.minecraft.clientWorld();
    final LabyConnectSession session = this.labyAPI.labyConnect().getSession();

    if (player == null || world == null || session == null) {
      return;
    }

    final BingoTeam team = this.bingo.getGame().getTeam(player.getName());

    if (team == null) {
      return;
    }

    final ClientPacketListener packetListener = this.minecraft.getClientPacketListener();
    final GameUserService userService = Laby.references().gameUserService();

    if (packetListener == null) {
      return;
    }

    final Collection<UUID> receivers = new ArrayList<>();

    for (final String entry : team.getEntries()) {
      final NetworkPlayerInfo playerInfo = packetListener.getNetworkPlayerInfo(entry);

      if (playerInfo == null) {
        continue;
      }

      final GameUser user = userService.gameUser(playerInfo.profile().getUniqueId());

      if (user.isUsingLabyMod() && !user.getUniqueId().equals(player.getUniqueId())) {
        receivers.add(user.getUniqueId());
      }
    }

    if (receivers.isEmpty()) {
      return;
    }

    final PayloadWriter writer = new PayloadWriter();
    writer.writeString(wayPoint.name());
    writer.writeVarInt(wayPoint.color());
    writer.writeString(wayPoint.dimension());
    writer.writeVarInt(floor(wayPoint.location().getX()));
    writer.writeVarInt(floor(wayPoint.location().getY()));
    writer.writeVarInt(floor(wayPoint.location().getZ()));

    session.sendAddonDevelopment(LABY_CONNECT_KEY, receivers.toArray(new UUID[0]), writer.toByteArray());
  }

  private WayPoint createWayPoint(final String name) {
    final ClientPlayer player = this.minecraft.getClientPlayer();
    final ClientWorld world = this.minecraft.clientWorld();

    if (player == null || world == null) {
      return null;
    }

    final FloatVector3 location = player.position().copy();
    location.set(
        floor(location.getX()) + 0.5f,
        floor(location.getY()) + 0.5f,
        floor(location.getZ()) + 0.5f
    );

    return new DefaultWayPoint(name, location, world.dimension().getPath());
  }

  private int floor(final double number) {
    final int floor = (int) number;
    return (double) floor == number ? floor : floor - (int) (Double.doubleToRawLongBits(number) >>> 63);
  }

}
