package com.example.iccchurch

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.app.DatePickerDialog
import android.view.Gravity
import android.widget.*
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import java.util.Calendar

class FinancialDonation : Fragment() {

    private lateinit var donationHistoryTable: TableLayout
    private lateinit var dateInput: TextView
    private lateinit var typeSpinner: Spinner
    private lateinit var amountInput: EditText
    private lateinit var donateButton: Button

    private val database = FirebaseDatabase.getInstance()
    private val donationsRef = database.getReference("donations")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_financial_donation, container, false)

        donationHistoryTable = view.findViewById(R.id.donation_history_table)
        dateInput = view.findViewById(R.id.date_input)
        typeSpinner = view.findViewById(R.id.type_spinner)
        amountInput = view.findViewById(R.id.amount_input)
        donateButton = view.findViewById(R.id.donate_button)

        dateInput.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            DatePickerDialog(
                requireContext(),
                { _, selectedYear, selectedMonth, selectedDay ->
                    val selectedDate = "$selectedYear-${selectedMonth + 1}-$selectedDay"
                    dateInput.text = selectedDate
                },
                year, month, day
            ).show()
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val types = arrayOf("Tithe", "Offering", "Building Fund")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, types)
        typeSpinner.adapter = adapter

        loadDonationHistory()

        donateButton.setOnClickListener {
            saveDonation()
        }
    }

    private fun saveDonation() {
        val date = dateInput.text.toString()
        val type = typeSpinner.selectedItem.toString()
        val amount = amountInput.text.toString()

        if (date.isEmpty() || type.isEmpty() || amount.isEmpty()) {
            Toast.makeText(requireContext(), "Please fill out all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val donation = Donation(date, type, amount)

        donationsRef.push().setValue(donation).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(requireContext(), "Donation saved successfully", Toast.LENGTH_SHORT).show()
                addDonationRow(date, type, amount)
            } else {
                Toast.makeText(requireContext(), "Failed to save donation", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadDonationHistory() {
        donationsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                donationHistoryTable.removeAllViews()
                for (donationSnapshot in snapshot.children) {
                    val donation = donationSnapshot.getValue(Donation::class.java)
                    if (donation != null) {
                        addDonationRow(donation.date, donation.type, donation.amount)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Failed to load donation history", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun addDonationRow(date: String, type: String, amount: String) {
        val row = TableRow(requireContext())
        val dateText = TextView(requireContext())
        val typeText = TextView(requireContext())
        val amountText = TextView(requireContext())

        dateText.text = date
        typeText.text = type
        amountText.text = "R$amount"

        dateText.setPadding(8, 8, 8, 8)
        dateText.gravity = Gravity.CENTER
        dateText.setTextColor(android.graphics.Color.BLACK)

        typeText.setPadding(8, 8, 8, 8)
        typeText.gravity = Gravity.CENTER
        typeText.setTextColor(android.graphics.Color.BLACK)

        amountText.setPadding(8, 8, 8, 8)
        amountText.gravity = Gravity.CENTER
        amountText.setTextColor(android.graphics.Color.BLACK)

        row.addView(dateText)
        row.addView(typeText)
        row.addView(amountText)

        donationHistoryTable.addView(row)
    }

    data class Donation(val date: String = "", val type: String = "", val amount: String = "")
}
