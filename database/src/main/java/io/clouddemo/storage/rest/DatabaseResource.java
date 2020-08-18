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

package io.clouddemo.storage.rest;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.google.gson.JsonParser;
import com.google.gson.JsonObject;
import com.google.gson.JsonElement;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;

import com.cloudant.client.api.CloudantClient;
import com.cloudant.client.api.Database;
import com.cloudant.client.api.query.QueryBuilder;
import com.cloudant.client.api.query.QueryResult;
import com.cloudant.client.api.query.Selector;
import com.cloudant.client.api.ClientBuilder;
import static com.cloudant.client.api.query.Operation.*;
import static com.cloudant.client.api.query.Expression.*;
import com.cloudant.client.org.lightcouch.DocumentConflictException;
import com.cloudant.client.org.lightcouch.NoDocumentException;
import com.cloudant.client.api.model.Response;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Path("database")
@ApplicationScoped
public class DatabaseResource {

	/***************************************************************************************************************/
	/************************************************ DATABASE CODE ************************************************/
	/***************************************************************************************************************/
	@Inject
	@ConfigProperty(name = "cloudantapikey")
	private String cloudantapikey;

	private final String DEMO_DATABASE = "cloud-app"; /**< Databse name */
	private final String END_OF_LOG_STRING = "---------------------------------------------------------------------------------------";

	/* Modify databaseEndpoint to point to your Cloudant EndPoint */
	private final String databaseEndpoint = "https://4b9321a6-d76e-4e55-8dec-ee0b88021030-bluemix.cloudantnosqldb.appdomain.cloud";
	private CloudantClient cloudantClient;

	private String getUUID() {
		String uuid = "";

		if (null != this.cloudantClient) {
			int numberOfUUIDs = 1;
			List<String> uuids = this.cloudantClient.uuids(numberOfUUIDs);
			uuid = uuids.get(0);
		}
		
		return uuid;
	}

	// Should only need to do this once, then store the db so it can be used for any database query
	protected Database connectToDatabase(final String databaseName) {

		URL databaseURL = null;
		try {
			databaseURL = new URL(databaseEndpoint);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		// Connect to Cloudant account
		this.cloudantClient = ClientBuilder.url(databaseURL)
			 .iamApiKey(this.cloudantapikey)
			 .disableSSLAuthentication()
			 .build();

		// Connect to database - false means don't create the database if it doesn't already exist
		Database db = this.cloudantClient.database(databaseName, false);

		System.out.println("Connected to Cloudant database");

		return db;
	}

	/***************************************************************************************************************/

	/***************************************************************************************************************/
	/************************************************* REST APIS **************************************************/
	/***************************************************************************************************************/

	/* curl http://localhost:9080/HelloCloudDemoProject/demo/database/retrieve/4df99c2c31bd6bde116cffc3bc13ae6e */
	@GET
    @Path("retrieve")
    @Produces(MediaType.APPLICATION_JSON)
    public String getFromDatabase(
			@QueryParam("firstName") final String firstName,
			@QueryParam("lastName") final String lastName) {
		Database db = connectToDatabase(DEMO_DATABASE); // DATABASE_CODE
		String documentId = firstName + lastName;

        try {
			// Find document in database
            DemoDocument document = db.find(DemoDocument.class, documentId); // DATABASE_CODE
            System.out.println("Retrieved " + documentId + " from database");

            return document.toJson();
        } catch (NoDocumentException e) {
            e.printStackTrace();
        } 	
        
        return "Document " + documentId + " not found";
	}
	
	@POST
	@Path("store/document")
	@Consumes(MediaType.APPLICATION_JSON)
	public String storeDocument(
		DemoDocument document
	) {
		// TODO: Make sure document is well formed
		String doc = document.toJson();
		String result = "Failed storing document to database";

		if (!document.isEmpty()) {
			Database db = connectToDatabase(DEMO_DATABASE); // DATABASE_CODE

			logDocument(document, "Store Document");
			/* Overides _id generated by Cloudant (this is a non-partitioned database) 
			* This way you need to make sure that, there's no duplicate _id's in the database */
			document.setFirstLastNameAsId();

			Response response = db.save(document); // DATABASE_CODE
			if (response.getError() != null) {
				System.out.println("Document store FAILED. Error message: " + response.getError());
			} else {
				result = document.toJson();
			}
		}

		return result;
	}

	/**
	 * Log the json content of a document with the specified title.
	 * 
	 * @param doc The document to log.
	 * @param title The title of the log.
	 */
	private void logDocument(DemoDocument doc, String title) {
		System.out.println(title);
		System.out.println(doc.toJsonPrettyPrinting());
		System.out.println(END_OF_LOG_STRING);
	}
}
