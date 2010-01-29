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
package com.kenai.wicketgae.web.page;

import org.apache.wicket.ResourceReference;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.CSSPackageResource;
import org.apache.wicket.markup.html.JavascriptPackageResource;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.resources.CompressedResourceReference;

import com.google.appengine.repackaged.com.google.common.base.Preconditions;
import com.kenai.wicketgae.web.WicketApplication;
import com.kenai.wicketgae.web.page.denied.AccessDeniedPage;
import com.kenai.wicketgae.web.panels.LoginPanel;
import com.kenai.wicketgae.web.panels.person.edit.NewPersonPanel;
import com.kenai.wicketgae.web.panels.person.list.ListPersonPanel;

/**
 * The main page of this single page ajax application.
 * <p>
 * Conists of three panels: a panel that lists all persons, a panel to
 * create/edit/delete persons and a feedback panel. These three panels are put
 * in a container that can be updated as needed. There are convenience mathods
 * for updating/replacing a single panel:
 * <ul>
 * <li>{@link #updateEditPersonPanel(WebMarkupContainer, AjaxRequestTarget)}</li>
 * <li>{@link #updateListPersonPanel(AjaxRequestTarget)} and</li>
 * <li>{@link #updateFeedbackPanel(AjaxRequestTarget)}</li>
 * </ul>
 * The first two methods replace the panel and add its container to the
 * AjaxRequestTarget. These panels must use a specific wicket id, which is
 * available through the public static constants of this class
 * {@link #LIST_PANEL_ID} and {@link #EDIT_PANEL_ID}.
 */
public class MainPage extends WebPage {

    private static final long serialVersionUID = 1L;

    /** Wicket id of the list panel. */
    public static final String LIST_PANEL_ID = "listPanel";

    /** Wicket id of the edit panel. */
    public static final String EDIT_PANEL_ID = "editPanel";

    private static final ResourceReference JQUERY = new CompressedResourceReference(
            MainPage.class, "jquery-1.3.2.js");

    private static final ResourceReference JS = new CompressedResourceReference(
            MainPage.class, "MainPage.js");

    private static final ResourceReference CSS = new CompressedResourceReference(
            MainPage.class, "MainPage.css");

    private WebMarkupContainer editPersonPanelContainer;
    private WebMarkupContainer listPersonPanelContainer;
    private WebMarkupContainer feedbackPanelContainer;
    private FeedbackPanel feedbackPanel;

    public MainPage() {
        initHeaderContributors();
        initComponents();
        checkAccess();
    }

    private void checkAccess() {
        final WicketApplication app = (WicketApplication) WicketApplication
                .get();
        if (app.isUserAdminAllowedOnly() && !app.isUserAdmin()) {
            setRedirect(true);
            setResponsePage(AccessDeniedPage.class);
        }
    }

    private void initHeaderContributors() {
        add(CSSPackageResource.getHeaderContribution(CSS));
        add(JavascriptPackageResource.getHeaderContribution(JQUERY));
        add(JavascriptPackageResource.getHeaderContribution(JS));
    }

    private void initComponents() {
        final LoginPanel loginPanel = new LoginPanel("loginPanel");
        add(loginPanel);

        listPersonPanelContainer = new WebMarkupContainer("listPanelContainer");
        add(listPersonPanelContainer);
        listPersonPanelContainer.setOutputMarkupId(true);
        listPersonPanelContainer.add(new ListPersonPanel(LIST_PANEL_ID, this));

        editPersonPanelContainer = new WebMarkupContainer("editPanelContainer");
        add(editPersonPanelContainer);
        editPersonPanelContainer.setOutputMarkupId(true);
        editPersonPanelContainer.add(new NewPersonPanel(EDIT_PANEL_ID, this));

        feedbackPanelContainer = new WebMarkupContainer(
                "feedbackPanelContainer");
        feedbackPanelContainer.setOutputMarkupId(true);
        add(feedbackPanelContainer);
        feedbackPanel = new FeedbackPanel("feedbackPanel");
        feedbackPanel.setOutputMarkupId(true);
        feedbackPanelContainer.add(feedbackPanel);
    }

    public WebMarkupContainer getEditPersonPanelContainer() {
        return editPersonPanelContainer;
    }

    public WebMarkupContainer getListPersonPanelContainer() {
        return listPersonPanelContainer;
    }

    public void updateEditPersonPanel(final WebMarkupContainer newPanel,
            final AjaxRequestTarget target) {
        Preconditions.checkArgument(EDIT_PANEL_ID.equals(newPanel.getId()),
                "edit panel must have wicket id MainPage.EDIT_PANEL_ID");

        editPersonPanelContainer.addOrReplace(newPanel);
        target.addComponent(editPersonPanelContainer);
    }

    public void updateListPersonPanel(final AjaxRequestTarget target) {
        target.addComponent(listPersonPanelContainer);
    }

    public void updateFeedbackPanel(final AjaxRequestTarget target) {
        target.addComponent(feedbackPanel);
    }

}
