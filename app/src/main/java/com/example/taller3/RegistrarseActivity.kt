package com.example.taller3




import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.taller3.databinding.ActivityRegistrarseBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.database.database


class RegistrarseActivity:AppCompatActivity() {
    private lateinit var binding: ActivityRegistrarseBinding
    private lateinit var auth: FirebaseAuth
    private val database = Firebase.database
    private val messageRef = database.getReference("messages")




    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityRegistrarseBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()


        binding.button.setOnClickListener {
            signUp()
        }
    }




    private fun signUp() {
        auth.createUserWithEmailAndPassword(
            binding.correoForm.text.toString(),
            binding.contraForm.text.toString()
        ).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                val user = auth.currentUser
                val profileUpdates = userProfileChangeRequest {
                    displayName = binding.nombreForm.text.toString()
                }
                user?.updateProfile(profileUpdates)
                    ?.addOnCompleteListener { profileUpdateTask ->
                        if (profileUpdateTask.isSuccessful) {
                            // Update successful, proceed to the next activity
                            val userid = Firebase.auth.currentUser?.uid
                            val identificacion = binding.latitudForm.text.toString()
                            val latitudString = binding.contraForm2.text.toString()
                            val latitud: Double = latitudString.toDoubleOrNull() ?: 0.0
                            val longitudString = binding.longitud.text.toString()
                            val longitud: Double = longitudString.toDoubleOrNull() ?: 0.0
                            val usuario = User(binding.nombreForm.text.toString(),binding.apellidoForm.text.toString(),identificacion, latitud,longitud,false)
                            if (userid != null) {
                                messageRef.child("users").child(userid).setValue(usuario).addOnSuccessListener {
                                    Toast.makeText(this, "Usuario guardado correctamente", Toast.LENGTH_SHORT).show()
                                }.addOnFailureListener{
                                    Toast.makeText(this, "Error al guardar el usuario", Toast.LENGTH_SHORT).show()
                                }


                            }
                            Toast.makeText(applicationContext, "Usuario registrado", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this@RegistrarseActivity, MainActivity::class.java))
                        } else {
                            // Update failed, display a message to the user
                            Toast.makeText(applicationContext, "Error al actualizar el perfil del usuario", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                // If sign in fails, display a message to the user.
                task.exception?.localizedMessage?.let {
                    val errorMessage = task.exception?.localizedMessage ?: "Error desconocido"
                    Toast.makeText(applicationContext, "Fallo al registrar usuario: $errorMessage", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}