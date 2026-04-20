package com.cherry.wakeupschedule

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cherry.wakeupschedule.adapter.SchoolAdapter

class SchoolListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SchoolAdapter

    private val schools = listOf(
        School("河南医药大学", "https://qgjw.xxmu.edu.cn/cas/login.action"),
        School("河南师范大学", "https://jwc.htu.edu.cn"),
        School("新乡学院", "https://jw.xxu.edu.cn/eams/homeExt.action"),
        School("河南工学院", "https://jwnew.hait.edu.cn/hngxyjw/cas/login.action"),
        School("河南大学", "https://grsmt.henu.edu.cn/grsmt/login?loginType=student"),
        School("河南农业大学", "https://jw.henau.edu.cn/cas/login.action")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_school_list)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "选择学校"

        setupRecyclerView()
        setupApplyAdapter()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun setupRecyclerView() {
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = SchoolAdapter(schools) { school ->
            openWebView(school.name, school.url)
        }
        recyclerView.adapter = adapter
    }

    private fun setupApplyAdapter() {
        val llApplyAdapter = findViewById<LinearLayout>(R.id.ll_apply_adapter)
        llApplyAdapter.setOnClickListener {
            showApplyDialog()
        }
    }

    private fun showApplyDialog() {
        val options = arrayOf("通过邮箱申请", "通过 GitHub Issue 申请")

        AlertDialog.Builder(this)
            .setTitle("申请适配")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> openEmail()
                    1 -> openGitHubIssue()
                }
            }
            .setNegativeButton("取消", null)
            .show()
    }

    private fun openEmail() {
        try {
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:Yngu196@qq.com")
                putExtra(Intent.EXTRA_SUBJECT, "申请教务系统适配")
            }
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(this, "无法打开邮箱", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openGitHubIssue() {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/ClassSchedule-CourseAdapter/CourseAdapter/issues/new"))
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(this, "无法打开链接", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openWebView(schoolName: String, url: String) {
        val intent = Intent(this, WebViewActivity::class.java)
        intent.putExtra("url", url)
        intent.putExtra("schoolName", schoolName)
        startActivity(intent)
    }
}

data class School(val name: String, val url: String)
