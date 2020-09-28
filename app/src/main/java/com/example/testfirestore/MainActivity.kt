package com.example.testfirestore

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val db = Firebase.firestore
    private val collectionName = "users"
    private val collection = db.collection(collectionName)
    private var documentId = 1
    private var adapter = MainAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
//        addUser()
    }

    private fun initView() {
        rvUser.layoutManager = LinearLayoutManager(this)
        rvUser.adapter = adapter
        readFireStore()
    }

    private fun addUser() {
        collection.get().addOnSuccessListener { result ->
            if (!result.isEmpty) {
                documentId = result.documents.last().id.toInt() + 1
            }
            val user = hashMapOf(
                "id" to documentId.toString(),
                "name" to "Ball $documentId"
            )
            collection.document(documentId.toString()).set(user)
                .addOnSuccessListener {
                    documentId += 1
                }
        }
    }

    fun readFireStore() {
        collection.get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d("read", "${document.id} => ${document.data}")

                    val user = hashMapOf(
                        "id" to document.data["id"].toString(),
                        "name" to document.data["name"].toString()
                    )

                    adapter.addList(user)
                }
            }
            .addOnFailureListener { exception ->
                Log.d("read_fail", "Error getting documents.", exception)
            }
    }
}