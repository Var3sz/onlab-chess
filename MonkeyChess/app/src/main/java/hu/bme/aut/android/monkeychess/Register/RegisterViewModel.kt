package hu.bme.aut.android.monkeychess.Register

import android.util.Patterns
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class RegisterViewModel: ViewModel() {
    private var fullname: MutableLiveData<String> = MutableLiveData("")
    private var email: MutableLiveData<String> = MutableLiveData("")
    private var username: MutableLiveData<String> = MutableLiveData("")
    private var password: MutableLiveData<String> = MutableLiveData("")
    private var confirmPassword: MutableLiveData<String> = MutableLiveData("")

    private lateinit var auth: FirebaseAuth

    fun registerUser(){
        auth = FirebaseAuth.getInstance()
        auth.createUserWithEmailAndPassword(email.value.toString(), password.value.toString())
    }

    fun isRegistrationInputValid(): String{
        if(fullname.value.toString().isNotEmpty() && username.value.toString().length >= 5 && password.value.toString().length >= 6 && (email.value.toString().isNotEmpty() || Patterns.EMAIL_ADDRESS.matcher(email.value.toString()).matches()) && confirmPassword.value.toString() == password.value.toString()){
            return "Successful Registration"
        }
        else{
            return if(fullname.value.toString().isEmpty()){
                "Fullname must not be empty!"
            }  else if(email.value.toString().isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email.value.toString()).matches()){
                "Please enter a valid e-mail address!"
            }else if(username.value.toString().length < 5){
                "Username must be at least 5 characters long!"
            } else if(password.value.toString().length < 6){
                "Password must be at least 6 characters long!"
            }
            else{
                "Passwords must match!"
            }
        }
    }



    fun setFullname(_fullname: String){
        fullname.value = _fullname
    }

    fun setEmail(_email: String) {
        email.value = _email
    }

    fun setUsername(_username: String){
        username.value = _username
    }

    fun setPassword(_password: String) {
        password.value = _password
    }

    fun setConfirmpassword(_confirmpassword: String){
        confirmPassword.value = _confirmpassword
    }

    fun getFullname(): String?{
        return fullname.value
    }

    fun getEmail(): String?{
        return email.value
    }

    fun getUsername(): String?{
        return username.value
    }

    fun getPassword(): String?{
        return password.value
    }



}