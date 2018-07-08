package com.gluigip.vaading10

import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.dependency.HtmlImport
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.Route
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.icon.Icon
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.value.ValueChangeMode
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout

@HtmlImport("styles/shared-styles.html")
@Route
class MainView() : VerticalLayout() {

    private val filterText = TextField()

    private val service = CustomerService.INSTANCE
    private val grid = Grid<Customer>()

    private val form = CustomerForm(this)

    init {
        filterText.placeholder = "Filter by name..."
        filterText.valueChangeMode = ValueChangeMode.EAGER
        filterText.addValueChangeListener { updateList() }

        val clearFilterTextBtn = Button(Icon(VaadinIcon.CLOSE_CIRCLE))
        clearFilterTextBtn.addClickListener({ filterText.clear() })

        val filtering = HorizontalLayout(filterText, clearFilterTextBtn)

        val addCustomerBtn = Button("Add new customer")
        addCustomerBtn.addClickListener {
            grid.asSingleSelect().clear()
            form.setCustomer(Customer())
        }

        val toolbar = HorizontalLayout(filtering, addCustomerBtn)

        grid.setSizeFull()

        grid.addColumn(Customer::firstName).setHeader("First name")
        grid.addColumn(Customer::lastName).setHeader("Last name")
        grid.addColumn(Customer::status).setHeader("Status")
        grid.asSingleSelect().addValueChangeListener { event -> form.setCustomer(event.value) }

        val main = HorizontalLayout(grid, form)
        main.alignItems = FlexComponent.Alignment.START
        main.setSizeFull()

        add(toolbar, main)
        height = "100vh"
        updateList()
    }

    fun updateList() {
        grid.setItems(service.findAll(filterText.value))
    }
}