package net.keksipurkki.charles_o_clock.api;

import io.vertx.core.Future;
import lombok.SneakyThrows;
import net.keksipurkki.charles_o_clock.domain.Client;
import net.keksipurkki.charles_o_clock.domain.PhoneNumber;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.math.BigInteger;
import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Objects.requireNonNull;

class ClientRepository {

    public final static ClientRepository shared = create(new BouncyCastleProvider());

    // TODO: Database
    private final Map<String, Client> db = new ConcurrentHashMap<>();

    private final static String namedCurve = "secp256k1";
    private final static SecureRandom random = new SecureRandom();

    private final KeyPairGenerator generator;
    private final MessageDigest hash;

    @SneakyThrows
    private ClientRepository() {
        generator = KeyPairGenerator.getInstance("ECDSA");
        generator.initialize(new ECGenParameterSpec(namedCurve), random);
        hash = MessageDigest.getInstance("SHA-256");
    }

    Client create(PhoneNumber number) {
        var keypair = generator.generateKeyPair();
        var registered = new Date();
        var confirmed = new Date(); // TODO: Phone number confirmation
        return new Client(clientId(number), keypair, registered, confirmed);
    }

    Future<Client> put(Client client) {
        requireNonNull(client);
        db.put(client.getId(), client);
        return Future.succeededFuture(client);
    }

    Future<Optional<Client>> getById(String id) {
        return Future.succeededFuture(Optional.ofNullable(db.get(id)));
    }

    Future<Optional<Client>> getByPhoneNumber(PhoneNumber number) {
        var id = clientId(number);
        return Future.succeededFuture(Optional.ofNullable(db.get(id)));
    }

    String clientId(PhoneNumber phone) {
        var digest = hash.digest(phone.getPhoneNumber().getBytes(UTF_8));
        return new BigInteger(1, digest).toString(16);
    }

    static ClientRepository create(Provider provider) {
        Security.addProvider(provider);
        return new ClientRepository();
    }

}
