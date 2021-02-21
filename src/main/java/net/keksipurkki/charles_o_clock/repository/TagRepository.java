package net.keksipurkki.charles_o_clock.repository;

import io.vertx.core.Future;
import net.keksipurkki.charles_o_clock.domain.Tag;
import net.keksipurkki.charles_o_clock.domain.User;

public class TagRepository {
    public Future<Tag[]> getByUser(User user) {
        return Future.succeededFuture(new Tag[]{});
    }

    public Future<Tag[]> put(User user, Tag tag) {
        return Future.succeededFuture(new Tag[]{ tag });
    }
}
