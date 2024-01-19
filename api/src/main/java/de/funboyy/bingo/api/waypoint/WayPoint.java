package de.funboyy.bingo.api.waypoint;

import net.labymod.api.client.component.Component;
import net.labymod.api.client.world.object.WorldObject;
import net.labymod.api.util.math.vector.FloatVector3;

public interface WayPoint extends WorldObject {

  boolean isOwn();

  String name();

  Component text();

  int color();

  FloatVector3 position();

  String dimension();

  void scale(final float scale);

  void distance(final float distance);

  float distance();

}
