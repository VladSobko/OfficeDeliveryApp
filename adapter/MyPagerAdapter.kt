package com.example.vlad.deliveryinstantapp.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.example.vlad.deliveryinstantapp.FirstFragment
import com.example.vlad.deliveryinstantapp.SecondFragment
import com.example.vlad.deliveryinstantapp.ThirdFragment

class MyPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment? {
        return when (position) {
            0 -> {
                FirstFragment()
            }
            1 -> {
                SecondFragment()
            }
//            2-> {
//                ThirdFragment()
//            }

            else -> null
        }
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence {
        return when (position) {
            0 -> "Клієнти"
            1 -> "Продажі"
            else -> {
                return "Загалом"
            }
        }
    }

}