package com.sl.contact;

import com.sl.contact.entity.Contact;
import com.sl.contact.exception.NullIdException;
import com.sl.contact.service.ContactService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ContactApplicationTests {
    @Autowired
    private ContactService contactService;

    @Test
    public void contextLoads() {
        List<Contact> allContact = contactService.findAllContact();
        allContact.forEach(System.out::println);
    }

    @Test
    public void testFindOne() throws NullIdException {
        Contact one = contactService.findOne("1");
        System.out.println(one);
    }
    @Test
    public void testSave() throws NullIdException {
        Contact contact = new Contact();
        contact.setName("8");
        contact.setTel("7");
        contact.setId("1");
        contactService.updateContact(contact);
        Contact one = contactService.findOne("1");
        Assert.assertEquals("asa",contact.getName(),one.getName());
        Assert.assertEquals("asa",contact.getTel(),one.getTel());
    }
}
