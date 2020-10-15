package hu.bme.aut.publictransport

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_list.*

class ListActivity : AppCompatActivity() {

    companion object {
        const val TYPE_BUS = 1
        const val TYPE_TRAIN = 2
        const val TYPE_BIKE = 3
        const val TYPE_BOAT = 4
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        btnBike.setOnClickListener {
            val intent = Intent(this, DetailsActivity::class.java)
            intent.putExtra(DetailsActivity.KEY_TRANSPORT_TYPE, TYPE_BIKE)
            startActivity(intent)
        }

        btnBus.setOnClickListener {
            val intent = Intent(this, DetailsActivity::class.java)
            intent.putExtra(DetailsActivity.KEY_TRANSPORT_TYPE, TYPE_BUS)
            startActivity(intent)
        }

        btnTrain.setOnClickListener {
            val intent = Intent(this, DetailsActivity::class.java)
            intent.putExtra(DetailsActivity.KEY_TRANSPORT_TYPE, TYPE_TRAIN)
            startActivity(intent)
        }

        btnBoat.setOnClickListener {
            val intent = Intent(this, DetailsActivity::class.java)
            intent.putExtra(DetailsActivity.KEY_TRANSPORT_TYPE, TYPE_BOAT)
            startActivity(intent)
        }
    }
}