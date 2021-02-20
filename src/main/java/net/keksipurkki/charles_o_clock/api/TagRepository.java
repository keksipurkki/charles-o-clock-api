package net.keksipurkki.charles_o_clock.api;

import io.vertx.core.Future;
import net.keksipurkki.charles_o_clock.domain.Client;
import net.keksipurkki.charles_o_clock.domain.Tag;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;


class TagRepository {

    public static final TagRepository shared = new TagRepository();

    // TODO: Database
    private final Map<UUID, Tag> db = new ConcurrentHashMap<>();

    private TagRepository() {
    }

    Future<Tag> createTag(UUID id) {
        var tag = new Tag(id, null);
        db.put(id, tag);
        return Future.succeededFuture(tag);
    }

    Future<Optional<Tag>> getById(UUID id) {
        return Future.succeededFuture(Optional.ofNullable(db.get(id)));
    }

    Future<Tag> claim(Client client, Tag tag) {
        var claimed = new Tag(tag.getId(), client);
        db.put(tag.getId(), claimed);
        return Future.succeededFuture(claimed);
    }

    Future<Tag> disclaim(Client client, Tag tag) {
        var disclaimed = new Tag(tag.getId(), null);
        db.put(tag.getId(), disclaimed);
        return Future.succeededFuture(disclaimed);
    }
}
