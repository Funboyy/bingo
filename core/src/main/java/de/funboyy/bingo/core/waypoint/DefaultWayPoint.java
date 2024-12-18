package de.funboyy.bingo.core.waypoint;

import de.funboyy.bingo.api.Bingo;
import de.funboyy.bingo.api.enums.Textures;
import de.funboyy.bingo.api.waypoint.WayPoint;
import java.util.Arrays;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.component.format.TextColor;
import net.labymod.api.client.render.font.ComponentRenderer;
import net.labymod.api.client.render.matrix.Stack;
import net.labymod.api.client.world.ClientWorld;
import net.labymod.api.client.world.MinecraftCamera;
import net.labymod.api.client.world.object.AbstractWorldObject;
import net.labymod.api.util.math.vector.FloatVector3;
import org.jetbrains.annotations.NotNull;

public class DefaultWayPoint extends AbstractWorldObject implements WayPoint {

  private static final ComponentRenderer COMPONENT_RENDERER = Laby.references()
      .renderPipeline().componentRenderer();

  private final boolean own;
  private final String name;
  private final Component text;
  private final int color;
  private final FloatVector3 position;
  private final String dimension;
  private float scale;
  private float distance;

  public DefaultWayPoint(final String name, final FloatVector3 location, final String dimension) {
    this(true, name, Bingo.get().color().get(), location, dimension);
  }

  public DefaultWayPoint(final String name, final int color, final FloatVector3 location, final String dimension) {
    this(false, name, color, location, dimension);
  }

  public DefaultWayPoint(final boolean own, final String name, final int color, final FloatVector3 location, final String dimension) {
    super(location);
    this.own = own;
    this.name = name;
    this.text = Component.text(name, TextColor.color(color));
    this.color = color;
    this.position = location.copy();
    this.dimension = dimension;
    this.scale = 1f;
    this.distance = 0f;
  }

  @Override
  public boolean isOwn() {
    return this.own;
  }

  @Override
  public String name() {
    return this.name;
  }

  @Override
  public Component text() {
    return this.text;
  }

  @Override
  public int color() {
    return this.color;
  }

  @Override
  public FloatVector3 position() {
    return this.position;
  }

  @Override
  public String dimension() {
    return this.dimension;
  }

  @Override
  public void scale(final float scale) {
    this.scale = scale;
  }

  @Override
  public void distance(final float distance) {
    this.distance = distance;

    this.text.setChildren(Arrays.asList(
       Component.text(" [", NamedTextColor.GRAY),
       Component.text((int) this.distance + "m", NamedTextColor.WHITE),
       Component.text("]", NamedTextColor.GRAY)
    ));
  }

  @Override
  public float distance() {
    return this.distance;
  }

  @Override
  public void renderInWorld(@NotNull final MinecraftCamera cam, @NotNull final Stack stack,
      final float x, final float y, final float z, final float delta, final boolean darker) {
    if (!Bingo.get().wayPoints()) {
      return;
    }

    if (!this.own && !Bingo.get().showOthers()) {
      return;
    }

    final ClientWorld world = Laby.labyAPI().minecraft().clientWorld();

    if (world == null || !world.dimension().getPath().equals(this.dimension)) {
      return;
    }

    stack.push();
    stack.scale(0.04f * this.scale);

    this.rotateHorizontally(cam, stack);
    this.rotateVertically(cam, stack);
    this.renderIcon(stack);
    this.renderText(stack);

    stack.pop();
  }

  private void renderIcon(final Stack stack) {
    Textures.WAY_POINT_ICON.getIcon().render(
        stack,
        -6f,
        -6f,
        12f,
        12f,
        false,
        this.color
    );
  }

  private void renderText(final Stack stack) {
    COMPONENT_RENDERER.builder()
        .text(this.text)
        .shadow(false)
        .discrete(true)
        .centered(true)
        .scale(0.75f)
        .pos(0f, -15f)
        .useFloatingPointPosition(true)
        .allowColors(true)
        .shouldBatch(false)
        .render(stack);
  }

}
