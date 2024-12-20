package com.victor.projectwhatsapp.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.victor.projectwhatsapp.fragments.ContactsFragment
import com.victor.projectwhatsapp.fragments.StatusFragment
import com.victor.projectwhatsapp.fragments.ConversationFragment

class ViewPagerAdapter(
    private val tabs: List<String>,
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return  tabs.size
    }

    override fun createFragment(position: Int): Fragment {
        when(position){
            1 -> return StatusFragment()
            2 -> return ContactsFragment()
        }
        return ConversationFragment()
    }
}