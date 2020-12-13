package tpr.antonius.weatherapp.service;


import lombok.RequiredArgsConstructor;
import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tpr.antonius.weatherapp.model.Weather;
import tpr.antonius.weatherapp.service.dto.OpenWeatherDto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Service
public class OpenWeatherService implements WeatherService {
    private static final String WEATHER_PRODUCER = "OpenWeather";
    private static final String TARGET_URL = "https://api.openweathermap.org/data/2.5/weather?q=%s&units=metric&lang=ru&appid=%s";

    private static Logger logger = LoggerFactory.getLogger(OpenWeatherService.class);

    private final RestTemplate restTemplate;

    @Value("${app.openweather-api-key}")
    private String apiKey;

    @Value("${app.city-name}")
    private String cityName;

    @Override
    public List<Weather> receiveWeather() {
        logger.info("OpenWeather performing request...");
        val url = String.format(TARGET_URL, cityName, apiKey);
        var dto = restTemplate.getForObject(url, OpenWeatherDto.class);
        logger.info("OpenWeather request done.");
        List<Weather> weathers = (dto != null)
                ? Collections.singletonList(toModel(dto))
                : new ArrayList<>();
        return weathers;
    }

    private Weather toModel(OpenWeatherDto dto) {
        return new Weather(WEATHER_PRODUCER, cityName, dto.getMain().getTemperature());
    }

    /*
    private final Gson gson = new GsonBuilder().registerTypeAdapter(
        Weather.class, new JsonDeserializer<Weather>() {
            @Override
            public Weather deserialize(JsonElement jsonElement, Type type,
            JsonDeserializationContext jsonDeserializationContext) {
                JsonObject main = jsonElement.getAsJsonObject().getAsJsonObject("main");
                return new Weather("OpenWeatherMap", cityName, main.get("temp").getAsString());
        }
    }).create();

    @Override
    public List<Weather> getWeather() {
        logger.info("Open performing request...");
        String url = String.format("https://api.openweathermap.org/data/2.5/weather?q=%s&units=metric&lang=ru&appid=%s", cityName, apiKey);
        String weatherString = restTemplate.getForObject(url, String.class);
        logger.info("Open request done.");
        return List.of(gson.fromJson(weatherString, Weather.class));
    }
    * */

}