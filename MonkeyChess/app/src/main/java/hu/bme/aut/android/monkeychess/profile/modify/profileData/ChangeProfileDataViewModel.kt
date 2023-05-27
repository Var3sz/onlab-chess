package hu.bme.aut.android.monkeychess.profile.modify.profileData

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ChangeProfileDataViewModel : ViewModel() {
    private var newUsername: MutableLiveData<String> = MutableLiveData("")
    private var newFullname: MutableLiveData<String> = MutableLiveData("")
    private var auth = FirebaseAuth.getInstance()
    private var userDB = FirebaseFirestore.getInstance()


    private fun changeProfileData(context: Context, navController: NavController){
        val user = auth.currentUser!!

        userDB.collection("users").get().addOnCompleteListener { task ->
            if(task.isSuccessful){
                for(document in task.result){
                    if(document.data.getValue("E-mail") == user.email){
                        if(document.data.getValue("Username") != newUsername.value.toString() && document.data.getValue("Name") != newFullname.value.toString()){
                            if(newFullname.value.toString().isNotEmpty() && newUsername.value.toString().isNotEmpty()){
                                if(newUsername.value.toString().length >= 5){
                                    userDB.collection("users").document(document.id).update("Username", newUsername.value.toString())
                                    userDB.collection("users").document(document.id).update("Name", newFullname.value.toString())
                                    navController.navigate("profile_screen")
                                    Toast.makeText(context, "Username and fullname updated successfully!", Toast.LENGTH_LONG).show()
                                }else{
                                    Toast.makeText(context, "Username must be at least 5 characters long!", Toast.LENGTH_LONG).show()
                                }
                            }
                            else if(newUsername.value.toString().isNotEmpty()){
                                if(newUsername.value.toString().length >= 5){
                                    userDB.collection("users").document(document.id).update("Username", newUsername.value.toString())
                                    navController.navigate("profile_screen")
                                    Toast.makeText(context, "Username updated successfully!", Toast.LENGTH_LONG).show()
                                }
                                else{
                                    Toast.makeText(context, "Username must be at least 5 characters long!", Toast.LENGTH_LONG).show()
                                }
                            }
                            else {
                                userDB.collection("users").document(document.id).update("Name", newFullname.value.toString())
                                navController.navigate("profile_screen")
                                Toast.makeText(context, "Fullname updated successfully!", Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                }
            }
        }.addOnFailureListener{e ->
            Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
        }
    }

    fun isChangeProfileDataInputValid(context: Context, navController: NavController){
        if(newUsername.value.toString().isNotEmpty() || newFullname.value.toString().isNotEmpty()){
            changeProfileData(context, navController)
        }
        else{
            Toast.makeText(context, "Please enter what you want to modify!", Toast.LENGTH_LONG).show()
        }
    }

    fun setNewFullname(_newFullname: String){
        newFullname.value = _newFullname
    }

    fun setNewUsername(_newUsername: String){
        newUsername.value = _newUsername
    }
}