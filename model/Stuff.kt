package com.example.vlad.deliveryinstantapp.model

import java.util.HashMap

class Stuff {

    var id: String? = null
    var title: String? = null
    var model: String? = null
    var price: Double? = null
    var count: Int? = null
    var description: String? = null
    var rating: Int? = null
    var image: String? = null

    constructor() {}

    constructor(id: String, title: String, model: String, price: Double, count: Int, description: String, rating: Int, image: String ) {
        this.id = id
        this.title = title
        this.model = model
        this.price = price
        this.count = count
        this.description = description
        this.rating = rating
        this.image = image

    }

    constructor(title: String, model: String, price: Double, count: Int, description: String, rating: Int, image: String  ) {
        this.title = title
        this.model = model
        this.price = price
        this.count = count
        this.description = description
        this.rating = rating
        this.image = image

    }
    fun toMap(): Map<String, Any> {

        val result = HashMap<String, Any>()
        result.put("title", title!!)
        result.put("model", model!!)
        result.put("price", price!!)
        result.put("count", count!!)
        result.put("description", description!!)
        result.put("rating", rating!!)
        result.put("image", image!!)

        return result
    }
}