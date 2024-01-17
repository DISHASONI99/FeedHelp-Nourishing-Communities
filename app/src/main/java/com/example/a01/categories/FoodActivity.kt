package com.example.a01.categories

import android.app.Dialog
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import com.example.a01.MainActivity
import com.example.a01.R
import com.example.a01.fragments.Home
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.travijuu.numberpicker.library.NumberPicker

class FoodActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var dialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)


        db = FirebaseFirestore.getInstance()

        val plasticAmountPicker: NumberPicker = findViewById(R.id.numberPickerPlastic)
        val cardboardAmountPicker: NumberPicker = findViewById(R.id.numberPickerCardboard)
        val metalAmountPicker: NumberPicker = findViewById(R.id.numberPickerMetal)

        plasticAmountPicker.setMax(10)
        plasticAmountPicker.setMin(0)
        plasticAmountPicker.setUnit(1)
        plasticAmountPicker.setValue(0)

        cardboardAmountPicker.setMax(10)
        cardboardAmountPicker.setMin(0)
        cardboardAmountPicker.setUnit(1)
        cardboardAmountPicker.setValue(0)

        metalAmountPicker.setMax(10)
        metalAmountPicker.setMin(0)
        metalAmountPicker.setUnit(1)
        metalAmountPicker.setValue(0)

        val address: EditText = findViewById(R.id.editTextAddress)
        val bookButton: Button = findViewById(R.id.buttonBookPickup)
        // upload all the data to firebase collection orders

        bookButton.setOnClickListener {
            val order = hashMapOf(
                "plasticAmount" to plasticAmountPicker.getValue(),
                "cardboardAmount" to cardboardAmountPicker.getValue(),
                "metalAmount" to metalAmountPicker.getValue(),
                "address" to address.text.toString(),
                "timestamp" to System.currentTimeMillis(),
                "userId" to FirebaseAuth.getInstance().currentUser?.uid,
            )

            db.collection("Food_orders")
                .add(order)
                .addOnSuccessListener { documentReference ->
                    Toast.makeText(this@FoodActivity, "Your donation was successfully placed! Your OrderId is: ${documentReference.id}", Toast.LENGTH_LONG).show()
                    // Create the Dialog here
                    dialog = Dialog(this)
                    dialog.setContentView(R.layout.custom_dialog_layout)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        dialog.window?.setBackgroundDrawable(ContextCompat.getDrawable(this@FoodActivity, R.drawable.custom_dialog_background))
                    }
                    dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                    dialog.setCancelable(false) // Optional
                    dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation // Setting the animations to dialog

                    val okayButton: Button = dialog.findViewById(R.id.btn_okay)

                    okayButton.setOnClickListener {
                        dialog.dismiss()
                    }
                    dialog.show()
                    val fragment = Home()
                    val transaction = supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.frame_layout, fragment)
                    transaction.addToBackStack(null)
                    transaction.commit()
                }
                .addOnFailureListener { e ->
                    println("Error adding document: $e")
                }
        }
    }
}