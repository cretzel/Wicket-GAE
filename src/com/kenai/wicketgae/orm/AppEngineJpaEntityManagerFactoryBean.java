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
package com.kenai.wicketgae.orm;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;

import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.LocalEntityManagerFactoryBean;

/**
 * Simplified {@link LocalEntityManagerFactoryBean} for use in Google App
 * Engine.
 * <p>
 * Somehow the {@link LocalEntityManagerFactoryBean} has some issues with
 * DataNucleus. Uses {@link Persistence#createEntityManagerFactory(String)}.
 * 
 * @author nwi
 */
public class AppEngineJpaEntityManagerFactoryBean extends
        LocalContainerEntityManagerFactoryBean {

    private String persistenceUnit;

    @Override
    protected EntityManagerFactory createNativeEntityManagerFactory()
            throws PersistenceException {
        return Persistence.createEntityManagerFactory(persistenceUnit);
    }

    /**
     * Sets the persistence unit name.
     * 
     * @param persistenceUnit
     *            name of the persistence unit
     */
    public void setPersistenceUnit(final String persistenceUnit) {
        this.persistenceUnit = persistenceUnit;
    }

}
