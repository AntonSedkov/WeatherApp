package tpr.antonius.weatherapp.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/*  pattern DTO
   JSON
   {
    ...,
        "main": {
            "temp":"0",
            ...
         }
   }
*/

@Data
public class OpenWeatherDto {

    @JsonProperty("main")
    private MainDto main;

    @Data
    public class MainDto {

        @JsonProperty("temp")
        private String temperature;
    }

}