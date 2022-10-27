package com.example.firedetect2

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.*
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import org.json.JSONException
import org.json.JSONObject
import java.util.*
import kotlin.math.roundToInt


class MainActivity : AppCompatActivity() {
    var requestQueue: RequestQueue? = null

    @SuppressLint("MissingInflatedId")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //사용 변수
        val readText: TextView = findViewById(R.id.readText)
        val temper: TextView = findViewById(R.id.temper)
        val humidity: TextView = findViewById(R.id.humidity)
        val dust: TextView = findViewById(R.id.dust)
        val gasView: TextView = findViewById(R.id.gasView)
        val rainView: TextView = findViewById(R.id.rainView)



        val clickBtn: Button = findViewById(R.id.clickBtn)

        val clickBtn2: Button = findViewById(R.id.clickBtn2)




        //activty 전환을 위한 클릭버튼
        clickBtn.setOnClickListener {
            val intent = Intent(this, cctv_activity::class.java)
            startActivity(intent)

        }

        //날씨를 받는 버튼
        clickBtn2.setOnClickListener {
                CurrentCall()
            }


        //쓰기
        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        val myRef = database.getReference("dht")
        // 읽기

        //센서 데이터를 실시간으로 읽어들임
        myRef.addValueEventListener(object : ValueEventListener {
            @SuppressLint("SetTextI18n")
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                //데이터베이스로 부터 실시간으로 가져오고 쓰는 정보값의 변수
                val heat = dataSnapshot.child("heat").getValue(String::class.java)
                val temp = dataSnapshot.child("temp").getValue(String::class.java)
                val humi = dataSnapshot.child("humi").getValue(String::class.java)
                val gas = dataSnapshot.child("gas").getValue(String::class.java)
                val rain = dataSnapshot.child("rain").getValue(String::class.java)

                temper.text = "온도 \n $temp°C"
                humidity.text = "습도 \n $humi%"
                dust.text = "미세먼지 \n $heat"
                gasView.text = "가스 감지 \n $gas"
                rainView.text = "빗물 감지 상태 \n $rain"


            }

            //센서 오류시 작동하는 error 코드
            @SuppressLint("SetTextI18n")
            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                readText.text = "error: " + error.toException()
            }
        })

        //날씨데이터의 Null값
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(applicationContext)
        }
    }


    fun CurrentCall(){

        val cityView: TextView = findViewById(R.id.cityView)
        val weatherView: TextView = findViewById(R.id.weatherView)
        val tempView: TextView = findViewById(R.id.tempView)

        val url = "https://api.openweathermap.org/data/2.5/weather?q=Seoul, KR,uk&APPID=bdc9150fa915f2970dda565e1e75047e"

        val request: StringRequest = @SuppressLint("SetTextI18n")
        object : StringRequest(Method.GET, url, Response.Listener
        { response ->
            try {

                //api로 받은 파일 jsonobject로 새로운 객체 선언
                val jsonObject = JSONObject(response)


                //도시 키값 받기
                val city = jsonObject.getString("name")
                cityView.text = "지역 \n $city"


                //날씨 키값 받기
                val weatherJson = jsonObject.getJSONArray("weather")
                val weatherObj = weatherJson.getJSONObject(0)
                val weather = weatherObj.getString("description")
                weatherView.text = "현재 날씨 \n $weather"


                //기온 키값 받기
                val tempK = JSONObject(jsonObject.getString("main"))

                //기온 받고 켈빈 온도를 섭씨 온도로 변경
                val tempDo = ((tempK.getDouble("temp") - 273.15) * 100).roundToInt() / 100.0
                tempView.text = "체감온도 \n $tempDo°C"

            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }, Response.ErrorListener { }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                return HashMap()
            }
        }

        request.setShouldCache(false)
        requestQueue!!.add(request)
    }
}




