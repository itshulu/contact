package com.sl.contact.service.impl;

import com.sl.contact.dao.ContactDao;
import com.sl.contact.entity.Contact;
import com.sl.contact.exception.NullIdException;
import com.sl.contact.service.ContactService;
import com.sl.contact.util.RedisUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author 舒露
 */
//@Service
public class ContactServiceImpl implements ContactService {
    private static final String NAME = "name";
    private static final Logger logger = LoggerFactory.getLogger(ContactServiceImpl.class);
    private static final String TEL = "tel";
    private final ContactDao contactDao;

    private final Jedis jedis;

    @Autowired
    public ContactServiceImpl(ContactDao contactDao, Jedis jedis) {
        this.contactDao = contactDao;
        this.jedis = jedis;
    }

    @Override
    public Contact saveContact(Contact contact) {
        if (contact == null) {
            logger.debug("Contact Null");
            throw new NullPointerException("传入数据有误！！！");
        }
        contact.setId(UUID.randomUUID().toString().replace("-", ""));
        jedis.hset("contact:" + contact.getId(), NAME, contact.getName());
        jedis.hset("contact:" + contact.getId(), TEL, contact.getTel());
        return contactDao.save(contact);
    }

    @Override
    public void delContact(String id) throws NullIdException {
        if (StringUtils.isBlank(id) || !contactDao.existsById(id)) {
            logger.debug("Contact Id Null");
            throw new NullIdException("id " + id + " 为空");
        }
        jedis.hdel("contact:" + id, NAME, TEL);
        contactDao.deleteById(id);
    }

    @Override
    public Contact updateContact(Contact contact) throws NullIdException {
        if (contact == null || StringUtils.isBlank(contact.getId()) || !contactDao.existsById(contact.getId())) {
            logger.debug("Contact Null");
            throw new NullIdException("传入数据有误！！！");
        }
        jedis.hset("contact:" + contact.getId(), NAME, contact.getName());
        jedis.hset("contact:" + contact.getId(), TEL, contact.getTel());
        return contactDao.save(contact);
    }

    @Override
    public Contact findOne(String id) throws NullIdException {
        /*return RedisUtils.getContact(id, jedis).orElseGet(() -> {
            if (StringUtils.isBlank(id) || !contactDao.existsById(id)) {
                logger.debug("findOne Contact Id Null");
                throw new NullPointerException("id " + id + " 为空");
            }
            //缓存中没有数据
            Contact contact = contactDao.getOne(id);
            jedis.hset(contact.getId(), NAME, contact.getName());
            jedis.hset(contact.getId(), TEL, contact.getTel());
            return contact;
        });*/

       /* Supplier<Contact> supplier = new Supplier<Contact>() {

            @Override
            public Contact get() {
                return null;
            }
        };*/

        return RedisUtils.getContact2(id, jedis, () -> {
            if (StringUtils.isBlank(id) || !contactDao.existsById(id)) {
                logger.debug("findOne Contact Id Null");
                throw new NullPointerException("id " + id + " 为空");
            }
            //缓存中没有数据
            Contact contact = contactDao.getOne(id);
            jedis.hset("contact:" + contact.getId(), NAME, contact.getName());
            jedis.hset("contact:" + contact.getId(), TEL, contact.getTel());
            return contact;
        });
    }

    @Override
    public List<Contact> findAllContact() {
        Set<String> keys = jedis.keys("contact:*");
        //缓存中没有数据
        if (keys.size() == 0) {
            return contactDao.findAll().stream().peek(contact -> {
                jedis.hset("contact:" + contact.getId(), NAME, contact.getName());
                jedis.hset("contact:" + contact.getId(), TEL, contact.getTel());
            }).collect(Collectors.toList());
        } else {
            //通过每一个Key返回一个Contact
            //收集Contact add List
            return keys.stream().map(key -> {
                String name = jedis.hget(key, NAME);
                String tel = jedis.hget(key, TEL);
                Contact contact = new Contact();
                contact.setName(name);
                contact.setTel(tel);
                contact.setId(key.split(":")[1]);
                return contact;
            }).collect(Collectors.toList());
        }
    }
}
