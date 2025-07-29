package com.thingclips.smart.bizbundle.miniapp.demo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.thing.smart.miniappclient.ThingMiniAppClient

/**
 *
 * Created by Ryan Hu on 2025/7/29
 */
class MiniAppTabSampleActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_miniappp_tab_sample)
        val viewPager = findViewById<ViewPager2>(R.id.view_pager)
        val tabLayout = findViewById<TabLayout>(R.id.tabs)
        viewPager.adapter = MiniAppFragmentAdapter()
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = "Tab $position"
        }.attach()
    }


    private inner class MiniAppFragmentAdapter : FragmentStateAdapter(this) {

        private val miniApps = listOf(
            "godzilla://tydxwunc8rjqvh4gaw",
            "godzilla://tyam4cmqd9eaztkjwi",
        )

        override fun getItemCount() = miniApps.size

        override fun createFragment(position: Int): androidx.fragment.app.Fragment {
            return ThingMiniAppClient.coreClient().getMiniAppFragmentByUrl(
                this@MiniAppTabSampleActivity, miniApps[position], null)
        }
    }
}