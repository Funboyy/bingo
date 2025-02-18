package de.funboyy.bingo.api.waypoint;

import net.labymod.api.client.component.Component;
import net.labymod.api.client.world.object.WorldObject;
import net.labymod.api.util.math.vector.DoubleVector3;

public interface WayPoint extends WorldObject {

  boolean isOwn();

  String name();

  Component text();

  int color();

  DoubleVector3 origin();

  String dimension();

  void scale(final float scale);

  void distance(final double distance);

  double distance();

}
