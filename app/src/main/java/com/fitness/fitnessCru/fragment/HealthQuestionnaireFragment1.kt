package com.fitness.fitnessCru.fragment

import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.fitness.fitnessCru.R
import com.fitness.fitnessCru.activities.DashboardActivity
import com.fitness.fitnessCru.body.HealthQuestionBody
import com.fitness.fitnessCru.databinding.FragmentHealthQuestionnaire1Binding
import com.fitness.fitnessCru.repository.Repository
import com.fitness.fitnessCru.response.GetHealthQuestionnaire
import com.fitness.fitnessCru.response.HealthQuestionResponse
import com.fitness.fitnessCru.response.ImageUploadResponse
import com.fitness.fitnessCru.utility.Constants
import com.fitness.fitnessCru.utility.CustomProgressLoading
import com.fitness.fitnessCru.utility.LimitDecimal
import com.fitness.fitnessCru.utility.Util
import com.fitness.fitnessCru.viewmodel.HealthQuestionViewModel
import com.fitness.fitnessCru.vm_factory.HealthQuestionVMFactory
import com.github.dhaval2404.imagepicker.ImagePicker
import kotlinx.android.synthetic.main.fragment_health_questionnaire.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream
import kotlin.system.exitProcess

class HealthQuestionnaireFragment1 : Fragment() {
    private lateinit var fragmentHealthQuestionnaire1Binding: FragmentHealthQuestionnaire1Binding
    private val binding get() = fragmentHealthQuestionnaire1Binding
    private lateinit var viewArray: ArrayList<TextView>
    private lateinit var viewGroupArray: ArrayList<LinearLayout>
    private lateinit var checkBox: ArrayList<CheckBox>
    private lateinit var editText: ArrayList<EditText>
    private lateinit var viewModel: HealthQuestionViewModel
    private lateinit var loading: CustomProgressLoading
    private var uri: Uri? = null
    private var filePath = arrayOf("", "", "")
    private var flag = 0
    private var imageArray = ArrayList<ImageUploadResponse.Data>()
    private lateinit var registerForActivityResult: ActivityResultLauncher<Array<String>>

    private var num = ""

    var doubleBackToExitPressedOnce = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerForActivityResult =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { resultMap ->

            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        bundleData = arguments?.getSerializable("data") as CoachBundle
        fragmentHealthQuestionnaire1Binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_health_questionnaire1,
            container,
            false
        )

        num = requireArguments()!!.getString("num", "")!!
        imageArray.addAll(imageData())

        loading = CustomProgressLoading(requireContext())

        hittingViews()

        val repository by lazy { Repository() }
        val factory by lazy { HealthQuestionVMFactory(repository) }
        viewModel = ViewModelProvider(this, factory).get(HealthQuestionViewModel::class.java)
        registerForActivityResult.launch(
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        )

        binding.btnRegister.setOnClickListener {
            uploadImage()
        }
        viewModel.response.observe(viewLifecycleOwner) {
            loading.hideProgress()
            val message = if (it.isSuccessful && it.body() != null) {
                Log.v("numaste", num)
                if (num == "done") {
                    val intent = Intent(context, DashboardActivity::class.java)
                    startActivity(intent)
                } else {
                    val intent = Intent(context, DashboardActivity::class.java)
                    intent.putExtra("coaching", "done")
                    startActivity(intent)
                }

                it.body()!!.message
            } else if (!it.isSuccessful)
                Util.error(it.errorBody(), HealthQuestionResponse::class.java).message
            else "Something went wrong"
            Util.toast(requireContext(), message)
        }
        binding.apply {
            viewArray = arrayListOf(
                tvQ11,
                tvQ22,
                tvQ30,
                tvQ77,
                tvQ88,
                tvQ99,
                tvQ1010,
                tvQ1212,
                tvQ1313,
                tvQ13130,
                tvQ1414
            )
            viewGroupArray = arrayListOf(
                llQ11,
                llQ22,
                llQ30,
                llQ77,
                llQ88,
                llQ99,
                llQ1010,
                llQ1212,
                llQ1313,
                llQ13130,
                llQ1414
            )
            checkBox = arrayListOf(cbOther, cbOther2, cbInjuries)
            editText = arrayListOf(etOther, etOther2, etInjuries)

            for (i in 0 until viewArray.size) {
                viewArray[i].setOnClickListener {
                    visibility(viewArray[i], viewGroupArray[i])
                }
            }

            for (i in 0 until checkBox.size) {
                checkBox[i].setOnClickListener {
                    visibility2(checkBox[i], editText[i])
                }
            }
        }

