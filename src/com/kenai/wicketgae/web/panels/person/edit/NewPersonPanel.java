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
package com.kenai.wicketgae.web.panels.person.edit;

import java.util.Date;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.kenai.wicketgae.domain.Person;
import com.kenai.wicketgae.service.IPersonService;
import com.kenai.wicketgae.web.page.MainPage;

/**
 * Panel for creating new persons.
 */
public class NewPersonPanel extends Panel {

    private static final long serialVersionUID = 1L;

    @SpringBean
    private IPersonService personService;

    private String firstName;

    private String lastName;

    private Date birthday;

    private final MainPage mainPage;

    public NewPersonPanel(final String id, final MainPage mainPage) {
        super(id);
        setDefaultModel(new CompoundPropertyModel<Person>(this));
        this.mainPage = mainPage;

        final Form<Void> form = new Form<Void>("form");
        add(form);

        final TextField<String> firstName = new RequiredTextField<String>(
                "firstName");
        form.add(firstName);

        final TextField<String> lastName = new RequiredTextField<String>(
                "lastName");
        form.add(lastName);

        final TextField<String> birthday = new TextField<String>("birthday");
        form.add(birthday);

        final Button submit = new AjaxButton("submit", form) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onSubmit(final AjaxRequestTarget target,
                    final Form<?> form) {
                saveNewPerson(target);
            }

        };
        form.add(submit);
    }

    private void saveNewPerson(final AjaxRequestTarget target) {
        final Person person = new Person(birthday, firstName, lastName);
        final Person newPerson = personService.makePersistent(person);
        onSaveNewPerson(target, newPerson);
    }

    protected void onSaveNewPerson(final AjaxRequestTarget target,
            final Person newPerson) {
        final NewPersonPanel newPanel = new NewPersonPanel(
                MainPage.EDIT_PANEL_ID, this.mainPage);
        info("Person saved");
        mainPage.updateEditPersonPanel(newPanel, target);
        mainPage.updateListPersonPanel(target);
    }
}
