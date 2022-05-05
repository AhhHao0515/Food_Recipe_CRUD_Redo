package com.example.py7.myapplication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity;
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.BaseAdapter
import android.widget.TextView

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {

    private var listRecipe= ArrayList<Recipe>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            var intent = Intent(this, RecipeActivity::class.java)
            startActivity(intent)
        }

        loadData()
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }

    private fun loadData() {
        var dbAdapter = DBAdapter(this)
        var cursor = dbAdapter.allQuery()

        listRecipe.clear()
        if (cursor.moveToFirst()){
            do {
                val id = cursor.getInt(cursor.getColumnIndex("Id"))
                val name = cursor.getString(cursor.getColumnIndex("Name"))
                val ing = cursor.getString(cursor.getColumnIndex("Ingredients"))
                val step = cursor.getString(cursor.getColumnIndex("Step"))

                listRecipe.add(Recipe(id, name, ing, step))
            }while (cursor.moveToNext())
        }

        var recipeAdapter = RecipeAdapter(this, listRecipe)
        lvRecipe.adapter = recipeAdapter
    }

    inner class RecipeAdapter: BaseAdapter{

        private var recipeList = ArrayList<Recipe>()
        private var context: Context? = null

        constructor(context: Context, recipeList: ArrayList<Recipe>) : super(){
            this.recipeList = recipeList
            this.context = context
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
            val view: View?
            val vh: ViewHolder

            if (convertView == null){
                view = layoutInflater.inflate(R.layout.recipe, parent, false)
                vh = ViewHolder(view)
                view.tag = vh
                Log.i("db", "set tag for ViewHolder, position: " + position)
            }else{
                view = convertView
                vh = view.tag as ViewHolder
            }

            var mRecipe = recipeList[position]

            vh.tvName.text = mRecipe.name
            vh.tvIng.text = mRecipe.ing
            vh.tvStep.text = mRecipe.step

            lvRecipe.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, position, id ->
                updateRecipe(mRecipe)
            }

            return view
        }

        override fun getItem(position: Int): Any {
            return recipeList[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return recipeList.size
        }

    }

    private fun updateRecipe(recipe: Recipe) {
        var  intent = Intent(this, RecipeActivity::class.java)
        intent.putExtra("MainActId", recipe.id)
        intent.putExtra("MainActName", recipe.name)
        intent.putExtra("MainActIng", recipe.ing)
        intent.putExtra("MainActStep", recipe.step)
        startActivity(intent)
    }

    private class ViewHolder(view: View?){
        val tvName: TextView
        val tvIng: TextView
        val tvStep: TextView

        init {
            this.tvName = view?.findViewById(R.id.tvName) as TextView
            this.tvIng = view?.findViewById(R.id.tvIng) as TextView
            this.tvStep = view?.findViewById(R.id.tvStep) as TextView
        }
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
            else -> super.onOptionsItemSelected(item)
        }
    }
}
