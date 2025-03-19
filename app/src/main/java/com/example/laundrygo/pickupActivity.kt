package com.example.laundrygo

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class pickupActivity : AppCompatActivity() {

    private lateinit var etFullName: EditText
    private lateinit var etDate: EditText
    private lateinit var timePicker: TimePicker
    private lateinit var btnConfirm: Button
    private lateinit var ivGcash: ImageView
    private lateinit var ivCash: ImageView
    private lateinit var tvTotalCost: TextView

    private var selectedPaymentMethod: String = ""
    private var totalCost: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pickup)

        // Initialize Views
        etFullName = findViewById(R.id.etfullname)
        etDate = findViewById(R.id.etDate) // FIXED: Corrected ID reference
        timePicker = findViewById(R.id.timePicker)
        btnConfirm = findViewById(R.id.buttonLogin)
        ivGcash = findViewById(R.id.ivgcash)
        ivCash = findViewById(R.id.ivcash)
        tvTotalCost = findViewById(R.id.tvTotalCost)

        // Retrieve total cost from Intent
        totalCost = intent.getIntExtra("TOTAL_COST", 0)
        tvTotalCost.text = "Total Cost: PHP $totalCost"

        // DatePicker setup
        etDate.inputType = android.text.InputType.TYPE_NULL
        etDate.setOnClickListener { showDatePicker() }

        // Back button listener
        findViewById<ImageView>(R.id.backButton)?.setOnClickListener {
            finish() // Finish the activity properly
        }

        // Payment method selection
        ivGcash.setOnClickListener {
            selectedPaymentMethod = "GCash"
            ivGcash.setBackgroundResource(R.drawable.selected_border)
            ivCash.setBackgroundResource(0) // Remove border from Cash
        }

        ivCash.setOnClickListener {
            selectedPaymentMethod = "Cash"
            ivCash.setBackgroundResource(R.drawable.selected_border)
            ivGcash.setBackgroundResource(0) // Remove border from GCash
        }


        // Confirm button listener
        btnConfirm.setOnClickListener {
            showConfirmationDialog()
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            val formattedDate = String.format("%02d/%02d/%02d", selectedMonth + 1, selectedDay, selectedYear % 100)
            etDate.setText(formattedDate)
        }, year, month, day)

        datePickerDialog.show()
    }

    private fun showConfirmationDialog() {
        val orderId = "PICK" + UUID.randomUUID().toString().take(6) // FIXED: Better ID generation
        val hour = timePicker.hour
        val minute = timePicker.minute
        val formattedTime = String.format("%02d:%02d", hour, minute)
        val selectedDate = etDate.text.toString()
        val fullName = etFullName.text.toString()

        if (fullName.isEmpty() || selectedDate.isEmpty() || selectedPaymentMethod.isEmpty()) {
            Toast.makeText(this, "Please fill all details and select a payment method", Toast.LENGTH_SHORT).show()
            return
        }

        val receiptMessage = """
        Order ID: $orderId
        Name: $fullName
        Pickup Date: $selectedDate
        Pickup Time: $formattedTime
        Payment: $selectedPaymentMethod
        Total Cost: PHP $totalCost
    """.trimIndent()

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Pickup Confirmation")
        builder.setMessage(receiptMessage)
        builder.setPositiveButton("Confirm") { dialog, _ ->
            dialog.dismiss()
            Toast.makeText(this, "Pickup Confirmed!", Toast.LENGTH_LONG).show()

            // Navigate to home screen
            val intent = Intent(this, home::class.java) // FIXED: Ensure correct HomeActivity name
            startActivity(intent)

            // Close PickupActivity
            finish()
        }
        builder.setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
        builder.create().show()
    }
}
