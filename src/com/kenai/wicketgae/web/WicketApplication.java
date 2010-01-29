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
package com.kenai.wicketgae.web;

import org.apache.wicket.Application;
import org.apache.wicket.Request;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.Response;
import org.apache.wicket.protocol.http.HttpSessionStore;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.protocol.http.WebRequest;
import org.apache.wicket.protocol.http.WebResponse;
import org.apache.wicket.session.ISessionStore;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.kenai.wicketgae.web.page.MainPage;
import com.kenai.wicketgae.web.page.denied.AccessDeniedPage;
import com.kenai.wicketgae.web.watcher.GaeModificationWatcher;

/**
 * {@link Application} class.
 */
public class WicketApplication extends WebApplication {

    /**
     * Wicket configuration Type.
     */
    private static final String CONFIGURATION_TYPE = Application.DEPLOYMENT;

    /**
     * Is the application only accessible for admin users? Every page should
     * then redirect to {@link AccessDeniedPage}, if the current user is not
     * admin user.
     */
    private static final boolean USER_ADMIN_ONLY = Boolean.TRUE;

    /**
     * Set up the home page. This is a single page Ajax application.
     */
    @Override
    public Class<MainPage> getHomePage() {
        return MainPage.class;
    }

    /**
     * The default {@code SecondLevelCacheSessionStore(this, new
     * DiskPageStore())} won't work on GAE, because Disk I/O is not allowed.
     * Let's simply use a {@link HttpSessionStore}.
     */
    @Override
    protected ISessionStore newSessionStore() {
        return new HttpSessionStore(this);
    }

    /**
     * Use a custom request cycle which calls our custom
     * {@link GaeModificationWatcher} to find modified resources.
     * 
     * @see CustomWebRequestCycle
     * @see GaeModificationWatcher
     */
    @Override
    public RequestCycle newRequestCycle(final Request request,
            final Response response) {
        return new CustomWebRequestCycle(this, (WebRequest) request,
                (WebResponse) response);
    }

    @Override
    protected void init() {

        // SpringBean injection
        addComponentInstantiationListener(new SpringComponentInjector(this));

        // Custom modification watcher
        getResourceSettings().setResourceWatcher(new GaeModificationWatcher());

    }

    @Override
    public String getConfigurationType() {
        return CONFIGURATION_TYPE;
    }

    public boolean isUserAdminAllowedOnly() {
        return USER_ADMIN_ONLY;
    }

    public boolean isUserAdmin() {
        final UserService userService = getUserService();
        return userService.isUserLoggedIn() && userService.isUserAdmin();
    }

    private UserService getUserService() {
        return UserServiceFactory.getUserService();
    }
}
