package com.kiseru.fix.fixtask.controllers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URI;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class CountControllerTest {

    private static final String URI_TEMPLATE = "%s?width=%d&height=%d&start=%s&end=%s";
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testGetMoves() {
        var uri = String.format(URI_TEMPLATE, CountController.URI, 14, 10, "A3", "B1");
        var responseEntity = restTemplate.getForEntity(URI.create(uri), String.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void testGetMovesWithoutOneParameter() {
        var uriWithoutWidthParameter = String.format("%s?&height=%d&start=%s&end=%s", CountController.URI, 10, "A3", "B1");
        assertEquals(HttpStatus.BAD_REQUEST,
                restTemplate.getForEntity(URI.create(uriWithoutWidthParameter), String.class).getStatusCode());

        var uriWithoutHeightParameter = String.format("%s?width=%d&start=%s&end=%s", CountController.URI, 14, "A3", "B1");
        assertEquals(HttpStatus.BAD_REQUEST,
                restTemplate.getForEntity(URI.create(uriWithoutHeightParameter), String.class).getStatusCode());

        var uriWithoutStartParameter = String.format("%s?width=%d&height=%d&end=%s", CountController.URI, 14, 10, "B1");
        assertEquals(HttpStatus.BAD_REQUEST,
                restTemplate.getForEntity(URI.create(uriWithoutStartParameter), String.class).getStatusCode());

        var uriWithoutEndParameter = String.format("%s?width=%d&height=%d&start=%s", CountController.URI, 14, 10, "A3");
        assertEquals(HttpStatus.BAD_REQUEST,
                restTemplate.getForEntity(URI.create(uriWithoutEndParameter), String.class).getStatusCode());
    }

    @Test
    public void testGetMovesWithInvalidParameter() {
        var uriWithWrongParameter = String.format("%s?width=%s&height=%d&start=%s&end=%s", CountController.URI, "A", 10,
                "A3", "B1");
        var responseEntity = restTemplate.getForEntity(URI.create(uriWithWrongParameter), String.class);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }


    @Test
    public void testGetMovesWithInvalidStartPosition() {
        var uriWithInvalidStartPosition = String.format(URI_TEMPLATE, CountController.URI, 3, 3, "A4", "B1");
        var responseEntity = restTemplate.getForEntity(URI.create(uriWithInvalidStartPosition), String.class);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void testGetMovesWithInvalidEndPosition() {
        var uriWithInvalidEndPosition = String.format(URI_TEMPLATE, CountController.URI, 3, 3, "A1", "B4");
        var responseEntity = restTemplate.getForEntity(URI.create(uriWithInvalidEndPosition), String.class);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }
}