package com.example.cowmanagementsystem

class user {
    var id: Int = 0
    var userName: String = ""
    var password: String = ""

    constructor(userName: String, password: String){
        this.userName = userName
        this.password = password
    }
}