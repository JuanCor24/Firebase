package com.example.taller3

import com.example.taller3.R
import android.content.Intent
import android.os.Bundle
import android.renderscript.Sampler.Value
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.example.taller3.databinding.ActivityMenuPrincipalBinding
import com.example.taller3.databinding.IniciarSesionBinding
import com.google.firebase.Firebase
import android.widget.Toast

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

import com.google.firebase.database.database


class MenuPrincipal: AppCompatActivity() {

    private lateinit var binding: ActivityMenuPrincipalBinding



    private lateinit var auth : FirebaseAuth
    private val database = Firebase.database

    private val messageRef = database.getReference("messages/users")





    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMenuPrincipalBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val listView: ListView = findViewById(R.id.listaView)


        messageRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val userList = mutableListOf<String>()
                for (userSnapshot in snapshot.children) {
                    val user = userSnapshot.getValue(User::class.java)
                    user?.let {
                        // Agregar el nombre del usuario a la lista
                        userList.add("${user.nombre} ${user.apellido}")
                    }
                }
                // Crear un adaptador para el ListView y establecerlo
                val adapter = ArrayAdapter(this@MenuPrincipal, android.R.layout.simple_list_item_1, userList)
                listView.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MenuPrincipal, "Error al cancelar la lectura de la base de datos", Toast.LENGTH_SHORT).show()
            }
        })




        binding.cameraActivityButton.setOnClickListener(){
            auth.signOut()
            val i = Intent(this,IniciarSesionBinding::class.java)
            startActivity(i)
        }


        binding.disponible.setOnClickListener(){
            val currentUser = FirebaseAuth.getInstance().currentUser
            val username = currentUser?.displayName ?: "Usuario desconocido"
            Toast.makeText(this, "Usuario autenticado: $username", Toast.LENGTH_SHORT).show()

            val userid = currentUser?.uid
            val myRef = database.getReference("messages/users/$userid")
            val childUpdates = hashMapOf<String, Any>(
                "disponible" to true
            )
            myRef.updateChildren(childUpdates)
        }
    }


}

class User(
    var nombre: String = "",
    var apellido: String = "",
    var numeroIdentificacion: String = "",
    var latitud: Double = 0.0,
    var longitud: Double = 0.0,
    var disponible: Boolean = false
)