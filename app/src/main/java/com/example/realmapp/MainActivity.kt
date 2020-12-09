package com.example.realmapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmResults
import io.realm.mongodb.App
import io.realm.mongodb.AppConfiguration
import io.realm.mongodb.Credentials
import kotlinx.android.synthetic.main.activity_main.view.*
import org.w3c.dom.Text


class MainActivity : AppCompatActivity() {

    private lateinit var realm: Realm

    private lateinit var nameTextView : TextView
    private lateinit var ageTextView: TextView

    private lateinit var nameInput : EditText
    private lateinit var ageInput : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        nameTextView = findViewById(R.id.nameview)
        ageTextView = findViewById(R.id.ageview)

        nameInput = findViewById(R.id.nameinput)
        ageInput = findViewById(R.id.ageinput)

        var createButton = findViewById<Button>(R.id.buttonCreate)
        var updateButton = findViewById<Button>(R.id.buttonUpdate)
        var deleteButton = findViewById<Button>(R.id.buttonDelete)
        var findButton = findViewById<Button>(R.id.buttonFind)

        Realm.init(this)
        val config = RealmConfiguration.Builder()
            .name("obj.realm").build()
        realm = Realm.getInstance(config)
        
        readAllObjects()


        createButton.setOnClickListener {
            var name = nameInput.text.toString()
            var age = ageInput.text.toString().toInt()

            createObject(name, age)
        }

        findButton.setOnClickListener {
            var name = nameInput.text.toString()
            readObjectsWithName(name)
        }

        deleteButton.setOnClickListener {
            var name = nameInput.text.toString()
            deleteObjectsWithName(name)
        }

        updateButton.setOnClickListener {
            var name = nameInput.text.toString()
            var newAge = ageInput.text.toString().toInt()
            updateObjectsWithName(name, newAge)
        }
    }

    fun createObject(name: String, age: Int) {
        realm.beginTransaction()
        var maxId = realm.where(Object::class.java).max("id") as Long
        maxId += 1;

        val obj = realm.createObject(Object::class.java, maxId)
        obj.name = name
        obj.age = age

        realm.commitTransaction()
    }

    fun readAllObjects() {
        val allObjects = realm.where(Object::class.java).findAll()
        allObjects.forEach { obj ->
            println("Object: ${obj.name} ${obj.age}")
        }
    }

    fun readObjectsWithName(name: String) {
        var matchingObjects = realm.where(Object::class.java).equalTo("name", name).findAll()
        if (matchingObjects.size != 0) {
            matchingObjects.forEach { obj ->
                nameTextView.text = obj.name
                ageTextView.text = obj.age.toString()
            }
        } else {
            nameTextView.text = "none found"
            ageTextView.text = "none found"
        }

    }

    fun updateObjectsWithName(name: String, newAge: Int) {
        var matchingObjects = realm.where(Object::class.java).equalTo("name", name).findAll()
        matchingObjects.forEach { obj ->
            realm.beginTransaction()
            obj.age = newAge
            realm.commitTransaction()
        }
    }

    fun deleteObjectsWithName(name: String) {
        var matchingObjects = realm.where(Object::class.java).equalTo("name", name).findAll()
        matchingObjects.forEach { obj ->
            realm.beginTransaction()
            obj.deleteFromRealm()
            realm.commitTransaction()
            println("Deleted")
        }
    }
}
