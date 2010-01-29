/*
 * Copyright [2009] [Nick Wiedenbrueck]
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 *
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.kenai.wicketgae.web.panels;

import javax.servlet.http.HttpServletRequest;

import org.apache.wicket.RequestCycle;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.protocol.http.WebRequest;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

/**
 * ExternalLink that redirects to the login or logout page depending of the
 * user's current login status.
 */
public class LoginLink extends ExternalLink {

    private static final long serialVersionUID = 1L;

    public LoginLink(final String id) {
        super(id, getURL());
        add(new Label("label", new LoginLabelModel()));
    }

    public static String getURL() {

        final RequestCycle requestCycle = RequestCycle.get();

        // Get the URL of this app to redirect to it
        final WebRequest request = (WebRequest) requestCycle.getRequest();
        final HttpServletRequest req = request.getHttpServletRequest();
        final String appUrl = req.getRequestURL().toString();

        // Create the login/logout page URL with with redirect tho this app
        final String targetUrl = getUserService().isUserLoggedIn() ? loginUrl(appUrl)
                : logoutUrl(appUrl);

        return targetUrl;

    }

    private static String logoutUrl(final String appUrl) {
        return getUserService().createLoginURL(appUrl);
    }

    private static String loginUrl(final String appUrl) {
        return getUserService().createLogoutURL(appUrl);
    }

    private static UserService getUserService() {
        return UserServiceFactory.getUserService();
    }

    /** Returns "Login"/"Logout/ depending of the current user's login status. */
    private final class LoginLabelModel extends AbstractReadOnlyModel<String> {
        private static final long serialVersionUID = 1L;

        @Override
        public String getObject() {
            return getUserService().isUserLoggedIn() ? "Logout" : "Login";
        }
    }

}