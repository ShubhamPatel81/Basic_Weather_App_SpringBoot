package com.Weather.controller;

import com.Weather.model.WeatherResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

@Controller
public class WeatherController {

    @Value("${api.key}")
    private String apikey ;
    @GetMapping("/")
    public String getIndexPage(){
        return "index";
    }

    @GetMapping("/weather")
    public String getWeather(@RequestParam("city") String city, Model model) {
        String url = "http://api.openweathermap.org/data/2.5/weather?q=" + city + "&appId=" + apikey + "&units=metric";

        RestTemplate restTemplate = new RestTemplate();
        WeatherResponse weatherResponse = restTemplate.getForObject(url, WeatherResponse.class);

        if (weatherResponse != null) {
            model.addAttribute("city", weatherResponse.getName());
            model.addAttribute("country", weatherResponse.getSys().getCountry());

            // Check if 'weathers' is not null or empty
            if (weatherResponse.getWeathers() != null && !weatherResponse.getWeathers().isEmpty()) {
                model.addAttribute("weatherDescription", weatherResponse.getWeathers().get(0).getDescription());
                String weatherIcon = "wi wi-owm-" + weatherResponse.getWeathers().get(0).getId();
                model.addAttribute("weatherIcon", weatherIcon);
            } else {
                model.addAttribute("weatherDescription", "No weather information available");
            }

            model.addAttribute("temperature", weatherResponse.getMain().getTemp());
            model.addAttribute("humidity", weatherResponse.getMain().getHumidity());
            model.addAttribute("windSpeed", weatherResponse.getWind().getSpeed());
        } else {
            model.addAttribute("error", "City not found");
        }


        return "weather";
    }

}
