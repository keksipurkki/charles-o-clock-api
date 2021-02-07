package net.keksipurkki.clock;

import io.vertx.core.Future;
import net.keksipurkki.api.Client;
import net.keksipurkki.api.PhoneNumber;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.math.BigInteger;
import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

class ClientRepository {

  // TODO: Database
  private Map<String, Client> db = new ConcurrentHashMap<>();

  private final static String namedCurve = "secp256k1";
  private static ClientRepository singleton;
  private final static SecureRandom random = new SecureRandom();

  private final KeyPairGenerator generator;
  private final MessageDigest hash;

  private ClientRepository() throws Exception {
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
    var digest = hash.digest(phone.getNumber().getBytes(UTF_8));
    return new BigInteger(1, digest).toString(16);
  }

  static ClientRepository create(Provider provider) {
    try {
      if (nonNull(singleton)) {
        return singleton;
      } else {
        Security.addProvider(provider);
        return new ClientRepository();
      }
    } catch(Exception cause) {
      throw new IllegalStateException("Client repository creation failed", cause);
    }
  }

  static ClientRepository create() {
    return create(new BouncyCastleProvider());
  }

}
