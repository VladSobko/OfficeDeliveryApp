package com.example.vlad.deliveryinstantapp

class A2 {
    var id: String? = null
    var title: String? = null
    var count: Int? = null

    constructor() {}

    constructor(id: String, title: String, count: Int) {
        this.id = id
        this.title = title
        this.count = count
    }

    constructor(title: String, count: Int) {
        this.title = title
        this.count = count

    }
}