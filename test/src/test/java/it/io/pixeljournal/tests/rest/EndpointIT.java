/*******************************************************************************
 * Copyright (c) 2020, 2020 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 *******************************************************************************/

package it.io.clouddemo.tests.rest;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled; // Use the annotation @Disabled to skip a test (put next to @Test)
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Calendar;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class EndpointIT {
	private static final Jsonb jsonb = JsonbBuilder.create();
	private static String databasePort;
	private static String databaseContext;
	private static String databaseUrl;

	private Client client;
	private Response response;

	@BeforeAll
	public static void oneTimeSetup() {
		// TODO: Test with hosted url
		databasePort = System.getProperty("database.http.port");
		databaseContext = System.getProperty("context.database.root");
		databaseUrl = "http://localhost:" + databasePort + "/" + databaseContext + "/";
	}

	@BeforeEach
	public void setupClient(TestInfo testInfo) {
		System.out.println("******" + "\n" + testInfo.getDisplayName() + ": BEGIN\n" + "********" + "\n");
		client = ClientBuilder.newClient();
	}

	@AfterEach
	public void tearDown(TestInfo testInfo) {
		response.close();
		client.close();
		System.out.println("\n***\n" + testInfo.getDisplayName() + ": END\n***\n");
	}

	/******************************************************
	 * Tests for Hello Cloud Demo
	 ******************************************************/

	@Test
	public void testStoreDocument() {
		WebTarget store = client.target(databaseUrl + "demo/database/store/document");

		String document =
			"{\n" +
			"\"payload\":\"This is some payload\",\n" +
			"\"random_number\":98765,\n" +
			"\"location\":\"Miami, USA\",\n" +
			"\"email\":\"someAnotherEmail@some.ca\"\n" +
			"}";

		System.out.println("Storing document: " + document);

		response = store.request()
			.post(Entity.json(document));
		assertResponse(databaseUrl, response);

		String store_json = response.readEntity(String.class);
		System.out.println("Message received from target:\n" + store_json);
	}

	@Test
	public void testGetDocument() {
		System.out.println("Getting document...");

		WebTarget retrieve = client.target(databaseUrl + "demo/database/retrieve/randomEmail@123.ca");
		response = retrieve.request()
			.get();
		assertResponse(databaseUrl, response);

		String retrieved_json = response.readEntity(String.class);
		System.out.println("Message received from target:\n" + retrieved_json);
	}

	/******************************************************
	 * Helper functions
	 ******************************************************/

	private void assertResponse(String url, Response response) {
		assertEquals(Response.Status.OK.getStatusCode(), response.getStatus(), "Incorrect response code from " + url);
	}

	/** 
	 * Retrieves documentId from a json document
	 * @param json json document
	 * @return _id related to json document 
	 */
	private String getDocumentIdHelper(String json) {
		JsonParser parser = new JsonParser();
		JsonObject jsonObj = (JsonObject) parser.parse(json);
		JsonElement strObj = jsonObj.get("_id");
		return strObj.getAsString();
	}
}
