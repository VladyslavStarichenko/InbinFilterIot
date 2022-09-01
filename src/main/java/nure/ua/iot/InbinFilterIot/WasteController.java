package nure.ua.iot.InbinFilterIot;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Scanner;


import static nure.ua.iot.InbinFilterIot.AuthenticationController.getHeaders;

@RestController
public class WasteController {


    private static RestTemplate restTemplate = new RestTemplate();

    @Autowired
    public WasteController(RestTemplate restTemplate) {
        WasteController.restTemplate = restTemplate;
    }


}
