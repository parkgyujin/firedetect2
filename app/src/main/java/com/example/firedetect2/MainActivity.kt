package com.example.firedetect2

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.firedetect2.Constants.Companion.TAG
import com.example.firedetect2.databinding.ActivityMainBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

//@AndroidEntryPoint
class MainActivity : AppCompatActivity() {


    // 뷰모델 생성
//    private val viewModel by viewModels<WeatherViewModel>()
//    private lateinit var binding : ActivityMainBinding

    @RequiresApi(Build.VERSION_CODES.O)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //사용 변수
        val readText: TextView = findViewById(R.id.readText)
        val temper: TextView = findViewById(R.id.temper)
        val humidity: TextView = findViewById(R.id.humidity)
        val dust: TextView = findViewById(R.id.dust)
        val today: TextView = findViewById(R.id.today)
        val clickBtn: Button = findViewById(R.id.clickBtn)

        //현재 날짜를 가져오는 메소드
        val now = LocalDate.now()
        val strnow = now.format(DateTimeFormatter.ofPattern("yyyy년MM월dd일"))

        //activty 전환을 위한 클릭버튼
        clickBtn.setOnClickListener {
            val intent = Intent(this, cctv_activity::class.java)
            startActivity(intent)

        }

        //오늘 날짜
        today.text= strnow

        //쓰기
        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        val myRef = database.getReference("dht")
        // 읽기

        //센서 데이터를 실시간으로 읽어들임
        myRef.addValueEventListener(object : ValueEventListener {

            @SuppressLint("SetTextI18n")
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                //데이터베이스로 부터 실시간으로 가져오고 쓰는 정보값의 변수
//                val heat = dataSnapshot.child("heat").getValue(String::class.java)
                val temp = dataSnapshot.child("temp").getValue(String::class.java)
                val humi = dataSnapshot.child("humi").getValue(String::class.java)
                val gas = dataSnapshot.child("gas").getValue(String::class.java)
//                val rain = dataSnapshot.child("rain").getValue(String::class.java)

                temper.text="온도 \n $temp°C"
                humidity.text="습도 \n $humi%"
                dust.text="미세먼지 \n $gas"


            }
            //센서 오류시 작동하는 error 코드
            @SuppressLint("SetTextI18n")
            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                readText.text = "error: " + error.toException()
            }
        })

            //기상청 데이터바인딩
//            binding = DataBindingUtil.setContentView(this,R.layout.activity_main)
//            binding.lifecycleOwner = this
//
//            viewModel.getWeather("JSON",14,1,
//                20220322,1100,"63","89")
//
//            viewModel.weatherResponse.observe(this){
//                for(i in it.body()?.response!!.body.items.item){
//                    Log.d(TAG, "$i")
//                }
//            }
        }
}


