package hu.bme.aut.android.monkeychess.login

import android.content.Context
import android.util.Patterns
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth

class LoginViewModel: ViewModel() {
    private var email: MutableLiveData<String> = MutableLiveData("")
    private var password: MutableLiveData<String> = MutableLiveData("")

    private lateinit var auth: FirebaseAuth

    private fun loginUser(context: Context, navController: NavController){
        auth = FirebaseAuth.getInstance()
        auth.signInWithEmailAndPassword(email.value.toString(), password.value.toString()).addOnCompleteListener {
            login -> if(login.isSuccessful){
                val user = auth.currentUser
                if(user != null){
                    if(user.isEmailVerified){
                        navController.navigate("MainMenu_screen")
                        Toast.makeText(context, "Successful login!", Toast.LENGTH_LONG).show()
                    }
                    else{
                        Toast.makeText(context, "E-mail has not been verified!", Toast.LENGTH_LONG).show()
                        auth.signOut()
                    }
                }
            }
        }.addOnFailureListener{
            Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
        }
    }

    fun isLoginInputValid(context: Context, navController: NavController){
        if(password.value.toString().length >= 6 && (email.value.toString().isNotEmpty() || Patterns.EMAIL_ADDRESS.matcher(email.value.toString()).matches())){
            loginUser(context, navController)
        }
        else{
            if(email.value.toString().isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email.value.toString()).matches()){
                Toast.makeText(context, "Please enter a valid e-mail address!", Toast.LENGTH_LONG).show()
            }
            else{
                Toast.makeText(context, "Password must be at least 6 characters long!", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun setEmail(_email: String) {
        email.value = _email
    }

    fun setPassword(_password: String) {
        password.value = _password
    }

    fun getEmail(): String? {
        return  email.value
    }

    fun getPassword(): String?{
        return  email.value
    }
}

