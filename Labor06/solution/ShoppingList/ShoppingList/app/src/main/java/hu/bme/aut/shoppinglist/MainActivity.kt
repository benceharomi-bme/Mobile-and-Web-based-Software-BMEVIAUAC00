package hu.bme.aut.shoppinglist

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import hu.bme.aut.shoppinglist.adapter.ShoppingAdapter
import hu.bme.aut.shoppinglist.data.ShoppingItem
import hu.bme.aut.shoppinglist.data.ShoppingListDatabase
import hu.bme.aut.shoppinglist.fragments.NewShoppingItemDialogFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity(), ShoppingAdapter.ShoppingItemClickListener,
    NewShoppingItemDialogFragment.NewShoppingItemDialogListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ShoppingAdapter
    private lateinit var database: ShoppingListDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        fab.setOnClickListener {
            NewShoppingItemDialogFragment().show(
                supportFragmentManager,
                NewShoppingItemDialogFragment.TAG
            )
        }
        database = Room.databaseBuilder(
            applicationContext,
            ShoppingListDatabase::class.java,
            "shopping-list"
        ).build()
        initRecyclerView()
    }

    private fun initRecyclerView() {
        recyclerView = MainRecyclerView
        adapter = ShoppingAdapter(this)
        loadItemsInBackground()
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    private fun loadItemsInBackground() {
        thread {
            val items = database.shoppingItemDao().getAll()
            runOnUiThread {
                adapter.update(items)
            }
        }
    }

    private fun removeAllItems() {
        thread {
            database.shoppingItemDao().deleteAll()
            Log.d("MainActivity", "All ShoppingItems removed successfully")
            runOnUiThread {
                adapter.removeAll()

            }
        }
    }

    override fun onItemChanged(item: ShoppingItem) {
        thread {
            database.shoppingItemDao().update(item)
            Log.d("MainActivity", "ShoppingItem update was successful")
        }
    }

    override fun onItemRemoved(item: ShoppingItem) {
        thread {
            database.shoppingItemDao().deleteItem(item)
            Log.d("MainActivity", "ShoppingItem removed successfully")
        }
    }

    override fun onItemEdited(item: ShoppingItem) {
        NewShoppingItemDialogFragment(item).show(
            supportFragmentManager,
            NewShoppingItemDialogFragment.TAG
        )
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            R.id.remove_all -> {
                removeAllDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun removeAllDialog() {
        AlertDialog.Builder(this)
            .setMessage(R.string.remove_all_dialog_message)
            .setPositiveButton(R.string.ok) { _, _ ->
                if (adapter.itemCount > 0) {
                    removeAllItems()
                    Snackbar.make(
                        findViewById(android.R.id.content),
                        R.string.all_items_removed_message,
                        BaseTransientBottomBar.LENGTH_SHORT
                    ).show()
                } else {
                    Snackbar.make(
                        findViewById(android.R.id.content),
                        R.string.no_item_to_remove,
                        BaseTransientBottomBar.LENGTH_SHORT
                    ).show()
                }
            }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }

    override fun onShoppingItemCreated(newItem: ShoppingItem) {
        thread {
            val newId = database.shoppingItemDao().insert(newItem)
            val newShoppingItem = newItem.copy(
                id = newId
            )
            runOnUiThread {
                adapter.addItem(newShoppingItem)
            }
        }
    }

    override fun onShoppingItemEdited(item: ShoppingItem) {
        thread {
            database.shoppingItemDao().update(item)

            runOnUiThread {
                adapter.editItem(item)
            }
        }
    }
}