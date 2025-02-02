package com.example.testfirestore

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.FirebaseException
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), MainAdapter.UpdateUserListener {
    private val collectionName = "users"
    private val collection = Firebase.firestore.collection(collectionName)
    private var documentId = 1
    private var adapter = MainAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()

//        uncomment for test error message from Could FireStore
//        readFireStoreFail()
    }

    private fun initView() {
        rvUser.layoutManager = LinearLayoutManager(this)
        rvUser.adapter = adapter

        //Read Data first from Could FireStore
        readFireStore()

        //Add User Data to Could FireStore
        btAddUser.setOnClickListener {
            addUser()
        }
    }

    private fun addUser() {
        collection.get().addOnSuccessListener { result ->
            if (!result.isEmpty) {
                documentId = result.documents.last().id.toInt() + 1
            }

            val user = User(documentId.toString(), "User$documentId")

            collection.document(documentId.toString()).set(user)
                .addOnSuccessListener {
                    adapter.addList(user)
                    observeEvent(user.id)
                    documentId += 1
                }
        }
    }

    private fun readFireStore() {
        collection.get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    observeEvent(document.id)
                }
            }
            .addOnFailureListener { exception ->
                Log.d("read_fail", "Error getting documents.", exception)
            }
    }

    private fun observeEvent(documentId: String) {
        collection.document(documentId).addSnapshotListener { value, error ->
            value?.let { document ->
                document.toObject(User::class.java)?.let { user ->
                    if (adapter.getList().none { it.id == user.id }) {
                        adapter.addList(user)
                    } else {
                        Log.d("observeEvent", user.toString())
                        adapter.updateList(user)
                    }
                }
            }
        }
    }

    private fun readFireStoreFail() {
        collection.document("Failssss").get()
            .addOnSuccessListener { document ->
                Log.d("read_success", document.toString())
            }
            .addOnFailureListener { exception ->
                val errorCode = (exception as FirebaseFirestoreException).code
                val errorMessage = exception.message

                Log.d("read_fail", errorCode.value().toString() + errorCode.name)
                Log.d("read_fail", errorMessage.toString())
            }
    }

    override fun updateProgressBar(data: User) {
        collection.document(data.id).set(data)
    }
}