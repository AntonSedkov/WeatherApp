package tpr.antonius.weatherapp.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import tpr.antonius.weatherapp.model.Weather;
import tpr.antonius.weatherapp.service.cache.WeatherCache;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service("weatherService")
public class WeatherAggregationService implements WeatherServiceAggregation {
    private static Logger logger = LoggerFactory.getLogger(WeatherAggregationService.class);

    private final List<WeatherService> weatherServices;
    private final WeatherCache weatherCache;

    public WeatherAggregationService(List<WeatherService> services, WeatherCache cache) {
        this.weatherServices = services;
        this.weatherCache = cache;
    }

    @Override
    public List<Weather> receiveAggregateWeather() {
        List<Weather> weathers = weatherCache.getValue().orElseGet(() -> {
            var weather = doRequest();
            weatherCache.putValue(weather);
            return weather;
        });
        return weathers;
    }

    private List<Weather> doRequest() {
        CompletableFuture<List<Weather>>[] weatherFutures = new CompletableFuture[weatherServices.size()];
        int idx = 0;
        for (WeatherService weatherService : weatherServices) {
            weatherFutures[idx++] = CompletableFuture.supplyAsync(weatherService::receiveWeather);
        }
        List<Weather> weathers;
        try {
            CompletableFuture<Object> completableFuture = CompletableFuture.anyOf(weatherFutures);
            weathers = (List<Weather>) completableFuture.get(30, TimeUnit.SECONDS);
        } catch (InterruptedException | TimeoutException | ExecutionException e) {
            logger.error(e.getMessage(), e);
            weathers = new ArrayList<>();
        }
        return weathers;
    }

}