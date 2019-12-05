package com.example.vlad.deliveryinstantapp.model

import android.content.ContentValues.TAG
import android.util.Log
import java.util.*

class Order {

    var id: String? = null
    var count: Int? = null
    var city: String? = null
    var street: String? = null
    var house: String? = null
    var flat: String? = null

    var titleo: String? = null
    var modelo: String? = null
    var priceo: Double? = null
    var imageo: String? = null

    var user: String? = null

    var done: Boolean = false

    var date: Date? = null

    constructor() {}

    constructor(id: String, count: Int, city: String, street: String, house: String, flat: String,titleo: String,modelo: String,priceo: Double,imageo: String, user: String, done: Boolean, date: Date) {
        this.id = id
        this.count = count
        this.city = city
        this.street = street
        this.house = house
        this.flat = flat
        this.titleo = titleo
        this.modelo = modelo
        this.priceo = priceo
        this.imageo = imageo
        this.user = user
        this.done = done
        this.date = date

    }

    constructor(count: Int, city: String, street: String, house: String, flat: String,titleo: String,modelo: String,priceo: Double,imageo: String, user: String, done: Boolean, date: Date) {
        this.count = count
        this.city = city
        this.street = street
        this.house = house
        this.flat = flat
        this.titleo = titleo
        this.modelo = modelo
        this.priceo = priceo
        this.imageo = imageo
        this.user = user
        this.done = done
        this.date = date


    }
    constructor(id: String, done: Boolean){
        this.id = id
        this.done = done
    }
    fun toMap(): Map<String, Any> {

        val result = HashMap<String, Any>()
        Log.v(TAG, result.toString())

        Log.v(TAG, count.toString())
        result.put("count", this.count!!)

        result.put("city", this.city!!)
        result.put("street", this.street!!)
        result.put("house", this.house!!)
        result.put("flat", this.flat!!)
        result.put("titleo", this.titleo!!)
        result.put("modelo", this.modelo!!)
        result.put("priceo", this.priceo!!)
        result.put("imageo", this.imageo!!)
        result.put("user", this.user!!)
        result.put("done", this.done)
        result.put("date", this.date!!)

        return result
    }
}