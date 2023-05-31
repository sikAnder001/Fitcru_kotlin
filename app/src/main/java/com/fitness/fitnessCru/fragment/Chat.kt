package com.fitness.fitnessCru.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.fitness.fitnessCru.activities.ViewProfileActivity
import com.fitness.fitnessCru.adapter.ChatAdapter
import com.fitness.fitnessCru.databinding.FragmentChatBinding
import com.fitness.fitnessCru.repository.Repository
import com.fitness.fitnessCru.utility.CustomProgressLoading
import com.fitness.fitnessCru.utility.Session
import com.fitness.fitnessCru.viewmodel.ChatViewModel
import com.fitness.fitnessCru.vm_factory.ChatVMFactory

class Chat : Fragment() {

    private lateinit var binding: FragmentChatBinding

    private lateinit var viewModel: ChatViewModel

    private lateinit var loading: CustomProgressLoading

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentChatBinding.inflate(inflater, container, false)

        loading = CustomProgressLoading(requireContext())

        val repository by lazy { Repository() }

        val factory = ChatVMFactory(repository)

        viewModel = ViewModelProvider(this, factory)[ChatViewModel::class.java]

        loading.showProgress()

        hittingViews()

        viewModel.getChatList()

        observer()

        return binding.root
    }

    private fun observer() {

        val adapter = ChatAdapter(requireContext())

        binding.chatRv.adapter = adapter

        viewModel.chatList.observe(viewLifecycleOwner) {

            loading.hideProgress()

            if (it.isSuccessful && it.body() != null) {
                adapter.setAddressList(it.body()?.data!!)

            } else if (!it.isSuccessful && it.code() == 404) {
                binding.toastTv.visibility = View.VISIBLE
            } else "Something went wrong"
        }
    }

    private fun hittingViews() {

        binding.apply {

            var userDetail = Session.getUserDetails()

            if (userDetail.name == null) {
                if (userDetail.email != null) {
                    var n = userDetail.email[0].toString().uppercase()
                    tvMenuBar.text = n
                } else {
                    tvMenuBar.visibility = View.GONE
                    placeholder.visibility = View.VISIBLE
                }
            } else {
                var n = userDetail.name[0].toString().uppercase()
                tvMenuBar.text = n
            }

            placeholder.setOnClickListener {
                startActivity(
                    Intent(
                        requireContext(),
                        ViewProfileActivity::class.java
                    )
                )
            }
            tvMenuBar.setOnClickListener {
                startActivity(
                    Intent(
                        requireContext(),
                        ViewProfileActivity::class.java
                    )
                )
            }
        }
    }

    override fun onResume() {
        loading.showProgress()
        viewModel.getChatList()
        super.onResume()
    }

}