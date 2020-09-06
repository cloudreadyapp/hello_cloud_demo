window.addEventListener('load', () => {
	// addContact elements
	const add_contact_form = document.getElementById('add_contact_form');
	const add_contact_first_name_input = document.getElementById('add_contact_first_name_input');
	const add_contact_last_name_input = document.getElementById('add_contact_last_name_input');
	const add_contact_email_input = document.getElementById('add_contact_email_input');
	const add_contact_data_p = document.getElementById('add_contact_data_p');

	// getContact elements
	const get_contact_form = document.getElementById('get_contact_form');
	const get_contact_first_name_input = document.getElementById('get_contact_first_name_input');
	const get_contact_last_name_input = document.getElementById('get_contact_last_name_input');
	const get_contact_data_p = document.getElementById('get_contact_data_p');

	// Server Endpoints
	const serverEndpointPOSTurl = 'http://' + window.location.host + '/HelloCloudDemoProject/demo/database/store';
	const serverEndpointGETurl = 'http://' + window.location.host + '/HelloCloudDemoProject/demo/database/retrieve';

	// Add contact form submit listener
	add_contact_form.addEventListener('submit', (event) => {
		event.preventDefault(); // Do not execute the default event handling, since we want our custom HTTP request handling
		addContact();
	});

	// Get contact form submit listener
	get_contact_form.addEventListener('submit', (event) => {
		event.preventDefault(); // Do not execute the default event handling, since we want our custom HTTP request handling
		getContact();
	});

	// Add a contact to the database (first name, last name, email)
	async function addContact() {
		add_contact_data_p.innerText = 'Adding contact...';

		const data = {
			'first_name': add_contact_first_name_input.value,
			'last_name': add_contact_last_name_input.value,
			'email': add_contact_email_input.value
		};

		const response = await submitPOSTRequest(data);

		if (response.ok) {
			add_contact_data_p.innerText = 'Added to database!';
		} else {
			add_contact_data_p.innerText = response.status + ' ' + response.statusText;
		}
	}

	// Use Fetch API to send a POST request
	async function submitPOSTRequest(requestBody = {}) {
		return fetch(serverEndpointPOSTurl, {
			method: 'POST',
			headers: {
				'Content-Type': 'application/json'
			},
			body: JSON.stringify(requestBody)
		});
	}

	// Get a contact from the database (retrieve by first name and last name)
	async function getContact() {
		get_contact_data_p.innerText = 'Getting contact...';

		const url_search_params = new URLSearchParams({
			'first_name': get_contact_first_name_input.value,
			'last_name': get_contact_last_name_input.value
		});

		let response = await submitGETRequest(url_search_params);

		if (response.ok) {
			const contact_data = await response.json();
			get_contact_data_p.innerText = contact_data.first_name + ' ' + contact_data.last_name + '\n' + contact_data.email;
		} else {
			get_contact_data_p.innerText = response.status + ' ' + response.statusText;
		}
	}

	// Use Fetch API to send a GET request
	async function submitGETRequest(searchParameters = '') {
		return fetch(serverEndpointGETurl + '?' + searchParameters, {
			method: 'GET',
			headers: {
				'Accept': 'application/json'
			}
		});
	}
});
