package hu.bme.aut.publictransport

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.DatePicker
import kotlinx.android.synthetic.main.activity_details.*
import java.util.*

class DetailsActivity : AppCompatActivity() {

    companion object {
        const val KEY_TRANSPORT_TYPE = "KEY_TRANSPORT_TYPE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        val transportType = this.intent.getIntExtra(KEY_TRANSPORT_TYPE, -1)
        tvTicketType.text = getTypeString(transportType)

        btnPurchase.setOnClickListener {
            val typeString = getTypeString(transportType)
            val dateString = getDateFrom(dpStartDate) + " - " + getDateFrom(dpEndDate)

            val intent = Intent(this, PassActivity::class.java)
            intent.putExtra(PassActivity.KEY_TYPE_STRING, typeString)
            intent.putExtra(PassActivity.KEY_DATE_STRING, dateString)
            startActivity(intent)
        }
    }

    private fun getTypeString(transportType: Int): String? {
        return when (transportType) {
            ListActivity.TYPE_BUS -> "Bus pass"
            ListActivity.TYPE_TRAIN -> "Train pass"
            ListActivity.TYPE_BIKE -> "Bike pass"
            ListActivity.TYPE_BOAT -> "Boat pass"

            else -> "Unknown pass type"
        }
    }

    private fun getDateFrom(picker: DatePicker): String? {
        return String.format(
            Locale.getDefault(), "%04d.%02d.%02d.",
            picker.year, picker.month + 1, picker.dayOfMonth
        )
    }
}