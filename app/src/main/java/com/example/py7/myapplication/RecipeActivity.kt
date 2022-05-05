package com.example.py7.myapplication

import android.content.ContentValues
import android.content.DialogInterface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_recipe.*

class RecipeActivity : AppCompatActivity() {

    var id=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe)

        try {
            val bundle = intent.extras
            id = bundle.getInt("MainActId", 0)
            if (id !=0){
                txName.setText(bundle.getString("MainActName"))
                txIng.setText(bundle.getString("MainActIng"))
                txStep.setText(bundle.getString("MainActStep"))
            }
        }catch (ex: Exception){
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.add_menu, menu)

        val itemDelete: MenuItem = menu.findItem(R.id.action_delete)

        itemDelete.isVisible = id != 0
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.action_save -> {
                val dbAdapter = DBAdapter(this)

                val values = ContentValues()
                values.put("Name", txName.text.toString())
                values.put("Ingredients", txIng.text.toString())
                values.put("Step", txStep.text.toString())

                if (id == 0){
                    val mID = dbAdapter.insert(values)

                    if (mID > 0){
                        Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show()
                        finish()
                    }else{
                        Toast.makeText(this, "Failed to save data", Toast.LENGTH_SHORT).show()
                    }
                }else{
                    val selectionArgs = arrayOf(id.toString())
                    val mID = dbAdapter.update(values, "Id=?", selectionArgs)
                    if (mID > 0){
                        Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show()
                        finish()
                    }else{
                        Toast.makeText(this, "Failed to update data", Toast.LENGTH_SHORT).show()
                    }
                }
                true
            }
            R.id.action_delete ->{
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Delete Data")
                builder.setMessage("This Data Will Be Deleted")

                builder.setPositiveButton("Delete") {dialog: DialogInterface?, which: Int ->
                    val dbAdapter = DBAdapter(this)
                    val selectionArgs = arrayOf(id.toString())
                    dbAdapter.delete("Id=?", selectionArgs)
                    Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show()
                    finish()
                }
                builder.setNegativeButton("Cancel"){dialog: DialogInterface?, which: Int ->  }

                val alertDialog: AlertDialog = builder.create()
                alertDialog.setCancelable(false)
                alertDialog.show()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
