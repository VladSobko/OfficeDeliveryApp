package com.example.vlad.deliveryinstantapp.model

import java.util.HashMap

class Review {
    var id: String? = null
    var text: String? = null
    var user: String? = null

    constructor() {}

    constructor(id: String, text: String, user: String) {
        this.id = id
        this.text = text
        this.user = user
    }

    constructor(text: String, user: String) {
        this.text = text
        this.user = user
    }
    fun toMap(): Map<String, Any> {

        val result = HashMap<String, Any>()
        result.put("text", text!!)
        result.put("user", user!!)
        return result
    }
}