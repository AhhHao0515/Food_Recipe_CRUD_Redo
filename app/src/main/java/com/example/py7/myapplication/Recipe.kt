package com.example.py7.myapplication

class Recipe {

    var id: Int? = null
    var name: String? = null
    var ing: String? = null
    var step: String? = null

    constructor(id: Int, name: String, ing: String, step:String){
        this.id = id
        this.name = name
        this.ing = ing
        this.step = step
    }
}