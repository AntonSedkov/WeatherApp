package tpr.antonius.weatherapp.service;

import org.springframework.stereotype.Service;
import tpr.antonius.weatherapp.model.Weather;

import java.util.ArrayList;
import java.util.List;

@Service
public class WeatherAggregationService implements WeatherService {

    private final List<WeatherService> weatherServices;

    public WeatherAggregationService(List<WeatherService> services) {
        this.weatherServices = services;
    }

    @Override
    public List<Weather> receiveWeather() {
        List<Weather> weathers = new ArrayList<>();
        weatherServices.forEach(
                ws -> weathers.addAll(ws.receiveWeather()));
        return weathers;
    }
}
