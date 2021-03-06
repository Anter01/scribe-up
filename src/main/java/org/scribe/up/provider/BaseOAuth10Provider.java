/*
  Copyright 2012 Jerome Leleu

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package org.scribe.up.provider;

import java.util.Map;

import org.scribe.exceptions.OAuthException;
import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.scribe.up.credential.OAuthCredential;
import org.scribe.up.session.UserSession;
import org.scribe.utils.OAuthEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is the common implementation for provider supporting OAuth protocol version 1.0.
 * 
 * @author Jerome Leleu
 * @since 1.0.0
 */
public abstract class BaseOAuth10Provider extends BaseOAuthProvider {
    
    protected static final Logger logger = LoggerFactory.getLogger(BaseOAuth10Provider.class);
    
    public static final String OAUTH_TOKEN = "oauth_token";
    
    public static final String OAUTH_VERIFIER = "oauth_verifier";
    
    public static final String REQUEST_TOKEN = "requestToken";
    
    /**
     * Return the name of the attribute storing in session the request token.
     * 
     * @return the name of the attribute storing in session the request token
     */
    protected String getRequestTokenSessionAttributeName() {
        return getType() + "#" + REQUEST_TOKEN;
    }
    
    public String getAuthorizationUrl(UserSession session) {
        init();
        Token requestToken = service.getRequestToken();
        logger.debug("requestToken : {}", requestToken);
        // save requestToken in user session
        session.setAttribute(getRequestTokenSessionAttributeName(), requestToken);
        String authorizationUrl = service.getAuthorizationUrl(requestToken);
        logger.debug("authorizationUrl : {}", authorizationUrl);
        return authorizationUrl;
    }
    
    public Token getAccessToken(OAuthCredential credential) {
        Token tokenRequest = credential.getRequestToken();
        String token = credential.getToken();
        String verifier = credential.getVerifier();
        logger.debug("tokenRequest : {}", tokenRequest);
        logger.debug("token : {}", token);
        logger.debug("verifier : {}", verifier);
        if (tokenRequest == null) {
            throw new OAuthException("Token request expired");
        }
        String savedToken = tokenRequest.getToken();
        logger.debug("savedToken : {}", savedToken);
        if (savedToken == null || !savedToken.equals(token)) {
            throw new OAuthException("Token received : " + token + " is different from saved token : " + savedToken);
        }
        Verifier providerVerifier = new Verifier(verifier);
        Token accessToken = service.getAccessToken(tokenRequest, providerVerifier);
        logger.debug("accessToken : {}", accessToken);
        return accessToken;
    }
    
    @Override
    public OAuthCredential extractCredentialFromParameters(UserSession session, Map<String, String[]> parameters) {
        String[] tokens = parameters.get(OAUTH_TOKEN);
        String[] verifiers = parameters.get(OAUTH_VERIFIER);
        if (tokens != null && tokens.length == 1 && verifiers != null && verifiers.length == 1) {
            // get tokenRequest from user session
            Token tokenRequest = (Token) session.getAttribute(getRequestTokenSessionAttributeName());
            logger.debug("tokenRequest : {}", tokenRequest);
            String token = OAuthEncoder.decode(tokens[0]);
            String verifier = OAuthEncoder.decode(verifiers[0]);
            logger.debug("token : {} / verifier : {}", token, verifier);
            return new OAuthCredential(tokenRequest, token, verifier, getType());
        } else {
            logger.error("No credential found");
            return null;
        }
    }
}
