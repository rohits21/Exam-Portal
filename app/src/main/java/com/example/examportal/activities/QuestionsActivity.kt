package com.example.examportal.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.examportal.R
import com.example.examportal.adapters.OptionAdapter
import com.example.examportal.databinding.ActivityQuestionsBinding
import com.example.examportal.models.Question
import com.example.examportal.models.Quiz
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson

class QuestionsActivity : AppCompatActivity() {

    private var binding : ActivityQuestionsBinding?= null
    lateinit var adapter : OptionAdapter
    lateinit var firebaseFirestore: FirebaseFirestore
    private var quizzes : MutableList<Quiz> ?= null
    private var questions:MutableMap<String, Question>?=null
    private var index = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuestionsBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        var subject = intent.getStringExtra("Subject")
        setUpFirestore(subject!!)
        setUpButtons()
    }

    private fun setUpButtons(){
        binding?.btnNext!!.setOnClickListener {
            index++
            setUpQuestion()
        }
        binding?.btnPrevious!!.setOnClickListener {
            index--
            setUpQuestion()
        }
        binding?.btnSubmit!!.setOnClickListener {
            Log.d("Submit",questions.toString())
            val intent = Intent(this,ResultActivity::class.java)
            val json = Gson().toJson(quizzes!![0])

            intent.putExtra("Quiz",json)
            startActivity(intent)
            finish()
        }
    }

    private fun setUpFirestore(subject:String){
        firebaseFirestore = FirebaseFirestore.getInstance()
        firebaseFirestore.collection("Subjects")
            .whereEqualTo("title",subject).get().addOnSuccessListener {

                quizzes = it.toObjects(Quiz::class.java)
                questions = quizzes!![0].questions
                setUpQuestion()
            }
    }


    private fun setUpQuestion(){

        binding?.btnNext!!.visibility = View.GONE
        binding?.btnPrevious!!.visibility = View.GONE
        binding?.btnSubmit!!.visibility = View.GONE

        if(index == 1 && questions!!.size > 1){
            binding?.btnNext!!.visibility = View.VISIBLE
        }
        else if(index == questions!!.size){
            binding?.btnNext!!.visibility = View.GONE
            binding?.btnSubmit!!.visibility = View.VISIBLE
            if(index>1){
                binding?.btnPrevious!!.visibility = View.VISIBLE
            }
        }
        else{
            binding?.btnPrevious!!.visibility = View.VISIBLE
            binding?.btnNext!!.visibility = View.VISIBLE
        }

        val question = questions!!["question$index"]

        adapter = OptionAdapter(this,question!!)

        binding?.description?.text = question!!.description
        binding?.optionList?.adapter = adapter
        binding?.optionList?.layoutManager = LinearLayoutManager(this)
        binding?.optionList?.setHasFixedSize(true)
    }
}