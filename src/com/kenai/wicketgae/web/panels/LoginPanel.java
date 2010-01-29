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

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

/**
 * Panel showing the current user as a Label and a login/logout button.
 */
public class LoginPanel extends Panel {

    private static final long serialVersionUID = 1L;

    public LoginPanel(final String id) {
        super(id);
        add(new Label("username", new UsernameModel()));
        add(new LoginLink("loginLink"));
    }

    /** Provides the username and email. */
    private final class UsernameModel extends AbstractReadOnlyModel<String> {
        private static final long serialVersionUID = 1L;

        @Override
        public String getObject() {
            final UserService userService = getUserService();
            if (userService.isUserLoggedIn()) {
                final User currentUser = userService.getCurrentUser();
                return String.format("%s [%s]", currentUser.getNickname(),
                        currentUser.getEmail());
            } else {
                return null;
            }
        }

        private UserService getUserService() {
            return UserServiceFactory.getUserService();
        }

    }

}
