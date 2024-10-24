package com.tsatserski.restapiconnector.service;

import com.tsatserski.restapiconnector.model.ContactEntity;
import com.tsatserski.restapiconnector.model.ContactEntityWrapper;
import com.tsatserski.restapiconnector.model.ContactItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

@Service
public class ContactServiceImpl implements ContactService {

    private final RestTemplate restTemplate;

    private final String externalUrl = "https://k-messages-api.herokuapp.com/api/v1/contacts";
    private final String token = "J7ybt6jv6pdJ4gyQP9gNonsY";
    private final int pageSize = 20;
    private final String constantValue = "KENECT_LABS";

    @Autowired
    public ContactServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public List<ContactItem> getAllContacts() {
        List<ContactItem> allContacts = new ArrayList<>();
        int page = 1;
        boolean morePages = true;

        while (morePages) {
            UriComponentsBuilder uriBuilder = UriComponentsBuilder
                    .fromHttpUrl(externalUrl)
                    .queryParam("page", page);

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + token);

            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<ContactEntityWrapper> responseEntity = restTemplate.exchange(
                    uriBuilder.toUriString(),
                    HttpMethod.GET,
                    entity,
                    ContactEntityWrapper.class
            );

            ContactEntityWrapper externalContacts = responseEntity.getBody();

            if (externalContacts != null && externalContacts.getContacts() != null && externalContacts.getContacts().size() > 0) {
                for (ContactEntity externalContact : externalContacts.getContacts()) {
                    allContacts.add(convertToContact(externalContact));
                }
                if (externalContacts.getContacts().size() < 20) {
                    morePages = false;
                }
            }
            page++;
        }

        return allContacts;
    }

    private ContactItem convertToContact(ContactEntity externalContact) {
        ContactItem contact = new ContactItem();
        contact.setId(externalContact.getId());
        contact.setName(externalContact.getName());
        contact.setEmail(externalContact.getEmail());
        contact.setSource(constantValue);
        contact.setCreatedAt(externalContact.getCreatedAt());
        contact.setUpdatedAt(externalContact.getUpdatedAt());
        return contact;
    }
}
