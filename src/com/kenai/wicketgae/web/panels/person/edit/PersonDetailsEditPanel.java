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
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.kenai.wicketgae.domain.Person;
import com.kenai.wicketgae.service.IPersonService;
import com.kenai.wicketgae.web.page.MainPage;

/**
 * Panel for editing the person detals.
 */
public class PersonDetailsEditPanel extends Panel {

    private static final long serialVersionUID = 1L;

    @SpringBean
    private IPersonService personService;

    private final MainPage mainPage;

    public PersonDetailsEditPanel(final String id,
            final IModel<Person> personModel, final MainPage mainPage) {
        super(id, new CompoundPropertyModel<Person>(personModel));
        this.mainPage = mainPage;

        final Form<Void> form = new Form<Void>("form");
        add(form);

        final TextField<String> firstName = new RequiredTextField<String>(
                "firstName");
        form.add(firstName);

        final TextField<String> lastName = new RequiredTextField<String>(
                "lastName");
        form.add(lastName);

        final TextField<Date> birthday = new TextField<Date>("birthday");
        form.add(birthday);

        final Button submit = new AjaxButton("submit", form) {

            private static final long serialVersionUID = 1L;

            @Override
            protected void onSubmit(final AjaxRequestTarget target,
                    final Form<?> form) {
                savePerson(target);
            }

        };
        form.add(submit);

        final Button delete = new AjaxButton("delete", form) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onSubmit(final AjaxRequestTarget target,
                    final Form<?> form) {
                deletePerson(target);
            }

        };
        form.add(delete);
    }

    private void savePerson(final AjaxRequestTarget target) {
        final Person person = (Person) getDefaultModelObject();
        onSavePerson(target, person);
    }

    private void deletePerson(final AjaxRequestTarget target) {
        final Person person = (Person) getDefaultModelObject();
        personService.deletePerson(person);
        onDeletePerson(target);
    }

    protected void onSavePerson(final AjaxRequestTarget target,
            final Person person) {
        final NewPersonPanel newPanel = new NewPersonPanel(
                MainPage.EDIT_PANEL_ID, this.mainPage);
        info("Person saved");
        mainPage.updateEditPersonPanel(newPanel, target);
        mainPage.updateListPersonPanel(target);
    }

    protected void onDeletePerson(final AjaxRequestTarget target) {
        final NewPersonPanel newPanel = new NewPersonPanel(
                MainPage.EDIT_PANEL_ID, mainPage);
        info("Person deleted");
        mainPage.updateEditPersonPanel(newPanel, target);
        mainPage.updateListPersonPanel(target);
    }

}
