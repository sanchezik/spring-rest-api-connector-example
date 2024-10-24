package com.tsatserski.restapiconnector.model;

import java.util.List;

public class ContactKenectEntityWrapper {
    private List<ContactKenectEntity> contacts;

    public List<ContactKenectEntity> getContacts() {
        return contacts;
    }

    public void setContacts(List<ContactKenectEntity> contacts) {
        this.contacts = contacts;
    }
}
