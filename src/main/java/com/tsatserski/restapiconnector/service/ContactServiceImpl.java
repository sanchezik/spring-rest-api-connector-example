package com.tsatserski.restapiconnector.service;

import com.tsatserski.restapiconnector.connector.KenectConnector;
import com.tsatserski.restapiconnector.model.ContactItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactServiceImpl implements ContactService {

    private final KenectConnector kenectConnector;

    @Autowired
    public ContactServiceImpl(KenectConnector kenectConnector) {
        this.kenectConnector = kenectConnector;
    }

    @Override
    public List<ContactItem> getAllContacts() {
        return kenectConnector.retrieveContacts();
    }

}
