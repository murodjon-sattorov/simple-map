package uz.murodjon_sattorov.yotoqxona_task.activities

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import uz.murodjon_sattorov.firebasechatapp.dialogs.LoadProgressDialog
import uz.murodjon_sattorov.yotoqxona_task.R
import uz.murodjon_sattorov.yotoqxona_task.databinding.ActivityProfileBinding
import uz.murodjon_sattorov.yotoqxona_task.model.User
import java.io.File
import java.io.IOException
import java.util.*


class ProfileActivity : AppCompatActivity() {

    private lateinit var profileBinding: ActivityProfileBinding

    companion object {
        val IMAGE_PICK_CODE = 1000
        val PERMISSION_CODE = 1001
    }

    private var auth: FirebaseAuth? = null
    private var firebaseUser: FirebaseUser? = null
    private lateinit var reference: DatabaseReference
    private lateinit var storage: FirebaseStorage

    private lateinit var dialog: LoadProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        profileBinding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(profileBinding.root)

        dialog = LoadProgressDialog(this)
        dialog.loadDialog()

        auth = Firebase.auth
        firebaseUser = auth?.currentUser

        storage = FirebaseStorage.getInstance()

        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser!!.uid)

        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(User::class.java)

                dialog.dismissDialog()

                val pathReference =
                    storage.getReferenceFromUrl("gs://yotoqxona-task.appspot.com")
                        .child("users_images/" + user?.id.toString())
                try {
                    val file: File = File.createTempFile("profile_image", "jpg")
                    pathReference.getFile(file).addOnSuccessListener { taskSnapshot ->
                        val bitmap: Bitmap = BitmapFactory.decodeFile(file.absolutePath)
                        Log.d("PATH", "onDataChange: ${file.absolutePath}")
                        profileBinding.selectImage.setImageBitmap(bitmap)
                        if (profileBinding.selectImage.drawable == null) {
                            profileBinding.imageError.setImageResource(R.drawable.ic_error)
                        } else {
                            profileBinding.imageError.setImageResource(R.drawable.ic_round_done_24)
                        }
                    }.addOnFailureListener {
                        Log.d("PATH", "onDataChange: ${it.message}")
                    }
                    Log.d("PATH", "onDataChange: $pathReference")
                } catch (e: IOException) {
                    e.printStackTrace()
                }

                profileBinding.inputFullName.setText(user?.userFullName)
                profileBinding.inputUniverName.setText(user?.userUniversitetName)
                profileBinding.inputGroupName.setText(user?.userGroupName)
                profileBinding.inputLoginName.setText(user?.userStudentId)
                profileBinding.inputViloyatName.setText(user?.userViloyat)
                profileBinding.inputTumanName.setText(user?.userTuman)
                profileBinding.inputManzilName.setText(user?.userManzil)
                profileBinding.inputNumberName.setText(user?.userPhoneNumber.toString())

                if (profileBinding.inputViloyatName.text!!.isEmpty()) {
                    profileBinding.inputViloyatName.error = "Empty space"
                }
                if (profileBinding.inputTumanName.text!!.isEmpty()) {
                    profileBinding.inputTumanName.error = "Empty space"
                }
                if (profileBinding.inputManzilName.text!!.isEmpty()) {
                    profileBinding.inputManzilName.error = "Empty space"
                }
                if (profileBinding.inputNumberName.text!!.length != 12) {
                    profileBinding.inputNumberName.error = "At last 12 chars"
                }


            }


            override fun onCancelled(error: DatabaseError) {
                dialog.dismissDialog()
            }

        })

        profileBinding.selectImage.setOnClickListener {
            checkPermission()
        }

        profileBinding.updateBtn.setOnClickListener {
            updateAllData()
        }

        profileBinding.exitUser.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle(getString(R.string.log_out))
            builder.setMessage(getString(R.string.chiqishni_hohlaysizmi))
            builder.setPositiveButton("Ok") { p0, p1 ->
                auth?.signOut()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
            builder.setNegativeButton(getString(R.string.cancel)) { p0, p1 ->
                p0.dismiss()
            }
            builder.show()
        }


    }

    private fun checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                val permission = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                requestPermissions(permission, PERMISSION_CODE)
            } else {
                pickImageFromGallery()
            }
        } else {
            pickImageFromGallery()
        }
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickImageFromGallery()
                }
            }
            else -> {
                Toast.makeText(this, "Permission denied...", Toast.LENGTH_SHORT).show()
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            dialog.loadDialog()
            profileBinding.selectImage.setImageURI(data?.data)
            val storageReference =
                FirebaseStorage.getInstance().getReference("users_images/${firebaseUser?.uid}")
            storageReference.putFile(data?.data!!).addOnSuccessListener {
                dialog.dismissDialog()
            }.addOnFailureListener {
                dialog.dismissDialog()
                Toast.makeText(this, "File not found", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateAllData() {
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser!!.uid)

        if (profileBinding.selectImage.drawable != null) {
            if (profileBinding.inputFullName.text!!.isNotEmpty()) {
                if (profileBinding.inputUniverName.text!!.isNotEmpty()) {
                    if (profileBinding.inputGroupName.text!!.isNotEmpty()) {
                        if (profileBinding.inputLoginName.text!!.isNotEmpty()) {
                            if (profileBinding.inputViloyatName.text!!.isNotEmpty()) {
                                if (profileBinding.inputTumanName.text!!.isNotEmpty()) {
                                    if (profileBinding.inputManzilName.text!!.isNotEmpty()) {
                                        if (profileBinding.inputNumberName.text!!.length == 12) {
                                            dialog.loadDialog()

                                            reference.child("userFullName")
                                                .setValue(profileBinding.inputFullName.text.toString())
                                                .addOnCompleteListener {

                                                }
                                            reference.child("userUniversitetName")
                                                .setValue(profileBinding.inputUniverName.text.toString())
                                                .addOnCompleteListener {

                                                }
                                            reference.child("userGroupName")
                                                .setValue(profileBinding.inputGroupName.text.toString())
                                                .addOnCompleteListener {

                                                }
                                            reference.child("userStudentId")
                                                .setValue(profileBinding.inputLoginName.text.toString())
                                                .addOnCompleteListener {

                                                }
                                            reference.child("userViloyat")
                                                .setValue(profileBinding.inputViloyatName.text.toString())
                                                .addOnCompleteListener {

                                                }
                                            reference.child("userTuman")
                                                .setValue(profileBinding.inputTumanName.text.toString())
                                                .addOnCompleteListener {

                                                }
                                            reference.child("userManzil")
                                                .setValue(profileBinding.inputManzilName.text.toString())
                                                .addOnCompleteListener {

                                                }
                                            reference.child("userPhoneNumber")
                                                .setValue(profileBinding.inputNumberName.text.toString())
                                                .addOnCompleteListener {
                                                    dialog.dismissDialog()
                                                    Toast.makeText(
                                                        this,
                                                        "All data updated",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                    startActivity(
                                                        Intent(
                                                            this,
                                                            ProfileActivity::class.java
                                                        )
                                                    )
                                                    finish()
                                                }

                                        } else {
                                            profileBinding.inputNumberName.error = "Empty space"
                                            profileBinding.inputNumberName.requestFocus()
                                        }
                                    } else {
                                        profileBinding.inputManzilName.error = "Empty space"
                                        profileBinding.inputManzilName.requestFocus()
                                    }
                                } else {
                                    profileBinding.inputTumanName.error = "Empty space"
                                    profileBinding.inputTumanName.requestFocus()
                                }
                            } else {
                                profileBinding.inputViloyatName.error = "Empty space"
                                profileBinding.inputViloyatName.requestFocus()
                            }
                        } else {
                            profileBinding.inputLoginName.error = "Empty space"
                            profileBinding.inputLoginName.requestFocus()
                        }
                    } else {
                        profileBinding.inputGroupName.error = "Empty space"
                        profileBinding.inputGroupName.requestFocus()
                    }
                } else {
                    profileBinding.inputUniverName.error = "Empty space"
                    profileBinding.inputUniverName.requestFocus()
                }
            } else {
                profileBinding.inputFullName.error = "Empty space"
                profileBinding.inputFullName.requestFocus()
            }
        }

    }

    override fun onResume() {
        super.onResume()
        if (profileBinding.selectImage.drawable == null) {
            profileBinding.imageError.setImageResource(R.drawable.ic_error)
        } else {
            profileBinding.imageError.setImageResource(R.drawable.ic_round_done_24)
        }
    }

}