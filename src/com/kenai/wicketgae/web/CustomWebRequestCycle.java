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
import org.apache.wicket.IRequestTarget;
import org.apache.wicket.Page;
import org.apache.wicket.Response;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.protocol.http.WebRequest;
import org.apache.wicket.protocol.http.WebRequestCycle;
import org.apache.wicket.util.watch.IModificationWatcher;

import com.kenai.wicketgae.web.page.MainPage;
import com.kenai.wicketgae.web.watcher.GaeModificationWatcher;

/**
 * Custom {@link WebRequestCycle}.
 * <p>
 * Does two things:
 * <ul>
 * <li>Triggers resource modification watching on each request in development
 * mode, see {@link GaeModificationWatcher#checkResources()}</li>
 * <li>Adds the {@link MainPage} feedback panel to the AjaxRequestTarget on each
 * Ajax request.</li>
 * </ul>
 */
class CustomWebRequestCycle extends WebRequestCycle {

    CustomWebRequestCycle(final WebApplication application,
            final WebRequest request, final Response response) {
        super(application, request, response);
    }

    /** Calls custom {@link IModificationWatcher}. */
    @Override
    protected void onBeginRequest() {
        if (getApplication().getConfigurationType() == Application.DEVELOPMENT) {
            final GaeModificationWatcher resourceWatcher = (GaeModificationWatcher) getApplication()
                    .getResourceSettings().getResourceWatcher(true);
            resourceWatcher.checkResources();
        }

    }

    /** Manages to update the feedback panel on each request. */
    @Override
    protected void onRequestTargetSet(final IRequestTarget requestTarget) {
        if (requestTarget instanceof AjaxRequestTarget) {
            final AjaxRequestTarget ajaxRequestTarget = (AjaxRequestTarget) requestTarget;
            final Page page = ajaxRequestTarget.getPage();
            if (page != null && page instanceof MainPage) {
                ((MainPage) page).updateFeedbackPanel(ajaxRequestTarget);
                ajaxRequestTarget.appendJavascript("fadeInFeedback()");
            }
        }
    }

}