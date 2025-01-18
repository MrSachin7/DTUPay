package org.acme.service;

import org.acme.dto.GenerateTokenResponse;


public interface TokenService {
    GenerateTokenResponse generateToken(String username, int amount);
}
