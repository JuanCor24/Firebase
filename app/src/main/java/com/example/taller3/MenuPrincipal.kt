package com.example.taller3

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.taller3.databinding.ActivityMenuPrincipalBinding
import com.example.taller3.databinding.IniciarSesionBinding
import com.google.firebase.Firebase
import android.widget.Toast

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

import com.google.firebase.database.database


class MenuPrincipal: AppCompatActivity() {

    private lateinit var binding: ActivityMenuPrincipalBinding


    private lateinit var auth : FirebaseAuth
    private val database = Firebase.database

    private val messageRef = database.getReference("messages")



    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMenuPrincipalBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        messageRef.setValue("Hello World!")





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