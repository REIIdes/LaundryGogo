package com.example.laundrygo

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class FabricConditionerActivity : AppCompatActivity() {
    private var selectedSoap: String? = null
    private var soapQuantity: Int = 0 // Changed to Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fabric_conditioner)

        // Retrieve selected detergent & quantity as INT
        selectedSoap = intent.getStringExtra("selectedSoap")
        soapQuantity = intent.getIntExtra("soapQuantity", 0) // ✅ Fixed data type

        val fabricOptions = mapOf(
            R.id.fabric1 to "DOWNY",
            R.id.fabric2 to "CHAMPION",
            R.id.fabric3 to "DEL",
            R.id.fabric4 to "SURF"
        )

        fabricOptions.forEach { (layoutId, fabricName) ->
            findViewById<LinearLayout>(layoutId).setOnClickListener {
                showQuantityDialog(fabricName)
            }
        }
    }

    private fun showQuantityDialog(fabricName: String) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_quantity, null)
        val quantityTextView = dialogView.findViewById<TextView>(R.id.quantityTextView)
        var quantity = 1 // Default quantity

        val decrementButton = dialogView.findViewById<Button>(R.id.buttonDecrement)
        val incrementButton = dialogView.findViewById<Button>(R.id.buttonIncrement)
        val confirmButton = dialogView.findViewById<Button>(R.id.buttonConfirm)
        val cancelButton = dialogView.findViewById<Button>(R.id.buttonCancel)

        // Create and Show Dialog
        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false) // Prevent accidental dismiss
            .create()
        dialog.show()

        quantityTextView.text = quantity.toString()

        decrementButton.setOnClickListener {
            if (quantity > 1) {
                quantity--
                quantityTextView.text = quantity.toString()
            }
        }

        incrementButton.setOnClickListener {
            quantity++
            quantityTextView.text = quantity.toString()
        }

        confirmButton.setOnClickListener {
            goToSummaryActivity(fabricName, quantity)
            dialog.dismiss() // Close dialog after confirming
        }

        cancelButton.setOnClickListener {
            dialog.dismiss()
        }
    }

    private fun goToSummaryActivity(fabricName: String, fabricQuantity: Int) {
        val intent = Intent(this, SummaryActivity::class.java).apply {
            putExtra("selectedSoap", selectedSoap) // ✅ Correctly passing detergent name
            putExtra("soapQuantity", soapQuantity) // ✅ Passing as an INT
            putExtra("fabricName", fabricName) // ✅ Passing fabric name
            putExtra("fabricQuantity", fabricQuantity) // ✅ Passing fabric quantity
        }
        startActivity(intent)
    }
}
