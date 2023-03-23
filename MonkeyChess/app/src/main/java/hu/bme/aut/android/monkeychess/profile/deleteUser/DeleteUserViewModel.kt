package hu.bme.aut.android.monkeychess.profile.deleteUser

import android.content.Context
import android.content.Intent
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class DeleteUserViewModel:ViewModel() {
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var userDB: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var password: MutableLiveData<String> = MutableLiveData("")
    private var email: MutableLiveData<String> = MutableLiveData("")

    private fun deleteAccount(context: Context, navController: NavController){
        val user = auth.currentUser!!
        val credential = EmailAuthProvider.getCredential(email.value.toString(), password.value.toString())

        user.reauthenticate(credential).addOnCompleteListener{
            if(it.isSuccessful){
                userDB.collection("users").get().addOnCompleteListener { task->
                    if(task.isSuccessful){
                            for(document in task.result){
                            if(document.data.getValue("E-mail") == auth.currentUser?.email){
                               userDB.collection("users").document(document.id).delete()
                            }
                        }
                    }
                }

               user.delete().addOnCompleteListener { task->
                    if(task.isSuccessful){
                        auth.signOut()
                        navController.navigate("welcome_screen")
                        Toast.makeText(context, "User deleted", Toast.LENGTH_LONG).show()
                    }else{
                        Toast.makeText(context, task.exception?.message, Toast.LENGTH_LONG).show()
                    }
                }.addOnFailureListener{e->
                    Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
                }
            }
        }.addOnFailureListener{
            Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
        }
    }

    fun isDeleteInputValid(context: Context, navController: NavController){
        if(password.value.toString().isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email.value.toString()).matches()){
            deleteAccount(context, navController)
        }
        else{
            Toast.makeText(context, "Please provide your credentials!", Toast.LENGTH_LONG).show()
        }
    }

    fun setPassword(_pass: String){
        password.value = _pass
    }

    fun setEmail(_email: String){
        email.value = _email
    }
}