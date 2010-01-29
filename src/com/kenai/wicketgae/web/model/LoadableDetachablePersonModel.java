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
package com.kenai.wicketgae.web.model;

import org.apache.wicket.injection.web.InjectorHolder;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.google.appengine.api.datastore.Key;
import com.kenai.wicketgae.domain.Person;
import com.kenai.wicketgae.persistence.IPersonDao;

/**
 * {@link LoadableDetachableModel} for {@link Person} entities.
 */
public class LoadableDetachablePersonModel extends
        LoadableDetachableModel<Person> {

    private static final long serialVersionUID = 1L;

    @SpringBean
    private IPersonDao personDao;

    private final Key key;

    public LoadableDetachablePersonModel(final Key key) {
        super();
        this.key = key;
        init();
    }

    public LoadableDetachablePersonModel(final Person person) {
        super(person);
        this.key = person.getKey();
        init();
    }

    private void init() {
        InjectorHolder.getInjector().inject(this);
    }

    @Override
    protected Person load() {
        return personDao.get(key);
    }

}
