package com.tundalabs.store.payments;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@AllArgsConstructor
@Getter
public class WebhookRequest {
    private Map<String, String> headers;
    private final String payload;
}
