package com.gluigip.vaading10

data class Customer(
        var id: Long? = null,
        var firstName: String? = "",
        var lastName: String? = "",
        var status: CustomerStatus? = null)