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

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;


@RestController
public class AuthenticationController {
    private static RestTemplate restTemplate = new RestTemplate();
    private static final String AUTHENTICATION_URL = "https://inbin-filter.herokuapp.com/auth/login";
    private static final String COMMITWASTEURL = "https://inbin-filter.herokuapp.com/house-complex/flat/residents/waste";


    @Autowired
    public AuthenticationController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }



    public static void main(String[] args) throws JsonProcessingException {
        commitWaste();
    }

    @RequestMapping(value = "/commitWaste", method = RequestMethod.GET)
    public static ResponseEntity<String> commitWaste() throws JsonProcessingException {
        Map<String, String> tokenInfo = AuthenticationController.getToken();
        if(tokenInfo.get("role").equals("ROLE_RESIDENT")){

            String token = tokenInfo.get("token");
            HttpHeaders headers = getHeaders();
            headers.set("Authorization", token);
            String wasteBody = new ObjectMapper().writeValueAsString(getLitterParameters());
            HttpEntity<String> jwtEntity = new HttpEntity<String>(wasteBody,headers);
            return restTemplate.exchange(COMMITWASTEURL, HttpMethod.POST, jwtEntity,
                    String.class);
        }else{
            return new ResponseEntity<>("You don't have permission", HttpStatus.FORBIDDEN);
        }

    }


    static WasteDto getLitterParameters() {
        LitterType litterType = detectLitterType();
        Double amount = measureAmount();
        System.out.println("Your litter type is: " + litterType.toString() + " in amount:" + amount);
        return new WasteDto(litterType, amount);
    }

    private static Double measureAmount() {
        Scanner in = new Scanner(System.in);
        System.out.println("\nEnter amount of litter in kilograms");
        return in.nextDouble();
    }

    private static LitterType detectLitterType() {
        Scanner in = new Scanner(System.in);
        System.out.println(
                "\nEnter number of litterType\n" +
                        "1 - Glass\n" +
                        "2 - Paper\n" +
                        "3 - Plastic\n" +
                        "Any key - Other waste"
        );
        int litterType = in.nextInt();
        LitterType litterTypeToSave;

        switch (litterType) {
            case 1:
                litterTypeToSave = LitterType.GLASS;
                break;
            case 2:
                litterTypeToSave = LitterType.PAPER;
                break;
            case 3:
                litterTypeToSave = LitterType.PLASTIC;
                break;

            default:
                litterTypeToSave = LitterType.OTHERWASTE;
        }
        return litterTypeToSave;
    }

    @RequestMapping(value = "/getToken", method = RequestMethod.GET)
    public static Map<String,String> getToken() throws JsonProcessingException {

        User authenticationUser = getAuthenticationUser();
        HttpHeaders authenticationHeaders = getHeaders();
        String authenticationBody = getBody(authenticationUser);
        HttpEntity<String> authenticationEntity = new HttpEntity<String>(authenticationBody,
                authenticationHeaders);

        ResponseEntity<ResponseToken> authenticationResponse = restTemplate.exchange(AUTHENTICATION_URL,
                HttpMethod.POST, authenticationEntity, ResponseToken.class);
        Map<String,String> result = new HashMap<>();
        result.put("token", "Bearer " + Objects.requireNonNull(authenticationResponse.getBody()).getToken());
        result.put("role", Objects.requireNonNull(authenticationResponse.getBody()).getRole());
        return result;
    }

    static HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        return headers;
    }

    static User getAuthenticationUser() {
        User user = new User();
        Scanner in = new Scanner(System.in);
        System.out.println("\nEnter your username");
        String userName = in.nextLine();
        System.out.println("\nEnter your password");
        String password = in.nextLine();
        user.setUsername(userName);
        user.setPassword(password);
        return user;
    }

    private static String getBody(final User user) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(user);
    }

}
