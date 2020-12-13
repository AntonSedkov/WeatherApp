package tpr.antonius.weatherapp.controller;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import tpr.antonius.weatherapp.model.Weather;
import tpr.antonius.weatherapp.service.WeatherService;

import java.util.List;

@RestController
public class WeatherRestController {

    private final WeatherService weatherService;

    public WeatherRestController(
            @Qualifier("weatherAggregationService")
                    WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("api/weather")
    public List<Weather> getWeather() {
        return weatherService.receiveWeather();
    }

}