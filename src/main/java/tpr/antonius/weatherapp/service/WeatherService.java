package tpr.antonius.weatherapp.service;

import tpr.antonius.weatherapp.model.Weather;

import java.util.List;

public interface WeatherService {
    List<Weather> receiveWeather();
}