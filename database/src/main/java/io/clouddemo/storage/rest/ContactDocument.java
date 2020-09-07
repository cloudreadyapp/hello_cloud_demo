package io.clouddemo.storage.rest;

import com.cloudant.client.api.model.Document;
import com.google.gson.Gson;

public class ContactDocument extends Document {
	private String email = null;
	private String first_name = null;
	private String last_name = null;

	/* Setters are needed to set the document fields for serialization/deserialization */
	public void setEmail(String email) {
		this.email = email;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	/* For simplicity, use the contact's first and last name as the document identifier */
	public void setFirstLastNameAsId() {
		String newID = this.first_name + this.last_name;
		super.setId(newID);
	}

	/* First Name and Last Name are required fields for the ContactDocument */
	public boolean isEmpty() {
		boolean isEmpty = false;

		if (null == this.first_name && null == this.last_name) {
			isEmpty = true;
		}

		return isEmpty;
	}

	/* JSON formatting helper method */
	public String toJson() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}
}
