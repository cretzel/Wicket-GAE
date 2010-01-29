/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.kenai.wicketgae.web.watcher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.wicket.util.listener.ChangeListenerSet;
import org.apache.wicket.util.listener.IChangeListener;
import org.apache.wicket.util.thread.Task;
import org.apache.wicket.util.time.Duration;
import org.apache.wicket.util.time.Time;
import org.apache.wicket.util.watch.IModifiable;
import org.apache.wicket.util.watch.IModificationWatcher;
import org.apache.wicket.util.watch.ModificationWatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is a mod of {@link ModificationWatcher}.
 * <p>
 * Wicket's ModificationWatcher spawns threads which is not allowed on App
 * Engine. So we place our own implementation at the same location in the
 * classpath. This implementation does not use a thread. Instead this watcher
 * has to be called manually by calling {@link #checkResources()}. This can be
 * called for example from a custom {@link RequestCycle#onBeginRequest().}
 */
public final class GaeModificationWatcher implements IModificationWatcher {

    /** logger */
    private static final Logger log = LoggerFactory
            .getLogger(GaeModificationWatcher.class);

    /** maps <code>IModifiable</code> objects to <code>Entry</code> objects */
    private final Map<IModifiable, Entry> modifiableToEntry = new ConcurrentHashMap<IModifiable, Entry>();

    /** the <code>Task</code> to run */
    private Task task;

    /**
     * Container class for holding modifiable entries to watch.
     */
    private static final class Entry {
        // The most recent lastModificationTime polled on the object
        Time lastModifiedTime;

        // The set of listeners to call when the modifiable changes
        final ChangeListenerSet listeners = new ChangeListenerSet();

        // The modifiable thing
        IModifiable modifiable;
    }

    /**
     * Default constructor for two-phase construction.
     */
    public GaeModificationWatcher() {
    }

    /**
     * Adds an <code>IModifiable</code> object and an
     * <code>IChangeListener</code> object to call when the modifiable object is
     * modified.
     * 
     * @param modifiable
     *            an <code>IModifiable</code> object to monitor
     * @param listener
     *            an <code>IChangeListener</code> to call if the
     *            <code>IModifiable</code> object is modified
     * @return <code>true</code> if the set did not already contain the
     *         specified element
     */
    public final boolean add(final IModifiable modifiable,
            final IChangeListener listener) {
        // Look up entry for modifiable
        final Entry entry = modifiableToEntry.get(modifiable);

        // Found it?
        if (entry == null) {
            if (modifiable.lastModifiedTime() != null) {
                // Construct new entry
                final Entry newEntry = new Entry();

                newEntry.modifiable = modifiable;
                newEntry.lastModifiedTime = modifiable.lastModifiedTime();
                newEntry.listeners.add(listener);

                // Put in map
                modifiableToEntry.put(modifiable, newEntry);
            } else {
                // The IModifiable is not returning a valid lastModifiedTime
                log
                        .info("Cannot track modifications to resource "
                                + modifiable);
            }

            return true;
        } else {
            // Add listener to existing entry
            return entry.listeners.add(listener);
        }
    }

    /**
     * Removes all entries associated with an <code>IModifiable</code> object.
     * 
     * @param modifiable
     *            an <code>IModifiable</code> object
     * @return the <code>IModifiable</code> object that was removed, else
     *         <code>null</code>
     */
    public IModifiable remove(final IModifiable modifiable) {
        final Entry entry = modifiableToEntry.remove(modifiable);
        if (entry != null) {
            return entry.modifiable;
        }
        return null;
    }

    /**
     * Starts watching at a given <code>Duration</code> polling rate.
     * 
     * @param pollFrequency
     *            the polling rate <code>Duration</code>
     */
    public void start(final Duration pollFrequency) {
        checkResources();
    }

    public void checkResources() {
        log.debug("Checking Resources");
        // Iterate over a copy of the list of entries to avoid
        // concurrent
        // modification problems without the associated liveness issues
        // of holding a lock while potentially polling file times!
        for (final Iterator<Entry> iterator = new ArrayList<Entry>(
                modifiableToEntry.values()).iterator(); iterator.hasNext();) {
            // Get next entry
            final Entry entry = iterator.next();

            // If the modifiable has been modified after the last known
            // modification time
            final Time modifiableLastModified = entry.modifiable
                    .lastModifiedTime();

            if (modifiableLastModified.after(entry.lastModifiedTime)) {
                // Notify all listeners that the modifiable was modified
                entry.listeners.notifyListeners();

                // Update timestamp
                entry.lastModifiedTime = modifiableLastModified;
            }
        }

    }

    /**
     * Stops this <code>ModificationWatcher</code>.
     */
    public void destroy() {
        if (task != null) {
            // task.stop();
            task.interrupt();
        }
    }

    /**
     * Retrieves a key set of all <code>IModifiable</code> objects currently
     * being monitored.
     * 
     * @return a <code>Set</code> of all <code>IModifiable</code> entries
     *         currently maintained
     */
    public final Set<IModifiable> getEntries() {
        return modifiableToEntry.keySet();
    }
}
