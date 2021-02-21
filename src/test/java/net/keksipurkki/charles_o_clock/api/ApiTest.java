package net.keksipurkki.charles_o_clock.api;

import net.keksipurkki.charles_o_clock.domain.PhoneNumber;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ApiTest {

 //   @Test
 //   public void register_given_some_phone_number_then_client_registers() {
 //       final var sut = Api.create();
 //       final var phoneNumber = new PhoneNumber("+358111111111");
 //       sut.register(phoneNumber).onComplete(result -> {
 //           assertNull(result.cause());
 //           assertTrue(result.succeeded());
 //       });
 //   }

 //   @Test
 //   public void register_given_duplicate_registration_then_exception() {
 //       final var sut = Api.create();
 //       final var phoneNumber = new PhoneNumber("+358111111111");
 //       sut.register(phoneNumber).onComplete(result -> {
 //           assertTrue(result.succeeded());
 //       }).compose(next -> sut.register(phoneNumber)).onComplete(result -> {
 //           System.out.println(result.cause().getMessage());
 //           assertTrue(result.failed());
 //           assertTrue(result.cause() instanceof RegistrationException);
 //       });
 //   }

 //   @Test
 //   public void createTag_given_fresh_tag_then_no_client() {
 //       final var sut = Api.create();
 //       sut.createTag().onComplete(r -> {
 //           assertTrue(r.succeeded());
 //           assertNull(r.result().getClient());
 //       });
 //   }

 //   @Test
 //   public void claim_given_fresh_tag_then_can_be_claimed() {
 //       final var sut = Api.create();
 //       final var phoneNumber = new PhoneNumber("+358111111111");
 //       final var client = ClientRepository.create().create(phoneNumber);
 //       sut.createTag().compose(tag -> sut.claim(client, tag)).onComplete(r -> {
 //           assertNull(r.cause());
 //           assertTrue(r.succeeded());
 //           assertEquals(client, r.result().getClient());
 //       });
 //   }

 //   @Test
 //   public void claim_given_out_of_band_tag_then_cannot_be_claimed() {
 //       final var sut = Api.create();
 //       final var phoneNumber = new PhoneNumber("+358111111111");
 //       final var client = ClientRepository.create().create(phoneNumber);
 //       final var rogue = new Tag(UUID.randomUUID(), null);
 //       sut.claim(client, rogue).onComplete(r -> {
 //           assertFalse(r.succeeded());
 //           assertTrue(r.cause() instanceof NonExistingTagException);
 //       });
 //   }

 //   @Test
 //   public void claim_given_existing_tag_then_cannot_be_claimed() {
 //       final var sut = Api.create();
 //       final var phoneNumber = new PhoneNumber("+358111111111");
 //       final var client = ClientRepository.create().create(phoneNumber);
 //       sut.createTag()
 //          .compose(tag -> sut.claim(client, tag))
 //          .compose(tag -> sut.claim(client, tag)).onComplete(r -> {
 //           assertFalse(r.succeeded());
 //           assertTrue(r.cause() instanceof TagClaimException);
 //       });

 //   }

 //   @Test
 //   public void disclaim_given_out_of_band_tag_then_cannot_be_disclaimed() {

 //       final var sut = Api.create();
 //       final var phoneNumber = new PhoneNumber("+358111111111");
 //       final var client = ClientRepository.create().create(phoneNumber);
 //       final var rogue = new Tag(UUID.randomUUID(), null);
 //       sut.disclaim(client, rogue).onComplete(r -> {
 //           assertFalse(r.succeeded());
 //           assertTrue(r.cause() instanceof NonExistingTagException);
 //       });

 //   }

 //   @Test
 //   public void disclaim_given_fresh_tag_then_not_ok() {
 //       final var sut = Api.create();
 //       final var phoneNumber = new PhoneNumber("+358111111111");
 //       final var client = ClientRepository.create().create(phoneNumber);
 //       sut.createTag()
 //          .compose(tag -> sut.disclaim(client, tag)).onComplete(r -> {
 //           assertFalse(r.succeeded());
 //           assertTrue(r.cause() instanceof TagClaimException);
 //       });
 //   }

 //   @Test
 //   public void disclaim_given_claimed_tag_then_ok() {
 //       final var sut = Api.create();
 //       final var phoneNumber = new PhoneNumber("+358111111111");
 //       final var client = ClientRepository.create().create(phoneNumber);
 //       sut.createTag()
 //          .compose(tag -> sut.claim(client, tag))
 //          .compose(tag -> sut.disclaim(client, tag)).onComplete(r -> {
 //           assertNull(r.cause());
 //           assertTrue(r.succeeded());
 //           assertNull(r.result().getClient());
 //       });
 //   }

 //   @Test
 //   public void clock_given_empty_state_then_can_clock_in() {
 //       final var sut = Api.create();
 //       final var phoneNumber = new PhoneNumber("+358111111111");
 //       final var client = ClientRepository.create().create(phoneNumber);
 //       sut.clock(ClockStatus.clockIn(client)).onComplete(r -> {
 //           assertNull(r.cause());
 //           assertTrue(r.succeeded());
 //           assertEquals(ClockStatus.Action.CLOCK_IN, r.result().getAction());
 //       });
 //   }

 //   @Test
 //   public void clock_given_empty_state_then_cannot_clock_out() {
 //       final var sut = Api.create();
 //       final var phoneNumber = new PhoneNumber("+358111111111");
 //       final var client = ClientRepository.create().create(phoneNumber);
 //       sut.clock(ClockStatus.clockOut(client)).onComplete(r -> {
 //           assertFalse(r.succeeded());
 //           assertTrue(r.cause() instanceof ConflictingStateException);
 //       });
 //   }

 //   @Test
 //   public void clock_given_clock_in_then_can_clock_out() {
 //       final var sut = Api.create();
 //       final var phoneNumber = new PhoneNumber("+358111111111");
 //       final var client = ClientRepository.create().create(phoneNumber);
 //       sut.clock(ClockStatus.clockIn(client))
 //          .compose(status -> sut.clock(ClockStatus.clockOut(client)))
 //          .onComplete(r -> {
 //              assertNull(r.cause());
 //              assertTrue(r.succeeded());
 //              assertEquals(ClockStatus.Action.CLOCK_OUT, r.result()
 //                                                          .getAction());
 //          });
 //   }

 //   @Test
 //   public void clock_given_clock_in_then_cannot_clock_in() {
 //       final var sut = Api.create();
 //       final var phoneNumber = new PhoneNumber("+358111111111");
 //       final var client = ClientRepository.create().create(phoneNumber);
 //       sut.clock(ClockStatus.clockIn(client))
 //          .compose(status -> sut.clock(ClockStatus.clockIn(client)))
 //          .onComplete(r -> {
 //              assertFalse(r.succeeded());
 //              assertTrue(r.cause() instanceof ConflictingStateException);
 //          });
 //   }

 //   @Test
 //   public void clock_given_given_clock_out_then_cannot_clock_out() {
 //       final var sut = Api.create();
 //       final var phoneNumber = new PhoneNumber("+358111111111");
 //       final var client = ClientRepository.create().create(phoneNumber);
 //       sut.clock(ClockStatus.clockIn(client))
 //          .compose(status -> sut.clock(ClockStatus.clockOut(client)))
 //          .compose(status -> sut.clock(ClockStatus.clockOut(client)))
 //          .onComplete(r -> {
 //              assertFalse(r.succeeded());
 //              assertTrue(r.cause() instanceof ConflictingStateException);
 //          });
 //   }

}