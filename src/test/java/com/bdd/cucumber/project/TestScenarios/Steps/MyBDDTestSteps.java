package com.bdd.cucumber.project.TestScenarios.Steps;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import junit.framework.Assert;

public class MyBDDTestSteps {

	private String URL_WITHOUT_API_UNKNOWN = "https://samples.openweathermap.org/data/3.0/stations";
	private String URL_WITHOUT_API = "https://api.openweathermap.org/data/3.0/stations";
	private String MY_API_URL = "http://api.openweathermap.org/data/3.0/stations?appid=bbf4b7ff96cccfeb2041bc6b3f3fe078";
	private String APPID = "bbf4b7ff96cccfeb2041bc6b3f3fe078";
	private String URL = null;
	
	private final RestTemplate restTemplate = new RestTemplate();
	ResponseEntity<String> responseEntity;
	ResponseEntity<String> responseEntityPost1;
	ResponseEntity<String> responseEntityPost2;
	ResponseEntity<String> responseEntityGet;
	ResponseEntity<String> responseEntityDelete1;
	ResponseEntity<String> responseEntityDelete2;
	
	String data1JSON;
	String data2JSON;
	
	private static String storedId1;
	private static String storedId2;
	
	private static String doubleDeleteResponseCode;
	private static String doubleDeleteResponseMessage;
	
	@Given("I have wheather station URL")
	public void IKnowURL() {
		URL = URL_WITHOUT_API;
	}
	
	@Given("URL do not have a API key")
	public void url_do_not_have_a_api_key() {
		URL = URL_WITHOUT_API_UNKNOWN;
	}

	@And("URL have a API key")
	public void url_have_a_api_key() {
		URL = appendAppId(URL);
	}
	
	@When("I register a station")
	public void i_register_a_station() {
		responseEntity = restTemplate.getForEntity(URL, String.class);
	}
	
	@When("I register a station with APIID with above data")
	public void i_register_a_station_wit_appid_with_data () {
		
	    RestTemplate restTemplate = new RestTemplate();
	    HttpHeaders headers = new HttpHeaders();
	    
	    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    
	    HttpEntity<String> request1 = new HttpEntity<String>(data1JSON, headers);
	    HttpEntity<String> request2 = new HttpEntity<String>(data2JSON, headers);

		responseEntityPost1 = restTemplate.postForEntity(URL, request1, String.class);
		responseEntityPost2 = restTemplate.postForEntity(URL, request2, String.class);
	}

	@Then("I get the {string} response")
	public void i_get_the_response(String responseCodeExpected) {
		 
		String jObjString = responseEntity.getBody();
		JSONObject jObj = new JSONObject(jObjString);
		
		String responseCodeRetunred = String.valueOf(jObj.getNumber("cod"));
		Assert.assertEquals(responseCodeExpected, responseCodeRetunred);
		
	}
	
	@And("I get the {string} response indicates station saved")
	public void I_get_the_message_in_response(String expectedMessage) {
		String responseMessageRetunred = responseEntityPost1.getStatusCode().toString();
		Assert.assertEquals(expectedMessage, responseMessageRetunred);
		
		responseMessageRetunred = responseEntityPost2.getStatusCode().toString();
		Assert.assertEquals(expectedMessage, responseMessageRetunred);
	}
	
	@Given("I have to register the data as below")
	public void i_have_to_register_the_data_as_below(DataTable dataTable) {
		
		List<List<String>> dataRows = dataTable.asLists();
		int dataCount = 0;
		
		for (List<String> rows : dataRows) {
			String dataJSON = "{"
							+ "\"external_id\":\""+rows.get(0)+"\","
							+ "\"name\":\""+rows.get(1)+"\","
							+ "\"latitude\":"+rows.get(2)+","
							+ "\"longitude\":"+rows.get(3)+","
							+ "\"altitude\":"+rows.get(4)
						+ "}";
			
			if(dataCount ==1)
				data1JSON = dataJSON;
			if(dataCount == 2)
				data2JSON = dataJSON;
			
			dataCount++;
	    }
	}
	
	@Then("I get the message {string} in response")
	public void i_get_the_message_in_response(String expectedMessage) {
		String jObjString = responseEntity.getBody();
		JSONObject jObj = new JSONObject(jObjString);
		
		String responseMessageRetunred = jObj.getString("message");
		Assert.assertEquals(expectedMessage, responseMessageRetunred);
	}
	
	@Given("I have wheather station URL to get the station info")
	public void i_have_wheather_station_url_to_get_the_station_info() {
		URL = MY_API_URL;
	}
	

	@And("I have already registered few stations")
	public void i_have_already_registered_a_stations() {
	    // This is covered in step2
	}
	
