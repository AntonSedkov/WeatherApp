package tpr.antonius.weatherapp.service.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import tpr.antonius.weatherapp.model.Weather;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class WeatherCache implements Cache<List<Weather>> {
    private static Logger logger = LoggerFactory.getLogger(WeatherCache.class);

    private List<Weather> weatherCache;

    public WeatherCache() {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(this::resetCache, 0, 10, TimeUnit.SECONDS);
    }

    @Override
    public synchronized void putValue(List<Weather> value) {
        this.weatherCache = value;
    }

    @Override
    public synchronized Optional<List<Weather>> getValue() {
        return Optional.ofNullable(weatherCache);
    }

    @Override
    public synchronized List<Weather> getValueNull() {
        return weatherCache;
    }

    private synchronized void resetCache() {
        logger.info("resetCache");
        weatherCache = null;
    }

}