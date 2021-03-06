package com.example.mainproject

import android.content.Context
import android.content.Intent
import android.icu.text.Transliterator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import androidx.lifecycle.Observer
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.SearchView
import androidx.core.view.GravityCompat
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.mainproject.adapter.WishlistAdapter
import com.example.mainproject.model.Wishes
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main2.*
import kotlinx.android.synthetic.main.main_include.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

class WishListTv : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    val db by lazy {
        Room.databaseBuilder(
            this,
            AppDatabase::class.java,
            "app.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    val list = arrayListOf<Wishes>()
    val wishlistAdapter = WishlistAdapter(list)

    var trigger = MutableLiveData<Boolean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wish_list)

        setSupportActionBar(toolbar)

        val toogle = ActionBarDrawerToggle(
            this,
            drawer,
            toolbar,
            R.string.open,
            R.string.close
        )

        drawer.addDrawerListener(toogle)
        toogle.syncState()

        navView.setNavigationItemSelectedListener(this)

        progressBar.visibility = View.INVISIBLE

//        var tv_id =intent.getStringExtra("id")
//        var movie_id = intent.getStringExtra("iid")

        db.wishesdao().getAllUsers().observe(this, Observer {
            if (!it.isNullOrEmpty()){
                list.clear()
                trigger.value = true
                list.addAll(it)
                wishlistAdapter.notifyDataSetChanged()
            }
            else{
                list.clear()
                wishlistAdapter.notifyDataSetChanged()
            }

        })

        ParentRView.apply {
            layoutManager = LinearLayoutManager(this@WishListTv,RecyclerView.VERTICAL,false)
            adapter = wishlistAdapter
        }
        wishlistAdapter.onItemClick = {
            val i = Intent(this,Main7Activity::class.java)
            i.putExtra("id",it.id.toString())
            startActivity(i)

//            val j = Intent(this,Main6Activity::class.java)
//            j.putExtra("id",it.id.toString())
//            startActivity(j)

        }

        val itemTouchHelper = ItemTouchHelper(simpleCallback)
        return itemTouchHelper.attachToRecyclerView(ParentRView)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.movies->{
                Toast.makeText(this,"Movies Pressed", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this,MainActivity::class.java))
                finish()
            }
            R.id.tv_series->{
                Toast.makeText(this,"TV Series Pressed", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this,Main3Activity::class.java))
                finish()
            }
            R.id.people->{
                Toast.makeText(this,"People Pressed", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this,Main4Activity::class.java))
                finish()
            }
            R.id.discover->{
                Toast.makeText(this,"Discover Pressed", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this,Main5Activity::class.java))
                finish()
            }
            R.id.wishlist->{
                Toast.makeText(this,"WishList Pressed", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this,WishListTv::class.java))
                finish()
            }
            R.id.wishlistMovie->{
                Toast.makeText(this,"WishList Pressed", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this,WishlistMovie::class.java))
                finish()
            }
        }
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    val simpleCallback = object : ItemTouchHelper.SimpleCallback(
        ItemTouchHelper.START
                or ItemTouchHelper.END
                or ItemTouchHelper.UP
                or ItemTouchHelper.DOWN,
        ItemTouchHelper.LEFT
                or ItemTouchHelper.RIGHT
    ){
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {

            val fromPosition = viewHolder.adapterPosition
            val toPosition = target.adapterPosition

            Collections.swap(list,fromPosition,toPosition)
            ParentRView.adapter?.notifyItemMoved(fromPosition,toPosition)
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

            val position = viewHolder.adapterPosition

            if (direction == ItemTouchHelper.LEFT){
                GlobalScope.launch(Dispatchers.IO) { db.wishesdao().deleteTask(wishlistAdapter.getItemId(position)) }

            }
            else if (direction == ItemTouchHelper.RIGHT){
               GlobalScope.launch(Dispatchers.IO) { db.wishesdao().deleteTask(wishlistAdapter.getItemId(position)) }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu3,menu)

        val item = menu?.findItem(R.id.search)
        val searchView = item?.actionView as SearchView
        searchView.setQueryHint("type here to search")

        item.setOnActionExpandListener(object : MenuItem.OnActionExpandListener{
            override fun onMenuItemActionExpand(p0: MenuItem?): Boolean {
                displayTodo()
                return true
            }

            override fun onMenuItemActionCollapse(p0: MenuItem?): Boolean {
                displayTodo()
                return true
            }
        })

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if(!newText.isNullOrEmpty()){
                    displayTodo(newText)
                }
                return true
            }

        })
        return super.onCreateOptionsMenu(menu)
    }

    fun displayTodo(newText:String = ""){
        db.wishesdao().getAllUsers().observe(this,Observer{
            if (it.isNotEmpty()){
                list.clear()
                list.addAll(
                    it.filter {todo ->
                        todo.name.contains(newText,true)
                    }
                )
                wishlistAdapter.notifyDataSetChanged()
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == R.id.search){
            Toast.makeText(this,"search clicked",Toast.LENGTH_SHORT).show()
        }
        return true
    }
}
