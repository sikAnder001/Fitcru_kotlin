package com.fitness.fitnessCru.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.fitness.fitnessCru.adapter.NotificationListAdapter
import com.fitness.fitnessCru.databinding.FragmentNotificationListBinding
import com.fitness.fitnessCru.repository.Repository
import com.fitness.fitnessCru.response.NotificationListResponse
import com.fitness.fitnessCru.utility.Util
import com.fitness.fitnessCru.viewmodel.NotificationsViewModel
import com.fitness.fitnessCru.vm_factory.NotificationsVMFactory

class NotificationList : Fragment() {
    private lateinit var binding: FragmentNotificationListBinding
    lateinit var notificationListAdapter: NotificationListAdapter

    private lateinit var repository: Repository
    private lateinit var viewModel: NotificationsViewModel
    private lateinit var factory: NotificationsVMFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentNotificationListBinding.inflate(inflater, container, false)

        repository = Repository()

        factory = NotificationsVMFactory(repository)

        viewModel = ViewModelProvider(this, factory)[NotificationsViewModel::class.java]

        getNotificationList()

        binding.gobackbtn.setOnClickListener {
            requireActivity().onBackPressed()
        }

        return binding.root
    }

    private fun getNotificationList() {
        notificationListAdapter =
            NotificationListAdapter(context, object : NotificationListAdapter.ReadNotificationItem {
                override fun onClick(id: Int) {
                    readNotification(id)

                }
            })

        binding.notificationsRv.adapter = notificationListAdapter

        viewModel.notificationList()
        viewModel.response.observe(viewLifecycleOwner) {
            if (it.isSuccessful && it.body() != null) {
                if (it.body()!!.data != null) {
                    notificationListAdapter.setList(it.body()!!.data)
                }
            } else if (!it.isSuccessful && it.code() == 404) {
                Util.error(it.errorBody(), NotificationListResponse::class.java).message
                binding.toastTv3.visibility = View.VISIBLE
                /*if (it.body()!!.data.isEmpty()) {
                    binding.toastTv3.visibility = View.VISIBLE
                } else {
                    binding.toastTv3.visibility = View.GONE
                    notificationListAdapter.setList(it.body()!!.data)

                }*/
            } else "Something went wrong"
        }
    }

    private fun readNotification(id: Int) {
        viewModel.readNotification(id)
        viewModel.readResponse.observe(viewLifecycleOwner) {
            if (it.isSuccessful && it.body() != null) {
                getNotificationList()
                notificationListAdapter.notifyDataSetChanged()
            } else if (!it.isSuccessful && it.errorBody() != null) {
                Util.error(it.errorBody(), NotificationListResponse::class.java).message
            } else "Something went wrong"
        }
    }

}