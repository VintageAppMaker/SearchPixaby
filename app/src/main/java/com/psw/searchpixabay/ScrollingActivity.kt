package com.psw.searchpixabay

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.view.MenuItemCompat
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.psw.searchpixabay.adapter.ImageAdapter
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.psw.adsloader.githubsearcher.viewmodel.MainViewModel


class ScrollingActivity : AppCompatActivity() {

    val viewModel : MainViewModel by lazy{
        ViewModelProviders.of(this).get(MainViewModel::class.java)
    }
    var recyclerView: RecyclerView? = null
    var nestedScrollView : NestedScrollView?=null
    var nPageCount = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scrolling)
        setSupportActionBar(findViewById(R.id.toolbar))
        findViewById<CollapsingToolbarLayout>(R.id.toolbar_layout).title = "Pixabay"

        recyclerView = findViewById<View>(R.id.recyclerView) as RecyclerView
        nestedScrollView = findViewById<SwipeRefreshLayout>(R.id.nestedScrollView) as NestedScrollView

        //val staggeredGridLayoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        //recyclerView!!.layoutManager = staggeredGridLayoutManager

        val GridLayoutManager = GridLayoutManager(this, 2)
        recyclerView!!.layoutManager = GridLayoutManager

        viewModel.apply{
            lst.observe(this@ScrollingActivity, Observer {
                val adapter = ImageAdapter(this@ScrollingActivity, it)
                recyclerView!!.adapter = adapter
                bLoading.postValue(false)
            })

            message.observe(this@ScrollingActivity, Observer {
                Toast.makeText(this@ScrollingActivity, it, Toast.LENGTH_LONG).show()
            })

            bLoading.observe(this@ScrollingActivity, Observer{
                bLoding ->
                findViewById<ProgressBar>(R.id.prgComm)?.apply {
                    this.visibility = if (bLoding) View.VISIBLE else View.GONE
                }
            })
            initList(); nPageCount = 1
            loadImage("keyword", page = nPageCount++)

        }

        // NestedScrollView 안의 RecyclerView의 스크롤 이벤트를 감지하기 위해서는
        // NestedScrollView 이벤트에서 처리를 할 수 밖에 없다.
        nestedScrollView!!.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (v.getChildAt(v.childCount - 1) != null) {
                if (scrollY > oldScrollY) {
                    if (scrollY >= v.getChildAt(v.childCount - 1)
                            .measuredHeight - v.measuredHeight
                    ) {
                        viewModel.loadImage(nPageCount++)
                    }
                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_scrolling, menu)
        menu.findItem(R.id.action_search)?.let{
            searchItem ->
            (MenuItemCompat.getActionView(searchItem) as SearchView)?.apply {
                setOnCloseListener(object : SearchView.OnCloseListener {
                    override fun onClose(): Boolean {
                        return false
                    }
                })

                val searchPlate =  findViewById(androidx.appcompat.R.id.search_src_text) as EditText
                searchPlate.hint = "검색키워드를 입력하세요"
                searchPlate.setTextColor(Color.parseColor("#FFFFaa"))
                searchPlate.setHintTextColor(Color.parseColor("#FFFFaa"))
                val searchPlateView: View =
                        findViewById(androidx.appcompat.R.id.search_plate)
                searchPlateView.setBackgroundColor(
                        ContextCompat.getColor(
                                this@ScrollingActivity,
                                android.R.color.transparent
                        )
                )

                setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        viewModel.initList(); nPageCount = 1
                        viewModel.loadImage(query!!, nPageCount++)
                        return false
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        return false
                    }
                })

                val searchManager =
                        getSystemService(Context.SEARCH_SERVICE) as SearchManager
                setSearchableInfo(searchManager.getSearchableInfo(componentName))
            }

        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_search -> true
            R.id.action_github -> {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/VintageAppMaker/SearchPixaby"))
                startActivity(browserIntent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}