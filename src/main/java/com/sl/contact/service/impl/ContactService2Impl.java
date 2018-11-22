package com.sl.contact.service.impl;

import com.sl.contact.dao.ContactDao;
import com.sl.contact.entity.Contact;
import com.sl.contact.exception.NullIdException;
import com.sl.contact.service.ContactService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * @author 舒露
 */
//@Service
public class ContactService2Impl implements ContactService {
    private static final Logger logger = LoggerFactory.getLogger(ContactServiceImpl.class);
    private final ContactDao contactDao;

    @Autowired
    public ContactService2Impl(ContactDao contactDao) {
        this.contactDao = contactDao;
    }

    @Override
    public Contact saveContact(Contact contact) throws NullIdException {
        if (contact == null) {
            logger.debug("Contact Null");
            throw new NullIdException("传入数据有误！！！");
        }
        contact.setId(UUID.randomUUID().toString().replace("-", ""));
        return contactDao.save(contact);
    }

    @Override
    public void delContact(String id) throws NullIdException {
        if (StringUtils.isBlank(id) || !contactDao.existsById(id)) {
            logger.debug("Contact Id Null");
            throw new NullIdException("id " + id + " 为空");
        }
        contactDao.deleteById(id);
    }

    @Override
    public Contact updateContact(Contact contact) throws NullIdException {
        if (contact == null || StringUtils.isBlank(contact.getId()) || !contactDao.existsById(contact.getId())) {
            logger.debug("Contact Null");
            throw new NullIdException("传入数据有误！！！");
        }
        return contactDao.save(contact);
    }

    @Override
    public Contact findOne(String id) throws NullIdException {
        if (StringUtils.isBlank(id) || !contactDao.existsById(id)) {
            logger.debug("findOne Contact Id Null");
            throw new NullIdException("id " + id + " 为空");
        }
        return contactDao.getOne(id);
    }

    @Override
    public List<Contact> findAllContact() {
        return contactDao.findAll();
    }
}
