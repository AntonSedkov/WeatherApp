package tpr.antonius.weatherapp.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import tpr.antonius.weatherapp.model.Weather;
import tpr.antonius.weatherapp.service.cache.WeatherCache;

import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service("weatherServiceNull")
public class WeatherAggregationServiceNull implements WeatherServiceAggregation {
    private static Logger logger = LoggerFactory.getLogger(WeatherAggregationServiceNull.class);

    private final List<WeatherService> weatherServices;
    private final WeatherCache weatherCache;
    private final BlockingQueue<List<Weather>> weatherQueue;
    private final ExecutorService executor;

    public WeatherAggregationServiceNull(List<WeatherService> services, WeatherCache cache) {
        this.weatherServices = services;
        this.weatherCache = cache;
        weatherQueue = new ArrayBlockingQueue<>(weatherServices.size());
        executor = Executors.newFixedThreadPool(weatherServices.size());
    }

    @Override
    public List<Weather> receiveAggregateWeather() {
        List<Weather> weatherList;
        try {
            weatherList = weatherCache.getValueNull();
            if (weatherList == null) {
                var weather = doRequest();
                weatherCache.putValue(weather);
                weatherList = weather;
            }
        } catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
            weatherList = new ArrayList<>();
        }
        return weatherList;
    }

    private List<Weather> doRequest() throws InterruptedException {
        weatherQueue.clear();
        for (WeatherService weatherService : weatherServices) {
            executor.submit(() -> {
                var result = weatherQueue.offer(weatherService.receiveWeather());
                logger.debug("weatherQueue.offer result:{}", result);
            });
        }
        return weatherQueue.take();
    }

    @PreDestroy
    public void destroy() {
        executor.shutdownNow();
    }

}