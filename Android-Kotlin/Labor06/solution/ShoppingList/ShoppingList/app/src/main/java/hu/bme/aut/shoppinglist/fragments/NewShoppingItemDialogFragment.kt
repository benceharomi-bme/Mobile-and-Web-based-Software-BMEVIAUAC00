package hu.bme.aut.shoppinglist.fragments

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
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

class NewShoppingItemDialogFragment() : DialogFragment() {
    interface NewShoppingItemDialogListener {
        fun onShoppingItemCreated(newItem: ShoppingItem)
        fun onShoppingItemEdited(item: ShoppingItem)
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

    private var item: ShoppingItem? = null
    private var type: CreateOrEdit = CreateOrEdit.CREATE

    enum class CreateOrEdit {
        CREATE, EDIT
    }

    constructor(item: ShoppingItem) : this() {
        this.item = item
        this.type = CreateOrEdit.EDIT
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        var title: Int = when (type) {
            CreateOrEdit.CREATE -> R.string.new_shopping_item
            CreateOrEdit.EDIT -> R.string.edit_shopping_item
        }
        return AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setView(getContentView())
            .setPositiveButton(R.string.ok) { _, _ ->
                if (isValid()) {
                    if (type == CreateOrEdit.CREATE)
                        listener.onShoppingItemCreated(getShoppingItem())
                    else if (type == CreateOrEdit.EDIT)
                        listener.onShoppingItemEdited(getShoppingItem());
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
        categorySpinner.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            resources.getStringArray(R.array.category_items)
        )
        alreadyPurchasedCheckBox = contentView.findViewById(R.id.ShoppingItemIsPurchasedCheckBox)
        if (item != null) {
            setFields()
        }
        return contentView
    }

    private fun isValid() = nameEditText.text.isNotEmpty()

    private fun getShoppingItem() = ShoppingItem(
        id = item?.id,
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

    fun setFields() {
        nameEditText.setText(item!!.name)
        descriptionEditText.setText(item!!.description)
        estimatedPriceEditText.setText(item!!.estimatedPrice.toString())
        categorySpinner.setSelection(item!!.category.ordinal)
        alreadyPurchasedCheckBox.isChecked = item!!.isBought
    }
}