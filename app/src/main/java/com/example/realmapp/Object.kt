package com.example.realmapp
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Object(): RealmObject() {
    @PrimaryKey
    var id: Long = 0
    var name: String? = null
    var age: Int = 0
}