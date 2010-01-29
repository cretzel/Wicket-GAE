package com.kenai.wicketgae.web.page.denied;

import org.apache.wicket.ResourceReference;
import org.apache.wicket.markup.html.CSSPackageResource;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.resources.CompressedResourceReference;

import com.kenai.wicketgae.web.page.MainPage;
import com.kenai.wicketgae.web.panels.LoginLink;

public class AccessDeniedPage extends WebPage {

    private static final ResourceReference CSS = new CompressedResourceReference(
            MainPage.class, "MainPage.css");

    public AccessDeniedPage() {

        add(CSSPackageResource.getHeaderContribution(CSS));

        add(new LoginLink("loginLink"));
        
    }

}
