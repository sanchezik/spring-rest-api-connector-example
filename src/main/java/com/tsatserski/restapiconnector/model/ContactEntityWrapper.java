package com.tsatserski.restapiconnector.model;

import java.util.List;

public class ContactEntityWrapper {
    private List<ContactEntity> contacts;

    public List<ContactEntity> getContacts() {
        return contacts;
    }

    public void setContacts(List<ContactEntity> contacts) {
        this.contacts = contacts;
    }
}
