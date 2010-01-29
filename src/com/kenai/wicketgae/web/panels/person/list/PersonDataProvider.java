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
package com.kenai.wicketgae.web.panels.person.list;

import java.util.Iterator;

import org.apache.wicket.injection.web.InjectorHolder;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.kenai.wicketgae.domain.Person;
import com.kenai.wicketgae.persistence.IPersonDao;
import com.kenai.wicketgae.web.model.LoadableDetachablePersonModel;

/**
 * Calls {@link IPersonDao#findAllPersonsDefaultSort(int, int)} to retrieve the
 * persons and returns {@link LoadableDetachablePersonModel}s.
 */
public final class PersonDataProvider implements IDataProvider<Person> {

    private static final long serialVersionUID = 1L;

    @SpringBean
    private IPersonDao personDao;

    public PersonDataProvider() {
        InjectorHolder.getInjector().inject(this);
    }

    @Override
    public Iterator<Person> iterator(final int first, final int count) {

        return personDao.findAllPersonsDefaultSort(first, count).iterator();
    }

    @Override
    public IModel<Person> model(final Person person) {
        return new LoadableDetachablePersonModel(person);
    }

    @Override
    public int size() {
        return personDao.countPersons();
    }

    @Override
    public void detach() {
    }

}