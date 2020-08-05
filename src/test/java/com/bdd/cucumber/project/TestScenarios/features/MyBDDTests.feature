#Author: dikshakalbhor4@gmail.com

@WheatherInfo
Feature: I want to to register a weather station

  @WheatherInfoTest1
  Scenario: I want to to register a weather station without appid
    Given I have wheather station URL
    And URL do not have a API key
    When I register a station
    Then I get the "401" response
    And I get the message "Invalid API key. Please see http://openweathermap.org/faq#error401 for more info." in response 

	@WheatherInfoTest2
	Scenario: I have appid and I want to to register a weather station
		Given  I have wheather station URL
    And URL have a API key
    And I have to register the data as below
			|external_id	|name			|latitude	|longitude	|altitude	|
			|DEMO_TEST001	|Test01 19756	|33.33		|-111.43	|444		|
			|DEMO_TEST999	|Test99 59756	|33.44		|-12.44		|444		|
		When I register a station with APIID with above data
    Then I get the "201" response indicates station saved

	@WheatherInfoTest3
	Scenario: I want to to check the registedred stations
		Given  I have wheather station URL
		And URL have a API key
		And I have already registered few stations
		When I requested to get my registed stations
		Then I get the details of registed stations with name contaning below two stations
			|name			|
			|Test01 19756	|
			|Test99 59756	|
	
	@WheatherInfoTest4
	Scenario: I want to delete my registedred stations
		Given  I have wheather station URL
		And URL have a API key
		And I have already registered few stations
		When I requested to delete my registed stations
			|name			|
			|Test01 19756	|
			|Test99 59756	|
		Then I get the "204" in response for delete operation 
			
	@WheatherInfoTest5
	Scenario: I want to delete my already deleted stations
		Given  I have wheather station URL
		And URL have a API key
		And I have already deleted few stations
		When I requested to delete my deleted stations
			|name			|
			|Test01 19756	|
			|Test99 59756	|

		Then I get the "404" in response for another delete operation
		And I get the message "Station not found" for delete operation