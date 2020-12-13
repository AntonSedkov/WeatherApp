package tpr.antonius.weatherapp.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import tpr.antonius.weatherapp.model.Weather;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@DisplayName("Class WeatherAggregationService")
@SpringBootTest
class WeatherAggregationServiceTest {

    @MockBean(name = "openWeatherService")
    private OpenWeatherService openWeatherService;

    @MockBean(name = "yandexWeatherService")
    private YandexWeatherService yandexWeatherService;

    @Autowired
    private WeatherAggregationService weatherAggregationService;

    @BeforeEach
    void setUp() {
        when(openWeatherService.receiveWeather())
                .thenReturn(singletonList(new Weather("1", "1", "1")));
        when(yandexWeatherService.receiveWeather())
                .thenReturn(singletonList(new Weather("2", "2", "2")));
    }

    @DisplayName("It has to aggregate weather from some origins")
    @Test
    void receiveWeather() {
        assertThat(weatherAggregationService.receiveWeather()).hasSize(2);
    }
}