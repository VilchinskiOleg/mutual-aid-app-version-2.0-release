package org.tms.common.auth.configuration.global.algorithms.model;

import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.groupingBy;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import lombok.EqualsAndHashCode;
import lombok.Getter;

public class ShortestValuablePathGenerator {

  public static void main(String[] args) {
    Location warsaw = new Location("Warsaw");
    Location berlin = new Location("Berlin");
    Location prague = new Location("Prague");
    Location vienna = new Location("Vienna");
    Location paris = new Location("Paris");

    List<Rout> simpleRouts = List.of(
        new Rout(warsaw, berlin, 10),  // W -> B (10)
        new Rout(warsaw, prague, 2),   // W -> P (2) — короткий старт
        new Rout(prague, berlin, 3),   // P -> B (3)
        new Rout(berlin, paris, 15),   // B -> Paris (15)
        new Rout(prague, vienna, 20),  // P -> V (20)
        new Rout(vienna, paris, 5)     // V -> Paris (5)
    );

    ShortestValuablePathGenerator generator = new ShortestValuablePathGenerator();
    var res = generator.buildShortestPath(simpleRouts, warsaw, paris);
    // Путь Warsaw -> Prague -> Berlin -> Paris: $2 + 3 + 15 = 20  (Победитель!)
    System.out.println("OK!");
  }

  public Rout buildShortestPath(List<Rout> simpleRouts, Location startPosition, Location finishPosition) {
    Queue<Location> startPositionsToCheck = new LinkedList<>();
    startPositionsToCheck.add(startPosition);
    Map<Location, List<Rout>> routsByStartPosition = buildRoutByStartPositionMap(simpleRouts);
    Map<Location, Rout> valueOfDestination = new HashMap<>(); // key - is destination of Rout

    // Handle each Route for A -> B :
    while (!startPositionsToCheck.isEmpty()) {
      Location start = startPositionsToCheck.remove();
      int startPosValue = Optional.ofNullable(valueOfDestination.get(start)) // actualized val
          .map(r -> r.totalValue).orElse(0);
      List<Rout> sRouts = routsByStartPosition.get(start);

      for (Rout simpleRout : sRouts) {
        // 1. Actualize destinations value :
        Rout parent = valueOfDestination.get(simpleRout.source); // parent - will be actualized rout to src
        int dstValRefinedWithSimpleRout = startPosValue + simpleRout.totalValue;
        Rout assignedDstRout = valueOfDestination.get(simpleRout.destination);

        if (assignedDstRout == null || dstValRefinedWithSimpleRout < assignedDstRout.totalValue) {
          Rout newRout = new Rout(startPosition, simpleRout.destination, dstValRefinedWithSimpleRout, parent);
          valueOfDestination.put(simpleRout.destination, newRout);
        }

        // 2. Put dst to queue as next start location :
        if (simpleRout.destination != finishPosition) {
          startPositionsToCheck.add(simpleRout.destination);
        }
      }
    }

    return valueOfDestination.get(finishPosition);
  }

  private Map<Location, List<Rout>> buildRoutByStartPositionMap(List<Rout> simpleRouts) {
    return simpleRouts.stream()
        .collect(groupingBy(r -> r.source));
  }

  @Getter
  public static class Rout {

    private final Location source;
    private final Location destination;
    private final Rout parent; // like previous step
    private final int totalValue;

    public Rout(Location src, Location dst, int currValue, Rout parent) {
      this.source = src;
      this.destination = dst;
      this.totalValue = nonNull(parent) ? parent.totalValue + currValue : currValue;
      this.parent = parent;
    }

    public Rout(Location src, Location dst, int currValue) {
      this.source = src;
      this.destination = dst;
      this.totalValue = currValue;
      this.parent = null;
    }
  }

  @Getter
  @EqualsAndHashCode(onlyExplicitlyIncluded = true)
  public static class Location {

    @EqualsAndHashCode.Include
    private final String identifier;

    // [Optional]: lat and long, or other fields ..

    public Location(String identifier) {
      this.identifier = identifier;
    }
  }
}


