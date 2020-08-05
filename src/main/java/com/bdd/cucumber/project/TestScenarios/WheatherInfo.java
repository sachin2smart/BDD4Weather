package com.bdd.cucumber.project.TestScenarios;

public class WheatherInfo {
	public static String urlWithoutAPI = "https://samples.openweathermap.org/data/3.0/stations";
	
	public static String urlWithAPI = "https://samples.openweathermap.org/data/3.0/stations?appid=b1b15e88fa797225412429c1c50c122a1";

	public static String getUrlWithoutAPI() {
		return urlWithoutAPI;
	}

	public static void setUrlWithoutAPI(String urlWithoutAPI) {
		WheatherInfo.urlWithoutAPI = urlWithoutAPI;
	}

	public static String getUrlWithAPI() {
		return urlWithAPI;
	}

	public static void setUrlWithAPI(String urlWithAPI) {
		WheatherInfo.urlWithAPI = urlWithAPI;
	}
	
	
}
