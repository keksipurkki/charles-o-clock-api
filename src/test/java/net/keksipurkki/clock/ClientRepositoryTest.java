package net.keksipurkki.clock;

import net.keksipurkki.api.PhoneNumber;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClientRepositoryTest {

  final private ClientRepository sut = ClientRepository.create();

  @Test
  public void clientId_given_some_phonenumber_then_ok() {
    var phoneNumber = new PhoneNumber("+358111111111");
    var digest = sut.clientId(phoneNumber);
    var expected = "a1ea822eadbdd020a1c1b3c06a62e5fd22b4c0b652be400cc5fbd7afb31fced3";
    assertEquals(expected, digest);
  }

}