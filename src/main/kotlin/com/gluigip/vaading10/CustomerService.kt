package com.gluigip.vaading10

import java.util.*
import java.util.logging.Level
import java.util.logging.Logger

class CustomerService {

    constructor() {
        ensureTestData()
    }

    companion object {
        val INSTANCE = CustomerService()
        private val LOGGER = Logger.getLogger(CustomerService.javaClass.name)!!
    }

    private val contacts: MutableMap<Long, Customer> = mutableMapOf();
    private var nextId: Long = 0


    /**
     * @return all available Customer objects.
     */
    @Synchronized
    fun findAll(): MutableList<Customer> {
        return findAll(null)
    }

    /**
     * Finds all Customer's that match given filter.
     *
     * @param stringFilter
     *            filter that returned objects should match or null/empty string
     *            if all objects should be returned.
     * @return list a Customer objects
     */
    @Synchronized
    fun findAll(stringFilter: String?): MutableList<Customer> {
        return contacts.filter { stringFilter == null || stringFilter.isEmpty() || it.value.toString().toLowerCase().contains(stringFilter.toLowerCase()) }
                .values.sortedBy { it.id }.toMutableList()
    }

    /**
     * Finds all Customer's that match given filter and limits the resultset.
     *
     * @param stringFilter
     *            filter that returned objects should match or null/empty string
     *            if all objects should be returned.
     * @param start
     *            the index of first result
     * @param maxresults
     *            maximum result count
     * @return list a Customer objects
     */
    @Synchronized
    fun findAll(stringFilter: String?, start: Int, maxresults: Int): MutableList<Customer> {

        var arrayList = contacts.filter { stringFilter == null || stringFilter.isEmpty() || it.value.toString().toLowerCase().contains(stringFilter.toLowerCase()) }
                .values.sortedBy { it.id }

        var end = start + maxresults
        if (end > arrayList.size) {
            end = arrayList.size
        }
        return arrayList.subList(start, end).toMutableList()
    }

    /**
     * @return the amount of all customers in the system
     */
    @Synchronized
    fun count(): Int {
        return contacts.size
    }

    /**
     * Deletes a customer from a system
     *
     * @param value
     *            the Customer to be deleted
     */
    @Synchronized
    fun delete(value: Customer) {
        contacts.remove(value.id)
    }

    /**
     * Persists or updates customer in the system. Also assigns an identifier
     * for new Customer instances.
     *
     * @param entry
     */
    @Synchronized
    fun save(entry: Customer) {
        if (entry == null) {
            LOGGER.log(Level.SEVERE,
                    "Customer is null. Are you sure you have connected your form to the application as described in tutorial chapter 7?")
            return
        }
        val entryToSave = entry.copy()
        if (entryToSave.id == null) {
            entryToSave.id = nextId++
        }
        contacts[entryToSave.id!!] = entryToSave
    }

    /**
     * Sample data generation
     */
    fun ensureTestData() {
        if (findAll().isEmpty()) {
            var names = listOf(
                    "Gabrielle Patel", "Brian Robinson", "Eduardo Haugen",
                    "Koen Johansen", "Alejandro Macdonald", "Angel Karlsson", "Yahir Gustavsson", "Haiden Svensson",
                    "Emily Stewart", "Corinne Davis", "Ryann Davis", "Yurem Jackson", "Kelly Gustavsson",
                    "Eileen Walker", "Katelyn Martin", "Israel Carlsson", "Quinn Hansson", "Makena Smith",
                    "Danielle Watson", "Leland Harris", "Gunner Karlsen", "Jamar Olsson", "Lara Martin",
                    "Ann Andersson", "Remington Andersson", "Rene Carlsson", "Elvis Olsen", "Solomon Olsen",
                    "Jaydan Jackson", "Bernard Nilsen"
            )
            val r = Random(0)

            names.forEach {
                val split = it.split(" ")
                save(Customer(null, split[0], split[1], CustomerStatus.values()[r.nextInt(CustomerStatus.values().size)]))
            }
        }
    }
}