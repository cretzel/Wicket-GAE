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
package com.kenai.wicketgae.persistence;

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;

import org.springframework.orm.jdo.PersistenceManagerFactoryUtils;
import org.springframework.transaction.annotation.Transactional;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.repackaged.com.google.common.base.Preconditions;
import com.kenai.wicketgae.domain.Person;

/**
 * JDO implementation of {@link IPersonDao}.
 */
@Transactional
public class PersonDao /* extends JdoDaoSupport */
implements IPersonDao {

    private PersistenceManagerFactory pmf;

    @Override
    public Person get(final Key key) {
        return getPersistenceManager().getObjectById(Person.class, key);
    }

    @Override
    public Person makePersistent(final Person person) {
        return getPersistenceManager().makePersistent(person);
    }

    @Override
    public void deletePerson(final Person person) {
        getPersistenceManager().deletePersistent(person);
    }

    @Override
    public List<Person> findAllPersonsDefaultSort() {
        final Query query = newPersonQuery();
        setDefaultOrdering(query);

        return executeQueryToPersonList(query);
    }

    @Override
    public List<Person> findAllPersonsDefaultSort(final int first,
            final int count) {
        Preconditions.checkArgument(first >= 0, "first must by >= 0");
        Preconditions.checkArgument(count >= 0, "count must by >= 0");

        final Query query = newPersonQuery();
        setDefaultOrdering(query);
        query.setRange(first, first + count);

        return executeQueryToPersonList(query);
    }

    @Override
    public int countPersons() {
        final Query query = newPersonQuery();
        query.setResult("count(key)");

        return (Integer) query.execute();
    }

    private PersistenceManager getPersistenceManager() {
        return PersistenceManagerFactoryUtils.getPersistenceManager(pmf, true);
    }

    private Query newPersonQuery() {
        return getPersistenceManager().newQuery(Person.class);
    }

    @SuppressWarnings("unchecked")
    private List<Person> executeQueryToPersonList(final Query query) {
        return (List<Person>) query.execute();
    }

    private void setDefaultOrdering(final Query query) {
        query.setOrdering("lastName asc");
    }

    public void setPmf(final PersistenceManagerFactory pmf) {
        this.pmf = pmf;
    }

}
