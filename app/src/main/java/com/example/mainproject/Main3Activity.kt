package com.example.mainproject

import ViewPagerAdapter
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mainproject.Client.MovieClient
import com.example.mainproject.adapter.tvadapter.ParentAdapterTv
import com.example.mainproject.model.model_tv.ParentTv
import com.example.mainproject.model.model_tv.TvOnAir
import com.example.mainproject.model.model_tv.TvPopular
import com.example.mainproject.model.model_tv.TvTopRated
import com.example.mainproject.model.trending.TrendingResultsItem
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.main_include.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Main3Activity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    val heading = arrayOf("TOP-RATED TV SHOWS","ON-AIR TV SHOWS","POPULAR TV SHOWS")

    val list = arrayListOf<ParentTv>()
    val adapter = ParentAdapterTv(list)

    val list1 = arrayListOf<TvTopRated>()

    val list2 = arrayListOf<TvOnAir>()

    val list3 = arrayListOf<TvPopular>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)

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

        for (i in 0..2){
            if (i==0){
                list.add(
                    ParentTv(
                        heading[i],
                        list1,
                        list2,
                        list3
                    )
                )
            }
            else if (i==1){
                list.add(
                    ParentTv(
                        heading[i],
                        list1,
                        list2,
                        list3
                    )
                )
            }
            else if (i==2){
                list.add(
                    ParentTv(
                        heading[i],
                        list1,
                        list2,
                        list3
                    )
                )
            }
        }

        val trendingList = arrayListOf<TrendingResultsItem>()
        val trendingAdapter = ViewPagerAdapter(trendingList)

        GlobalScope.launch {
            val response = withContext(Dispatchers.IO){ MovieClient.api.getTrendings("tv") }
            if (response.isSuccessful){
                response.body()?.let { res->
                    res.results?.let { trendingList.addAll(it) }
                }
                runOnUiThread{
                    trendingAdapter.notifyDataSetChanged()
                    trendingVp.adapter = trendingAdapter
                }
            }
        }

        trendingAdapter.onItemClick = {
            val i = Intent(this,Main7Activity::class.java)
            i.putExtra("id",it.id.toString())

            startActivity(i)
        }




        ParentRView.apply {
            layoutManager = LinearLayoutManager(this@Main3Activity, RecyclerView.VERTICAL,false)
            adapter = this@Main3Activity.adapter
        }
        adapter.onItemClick = {
            val i = Intent(this,Main2Activity::class.java)
            i.putExtra("category",it.category)
            startActivity(i)
        }

        ParentRView.visibility = View.INVISIBLE
        GlobalScope.launch {
            val response1 = withContext(Dispatchers.IO){ MovieClient.api.getAllTopRatedTvShows("1") }
            if(response1.isSuccessful){
                response1.body()?.let {res ->
                    res.results?.let { list1.addAll(it) }
                    runOnUiThread{
                        adapter.notifyDataSetChanged()
                        progressBar.visibility = View.INVISIBLE
                        ParentRView.visibility = View.VISIBLE
                    }
                }

//                withContext(Dispatchers.Main){
//                    //Toast.makeText(this@MainActivity,"top rated movies",Toast.LENGTH_SHORT).show()
//                    tv_parent.visibility = View.VISIBLE
//                    more.visibility = View.VISIBLE
//                }
            }
        }

        GlobalScope.launch {
            val response1 = withContext(Dispatchers.IO){ MovieClient.api.getAllOnAirTvShows("1") }
            if(response1.isSuccessful){
                response1.body()?.let {res ->
                    res.results?.let { list2.addAll(it) }
                    runOnUiThread{
                        adapter.notifyDataSetChanged()
                        progressBar.visibility = View.INVISIBLE
                    }
                }
//                withContext(Dispatchers.Main){
//                    //Toast.makeText(this@MainActivity,"top rated movies",Toast.LENGTH_SHORT).show()
//                    tv_parent.visibility = View.VISIBLE
//                    more.visibility = View.VISIBLE
//                }
            }
        }

        GlobalScope.launch {
            val response1 = withContext(Dispatchers.IO){ MovieClient.api.getAllPopularTvShows("1") }
            if(response1.isSuccessful){
                response1.body()?.let {res ->
                    res.results?.let { list3.addAll(it) }
                    runOnUiThread{
                        adapter.notifyDataSetChanged()
                        progressBar.visibility = View.INVISIBLE
                    }
                }
//                withContext(Dispatchers.Main){
//                    //Toast.makeText(this@MainActivity,"top rated movies",Toast.LENGTH_SHORT).show()
//                    tv_parent.visibility = View.VISIBLE
//                    more.visibility = View.VISIBLE
//                }
            }
        }

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.movies->{
                Toast.makeText(this,"Movies Pressed", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this,MainActivity::class.java))
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
