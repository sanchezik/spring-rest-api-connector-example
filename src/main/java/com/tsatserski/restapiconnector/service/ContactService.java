package com.tsatserski.restapiconnector.service;

import com.tsatserski.restapiconnector.model.ContactItem;

import java.util.List;

public interface ContactService {

    List<ContactItem> getAllContacts();

}
