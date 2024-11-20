package com.example.iccchurch



import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.iccchurch.databinding.ActivityNavigationpageBinding

class Navigationpage : AppCompatActivity() {

    private lateinit var binding: ActivityNavigationpageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNavigationpageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set the default fragment
        replaceFragment(Home())

        // Handle bottom navigation item selections
        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> replaceFragment(Home())
                R.id.announcement -> replaceFragment(Annoucement())
                R.id.bible -> replaceFragment(Bible())
//                R.id.event -> replaceFragment(EventManagement())
                R.id.financial_donation -> replaceFragment(FinancialDonation())
                R.id.note -> replaceFragment(Notes())
                else -> {

                }
            }
            true
        }
    }

    // Function to replace fragments
    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout55555, fragment)
        fragmentTransaction.commit()
    }
}
