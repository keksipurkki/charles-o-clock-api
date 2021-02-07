package net.keksipurkki.clock;

import io.vertx.core.Future;
import net.keksipurkki.api.Client;
import net.keksipurkki.api.ClockStatus;

import java.util.Deque;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

class ClockStateRepository {

  // TODO: Database
  private final Map<Client, Deque<ClockStatus>> db = new ConcurrentHashMap<>();
  private static ClockStateRepository singleton;

  private ClockStateRepository() {
  }

  Future<ClockStatus> put(ClockStatus status) {
    requireNonNull(status.getClient(), "Status with no associated client");
    var stack = db.getOrDefault(status.getClient(), new ConcurrentLinkedDeque<>());
    stack.addLast(status);
    db.put(status.getClient(), stack);
    return Future.succeededFuture(status);
  }

  Future<Optional<ClockStatus>> getLatest(Client client) {
    var latest = Optional.ofNullable(db.get(client)).stream()
      .map(Deque::getLast).findFirst();
    return Future.succeededFuture(latest);
  }

  static ClockStateRepository create() {
    if (nonNull(singleton)) {
      return singleton;
    }
    singleton = new ClockStateRepository();
    return singleton;

  }

}