	@When("I requested to get my registed stations")
	public void i_requested_to_get_my_registered_stations() {
		responseEntityGet = restTemplate.getForEntity(URL, String.class);
	}

	@Then("I get the details of registed stations with name contaning below two stations")
	public void i_get_the_details_of_registed_stations_with_name_contaning_below_two_stations(DataTable dataTable) {
	    
	    List<List<String>> dataRows = dataTable.asLists();
	    String name1 = dataRows.get(1).get(0);
	    String name2 = dataRows.get(2).get(0);
	    
	    List<String> myStationsList = new ArrayList<String>();
	    
	    String jObjString = responseEntityGet.getBody();
	    JSONArray jArray = new JSONArray(jObjString);
	    for(int i=0; i<jArray.length(); i++) {
	    	JSONObject jObj = jArray.getJSONObject(i);
	    	myStationsList.add(jObj.getString("name"));
	    }
	    
	    if(myStationsList.contains(name1) && myStationsList.contains(name2))
	    	assert(true);
	    else
	    	assert(false);
		
	}
	
	@When("I requested to delete my registed stations")
	public void i_requested_to_delete_my_registed_stations(DataTable dataTable) {
		List<List<String>> dataRows = dataTable.asLists();
		
	    String name1 = dataRows.get(1).get(0);
	    String name2 = dataRows.get(2).get(0);
	    
	    String URL1;
	    String URL2;
	    
	    responseEntityGet = restTemplate.getForEntity(URL, String.class);
	    String jObjString = responseEntityGet.getBody();
	    JSONArray jArray = new JSONArray(jObjString);
	    for(int i=0; i<jArray.length(); i++) {
	    	JSONObject jObj = jArray.getJSONObject(i);
	    	
	    	if(jObj.getString("name").equals(name1))
	    		storedId1 = jObj.getString("id");
	    	
	    	if(jObj.getString("name").equals(name2))
	    		storedId2 = jObj.getString("id");
	    }
	    
	    URL1 = appendAppId(URL_WITHOUT_API + "/" + storedId1);
	    URL2 = appendAppId(URL_WITHOUT_API + "/" + storedId2);
	    
	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    HttpEntity<Object> entity = new HttpEntity<Object>(headers);
	    
	    responseEntityDelete1 = restTemplate.exchange(URL1, HttpMethod.DELETE, entity, String.class);
	    responseEntityDelete2 = restTemplate.exchange(URL2, HttpMethod.DELETE, entity, String.class);

	}
	
	@Then("I get the {string} in response for delete operation")
	public void i_get_the_response_for_delete_operation(String expectedResponse) {
		Assert.assertEquals(expectedResponse,responseEntityDelete1.getStatusCode().toString());
		Assert.assertEquals(expectedResponse,responseEntityDelete2.getStatusCode().toString());
	}
	
	@And("I have already deleted few stations")
	public void i_have_already_deleted_few_stations() {
		// this is already done in step4
	}
	
	@When("I requested to delete my deleted stations")
	public void i_requested_to_delete_my_deleted_stations(DataTable dataTable) throws HttpClientErrorException{

	    String URL1;
	    String URL2;
 
	    URL1 = appendAppId(URL_WITHOUT_API + "/" + storedId1);
	    URL2 = appendAppId(URL_WITHOUT_API + "/" + storedId2);
	    
	    HttpHeaders headers = new HttpHeaders();
	    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    HttpEntity<Object> entity = new HttpEntity<Object>(headers);
	    
	    try {
		    responseEntityDelete1 = restTemplate.exchange(URL1, HttpMethod.DELETE, entity, String.class);
		    responseEntityDelete2 = restTemplate.exchange(URL2, HttpMethod.DELETE, entity, String.class);
	    }
	    catch(final HttpClientErrorException e) {
	    	String jObjString = e.getResponseBodyAsString();
			JSONObject jObj = new JSONObject(jObjString);
			doubleDeleteResponseMessage = jObj.getString("message");
	    	doubleDeleteResponseCode = e.getStatusCode().toString();
	    }

	}
	
	@Then("I get the {string} in response for another delete operation")
	public void i_get_the_response_for_another_delete_operation(String expectedResponse) {
		Assert.assertEquals(expectedResponse,doubleDeleteResponseCode);
		Assert.assertEquals(expectedResponse,doubleDeleteResponseCode);
	}
	
	@And("I get the message {string} for delete operation")
	public void i_get_the_message_for_delete_operation(String expectedMessage) {
		Assert.assertEquals(expectedMessage, doubleDeleteResponseMessage);
		Assert.assertEquals(expectedMessage, doubleDeleteResponseMessage);
	}
	
	public String appendAppId(String URL) {
		return URL+ "?appid=" + APPID; 
	}
}
