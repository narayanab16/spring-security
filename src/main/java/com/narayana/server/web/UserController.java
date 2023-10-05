package com.narayana.server.web;

import com.narayana.server.model.User;
import com.narayana.server.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.List;
import java.util.Map;

@RestController
public class UserController {
    Logger LOG = LoggerFactory.getLogger(UserController.class);
    @Value("${keycloak.introspect_token_url}")
    private String introspectTokenUrl;
    @Value("${keycloak.client_id}")
    private String clientId;
    @Value("${keycloak.client_secret}")
    private String clientSecret;
    private UserService userService;
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }
    @RequestMapping(value = "/getUserById/{id}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public User getUserById(@PathVariable Integer id) {
        return userService.getUserById(id);
    }

    @PostMapping(value = "/addUser", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public User addUser(@RequestBody User user) {
        return userService.addUser(user);
    }

    @GetMapping(value = "/getUsers", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<User> getUsers() {
        return userService.getUsers();
    }
    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public String home(@RequestHeader("Authorization") String bearerToken) {
        if(validateToken(bearerToken.split(" ")[1]))
            return "Welcome back Narayana Basetty!!!";
        else
            return "Invalid Token, please check your with App Owner/admin";
    }

    private boolean validateToken(String accessToken) {
        RestTemplate client = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        String valueToEncode = clientId + ":" + clientSecret; // client_id:client_secret
        String base64 = Base64.getEncoder().encodeToString(valueToEncode.getBytes());
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.AUTHORIZATION, "Basic " + base64);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("token", accessToken);
        params.add("token_type_hint", "access_token");
        HttpEntity httpEntity = new HttpEntity<>(params, headers);
        LOG.info("introspectTokenUrl :  "+ introspectTokenUrl);
        // call introspect token rest api
        ResponseEntity<Map> response = client.exchange(introspectTokenUrl, HttpMethod.POST, httpEntity, Map.class);
        if(response.getStatusCode().value() == 200) {
            if ((Boolean) response.getBody().get("active")) {
                LOG.info("validate Token - success");
                return true;
             } else {
                LOG.info("validate Token - failed");
                return false;
            }
        }
        return false;
    }
}
