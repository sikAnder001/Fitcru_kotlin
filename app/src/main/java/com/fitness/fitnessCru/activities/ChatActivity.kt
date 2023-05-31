package com.fitness.fitnessCru.activities

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.fitness.fitnessCru.FirebaseUtils
import com.fitness.fitnessCru.adapter.MessageAdapter
import com.fitness.fitnessCru.body.LastMessage
import com.fitness.fitnessCru.databinding.ActivityChatBinding
import com.fitness.fitnessCru.model.Message
import com.fitness.fitnessCru.repository.Repository
import com.fitness.fitnessCru.utility.CustomProgressLoading
import com.fitness.fitnessCru.utility.GeneralFunctions
import com.fitness.fitnessCru.utility.showToast
import com.fitness.fitnessCru.viewmodel.ChatViewModel
import com.fitness.fitnessCru.vm_factory.ChatVMFactory
import com.google.firebase.FirebaseApp
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageMetadata
import java.text.SimpleDateFormat
import java.util.*

class ChatActivity : AppCompatActivity() {
    //chat

    private var reference = "chat/one_to_one/%s/%s/messages"
    private var resultUri: Uri? = null
    private lateinit var binding: ActivityChatBinding
    private var user_id = "0"
    private var coach_id = "0"
    var myRef: DatabaseReference? = null
    private lateinit var messageAdapter: MessageAdapter
    private val messageList = arrayListOf<Message>()
    private lateinit var viewModel: ChatViewModel
    private var message = ""
    private lateinit var loading: CustomProgressLoading

    private val firebaseDatabase: FirebaseDatabase
        get() {
            return Firebase.database
        }
    private val firebaseStorage: FirebaseStorage
        get() {
            return FirebaseStorage.getInstance()
        }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = ActivityChatBinding.inflate(layoutInflater)

        setContentView(binding.root)

        user_id = intent.getIntExtra("user_id", 0).toString()

        coach_id = intent.getIntExtra("coach_id", 0).toString()

        loading = CustomProgressLoading(applicationContext)

        messageAdapter = MessageAdapter(user_id) { view, imageUrl ->
            val intent = Intent(this, ViewImageActivity::class.java).apply {
                putExtra(
                    FirebaseUtils.IMAGE,
                    imageUrl
                )
            }
            val transitionName = "transition_name"
            val activityOption =
                ActivityOptionsCompat.makeSceneTransitionAnimation(this, view, transitionName)
            ActivityCompat.startActivity(this, intent, activityOption.toBundle())
        }

        if (user_id == "0" || coach_id == "0") return

        FirebaseApp.initializeApp(applicationContext)

        myRef = firebaseDatabase.getReference(String.format(reference, coach_id, user_id))!!

        binding.btnSend.setOnClickListener {

            val message = binding.etMessageBox.text.toString()

            if (message.isEmpty()) {
                binding.etMessageBox.error = "Please type message."
                return@setOnClickListener
            }

            val time = dateTime()

            this@ChatActivity.message = message

            val messageObj = Message(
                "", "", "", FirebaseUtils.TEXT, message,
                "client$user_id", time, ""
            )

            val randomKey = myRef?.push()?.key.toString()

            binding.etMessageBox.text.clear()

            myRef?.child(randomKey)?.setValue(messageObj)?.addOnCompleteListener {
                if (it.isSuccessful) {

                } else it.exception?.message?.let { it1 -> showToast(it1) }
            }
        }

        binding.tvName.text = intent.getStringExtra("coach_name")

        GeneralFunctions.loadImage(
            this, (intent.getStringExtra("coach_profile").toString()),
            binding.ivProfilePic
        )

        binding.rvChats.apply {
            layoutManager = LinearLayoutManager(this@ChatActivity)
            adapter = messageAdapter
        }

        messageAdapter.setData(messageList)
        Log.v("messagesList", messageList.toString())
        loading.showProgress()

        myRef?.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                messageList.clear()

                for (children in snapshot.children) {
                    val message = children.getValue(Message::class.java)
                    if (message?.chat_sender_Id.equals(
                            "coach$coach_id",
                            true
                        ) && message?.read_time == ""
                    ) {
                        message?.read_time = dateTime()
                        myRef?.child(children.key.toString())?.setValue(message)
                        Log.v("messages", message.toString())
                    }
                    message?.let { messageList.add(it) }
                }

