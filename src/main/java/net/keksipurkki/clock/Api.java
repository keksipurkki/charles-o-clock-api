package net.keksipurkki.clock;

import io.vertx.core.Future;
import net.keksipurkki.api.*;
import net.keksipurkki.exceptions.ConflictingStateException;
import net.keksipurkki.exceptions.NonExistingTagException;
import net.keksipurkki.exceptions.RegistrationException;
import net.keksipurkki.exceptions.TagClaimException;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.util.UUID;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class Api implements CharlesOClock {

  private final ClientRepository clients;
  private final TagRepository tags;
  private final ClockStateRepository statuses;

  private Api(ClientRepository clients, TagRepository tags, ClockStateRepository statuses) {
    this.clients = clients;
    this.tags = tags;
    this.statuses = statuses;
  }

  @Override
  public Future<Tag> createTag() {
    return tags.createTag(UUID.randomUUID());
  }

  @Override
  public Future<ClientCredentials> register(PhoneNumber number) {
    return clients.getByPhoneNumber(number).compose(existing -> {

      if (existing.isPresent()) {
        throw new RegistrationException("Client already registered");
      }

      return clients.put(clients.create(number));

    }).map(client -> {
        return new ClientCredentials(client.getId(), client.getKeypair().getPrivate());
      }
    );
  }

  @Override
  public Future<Tag> claim(Client client, Tag tag) {
    return tags.getById(tag.getId()).compose(existing -> {

      if (existing.isEmpty()) {
        var message = String.format("No tag with id %s", tag.getId());
        throw new NonExistingTagException(message);
      }

      var target = existing.get();

      if (nonNull(target.getClient())) {
        var message = String.format("Tag %s already owned by client %s", tag.getId(), client.getId());
        throw new TagClaimException(message);
      }

      return tags.claim(client, tag);

    });
  }

  @Override
  public Future<Tag> disclaim(Client client, Tag tag) {
    return tags.getById(tag.getId()).compose(existing -> {

      if (existing.isEmpty()) {
        var message = String.format("No tag with id %s", tag.getId());
        throw new NonExistingTagException(message);
      }

      var target = existing.get();

      if (isNull(target.getClient()) || !target.getClient().equals(client)) {
        var message = String.format("Tag %s has not been claimed by client %s", tag.getId(), client.getId());
        throw new TagClaimException(message);
      }

      return tags.disclaim(client, tag);

    });

  }

  @Override
  public Future<ClockStatus> clock(ClockStatus status) {
    final var client = status.getClient();
    return statuses.getLatest(client).compose(latest -> {

      if (latest.isEmpty()) {
        if (status.getAction().equals(ClockStatus.Action.CLOCK_OUT)) {
          var message = String.format("No existing clock state for client %s", client);
          throw new ConflictingStateException(message);
        }
      }

      if (latest.isPresent() && latest.get().getAction().equals(status.getAction())) {
        var message = String.format("Already in state %s", status.getAction());
        throw new ConflictingStateException(message);
      }

      return statuses.put(status);

    });
  }

  public static Api create() {
    var clients = ClientRepository.create(new BouncyCastleProvider());
    var tags = TagRepository.create();
    var statuses = ClockStateRepository.create();
    return new Api(clients, tags, statuses);
  }

}
