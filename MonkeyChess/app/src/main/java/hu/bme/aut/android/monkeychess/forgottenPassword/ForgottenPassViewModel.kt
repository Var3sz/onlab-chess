package hu.bme.aut.android.monkeychess.forgottenPassword

import android.content.Context
import android.util.Patterns
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth

class ForgottenPassViewModel : ViewModel(){
    private var email: MutableLiveData<String> = MutableLiveData("")
    private lateinit var auth: FirebaseAuth


    private fun sendResetPasswordEmail(context: Context, navController: NavController){
        auth = FirebaseAuth.getInstance()
        auth.sendPasswordResetEmail(email.value.toString()).addOnCompleteListener {
            if(it.isSuccessful){
                navController.navigate("login_screen")
                Toast.makeText(context, "E-mailt sent!", Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(context, "Unable to send e-mail!", Toast.LENGTH_LONG).show()
            }
        }.addOnFailureListener{
            Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
        }
    }

    fun isForgottenInputValid(context: Context, navController: NavController){
        if(email.value.toString().isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email.value.toString()).matches()){
            sendResetPasswordEmail(context, navController)
        }
        else{
            Toast.makeText(context, "Please provide a proper e-mail address!", Toast.LENGTH_LONG).show()
        }
    }

    fun setEmail(_email: String){
        email.value = _email
    }
}