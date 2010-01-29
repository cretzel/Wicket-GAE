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
package com.kenai.wicketgae.service;

import org.springframework.transaction.annotation.Transactional;

import com.kenai.wicketgae.domain.Person;
import com.kenai.wicketgae.domain.PhoneNumber;
import com.kenai.wicketgae.persistence.IPersonDao;

/**
 * Default {@link IPersonService} implementation.
 */
@Transactional
public class PersonService implements IPersonService {

    private IPersonDao personDao;

    @Override
    public Person makePersistent(final Person person) {
        return personDao.makePersistent(person);
    }

    @Override
    public void deletePerson(final Person person) {
        personDao.deletePerson(person);
    }

    public void setPersonDao(final IPersonDao personDao) {
        this.personDao = personDao;
    }

    @Override
    public void removePhoneNumber(final Person person, final int index) {
        person.getPhoneNumbers().remove(index);
    }

    @Override
    public void removePhoneNumber(final Person person,
            final PhoneNumber phoneNumber) {
        // getPhoneNumbers().remove(phoneNumber) throws datastore exception
        final int index = person.getPhoneNumbers().indexOf(phoneNumber);
        removePhoneNumber(person, index);
    }

    @Override
    public void addPhoneNumber(final Person person,
            final PhoneNumber newPhoneNumber) {
        person.getPhoneNumbers().add(newPhoneNumber);
    }

}
