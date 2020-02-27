package com.example.chatground2.view.Forums

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import androidx.core.content.ContextCompat
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.chatground2.Model.forumitemdata
import kotlinx.android.synthetic.main.fragment_forums.*
import kotlinx.android.synthetic.main.fragment_forums.view.*
import java.util.ArrayList
import com.example.chatground2.Model.DefaultResponse2
import com.example.chatground2.R
import kotlinx.coroutines.*



class ForumsFragment : Fragment()
//    , SwipeRefreshLayout.OnRefreshListener
{

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_forums, container, false)
    }

//
//    private lateinit var pref: SharedPreferences
//    private var items: ArrayList<forumitemdata> = ArrayList()
//    private lateinit var mAdapter: forumAdapter
//    lateinit var searcharraAdapter: ArrayAdapter<CharSequence>
//    val WRITEFORUMCODE = 500
//    private var searching = false
//    private var isLoading = false
//    private var pageid = 1
//    var visibleItemCounter = 0
//    var totalItemCount = 0
//    var firstVisibleItemPosition = 0
//    lateinit var layoutManager: LinearLayoutManager
//    lateinit var progressBar: ProgressBar
//    lateinit var F_recyclerView: RecyclerView
//    private var bestforum = false
//    val handler = Handler()
//    lateinit var r:Runnable
//    var swipeRefreshLayout: SwipeRefreshLayout? = null
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//
//        bestforum = false
//        isLoading = false
//        pageid = 1
//        visibleItemCounter = 0
//        totalItemCount = 0
//        firstVisibleItemPosition = 0
//        mAdapter.clear()
//    }
//
//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        var view: View = inflater.inflate(R.layout.fragment_forums, container, false)
//
//        searcharraAdapter =
//            ArrayAdapter.createFromResource(context,
//                R.array.data_search, android.R.layout.simple_spinner_item)
//
//        view.F_search_spinner.adapter = searcharraAdapter
//
//        swipeRefreshLayout = view.F_swipeRefresh
//        view.F_swipeRefresh.setOnRefreshListener(this)
//
//        progressBar = view.F_progressBar
//        F_recyclerView = view.F_forumRecy
//
//        mAdapter = forumAdapter(this.context!!, items)
//        layoutManager = LinearLayoutManager(this.context)
//        view.F_forumRecy.layoutManager = layoutManager
//        view.F_forumRecy.setHasFixedSize(true)
//        view.F_forumRecy.adapter = mAdapter
//
//        GlobalScope.launch {
//            getforums(pageid, bestforum)
//        }
//        pref = activity!!.getSharedPreferences("chatgroundlogin", Context.MODE_PRIVATE)
//
//        view.F_writebutton.setOnClickListener {
//            activity!!.startActivityForResult(Intent(context, writeforum::class.java), WRITEFORUMCODE)
//        }
//
//        view.F_search_cancel.setOnClickListener { F_search_edit.setText("") }
//
//        r = Runnable { mAdapter.notifyDataSetChanged() }
//
//        view.F_search_edit.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
//
//            }
//
//            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
//
//            }
//
//            override fun afterTextChanged(s: Editable) {
//                val text = s.toString()
//                filter(text, F_search_spinner)
//            }
//
//        })
//
//        view.F_bestforumsbutton.setOnClickListener {
//            if (bestforum) {
//                bestforum = false
//                view.F_bestforumsbutton.background =
//                    ContextCompat.getDrawable(this.context!!,
//                        R.drawable.forums_bestforums_icon
//                    )
//                isLoading = false
//                pageid = 1
//                mAdapter.clear()
//
//                GlobalScope.launch {
//                    getforums(pageid, bestforum)
//                }
//            } else {
//                bestforum = true
//                view.F_bestforumsbutton.background =
//                    ContextCompat.getDrawable(this.context!!,
//                        R.drawable.forumsitem_recommend
//                    )
//                isLoading = false
//                pageid = 1
//                mAdapter.clear()
//                GlobalScope.launch {
//                    getforums(pageid, bestforum)
//                }
//            }
//        }
//
//        view.F_searchbutton.setOnClickListener {
//            if (searching) {
//                F_root.visibility = View.GONE
//                searching = false
//
//                mAdapter.changeList(items)
//            } else {
//                F_root.visibility = View.VISIBLE
//                searching = true
//            }
//        }
//
//        return view
//    }
//
//    private suspend fun getforums(pageid: Int, bestforums: Boolean) {
//        withContext(Dispatchers.Main)
//        {
//            progressBar.visibility = View.VISIBLE
//        }
//
//        var result: DefaultResponse2? = ApiClient.requestforumsresult(pageid, bestforum)
//
//        if (result == null) {
//            Toast.makeText(context, "실패", Toast.LENGTH_LONG).show();
//            progressBar.visibility = View.GONE
//            swipeRefreshLayout?.isRefreshing = false
//        } else {
//            if (result.type) {
//                isLoading = true
//                withContext(Dispatchers.Main)
//                {
//                    progressBar.visibility = View.GONE
//                    swipeRefreshLayout?.isRefreshing = false
//                }
//                setUpAdapter(result)
//            } else {
//                withContext(Dispatchers.Main)
//                {
//                    progressBar.visibility = View.GONE
//                    swipeRefreshLayout?.isRefreshing = false
//                }
//                Toast.makeText(context, "더 이상 글이 없습니다.", Toast.LENGTH_LONG).show();
//            }
//        }
//
////        ApiClient.instance.requestforums(pageid,bestforums).enqueue(object : Callback<DefaultResponse2> {
////            override fun onResponse(call: Call<DefaultResponse2>, response: Response<DefaultResponse2>) {
////                if (response.body()!!.type) {
////                    isLoading = true
////                    progressBar.visibility = View.GONE
////                    setUpAdapter(response.body()!!)
////                } else {
////                    progressBar.visibility = View.GONE
////                    Toast.makeText(context, "더 이상 글이 없습니다.", Toast.LENGTH_LONG).show();
////                }
////            }
////
////            override fun onFailure(call: Call<DefaultResponse2>, t: Throwable) {
////                Toast.makeText(context, "실패", Toast.LENGTH_LONG).show();
////                progressBar.visibility = View.GONE
////                System.out.println("에러 : $t")
////            }
////        })
//    }
//
//    private fun setUpAdapter(body: DefaultResponse2) {
//        val items2 = ArrayList<forumitemdata>()
//        for (i in 0 until body.data!!.size) {
//            items2.add(body.data[i])
//        }
//        mAdapter.addItems(items2)
//        handler.post(r)
//        F_recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                if (dy > 0) {
//                    visibleItemCounter = layoutManager.childCount
//                    totalItemCount = layoutManager.itemCount
//                    firstVisibleItemPosition =
//                        (F_recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
//
//                    if (isLoading) {
//                        if ((visibleItemCounter + firstVisibleItemPosition) >= totalItemCount) {
//                            isLoading = false
//                            pageid++
//                            GlobalScope.launch {
//                                getforums(pageid, bestforum)
//                            }
//                        }
//                    }
//                }
//            }
//        })
//    }
//
//    override fun onRefresh() {
//        isLoading = false
//        pageid = 1
//        mAdapter.clear()
//        bestforum = false
//        GlobalScope.launch {
//            getforums(pageid, bestforum)
//        }
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        if (requestCode == WRITEFORUMCODE) {
//            if (resultCode == Activity.RESULT_OK) {
//                System.out.println("WRITEFORUMCODE성공")
//            } else {
//                System.out.println("WRITEFORUMCODE실패")
//                Toast.makeText(context, "취소", Toast.LENGTH_LONG).show();
//            }
//        } else {
//            System.out.println("requestcode : $requestCode")
//        }
//    }
//
//    private fun filter(text: String, spinner: Spinner) {
//        val filteredlist: ArrayList<forumitemdata> = ArrayList()
//        val spinneritem = spinner.selectedItem.toString()
//
//        for (item: forumitemdata in items) {
//            when (spinneritem) {
//                "제목" -> {
//                    if (item.title.toLowerCase().contains(text.toLowerCase())) {
//                        filteredlist.add(item)
//                    }
//                }
//                "내용" -> {
//                    if (item.content.toLowerCase().contains(text.toLowerCase())) {
//                        filteredlist.add(item)
//                    }
//                }
//                "제목+내용" -> {
//                    if (item.content.toLowerCase().contains(text.toLowerCase()) || item.title.toLowerCase().contains(
//                            text.toLowerCase()
//                        )
//                    ) {
//                        filteredlist.add(item)
//                    }
//                }
//            }
//        }
//        mAdapter.changeList(filteredlist)
//    }
}