        binding.apply {
            ivImage1.setOnClickListener {
                flag = 1
                ImagePicker.with(this@HealthQuestionnaireFragment1)
                    .crop()
                    .compress(3072)
                    .maxResultSize(1080, 1080)
                    .start()
            }
            ivImage2.setOnClickListener {
                flag = 2
                ImagePicker.with(this@HealthQuestionnaireFragment1)
                    .crop()
                    .compress(3072)
                    .maxResultSize(1080, 1080)
                    .start()
            }
            ivImage3.setOnClickListener {
                flag = 3
                ImagePicker.with(this@HealthQuestionnaireFragment1).crop()
                    .compress(3072)
                    .maxResultSize(1080, 1080)
                    .start()
            }
        }
//        singleSelection()
        addImageAndRemove()
        getAllQuestionsAnswer()

        backPress()
        return binding.root
    }

    private fun imageData(): ArrayList<ImageUploadResponse.Data> {
        var arr = ArrayList<ImageUploadResponse.Data>()
        arr.add(ImageUploadResponse.Data(""))
        arr.add(ImageUploadResponse.Data(""))
        arr.add(ImageUploadResponse.Data(""))
        return arr
    }

    private fun updateImage() {
        val builder = MultipartBody.Builder()
        val array = arrayOf(ivImage1, ivImage2, ivImage3)
        builder.setType(MultipartBody.FORM)

        for ((index, value) in array.withIndex()) {
            try {
                val drawable: BitmapDrawable = value.drawable as BitmapDrawable
                val bitmap: Bitmap = drawable.bitmap
                val file = createDirectoryAndSaveFile(bitmap, "image$index.jpeg")
                builder.addFormDataPart(
                    "image[]",
                    file.name,
                    file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                )
            } catch (e: Exception) {
                Log.e("TAG", "uploadImage: $e")
                loading.hideProgress()
            }
        }

        viewModel.uploadImageQuestionnaire(builder.build())
        viewModel.image.observe(viewLifecycleOwner) {
            try {
                if (it.isSuccessful && it.body() != null) {
                    imageArray.clear()
                    imageArray.addAll(it.body()!!.data)
                    Log.v("image Upload", "done")
                    /*it.body()!!.message*/
                } else if (!it.isSuccessful && it.errorBody() != null) {
                    Util.error(it.errorBody(), ImageUploadResponse::class.java).message
                } else Util.toast(requireContext(), "Something went wrong")
            } catch (e: Exception) {
                Log.e("TAG", "uploadImage1: $e")
            }
        }
    }

    private fun hittingViews() {
        binding.apply {
            et44.filters = arrayOf(LimitDecimal(5, 2))
            et55.filters = arrayOf(LimitDecimal(5, 2))
            et66.filters = arrayOf(LimitDecimal(5, 2))
            et33.filters = arrayOf(LimitDecimal(5, 2))
        }
    }

    /* private fun singleSelection() {
         binding.apply {
             cb121.setOnClickListener {
                 if (cb121.isChecked) {
                     cb122.isChecked = false
                     cb123.isChecked = false
                 }
             }
             cb122.setOnClickListener {
                 if (cb122.isChecked) {
                     cb121.isChecked = false
                     cb123.isChecked = false
                 }
             }
             cb123.setOnClickListener {
                 if (cb123.isChecked) {
                     cb121.isChecked = false
                     cb122.isChecked = false
                 }
             }
         }
     }*/

    private fun addImageAndRemove() {
        binding.apply {
            ivRemove1.setOnClickListener {
                ivImage1.setImageResource(R.drawable.upload_img)
                tvAddImage1.visibility = View.VISIBLE
            }
            ivRemove2.setOnClickListener {
                ivImage2.setImageResource(R.drawable.upload_img)
                tvAddImage2.visibility = View.VISIBLE
            }
            ivRemove3.setOnClickListener {
                ivImage3.setImageResource(R.drawable.upload_img)
                tvAddImage3.visibility = View.VISIBLE
            }
        }
    }

    private fun getAllQuestionsAnswer() {
        loading = CustomProgressLoading(requireContext())
        try {
            viewModel.getHealthQuestion()
            viewModel.responseOne.observe(viewLifecycleOwner) {
                loading.hideProgress()
                if (it.isSuccessful && it.code() == 200 && it.body() != null) {
                    binding.apply {
                        try {
                            fillQuestionnaire(it.body()!!)
                            if (it.body()!!.data == null) {
                                updateImage()
                            } else {
                                imageArray[0].image_url =
                                    it.body()!!.data!!.questions.image[0].image_url
                                imageArray[1].image_url =
                                    it.body()!!.data!!.questions.image[1].image_url
                                imageArray[2].image_url =
                                    it.body()!!.data!!.questions.image[2].image_url
                            }
                        } catch (e: Exception) {
                            Log.e(Constants.TAG, "getAllquestionAnswer: $e")
                        }
                    }

                }
            }
        } catch (e: Exception) {

        }
    }

    private fun fillQuestionnaire(body: GetHealthQuestionnaire) {
        binding.apply {
            for (value in body.data!!.questions.question_data)
                when (value.id) {
                    1 -> for (j in value.answer)
                        for (i in arrayListOf(cb_11, cb_12, cb_13, cb_14, cb_15)) {
                            var checked = i.text
                            if (checked.toString().lowercase() == j.lowercase()) {
                                i.isChecked = true
                            }
                        }
                    2 -> if (value.answer.isNotEmpty())
                        et_22.setText(value.answer[0])
                    3 -> if (value.answer.isNotEmpty())
                        et_44.setText(value.answer[0])
                    4 -> if (value.answer.isNotEmpty())
                        et_55.setText(value.answer[0])
                    5 -> if (value.answer.isNotEmpty())
                        et_66.setText(value.answer[0])
                    6 -> if (value.answer.isNotEmpty())
                        et_33.setText(value.answer[0])
                    7 -> for (j in value.answer) {
                        for (i in arrayListOf(
                            cb_71,
                            cb_72,
                            cb_73,
                            cb_74,
                            cb_75,
                            cb_76,
                            cb_77
                        )) {
                            var checked = i.text
                            if (checked == j) {
                                i.isChecked = true
                            }
                        }
                        if (j.startsWith("Injuries:")) {
                            cbInjuries.isChecked = true
                            etInjuries.setText(j.split(":")[1])
                            etInjuries.visibility = View.VISIBLE
                        }
                        if (j.startsWith("Other:")) {
                            cbOther.isChecked = true
                            etOther.setText(j.split(":")[1])
                            etOther.visibility = View.VISIBLE
                        }
                    }
                    8 -> for (j in value.answer) {
                        for (i in arrayListOf(
                            cb_81,
                            cb_82,
                            cb_83,
                            cb_84,
                            cb_85,
                        )) {
                            var checked = i.text
                            if (checked == j) {
                                i.isChecked = true
                            }
                        }
                        if (j.startsWith("Other:")) {
                            cbOther2.isChecked = true
                            etOther2.setText(j.split(":")[1])
                            etOther2.visibility = View.VISIBLE
                        }
                    }
                    9 -> for (j in value.answer) {
                        for (i in arrayListOf(
                            cb_91, cb_92, cb_93, cb_94, cb_95, cb_96, cb_97
                        )) {
                            var checked = i.text
                            if (checked == j) {
                                i.isChecked = true
                            }
                        }
                    }
                    10 -> for (j in value.answer) {
                        for (i in arrayListOf(cb_1010, cb_1011, cb_1012, cb_1013, cb_1014)) {
                            var checked = i.text
                            if (checked == j) {
                                i.isChecked = true
                            }
                        }
                    }
                    11 -> for (j in value.answer) {
                        for (i in arrayListOf(cb_12_1, cb_12_2, cb_12_3)) {
                            var checked = i.text
                            if (checked == j) {
                                i.isChecked = true
                            }
                        }
                    }
                    12 -> if (value.answer.isNotEmpty())
                        etBreakfast.setText(value.answer[0])
                    13 -> if (value.answer.isNotEmpty())
                        etComments.setText(value.answer[0])
                }
            try {
                Util.loadImage(requireContext(), ivImage1, body.data.questions.image[0].image_url)
                Util.loadImage(requireContext(), ivImage2, body.data.questions.image[1].image_url)
                Util.loadImage(requireContext(), ivImage3, body.data.questions.image[2].image_url)
            } catch (e: Exception) {
            }

        }
    }

    private fun createBody(): HealthQuestionBody {
        binding.apply {
            val questionList = ArrayList<HealthQuestionBody.Question>()
            questionList.add(
                HealthQuestionBody.Question(
                    1, tvQ11.text.toString(), generateMultipleAnswer(
                        arrayListOf(cb_11, cb_12, cb_13, cb_14, cb_15)
                    )
                )
            )
            questionList.add(
                HealthQuestionBody.Question(
                    2, tvQ22.text.toString(), arrayListOf(et_22.text.toString())
                )
            )
            questionList.add(
                HealthQuestionBody.Question(
                    3, tvQ44.text.toString(), arrayListOf(et_44.text.toString())
                )
            )
            questionList.add(
                HealthQuestionBody.Question(
                    4, tvQ55.text.toString(), arrayListOf(et_55.text.toString())
                )
            )
            questionList.add(
                HealthQuestionBody.Question(
                    5, tvQ66.text.toString(), arrayListOf(et_66.text.toString())
                )
            )
            questionList.add(
                HealthQuestionBody.Question(
                    6, tvQ33.text.toString(), arrayListOf(et_33.text.toString())
                )
            )
            questionList.add(
                HealthQuestionBody.Question(
                    7, tvQ77.text.toString(), generateMultipleAnswer(
                        arrayListOf(
                            cb_71,
                            cb_72,
                            cb_73,
                            cb_74,
                            cb_75,
                            cb_76,
                            cb_77,
                            cbInjuries,
                            cbOther
                        ),
                        etInjuries,
                        etOther
                    )
                )
            )
            questionList.add(
                HealthQuestionBody.Question(
                    8, tvQ88.text.toString(), generateMultipleAnswer(
                        arrayListOf(
                            cb_81,
                            cb_82,
                            cb_83,
                            cb_84,
                            cb_85,
                            cbOther2
                        ),
                        editText2 = etOther2
                    )
                )
            )
            questionList.add(
                HealthQuestionBody.Question(
                    9, tvQ99.text.toString(), generateMultipleAnswer(
                        arrayListOf(
                            cb_91,
                            cb_92,
                            cb_93,
                            cb_94,
                            cb_95,
                            cb_96,
                            cb_97
                        )
                    )
                )
            )
            questionList.add(
                HealthQuestionBody.Question(
                    10, tvQ1010.text.toString(), generateMultipleAnswer(
                        arrayListOf(
                            cb_1010,
                            cb_1011,
                            cb_1012,
                            cb_1013,
                            cb_1014
                        )
                    )
                )
            )
            questionList.add(
                HealthQuestionBody.Question(
                    11, tvQ1212.text.toString(), generateMultipleAnswer(
                        arrayListOf(
                            cb_12_1,
                            cb_12_2,
                            cb_12_3
                        )
                    )
                )
            )
            questionList.add(
                HealthQuestionBody.Question(
                    12, tvQ1313.text.toString(), generateMultipleAnswer2(
                        arrayListOf(
                            etBreakfast
                        )
                    )
                )
            )
            questionList.add(
                HealthQuestionBody.Question(
                    13, tvQ1414.text.toString(), generateMultipleAnswer2(
                        arrayListOf(etComments)
                    )
                )
            )
            return HealthQuestionBody(/*Session.getUserDetails().id*/ questionList, imageArray)
        }
    }

    private fun generateMultipleAnswer(
        view: ArrayList<CheckBox>,
        editText: EditText? = null,
        editText2: EditText? = null
    ): ArrayList<String> {
        var list = ArrayList<String>()
        view.forEachIndexed { index, checkBox ->
            if (checkBox.isChecked) list.add(
                if (view.size - 2 == index && checkBox.text.toString() == "Injuries") "Injuries:${editText?.text.toString()}"
                else if (view.size - 1 == index && checkBox.text.toString() == "Other") "Other:${editText2?.text.toString()}"
                else checkBox.text.toString()
            )
        }
        return list
    }

    private fun generateMultipleAnswer2(view: ArrayList<EditText>): ArrayList<String> {
        var list = ArrayList<String>()
        view.forEachIndexed { _, editText ->
            list.add(editText.text.toString())
        }
        return list
    }

    private fun visibility(textView: TextView, linearLayout: LinearLayout) {
        linearLayout.visibility =
            if (linearLayout.visibility == View.GONE) View.VISIBLE else View.GONE
        textView.setCompoundDrawablesWithIntrinsicBounds(
            0,
            0,
            if (linearLayout.visibility == View.GONE) R.drawable.ic_arrow_down else R.drawable.ic_arrow_up,
            0
        )
    }

    private fun visibility2(checkBox: CheckBox, editText: EditText) {
        editText.visibility = if (checkBox.isChecked)
            View.VISIBLE
        else View.GONE
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        try {
            uri = data?.data
            when (flag) {
                1 -> binding.apply {
                    ivImage1.setImageURI(uri)
                    tvAddImage1.visibility = View.GONE
                    filePath[0] = (data?.getStringExtra("extra.file_path")!!)
                }
                2 -> binding.apply {
                    ivImage2.setImageURI(uri)
                    tvAddImage2.visibility = View.GONE
                    filePath[1] = (data?.getStringExtra("extra.file_path")!!)
                }
                3 -> binding.apply {
                    ivImage3.setImageURI(uri)
                    tvAddImage3.visibility = View.GONE
                    filePath[2] = (data?.getStringExtra("extra.file_path")!!)
                }
            }
        } catch (e: Exception) {
        }
    }

    private fun createDirectoryAndSaveFile(imageToSave: Bitmap, fileName: String): File {
        val direct = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                .toString() + "/Fitcru"
        )
        if (!direct.exists()) {
            direct.mkdirs()
        }
        val file = File(direct, fileName)
        if (file.exists()) {
            file.delete()
        }
        try {
            val out = FileOutputStream(file)
            imageToSave.compress(Bitmap.CompressFormat.JPEG, 100, out)
            out.flush()
            out.close()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return file
    }

    private fun uploadImage() {

        if (filePath[0].trim().isNotEmpty() || filePath[1].isNotEmpty() || filePath[2].isNotEmpty()
        ) {
            Log.v("img", "filepath is empty")
            val builder = MultipartBody.Builder()
            val array = arrayOf(filePath)
            builder.setType(MultipartBody.FORM)
            Log.v("imageArray", filePath[0] + "," + filePath[1] + "," + "," + filePath[2])
            for ((index, value) in filePath.withIndex()) {
                try {
                    /* val drawable: BitmapDrawable = value.drawable as BitmapDrawable
                    val bitmap: Bitmap = drawable.bitmap
                    val file = createDirectoryAndSaveFile(bitmap, "image$index.jpeg")*/
                    Log.v("image$index", filePath[index])
                    if (filePath[0].isNotEmpty()) {
                        val file = File(filePath[0])
                        builder.addFormDataPart(
                            "image[0]",
                            file.name,
                            file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                        )
                    }
                    if (filePath[1].isNotEmpty()) {
                        val file = File(filePath[1])
                        builder.addFormDataPart(
                            "image[1]",
                            file.name,
                            file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                        )
                    }
                    if (filePath[2].isNotEmpty()) {
                        val file = File(filePath[2])
                        builder.addFormDataPart(
                            "image[2]",
                            file.name,
                            file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                        )
                    }
                } catch (e: Exception) {
                    Log.e("TAG", "uploadImage: $e")
                    loading.hideProgress()
                }
            }

            if (valid()) {
                loading.showProgress()
                viewModel.uploadImageQuestionnaire(builder.build())
            }
        } else {
            /* try {
                   Log.v("img", "filepath is not empty")
                   val builder = MultipartBody.Builder()
                   val array = arrayOf(ivImage1, ivImage2, ivImage3)
                   builder.setType(FORM)

                   for ((index, value) in array.withIndex()) {
                       try {
                           val drawable: BitmapDrawable = value.drawable as BitmapDrawable
                           val bitmap: Bitmap = drawable.bitmap
                           val file = createDirectoryAndSaveFile(bitmap, "image$index.jpeg")
                           builder.addFormDataPart(
                               "image[]",
                               file.name,
                               file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                           )
                       } catch (e: Exception) {
                           Log.e("TAG", "uploadImage: $e")
                           loading.hideProgress()
                       }
                   }

                   if (valid()) {
                       loading.showProgress()
                       viewModel.uploadImageQuestionnaire(builder.build())
                   }
               } catch (e: Exception) {
                   Log.e("checks", "comes")

               }*/
            if (valid()) {
                loading.showProgress()
                Log.v("imageArrayData", imageArray.toString())
                updateHealth(createBody())
            }
        }

        viewModel.image.observe(viewLifecycleOwner) {
            try {
                if (it.isSuccessful && it.body() != null) {
                    /*imageArray.clear()
                  imageArray.addAll(it.body()!!.data)*/
                    if (filePath[0].isNotEmpty()) {
                        imageArray[0].image_url = (it.body()!!.data[0].image_url)
                        if (filePath[1].isNotEmpty()) {
                            imageArray[1].image_url = (it.body()!!.data[1].image_url)
                            if (filePath[2].isNotEmpty()) {
                                imageArray[2].image_url = (it.body()!!.data[2].image_url)
                            }
                        }
                    } else if (filePath[1].isNotEmpty()) {
                        imageArray[1].image_url = (it.body()!!.data[0].image_url)
                        if (filePath[2].isNotEmpty()) {
                            imageArray[2].image_url = (it.body()!!.data[1].image_url)
                        }
                    } else if (filePath[2].isNotEmpty()) imageArray[2].image_url =
                        (it.body()!!.data[0].image_url)

                    Log.v("image Upload", "done")
                    updateHealth(createBody())
                    /*it.body()!!.message*/
                } else if (!it.isSuccessful && it.errorBody() != null) {
                    Util.error(it.errorBody(), ImageUploadResponse::class.java).message
                } else Util.toast(requireContext(), "Something went wrong")
            } catch (e: Exception) {
                Log.e("TAG", "uploadImage1: $e")
            }
        }

    }

    private fun updateHealth(createBody: HealthQuestionBody) {
        viewModel.healthQuestion(createBody)
        viewModel.response.observe(viewLifecycleOwner) {
            loading.hideProgress()
            val message = if (it.isSuccessful && it.body() != null) {
                Log.v("update health", "done")
                it.body()!!.message
            } else if (!it.isSuccessful)
                Util.error(it.errorBody(), HealthQuestionResponse::class.java).message
            else "Something went wrong"
            Util.toast(requireContext(), message)
        }
    }

    private fun valid(): Boolean {
        binding.apply {
            var pair = Pair(true, "")
            if (!isOneSelected(arrayListOf(cb11, cb12, cb13, cb14, cb15)))
                pair = Pair(false, "Please select reason that you want to sign up")
            else if (et22.text.toString().isEmpty())
                pair = Pair(false, "Please fill the true reason for a change")
            else if (et44.text.toString().isEmpty())
                pair = Pair(false, "Please fill your waist size")
            else if (et55.text.toString().isEmpty())
                pair = Pair(false, "Please fill your neck size")
            else if (et66.text.toString().isEmpty())
                pair = Pair(false, "Please fill your hip size")
            else if (et33.text.toString().isEmpty())
                pair = Pair(false, "Please fill your body weight")
            else if (cbInjuries.isChecked && etInjuries.text.toString().isEmpty())
                pair = Pair(false, "Please fill about injuries details")
            else if (cbOther.isChecked && etOther.text.toString().isEmpty())
                pair = Pair(false, "Please fill about other medical condition")
            else if (cbOther2.isChecked && etOther2.text.toString().isEmpty())
                pair = Pair(false, "Please fill about other dietary allergies")
            else if (!isOneSelected(arrayListOf(cb_91, cb_92, cb_93, cb_94, cb_95, cb_96, cb_97)))
                pair = Pair(false, "Please select preferred cuisines")
            else if (!isOneSelected(arrayListOf(cb_1010, cb_1011, cb_1012, cb_1013, cb_1014)))
                pair = Pair(false, "Please select preferred style of nutrition")
            else if (!isOneSelected(arrayListOf(cb_12_1, cb_12_2, cb_12_3)))
                pair = Pair(false, "Please select macronutrient split")
            if (!pair.first)
                Util.toast(requireContext(), pair.second)
            return pair.first
        }
    }

    private fun isOneSelected(checkbox: ArrayList<CheckBox>): Boolean {
        for (i in checkbox)
            if (i.isChecked)
                return true
        return false
    }

    private fun backPress() {
        requireActivity()
            .onBackPressedDispatcher
            .addCallback(requireActivity(), object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (doubleBackToExitPressedOnce) {
                        activity!!.finishAffinity()
                        exitProcess(0)
                    }
                    doubleBackToExitPressedOnce = false
                    Toast.makeText(context, "Please save the details", Toast.LENGTH_SHORT)
                        .show()

                    Handler(Looper.getMainLooper()).postDelayed(Runnable {
                        doubleBackToExitPressedOnce = false
                    }, 2000)
                }
            }
            )
    }

}