package tpr.antonius.weatherapp.service;

import lombok.SneakyThrows;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tpr.antonius.weatherapp.model.Weather;

import java.util.List;

/*@RequiredArgsConstructor*/
@Service
public class YandexWeatherService implements WeatherService {

    private static final String WEATHER_PRODUCER = "YandexWeather";
    private static final String TARGET_URL = "https://yandex.ru/pogoda/%s";
    private static final String TEMPERATURE_TAG = ".temp__value";

    /* @Value("${app.city-name}")*/
    private String cityName;

    public YandexWeatherService(
            @Value("${app.city-name}")
                    String cityName) {
        this.cityName = cityName;
    }

    @SneakyThrows
    @Override
    public List<Weather> receiveWeather() {
        Document document = Jsoup.connect(String.format(TARGET_URL, cityName)).get();
        Element temperatureValue = document.selectFirst(TEMPERATURE_TAG);
        List<Weather> weathers = List.of(new Weather(WEATHER_PRODUCER, cityName, temperatureValue.text()));
        return weathers;
    }

}