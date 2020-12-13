package tpr.antonius.weatherapp.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tpr.antonius.weatherapp.model.Weather;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/*@RequiredArgsConstructor*/
@Service
public class YandexWeatherService implements WeatherService {
    private static final String WEATHER_PRODUCER = "YandexWeather";
    private static final String TARGET_URL = "https://yandex.ru/pogoda/%s";
    private static final String TEMPERATURE_TAG = ".temp__value";

    private static Logger logger = LoggerFactory.getLogger(YandexWeatherService.class);

    /* @Value("${app.city-name}")*/
    private String cityName;

    public YandexWeatherService(
            @Value("${app.city-name}")
                    String cityName) {
        this.cityName = cityName;
    }

    @Override
    public List<Weather> receiveWeather() {
        List<Weather> weathers;
        try {
            logger.info("Yandex performing request...");
            //Thread.sleep(500);
            Document document = Jsoup.connect(String.format(TARGET_URL, cityName)).get();
            Element temperatureValue = document.selectFirst(TEMPERATURE_TAG);
            logger.info("Yandex request done.");
            weathers = List.of(new Weather(WEATHER_PRODUCER, cityName, temperatureValue.text()));
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            weathers = new ArrayList<>();
        }
        return weathers;
    }

}