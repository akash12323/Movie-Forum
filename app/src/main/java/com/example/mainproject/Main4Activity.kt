package com.example.mainproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.SearchView
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mainproject.Client.MovieClient
import com.example.mainproject.adapter.personadapter.PopularPersonAdapter
import com.example.mainproject.model.model_person.PersonPopular
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main2.*
import kotlinx.android.synthetic.main.main_include.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Main4Activity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    val list1 = arrayListOf<PersonPopular>()
    val personAdapter = PopularPersonAdapter(list1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main4)

        setSupportActionBar(toolbar5)

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

//        tv2.text = "POPULAR PERSON"

        personAdapter.onItemClick ={
            val i = Intent(this,Main8Activity::class.java)

            i.putExtra("person",it.profilePath.toString())
            i.putExtra("name",it.name)
            i.putExtra("knownforTitle",it.knownFor?.get(0)?.originalTitle)
            i.putExtra("knownforoverview",it.knownFor?.get(0)?.overview)
            i.putExtra("title",it.knownFor?.get(0)?.title)
            i.putExtra("posterpath",it.knownFor?.get(0)?.posterPath)
            i.putExtra("backdroppath",it.knownFor?.get(0)?.backdropPath)
            i.putExtra("mediatype",it.knownFor?.get(0)?.mediaType)

            startActivity(i)

        }
        ParentRView.apply {
            layoutManager = GridLayoutManager(context,2, RecyclerView.VERTICAL,false)
            adapter = personAdapter
        }

        ParentRView.visibility = View.INVISIBLE
//        tv2.visibility = View.INVISIBLE

        GlobalScope.launch {
            for (i in 1..10){
                val response1 = withContext(Dispatchers.IO){ MovieClient.api.getAllPopularPerson("${i}") }
                if(response1.isSuccessful){
                    response1.body()?.let {res ->
                        res.results?.let { list1.addAll(it) }
                        runOnUiThread{
                            personAdapter.notifyDataSetChanged()
                            progressBar.visibility = View.INVISIBLE

                            ParentRView.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu2,menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == R.id.search){
            Toast.makeText(this,"search clicked",Toast.LENGTH_SHORT).show()
            startActivity(Intent(this,SearchActivity::class.java))
        }
        return true
    }
}
