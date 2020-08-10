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

import com.cloudant.client.api.model.Document;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.json.bind.annotation.JsonbCreator;
import java.util.List;

import com.google.gson.JsonParser;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonElement;

import javax.json.bind.annotation.JsonbProperty;

public class DemoDocument extends Document {

	public static final String PAYLOAD_PROPERTY_STRING = "payload";
	public static final String RANDOM_NUMBER_PROPERTY_STRING = "random_number";
	public static final String LOCATION_PROPERTY_STRING = "location";
	public static final String EMAIL_PROPERTY_STRING = "email";

	@JsonbProperty(EMAIL_PROPERTY_STRING)
	private String email = null;

	@JsonbProperty(PAYLOAD_PROPERTY_STRING)
	private String payload = null;

	@JsonbProperty(RANDOM_NUMBER_PROPERTY_STRING)
	private Integer random_number = null;

	@JsonbProperty(LOCATION_PROPERTY_STRING)
	private String location = null;

	public DemoDocument() {
	}

	/* For POST requests */
	@JsonbCreator
	public DemoDocument(
		@JsonbProperty(PAYLOAD_PROPERTY_STRING) String payload,
		@JsonbProperty(RANDOM_NUMBER_PROPERTY_STRING) Integer random_number,
		@JsonbProperty(LOCATION_PROPERTY_STRING) String location,
		@JsonbProperty(EMAIL_PROPERTY_STRING) String email
	) {
		this.payload = payload;
		this.random_number = random_number;
		this.location = location;
		this.email = email;
	}

	public void setPayload(String payload){
		this.payload = payload;
	}
		
	public void setRandom_number(Integer random_number){
		this.random_number = random_number;
	}
	
	public void setLocation(String location){
		this.location = location;
	}

	public void setEmail(String email){
		this.email = email;
	}

	public void setEmailAsId(){
		super.setId(this.email);
	}

	/**
	 * Set id for a non-partitioned document.
	 */
	@Override
	public void setId(String id){
		super.setId(id);
	}

	/**
	 * Used to strip the author ID from a partitioned document, so that
	 * only the document ID remains. The author ID is secure data, which
	 * should NOT get passed to the client.
	 */
	public void removeAuthorId() {
		String id = super.getId();
		int index = id.indexOf(":");
		if (-1 != index) {
			super.setId(id.substring(index + 1));
		}
	}

	public String toJson() {
		Gson gson = new Gson();
		return parseResponseJson(gson.toJson(this));
	}

	public String toJsonPrettyPrinting() {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		return gson.toJson(this);
	}

	/**
	 * Removes irrelevant fields from json object to be returned
	 * @param json String Json Object
	 * @return String Json object without irrelevant fields.
	 */
	private String parseResponseJson(String json) {
		final String _REV = "_rev";
		final String _DELETED = "_deleted";
		final String _ID = "_id";
		
		JsonParser parser = new JsonParser();

		/* Remove _rev and _deleted */
		JsonObject jsonObj = (JsonObject) parser.parse(json);
		JsonElement strObjRev = jsonObj.remove(_REV);
		JsonElement strObjDel = jsonObj.remove(_DELETED);

		/* Only remove _id if the document is non-partitioned */
		JsonElement strObjId = null;
		strObjId = jsonObj.remove(_ID);

		String result = jsonObj.toString();

		if (null != strObjRev) {
			System.out.println("Field removed: " + _REV);
		}
		if (null != strObjDel) {
			System.out.println("Field removed: " + _DELETED);
		}
		if (null != strObjId) {
			System.out.println("Field removed: " + _ID);
		}

		return result;
	}

	public boolean isEmpty() {
		boolean isEmpty = false;

		if (null == this.email) {
			isEmpty = true;	
		}

		return isEmpty;
	}

}