                messageAdapter.setData(messageList)
                Log.v("Las resort", messageList.toString())
                if (messageList.isNotEmpty())

                    binding.rvChats.scrollToPosition(messageList.size - 1)

                loading?.hideProgress()
            }

            override fun onCancelled(error: DatabaseError) {

                loading?.hideProgress()
            }
        })

        binding.ivBack.setOnClickListener { onBackPressed() }

        val pickImageContract =
            registerForActivityResult(ActivityResultContracts.GetContent()) { imageUri ->
                imageUri?.let {
                    showToast("Sending image...")
                    sendImageMessage(imageUri)
                } ?: showToast("an ERROR occurred.")
            }

        binding.ivAttachment.setOnClickListener {
            pickImageContract.launch("image/*")
        }

        val takePhotoContract =
            registerForActivityResult(ActivityResultContracts.TakePicture()) { status ->
                if (status) {
                    resultUri?.let {
                        showToast("Sending image...")
                        sendImageMessage(it)
                    } ?: showToast("an ERROR occurred.")
                } else
                    showToast("an ERROR occurred.")
            }

        val permissionContract =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { resultMap ->
                resultMap.entries.forEach { entry ->
                    if (entry.value) {
                        resultUri = GeneralFunctions.createImageURI(this)
                        resultUri?.let { takePhotoContract.launch(resultUri) }
                    }
                }
            }

        binding.ivCamera.setOnClickListener {
            if (Build.VERSION.SDK_INT >= 29) {
                resultUri = GeneralFunctions.createImageURI(this)
                resultUri?.let { takePhotoContract.launch(resultUri) }
            } else
                permissionContract.launch(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE))
        }
    }

    private fun sendImageMessage(imageUri: Uri) {
        val name = dateTime()
        val msg = Message("", "", "", FirebaseUtils.UPLOADING, "", "client$user_id", name, "")
        messageList.add(msg)
        message = "${imageUri.pathSegments.last()}.jpeg"
        messageAdapter.notifyDataSetChanged()
        binding.rvChats.scrollToPosition(messageList.size - 1)
        val reference =
            firebaseStorage.reference.child("chat/one_to_one/$coach_id$user_id").child("$name.jpeg")
        reference.putFile(imageUri, StorageMetadata.Builder().setContentType("image/jpeg").build())
            .addOnCompleteListener { uploadedImageResponse ->
                if (uploadedImageResponse.isSuccessful) {
                    reference.downloadUrl.addOnCompleteListener { uploadedImageDownloadUrlResponse ->
                        if (uploadedImageDownloadUrlResponse.isSuccessful) {
                            val message = Message(
                                this@ChatActivity.message,
                                uploadedImageDownloadUrlResponse.result.toString(),
                                "${user_id}_${coach_id}_$name.jpeg",
                                FirebaseUtils.IMAGE,
                                "",
                                "client$user_id",
                                name,
                                ""
                            )
                            myRef?.child(myRef?.push()?.key.toString())?.setValue(message)
                                ?.addOnCompleteListener {
                                    if (it.isSuccessful) {
                                        // Send Notification
                                    } else
                                        it.exception?.message?.let { it1 -> showToast(it1) }
                                }
                        } else {
                            uploadedImageDownloadUrlResponse.exception?.message?.let { showToast(it) }
                        }
                    }
                } else
                    uploadedImageResponse.exception?.message?.let { showToast(it) }
            }
    }

    private fun dateTime(): String {
        val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z", Locale.US)
        return df.format(Date())
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onBackPressed() {
        loading.showProgress()
        val repository by lazy { Repository() }
        val factory = ChatVMFactory(repository)
        viewModel = ViewModelProvider(this, factory)[ChatViewModel::class.java]
        if (message.isNotEmpty()) {
            viewModel.lastMessage(
                LastMessage(
                    user_id.toInt(),
                    coach_id.toInt(),
                    message,
                    user_id.toInt(),
                    coach_id.toInt()
                )
            )
            viewModel.lastMessages.observe(this) {
                loading.hideProgress()
                super.onBackPressed()
            }
        } else {
            loading.hideProgress()
            super.onBackPressed()
        }


    }
}