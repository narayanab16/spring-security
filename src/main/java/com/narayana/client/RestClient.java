package com.narayana.client;

import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.Map;

public class RestClient {
    public static void main(String[] args) {
        //String accessToken = getAccessTokenForPublicClient();// Can't be introspected
        //introspectToken(accessToken);
        String accessTokenConfClient = getAccessTokenForConfidentialClient(); // Can be introspected
        introspectToken(accessTokenConfClient);
        // call microservice end points
        restAPIGetHome(accessTokenConfClient);
        restAPIAddUser(accessTokenConfClient);
        restAPIGetUsers(accessTokenConfClient);
    }

    private static String getAccessTokenForConfidentialClient() {
        RestTemplate client = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        // oauth server token end point
        String token_url = "http://localhost:8383/realms/kc-realm-app/protocol/openid-connect/token";
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "client_credentials");
        params.add("client_id", "kc-app-clientcreds");
        params.add("client_secret", "QmKKHm6pFbu2IXRSK8mqlY0O0GfO8aU7");
        HttpEntity httpEntity = new HttpEntity<>(params, headers);
        // call token rest api
        ResponseEntity<Map> response = client.exchange(token_url, HttpMethod.POST, httpEntity, Map.class);
        String accessToken = "";
        if(response.getStatusCode().value() == 200) {
            accessToken = (String) response.getBody().get("access_token");
        }
        return accessToken;
    }

    private static void introspectToken(String accessToken) {
        RestTemplate client = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        // oauth server token end point
        String introspect_token_url = "http://localhost:8383/realms/kc-realm-app/protocol/openid-connect/token/introspect";
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        String valueToEncode = "kc-app-clientcreds" + ":" + "QmKKHm6pFbu2IXRSK8mqlY0O0GfO8aU7"; // client_id:client_secret
        String base64 = Base64.getEncoder().encodeToString(valueToEncode.getBytes());
        System.out.println(base64);
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.AUTHORIZATION, "Basic " + base64);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("token", accessToken);
        params.add("token_type_hint", "access_token");
        HttpEntity httpEntity = new HttpEntity<>(params, headers);
        // call introspect token rest api
        ResponseEntity<Map> response = client.exchange(introspect_token_url, HttpMethod.POST, httpEntity, Map.class);
        if(response.getStatusCode().value() == 200) {
            System.out.println("introspect token " + response.getBody().get("active"));
        }
    }

    private static String getAccessTokenForPublicClient() {
        RestTemplate client = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        // oauth server token end point
        String token_url = "http://localhost:8383/realms/kc-realm-app/protocol/openid-connect/token";
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "password");
        params.add("client_id", "kc-app");
        params.add("username", "app-user1");
        params.add("password", "admin123");
        HttpEntity httpEntity = new HttpEntity<>(params, headers);
        // call token rest api
        ResponseEntity<Map> response = client.exchange(token_url, HttpMethod.POST, httpEntity, Map.class);
        String accessToken = "";
        if(response.getStatusCode().value() == 200) {
            accessToken = (String) response.getBody().get("access_token");
        }
        return accessToken;
    }

    private static void restAPIGetHome(String accessToken) {
        RestTemplate client = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        // call application rest api service
        String rest_api = "http://localhost:10000/springsecurity/";
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
        HttpEntity httpEntity = new HttpEntity<>(headers);
        ResponseEntity<String> restResponse = client.exchange(rest_api, HttpMethod.GET, httpEntity, String.class);
        if(restResponse.getStatusCode().value() == 200) {
            System.out.println(restResponse.getBody());
        }
    }
    private static void restAPIGetUsers(String accessToken) {
        RestTemplate client = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        String getUsersAPI = "http://localhost:10000/springsecurity/getUsers";
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
        HttpEntity httpEntity = new HttpEntity<>(headers);
        ResponseEntity<String> getUsers = client.exchange(getUsersAPI, HttpMethod.GET, httpEntity, String.class);
        if(getUsers.getStatusCode().value() == 200) {
            System.out.println(getUsers.getBody());
        }
    }
    private static void restAPIAddUser(String accessToken) {
        RestTemplate client = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        String addUserAPI = "http://localhost:10000/springsecurity/addUser";
        String body = """
                {
                 "id": 100,
                 "username": "narayanabasetty"
                }
                """.strip();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
        HttpEntity httpEntity = new HttpEntity<>(body, headers);
        ResponseEntity<String> restResponse2 = client.exchange(addUserAPI, HttpMethod.POST, httpEntity, String.class);
        if(restResponse2.getStatusCode().value() == 200) {
            System.out.println(restResponse2.getBody());
        }
    }
}
