package com.example.vlad.deliveryinstantapp

class A1 {
    var id: String? = null
    var email: String? = null
    var count: Int? = null

    constructor() {}

    constructor(id: String, email: String, count: Int) {
        this.id = id
        this.email = email
        this.count = count
    }

    constructor(email: String, count: Int) {
        this.email = email
        this.count = count

    }
}