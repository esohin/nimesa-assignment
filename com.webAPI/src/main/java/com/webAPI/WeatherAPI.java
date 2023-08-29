package com.webAPI;

import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherAPI {

    private static final String API_URL = "https://samples.openweathermap.org/data/2.5/forecast/hourly?q=London,us&appid=b6907d289e10d714a6e88b30761fae22";

    private static void printWeather(JSONObject ans) {
    	 JSONObject mainData = ans.getJSONObject("main");
         double temperature = mainData.getDouble("temp");
    	 System.out.println("Temperature: "+ temperature);
    }

    private static void printWind(JSONObject ans) {
        JSONObject windData = ans.getJSONObject("wind");
        double speed = windData.getDouble("speed");
        double direction = windData.getDouble("deg");
        System.out.println("Speed: " + speed + "\nDirection: " + direction + "\n");
    }

    private static void printPressure(JSONObject ans) {
        JSONObject mainData = ans.getJSONObject("main");
        double seaLevelPressure = mainData.getDouble("sea_level");
        double groundLevelPressure = mainData.getDouble("grnd_level");
        System.out.println("Pressure\n\tSea Level: " + seaLevelPressure + "\n\tGround Level: " + groundLevelPressure + "\n");
    }

    private static void callApi(String choice, String query) throws Exception {
        URL url = new URL(API_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {//need to change
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                JSONObject jsonResponse = new JSONObject(response.toString());
                JSONArray list = jsonResponse.getJSONArray("list");

                for (int i = 0; i < list.length(); i++) {
                    JSONObject ans = list.getJSONObject(i);
                    if (ans.getString("dt_txt").equals(query)) {
                        if (choice.equals("1")) {
                            printWeather(ans);
                        } else if (choice.equals("2")) {
                            printWind(ans);
                        } else if (choice.equals("3")) {
                            printPressure(ans);
                        } else {
                            System.out.println("Data not Found\n");
                        }
                        break;
                    }
                }
            }
        } else {
            System.out.println("Request failed with HTTP error code: " + connection.getResponseCode());
        }
        connection.disconnect();
    }
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Options:");
            System.out.println("1. Get weather");
            System.out.println("2. Get Wind Speed");
            System.out.println("3. Get Pressure");
            System.out.println("0. Exit");

            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine();

            if (choice.equals("0")) {
                break;
            }

            System.out.print("Enter the date (yyyy-MM-dd): ");
            String date = scanner.nextLine();
            System.out.print("Enter the time (HH:mm:ss): ");
            String time = scanner.nextLine();
            String query = date + " " + time;

            callApi(choice, query);
        }

        scanner.close();
    }
}
