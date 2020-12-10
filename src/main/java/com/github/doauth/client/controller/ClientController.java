package com.github.doauth.client.controller;

import com.github.doauth.client.domain.Scope;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ClientController {

    private static final String AUTHORIZATION_PROXY_URL = "http://localhost:9090";
    private static final String AUTHORIZATION_ENDPOINT = "/authorize";
    private static final String QR_CODE_ENDPOINT = "/qrcode";

    private static final String CLIENT_ID = "doauth-client";

    private static final String REDIRECT_URL = "http://localhost:8080/callback";

    private static final String RESPONSE_TYPE_PARAM = "response_type";
    private static final String SCOPE_PARAM = "scope";
    private static final String CLIENT_ID_PARAM = "client_id";
    private static final String REDIRECT_URL_PARAM = "redirect_uri";
    private static final String STATE_PARAM = "state";
    private static final String DOCUMENT_URI_PARAM = "document_uri";
    private static final String AUTHORIZATION_URI_PARAM = "authorization_uri";

    @PostMapping("/authorize")
    public String requestAuthorization(
            @ModelAttribute Scope scope,
            @RequestParam(name = "document_uri", required = false) URI documentURI) {

        String responseType = "code";
        String scopeParam = scope.configure();
        String state = UUID.randomUUID().toString();

        log.info("{} = {}", SCOPE_PARAM, scopeParam);
        log.info("{} = {}", DOCUMENT_URI_PARAM, documentURI);

        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder
                .fromUriString(AUTHORIZATION_PROXY_URL)
                .path(AUTHORIZATION_ENDPOINT)
                .queryParam(RESPONSE_TYPE_PARAM, responseType)
                .queryParam(SCOPE_PARAM, scopeParam)
                .queryParam(CLIENT_ID_PARAM, CLIENT_ID)
                .queryParam(REDIRECT_URL_PARAM, URI.create(REDIRECT_URL))
                .queryParam(STATE_PARAM, state);

        if (documentURI != null) {
            uriComponentsBuilder.queryParam(DOCUMENT_URI_PARAM, documentURI);
        }

        URI authorizationUri = uriComponentsBuilder.build().encode().toUri();

        URI qrcodeUri = UriComponentsBuilder
                .fromUriString(AUTHORIZATION_PROXY_URL)
                .path(QR_CODE_ENDPOINT)
                .queryParam(AUTHORIZATION_URI_PARAM, authorizationUri.toString())
                .build()
                .encode()
                .toUri();

        return "redirect:" + qrcodeUri.toString();
    }
}
