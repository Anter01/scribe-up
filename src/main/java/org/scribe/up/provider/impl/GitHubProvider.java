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
package org.scribe.up.provider.impl;

import org.codehaus.jackson.JsonNode;
import org.scribe.builder.ServiceBuilder;
import org.scribe.up.addon_to_scribe.GitHubApi;
import org.scribe.up.profile.AttributesDefinitions;
import org.scribe.up.profile.JsonHelper;
import org.scribe.up.profile.UserProfile;
import org.scribe.up.profile.github.GitHubProfile;
import org.scribe.up.provider.BaseOAuth20Provider;

/**
 * This class is the OAuth provider to authenticate user in GitHub. Scope is not used.<br />
 * Attributes (Java type) available in {@link org.scribe.up.profile.github.GitHubProfile} : type (String), blog (String), url (String),
 * public_gists (Integer), following (Integer), private_gists (Integer), public_repos (Integer), gravatar_id (String), avatar_url (String),
 * followers (Integer), login (String), company (String), email (String), hireable (Boolean), collaborators (Integer), html_url (String),
 * bio (String), total_private_repos (Integer), created_at (FormattedDate), name (String), disk_usage (Integer), plan (GitHubPlan),
 * owned_private_repos (Integer) and location (String).<br />
 * More information at http://developer.github.com/v3/users/
 * 
 * @author Jerome Leleu
 * @since 1.0.0
 */
public class GitHubProvider extends BaseOAuth20Provider {
    
    protected GitHubProvider newProvider() {
        return new GitHubProvider();
    }
    
    @Override
    protected void internalInit() {
        service = new ServiceBuilder().provider(GitHubApi.class).apiKey(key).apiSecret(secret).callback(callbackUrl)
            .scope("user").build();
    }
    
    @Override
    protected String getProfileUrl() {
        return "https://api.github.com/user";
    }
    
    @Override
    protected UserProfile extractUserProfile(String body) {
        GitHubProfile profile = new GitHubProfile();
        JsonNode json = JsonHelper.getFirstNode(body);
        if (json != null) {
            profile.setId(JsonHelper.get(json, "id"));
            for (String attribute : AttributesDefinitions.githubDefinition.getAllAttributes()) {
                profile.addAttribute(attribute, JsonHelper.get(json, attribute));
            }
        }
        return profile;
    }
}
