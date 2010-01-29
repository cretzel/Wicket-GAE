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

import com.kenai.wicketgae.domain.Person;
import com.kenai.wicketgae.domain.PhoneNumber;

/**
 * Transactional person service.
 */
public interface IPersonService {

    Person makePersistent(Person person);

    void deletePerson(Person person);

    void removePhoneNumber(Person person, int index);

    void removePhoneNumber(Person person, PhoneNumber phoneNumber);

    void addPhoneNumber(Person person, PhoneNumber newPhoneNumber);

}
