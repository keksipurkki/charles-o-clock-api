package net.keksipurkki.charles_o_clock.repository;

import io.vertx.core.Future;
import net.keksipurkki.charles_o_clock.domain.PhoneNumber;
import net.keksipurkki.charles_o_clock.domain.User;

import java.util.Optional;
import java.util.UUID;

public class UserRepository {

    public Future<Optional<User>> getById(UUID id) {
        return Future.succeededFuture(Optional.empty());
    }

    public Future<User> createFrom(PhoneNumber phoneNumber) {
        return Future.succeededFuture(new User(phoneNumber));
    }
}
