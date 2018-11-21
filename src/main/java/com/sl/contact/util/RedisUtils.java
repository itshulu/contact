package com.sl.contact.util;

import com.sl.contact.entity.Contact;
import redis.clients.jedis.Jedis;

import java.util.Optional;
import java.util.function.Supplier;

public class RedisUtils {
    private static final String NAME = "name";
    private static final String TEL = "tel";
    private static final String PONG = "PONG";

    public static Optional<Contact> getContact(String id, Jedis jedis) {
        id = "contact:" + id;
        if (jedis.ping().equals(PONG)) {
            String name = jedis.hget(id, NAME);
            if (name != null) {
                Contact contact = new Contact();
                contact.setName(name);
                contact.setTel(jedis.hget(id, TEL));
                contact.setId(id);
                return Optional.of(contact);
            } else {
                return Optional.empty();
            }
        } else {
            return Optional.empty();
        }
    }

    public static Contact getContact2(String id, Jedis jedis, Supplier<Contact> other) {
        id = "contact:" + id;
        if (jedis.ping().equals(PONG)) {
            String name = jedis.hget(id, NAME);
            if (name != null) {
                Contact contact = new Contact();
                contact.setName(name);
                contact.setTel(jedis.hget(id, TEL));
                contact.setId(id);
                return contact;
            } else {
                return other.get();
            }
        } else {
            return other.get();
        }
    }
}
