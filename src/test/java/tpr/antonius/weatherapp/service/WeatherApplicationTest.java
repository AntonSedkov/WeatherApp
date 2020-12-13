package tpr.antonius.weatherapp.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import tpr.antonius.weatherapp.model.Weather;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WeatherApplicationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @MockBean
    private OpenWeatherService openWeatherService;

    @MockBean
    private YandexWeatherService yandexWeatherService;

    private final Gson gson = new Gson();
    private final Type listType = new TypeToken<ArrayList<Weather>>() {
    }.getType();

    private static final List<Weather> openWeather = new ArrayList<>();
    private static final List<Weather> yandexWeather = new ArrayList<>();

    private static void prepareData() {
        openWeather.add(new Weather("open", "Msk", "1"));
        yandexWeather.add(new Weather("ydx", "Mnk", "2"));
    }

    @BeforeAll
    public static void prepare() {
        prepareData();
    }

    @ParameterizedTest
    @ValueSource(strings = {"/api/weather", "/api/weatherNull"})
    public void getWeatherTest(String url) {
        when(openWeatherService.receiveWeather()).thenReturn(openWeather);
        when(yandexWeatherService.receiveWeather()).thenReturn(yandexWeather);

        ResponseEntity<String> response = this.testRestTemplate.getForEntity(
                "http://localhost:" + this.port + url, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertNotNull(response.getBody());
        List<Weather> result = gson.fromJson(response.getBody(), listType);

        assertThat(result).isNotEmpty().satisfiesAnyOf(
                wl -> assertThat(wl).isEqualTo(openWeather),
                wl -> assertThat(wl).isEqualTo(yandexWeather)
        );
    }

}