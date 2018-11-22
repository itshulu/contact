package com.sl.contact.service.impl;

import com.sl.contact.dao.ContactDao;
import com.sl.contact.entity.Contact;
import com.sl.contact.exception.NullIdException;
import com.sl.contact.service.ContactService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 舒露
 */
@Service
public class ContactService3Impl implements ContactService {
    private static final Logger logger = LoggerFactory.getLogger(ContactServiceImpl.class);
    private final ContactDao contactDao;

    @Autowired
    public ContactService3Impl(ContactDao contactDao) {
        this.contactDao = contactDao;
    }

    @Override
    @CachePut(cacheNames = "contact", key = "#contact.id")
    @CacheEvict(cacheNames = "contacts", allEntries = true)
    public Contact saveContact(Contact contact) throws NullIdException {
        if (contact == null) {
            logger.debug("Contact Null");
            throw new NullIdException("传入数据有误！！！");
        }
        return contactDao.save(contact);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "contact", key = "#id"),
            @CacheEvict(cacheNames = "contacts", allEntries = true)})
    public void delContact(String id) throws NullIdException {
        if (StringUtils.isBlank(id) || !contactDao.existsById(id)) {
            logger.debug("Contact Id Null");
            throw new NullIdException("id " + id + " 为空");
        }
        contactDao.deleteById(id);
    }

    @Override
    @CachePut(cacheNames = "contact", key = "#contact.id")
    @CacheEvict(cacheNames = "contacts", allEntries = true)
    public Contact updateContact(Contact contact) throws NullIdException {
        if (contact == null || StringUtils.isBlank(contact.getId()) || !contactDao.existsById(contact.getId())) {
            logger.debug("Contact Null");
            throw new NullIdException("传入数据有误！！！");
        }
        return contactDao.save(contact);
    }

    @Override
    @Cacheable(cacheNames = "contact", key = "#id")
    public Contact findOne(String id) throws NullIdException {
        if (StringUtils.isBlank(id) || !contactDao.existsById(id)) {
            logger.debug("findOne Contact Id Null");
            throw new NullIdException("id " + id + " 为空");
        }
        return contactDao.getOne(id);
    }

    @Override
    @Cacheable(cacheNames = "contacts")
    public List<Contact> findAllContact() {
        return contactDao.findAll();
    }
}
