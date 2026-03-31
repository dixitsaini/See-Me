package com.example.seeme.activity

import android.os.Bundle
import com.example.seeme.R

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        // The FragmentContainerView in activity_main.xml will 
        // automatically load the HomeFragment via main_nav_graph.xml
    }
}
