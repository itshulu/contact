package com.sl.contact.service;

import com.sl.contact.entity.Contact;
import com.sl.contact.exception.NullIdException;

import java.util.List;

/**
 * 联系人服务
 * @author 舒露
 */
public interface ContactService {
    /**
     * 添加contact
     *
     * @param contact 联系人
     */
    Contact saveContact(Contact contact) throws NullIdException;

    /**
     * 删除contact
     *
     * @param id contactID
     * @throws NullIdException id不存在时抛出异常
     */

    void delContact(String id) throws NullIdException;

    /**
     * 修改contact
     *
     * @param contact 联系人
     * @throws NullIdException id不存在时抛出异常
     */
    Contact updateContact(Contact contact) throws NullIdException;

    /**
     * 根据id查contact
     *
     * @param id contactID
     * @return contact
     * @throws NullIdException id不存在时抛出异常
     */
    Contact findOne(String id) throws NullIdException;

    /**
     * findAllContact
     *
     * @return contactList
     */
    List<Contact> findAllContact();
}
