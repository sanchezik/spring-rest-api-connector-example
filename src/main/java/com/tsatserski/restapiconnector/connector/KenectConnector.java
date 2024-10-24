package com.tsatserski.restapiconnector.connector;

import com.tsatserski.restapiconnector.model.ContactKenectEntity;
import com.tsatserski.restapiconnector.model.ContactKenectEntityWrapper;
import com.tsatserski.restapiconnector.model.ContactItem;
import com.tsatserski.restapiconnector.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

@Component
public class KenectConnector {

    private final RestTemplate restTemplate;

    @Autowired
    public KenectConnector(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<ContactItem> retrieveContacts() {
        List<ContactItem> allContacts = new ArrayList<>();
        int page = 1;
        boolean morePages = true;

        while (morePages) {
            UriComponentsBuilder uriBuilder = UriComponentsBuilder
                    .fromHttpUrl(Constants.EXT_CONTACTS_URL)
                    .queryParam("page", page);

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + Constants.EXT_CONTACTS_TOKEN);

            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<ContactKenectEntityWrapper> responseEntity = restTemplate.exchange(
                    uriBuilder.toUriString(),
                    HttpMethod.GET,
                    entity,
                    ContactKenectEntityWrapper.class
            );

            if (responseEntity == null) {
                break;
            }
            ContactKenectEntityWrapper externalContacts = responseEntity.getBody();
            if (externalContacts != null && externalContacts.getContacts() != null && externalContacts.getContacts().size() > 0) {
                for (ContactKenectEntity externalContact : externalContacts.getContacts()) {
                    allContacts.add(convertToContact(externalContact));
                }
                if (externalContacts.getContacts().size() < Constants.PAGE_SIZE) {
                    morePages = false;
                }
            }
            page++;
        }

        return allContacts;
    }


    private ContactItem convertToContact(ContactKenectEntity externalContact) {
        ContactItem contact = new ContactItem();
        contact.setId(externalContact.getId());
        contact.setName(externalContact.getName());
        contact.setEmail(externalContact.getEmail());
        contact.setSource(Constants.EXT_CONTACTS_ID);
        contact.setCreatedAt(externalContact.getCreatedAt());
        contact.setUpdatedAt(externalContact.getUpdatedAt());
        return contact;
    }
}
