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

	public static final String EMAIL_PROPERTY_STRING = "email";
	public static final String FIRSTNAME_PROPERTY_STRING = "first_name";
	public static final String LASTNAME_PROPERTY_STRING = "last_name";

	@JsonbProperty(EMAIL_PROPERTY_STRING)
	private String email = null;

	@JsonbProperty(FIRSTNAME_PROPERTY_STRING)
	private String first_name = null;

	@JsonbProperty(LASTNAME_PROPERTY_STRING)
	private String last_name = null;

	public DemoDocument() {
	}

	/* For POST requests */
	@JsonbCreator
	public DemoDocument(
		@JsonbProperty(EMAIL_PROPERTY_STRING) String email,
		@JsonbProperty(FIRSTNAME_PROPERTY_STRING) String first_name,
		@JsonbProperty(LASTNAME_PROPERTY_STRING) String last_name
	) {
		this.email = email;
		this.first_name = first_name;
		this.last_name = last_name;
	}

	public void setEmail(String email){
		this.email = email;
	}

	public void setFirstLastNameAsId(){
		String newID = this.first_name + this.last_name;
		super.setId(newID);
	}

	public void setFirst_name(String first_name){
		this.first_name = first_name;
	}

	public void setLast_name(String last_name){
		this.last_name = last_name;
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

		if (null == this.first_name && null == this.last_name) {
			isEmpty = true;	
		}

		return isEmpty;
	}

}
