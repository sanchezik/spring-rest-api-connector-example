package com.tsatserski.restapiconnector;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.tsatserski.restapiconnector.connector.KenectConnector;
import com.tsatserski.restapiconnector.model.ContactItem;
import com.tsatserski.restapiconnector.model.ContactKenectEntity;
import com.tsatserski.restapiconnector.model.ContactKenectEntityWrapper;
import com.tsatserski.restapiconnector.util.Constants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class KenectConnectorTest {
    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private KenectConnector kenectConnector;

    private final String url = Constants.EXT_CONTACTS_URL;

    @BeforeEach
    void setup() {
        // Reset all mocks before each test
        reset(restTemplate);
    }

    @Test
    void testSuccess() {
        // Mock response from the external service
        ContactKenectEntity contact1 = new ContactKenectEntity(1L, "jmadsen");
        ContactKenectEntity contact2 = new ContactKenectEntity(2L, "Jalisa Quigley");

        ContactKenectEntityWrapper firstPage = new ContactKenectEntityWrapper();
        firstPage.setContacts(Arrays.asList(contact1, contact2));

        ContactKenectEntityWrapper secondPage = new ContactKenectEntityWrapper();  // No contacts on the second page

        // Mock the RestTemplate response
        when(restTemplate.exchange(
                eq(url + "?page=1"),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(ContactKenectEntityWrapper.class))
        ).thenReturn(ResponseEntity.ok(firstPage));

        when(restTemplate.exchange(
                eq(url + "?page=2"),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(ContactKenectEntityWrapper.class))
        ).thenReturn(ResponseEntity.ok(secondPage));

        List<ContactItem> contacts = kenectConnector.retrieveContacts();
        assertEquals(2, contacts.size());
        assertEquals("jmadsen", contacts.get(0).getName());
        assertEquals(1L, contacts.get(0).getId());
        assertEquals("Jalisa Quigley", contacts.get(1).getName());
    }

    @Test
    void testEmptyResponse() {
        ContactKenectEntityWrapper emptyPage = new ContactKenectEntityWrapper();
        emptyPage.setContacts(new ArrayList<>());
        when(restTemplate.exchange(
                eq(url + "?page=1"),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(ContactKenectEntityWrapper.class))
        ).thenReturn(ResponseEntity.ok(emptyPage));

        List<ContactItem> contacts = kenectConnector.retrieveContacts();
        assertTrue(contacts.isEmpty());
    }

    @Test
    void testFailure() {
        when(restTemplate.exchange(
                eq(url + "?page=1"),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(ContactKenectEntityWrapper.class))
        ).thenThrow(new RuntimeException("API Failure"));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            kenectConnector.retrieveContacts();
        });

        assertEquals("API Failure", exception.getMessage());
    }

}
