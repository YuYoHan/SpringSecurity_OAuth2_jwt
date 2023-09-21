package com.example.oauth.config.OAuth2;

import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

public interface ProviderUser {
    String getId();
    String getUserName();
    String getPassword();
    String getEmail();
    String getProvider();
    List<? extends GregorianCalendar>  getAuthorities();
    Map<String, Object> getAttributes();
}
