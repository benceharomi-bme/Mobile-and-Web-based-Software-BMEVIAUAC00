package hu.bme.aut.shoppinglist.fragments

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import hu.bme.aut.shoppinglist.R
import hu.bme.aut.shoppinglist.data.ShoppingItem

class NewShoppingItemDialogFragment : DialogFragment() {
    interface NewShoppingItemDialogListener {
        fun onShoppingItemCreated(newItem: ShoppingItem)
    }

    companion object {
        const val TAG = "NewShoppingItemDialogFragment"
    }

    private lateinit var nameEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var estimatedPriceEditText: EditText
    private lateinit var categorySpinner: Spinner
    private lateinit var alreadyPurchasedCheckBox: CheckBox

    private lateinit var listener: NewShoppingItemDialogListener

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setTitle(R.string.new_shopping_item)
            .setView(getContentView())
            .setPositiveButton(R.string.ok) { dialogInterface, i ->
                if (isValid()) {
                    listener.onShoppingItemCreated(getShoppingItem());

                }
            }
            .setNegativeButton(R.string.cancel, null)
            .create()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as? NewShoppingItemDialogListener
            ?: throw RuntimeException("Activity must implement the NewShoppingItemDialogListener interface!")
    }
    
    private fun getContentView(): View {
        val contentView =
            LayoutInflater.from(context).inflate(R.layout.dialog_new_shopping_item, null)
        nameEditText = contentView.findViewById(R.id.ShoppingItemNameEditText)
        descriptionEditText = contentView.findViewById(R.id.ShoppingItemDescriptionEditText)
        estimatedPriceEditText = contentView.findViewById(R.id.ShoppingItemEstimatedPriceEditText)
        categorySpinner = contentView.findViewById(R.id.ShoppingItemCategorySpinner)
        categorySpinner.setAdapter(
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                resources.getStringArray(R.array.category_items)
            )
        )
        alreadyPurchasedCheckBox = contentView.findViewById(R.id.ShoppingItemIsPurchasedCheckBox)
        return contentView
    }

    private fun isValid() = nameEditText.text.isNotEmpty()

    private fun getShoppingItem() = ShoppingItem(
        id = null,
        name = nameEditText.text.toString(),
        description = descriptionEditText.text.toString(),
        estimatedPrice = try {
            estimatedPriceEditText.text.toString().toInt()
        } catch (e: java.lang.NumberFormatException) {
            0
        },
        category = ShoppingItem.Category.getByOrdinal(categorySpinner.selectedItemPosition)
            ?: ShoppingItem.Category.BOOK,
        isBought = alreadyPurchasedCheckBox.isChecked
    )
}