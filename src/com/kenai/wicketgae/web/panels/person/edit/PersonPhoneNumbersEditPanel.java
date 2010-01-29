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

import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.list.OddEvenListItem;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.kenai.wicketgae.domain.Person;
import com.kenai.wicketgae.domain.PhoneNumber;
import com.kenai.wicketgae.service.IPersonService;
import com.kenai.wicketgae.web.page.MainPage;

/**
 * Panel for editing phone numbers of a person.
 */
public class PersonPhoneNumbersEditPanel extends Panel {

    private static final long serialVersionUID = 1L;

    @SpringBean
    private IPersonService personService;

    private final ListView<PhoneNumber> listView;

    private String newType;
    private String newNumber;

    public PersonPhoneNumbersEditPanel(final String id,
            final IModel<Person> model, final MainPage mainPage) {
        super(id, model);
        setOutputMarkupId(true);

        final Form<Void> form = new Form<Void>("form");
        add(form);

        final IModel<List<PhoneNumber>> listModel = new PropertyModel<List<PhoneNumber>>(
                model, "phoneNumbers");

        listView = new ListView<PhoneNumber>("phoneNumbers", listModel) {

            private static final long serialVersionUID = 1L;

            @Override
            protected void populateItem(final ListItem<PhoneNumber> item) {
                final IModel<PhoneNumber> phoneNumberModel = item.getModel();
                item.add(new Label("type", new PropertyModel<String>(
                        phoneNumberModel, "type")));
                item.add(new Label("number", new PropertyModel<String>(
                        phoneNumberModel, "number")));

                final AjaxLink<Void> delete = new AjaxLink<Void>("delete") {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public void onClick(final AjaxRequestTarget target) {
                        deletePhoneNumber(phoneNumberModel, target);
                    }

                };
                item.add(delete);

            }

            @Override
            protected ListItem<PhoneNumber> newItem(final int index) {
                return new OddEvenListItem<PhoneNumber>(index,
                        getListItemModel(getModel(), index));
            }

        };
        listView.setOutputMarkupId(true);
        form.add(listView);

        final TextField<String> typeTextField = new TextField<String>(
                "newType", new PropertyModel<String>(this, "newType"));
        form.add(typeTextField);

        final TextField<String> numberTextField = new TextField<String>(
                "newNumber", new PropertyModel<String>(this, "newNumber"));
        numberTextField.setRequired(true);
        form.add(numberTextField);

        final Button add = new AjaxButton("add", form) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onSubmit(final AjaxRequestTarget target,
                    final Form<?> form) {
                addPhoneNumber(target);
            }

        };
        form.add(add);

    }

    private void deletePhoneNumber(final IModel<PhoneNumber> phoneNumberModel,
            final AjaxRequestTarget target) {
        final Person person = (Person) getDefaultModelObject();
        final PhoneNumber phoneNumber = phoneNumberModel.getObject();
        personService.removePhoneNumber(person, phoneNumber);
        onPhoneNumberDeleted(target);
    }

    protected void onPhoneNumberDeleted(final AjaxRequestTarget target) {
        info("Phone number deleted");
        target.addComponent(this);
    }

    private void addPhoneNumber(final AjaxRequestTarget target) {
        final PhoneNumber newPhoneNumber = new PhoneNumber(newType, newNumber);
        final Person person = (Person) getDefaultModelObject();
        personService.addPhoneNumber(person, newPhoneNumber);
        onPhoneNumberAdded(target);
    }

    protected void onPhoneNumberAdded(final AjaxRequestTarget target) {
        newType = null;
        newNumber = null;
        info("Phone number added");
        target.addComponent(this);
    }

}
