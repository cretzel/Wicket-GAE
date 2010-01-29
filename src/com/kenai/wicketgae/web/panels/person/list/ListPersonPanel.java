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

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.repeater.data.table.AjaxNavigationToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.HeadersToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.OddEvenItem;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.kenai.wicketgae.domain.Person;
import com.kenai.wicketgae.web.page.MainPage;
import com.kenai.wicketgae.web.panels.person.edit.EditPersonPanel;

/**
 * Contains a {@link DataTable} that displays all persons.
 */
public class ListPersonPanel extends Panel {

    private static final int ROWS_PER_PAGE = 5;

    private static final long serialVersionUID = 1L;

    private final MainPage mainPage;

    public ListPersonPanel(final String id, final MainPage mainPage) {
        super(id);
        this.mainPage = mainPage;

        final PersonDataProvider dataProvider = new PersonDataProvider();
        final IColumn<Person>[] columns = createColumns();

        final DataTable<Person> table = new PersonDataTable("table", columns,
                dataProvider, ROWS_PER_PAGE);
        table.setOutputMarkupId(true);
        add(table);

        table.addTopToolbar(new HeadersToolbar(table, null));
        table.addBottomToolbar(new AjaxNavigationToolbar(table));
    }

    private IColumn<Person>[] createColumns() {
        final PropertyColumn<Person> firstName = new PropertyColumn<Person>(
                new Model<String>("First Name"), "firstName");
        final PropertyColumn<Person> lastName = new PropertyColumn<Person>(
                new Model<String>("Last Name"), "lastName");
        final PropertyColumn<Person> birthday = new PropertyColumn<Person>(
                new Model<String>("Birthday"), "birthday");
        final IColumn<Person> edit = new AbstractColumn<Person>(null) {
            private static final long serialVersionUID = 1L;

            @Override
            public void populateItem(
                    final Item<ICellPopulator<Person>> cellItem,
                    final String componentId, final IModel<Person> rowModel) {
                final Fragment frag = new Fragment(componentId, "linkFrag",
                        ListPersonPanel.this);
                final AjaxLink<?> link = new AjaxLink<Void>("link") {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public void onClick(final AjaxRequestTarget target) {
                        mainPage.updateEditPersonPanel(new EditPersonPanel(
                                MainPage.EDIT_PANEL_ID, rowModel, mainPage),
                                target);
                    }

                };
                frag.add(link);
                cellItem.add(frag);

            }

        };

        @SuppressWarnings("unchecked")
        final IColumn<Person>[] columns = new IColumn[] { firstName, lastName,
                birthday, edit };
        return columns;
    }

    private final class PersonDataTable extends DataTable<Person> {
        
        private static final long serialVersionUID = 1L;

        private PersonDataTable(final String id, final IColumn<Person>[] columns,
                final IDataProvider<Person> dataProvider, final int rowsPerPage) {
            super(id, columns, dataProvider, rowsPerPage);
        }

        @Override
        protected Item<Person> newRowItem(final String id, final int index,
                final IModel<Person> model) {
            return new OddEvenItem<Person>(id, index, model);
        }
    }

}
