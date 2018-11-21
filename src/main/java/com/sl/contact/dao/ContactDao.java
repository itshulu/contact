package com.sl.contact.dao;

import com.sl.contact.entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 联系人Dao
 *
 * @author 舒露
 */
public interface ContactDao extends JpaRepository<Contact, String> {
}
