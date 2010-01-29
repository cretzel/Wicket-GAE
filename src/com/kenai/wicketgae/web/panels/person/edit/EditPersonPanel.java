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

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import com.kenai.wicketgae.domain.Person;
import com.kenai.wicketgae.web.page.MainPage;

/**
 * Panel containg sub panels for editing a person's details and phone numbers.
 */
public class EditPersonPanel extends Panel {

    private static final long serialVersionUID = 1L;

    public EditPersonPanel(final String id, final IModel<Person> personModel,
            final MainPage mainPage) {
        super(id);

        add(new PersonDetailsEditPanel("detailEditPanel", personModel, mainPage));
        add(new PersonPhoneNumbersEditPanel("phoneNumbersPanel", personModel,
                mainPage));
    }

}
