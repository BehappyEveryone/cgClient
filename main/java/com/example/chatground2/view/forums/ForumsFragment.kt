package com.example.chatground2.view.forums

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.chatground2.model.Constants.DETAIL_FORUM
import com.example.chatground2.model.Constants.WRITE_FORUM
import com.example.chatground2.R
import com.example.chatground2.adapter.ForumsAdapter
import com.example.chatground2.view.detailForum.DetailForumActivity
import com.example.chatground2.view.writeForum.WriteForumActivity
import kotlinx.android.synthetic.main.fragment_forums.*
import kotlinx.android.synthetic.main.fragment_forums.view.*


class ForumsFragment : Fragment(), View.OnClickListener, ForumsContract.IForumsView,
    SwipeRefreshLayout.OnRefreshListener {

    private var presenter: ForumsPresenter? = null
    private var forumsAdapter: ForumsAdapter? = null
    private var lm: LinearLayoutManager? = null
    var visibleItemCounter = 0
    var totalItemCount = 0
    var firstVisibleItemPosition = 0

    private var isLoading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initialize()
    }

    private fun initialize() {
        forumsAdapter = context?.let { ForumsAdapter(it) }
        lm = LinearLayoutManager(context)

        presenter = context?.let {
            ForumsPresenter(it, this).apply {
                adapterModel = forumsAdapter
                adapterView = forumsAdapter
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_forums, container, false)

        return uiInitialize(view)
    }

    private fun uiInitialize(view: View): View {
        view.run {

            F_swipeRefresh.setOnRefreshListener(this@ForumsFragment)
            F_writeButton.setOnClickListener(this@ForumsFragment)
            F_searchButton.setOnClickListener(this@ForumsFragment)
            F_bestForumsButton.setOnClickListener(this@ForumsFragment)
            F_searchCancel.setOnClickListener(this@ForumsFragment)

            F_forumRecycle.run {
                layoutManager = lm
                setHasFixedSize(true)
                adapter = forumsAdapter
                addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        if (dy > 0) {
                            visibleItemCounter = lm!!.childCount
                            totalItemCount = lm!!.itemCount
                            firstVisibleItemPosition =
                                (this@run.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()

                            if (isLoading) {
                                if ((visibleItemCounter + firstVisibleItemPosition) >= totalItemCount) {
                                    isLoading = false
                                    presenter?.callForums()
                                }
                            }
                        }
                    }
                })
            }

            F_searchEdit.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    presenter?.searching(s.toString())
                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }
            })

            return this
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        presenter?.callForums()
    }

    override fun isLoading(boolean: Boolean) {
        isLoading = boolean
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.F_writeButton -> presenter?.writeClick()
            R.id.F_searchButton -> presenter?.searchClick()
            R.id.F_bestForumsButton -> presenter?.bestForumsClick()
            R.id.F_searchCancel -> presenter?.searchCancelClick()
        }
    }

    override fun onRefresh() {
        presenter?.refresh()
    }

    override fun isRefreshing(boolean: Boolean) {
        F_swipeRefresh.isRefreshing = boolean
    }

    override fun toastMessage(text: String) =
        Toast.makeText(context, text, Toast.LENGTH_LONG).show()

    override fun progressVisible(boolean: Boolean) {
        if (boolean) {
            F_progressBar.visibility = View.VISIBLE
        } else {
            F_progressBar.visibility = View.INVISIBLE
        }
    }

    override fun enterWriteForum() {
        startActivityForResult(Intent(context, WriteForumActivity::class.java), WRITE_FORUM)
    }

    override fun enterDetailForum(idx: Int?) {
        idx?.let {
            startActivityForResult(
                Intent(context, DetailForumActivity::class.java).putExtra(
                    "idx",
                    it
                ), DETAIL_FORUM
            )
        }
    }

    override fun setBestForumBackground(int: Int) {
        F_bestForumsButton.background = context?.let { ContextCompat.getDrawable(it,int) }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                WRITE_FORUM -> {
                    presenter?.refresh()
                }
                DETAIL_FORUM -> {
                    presenter?.refresh()
                }
            }
        } else {
            Toast.makeText(context, "취소 되었습니다.", Toast.LENGTH_LONG).show();
        }
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