package com.koinvois.generator.ui.notifications

import android.view.LayoutInflater
import androidx.lifecycle.ViewModelProvider
import com.koinvois.generator.core.common.base.BaseActivity
import com.koinvois.generator.databinding.FragmentNotificationsBinding

class NotificationsActivity : BaseActivity<FragmentNotificationsBinding>() {

    override fun inflateBinding(): FragmentNotificationsBinding =
        FragmentNotificationsBinding.inflate(LayoutInflater.from(this))

    override fun setupView() {
        val notificationsViewModel =
            ViewModelProvider(this).get(NotificationsViewModel::class.java)

        val textView = binding.textNotifications
        notificationsViewModel.text.observe(this) {
            textView.text = it
        }
    }
}
