package com.gluigip.vaading10

import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.combobox.ComboBox
import com.vaadin.flow.component.formlayout.FormLayout
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.data.binder.Binder

class CustomerForm(val view: MainView) : FormLayout() {

    private val firstName = TextField("First name")
    private val lastName = TextField("Last name")
    private val status: ComboBox<CustomerStatus> = ComboBox("Status")
    private val save = Button("Save")
    private val delete = Button("Delete")

    private val binder = Binder(Customer::class.java)

    private val service = CustomerService.INSTANCE
    private var customer: Customer? = null

    init {
        val buttons = HorizontalLayout(save, delete)

        add(firstName, lastName, status, buttons)

        status.setItems(CustomerStatus.values().toMutableList())
        save.element.setAttribute("theme", "primary")

        binder.bindInstanceFields(this)
        save.addClickListener { this.save() }
        delete.addClickListener { this.delete() }
        setCustomer(null)

    }

    fun setCustomer(customer: Customer?) {
        this.customer = customer
        binder.bean = customer
        val enabled = customer != null
        save.isEnabled = enabled
        delete.isEnabled = enabled
        if (enabled) {
            firstName.focus()
        }
    }

    private fun delete() {
        service.delete(customer!!)
        view.updateList()
        setCustomer(null)
    }

    private fun save() {
        service.save(customer!!)
        view.updateList()
        setCustomer(null)
    }
}