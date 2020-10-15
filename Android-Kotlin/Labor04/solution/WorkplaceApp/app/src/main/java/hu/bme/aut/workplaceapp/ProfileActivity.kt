package hu.bme.aut.workplaceapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import hu.bme.aut.workplaceapp.adapter.ProfilePageAdapter
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        vpProfile.adapter = ProfilePageAdapter(supportFragmentManager)
    }
}