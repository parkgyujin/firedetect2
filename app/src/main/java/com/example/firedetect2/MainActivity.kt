package com.example.firedetect2

/*import com.example.firedetect2.ApiKey.Companion.API_KEY     API키값 임포트*/

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


/*
data class Weather(
    val response: Response
) {

    data class Response(
        val header: Header,
        val body: Body
    )

    data class Header(
        val resultCode: Int,
        val resultMsg: String
    )

    data class Body(
        val dataType: String,
        val items: Items
    )

    data class Items(
        val item: List<Item>
    )

    data class Item(
        val baseData: Int,
        val baseTime: Int,
        val category: String,
        val fcstDate : Int,
        val fcstTime : Int,
        val fcstValue : String,
        val nx : Int,
        val ny : Int
    )
}

class ApiKey{
    companion object{
        const val API_KEY = "D1Qafvc%2Bj2M50JW5MbeoVLOGxhOf6QmkaYAyBL8WYqaMlGBgC3tK35Uxm5pxwkq74ooF22ydOWxpPwhHz299qQ%3D%3D"
    }
}

class Constants{
    companion object{
        const val BASE_URL = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/"
        const val TAG = "tst5"
    }
}

interface WeatherApi {

    @GET("getVilageFcst?serviceKey=$API_KEY")
    suspend fun getWeather(
        @Query("dataType") dataType : String,
        @Query("numOfRows") numOfRows : Int,
        @Query("pageNo") pageNo : Int,
        @Query("base_date") baseDate : Int,
        @Query("base_time") baseTime : Int,
        @Query("nx") nx : String,
        @Query("ny") ny : String
    ) : Response<Weather>
}
*/

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val readText: TextView = findViewById(R.id.readText)
        val temper: TextView = findViewById(R.id.temper)
        val humidity: TextView = findViewById(R.id.humidity)
        val dust: TextView = findViewById(R.id.dust)

        val clickBtn: Button = findViewById(R.id.clickBtn)

        clickBtn.setOnClickListener {
            val intent = Intent(this, cctv_activity::class.java)
            startActivity(intent)
        }

        //쓰기
        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        val myRef = database.getReference("dht")
        // 읽기

        myRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val heat = dataSnapshot.child("heat").getValue(String::class.java)
                val temp = dataSnapshot.child("temp").getValue(String::class.java)
                val humi = dataSnapshot.child("humi").getValue(String::class.java)
                val gas = dataSnapshot.child("gas").getValue(String::class.java)
                val rain = dataSnapshot.child("rain").getValue(String::class.java)

                temper.text="온도 \n $temp°C"
                humidity.text="습도 \n $humi%"
                dust.text="미세먼지 \n $gas"


            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                readText.text = "error: " + error.toException()
            }
        })
    }
        /*myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val value = dataSnapshot.getValue<String>()
                Log.d(TAG, "Value is : $value")
                readText.setText(value)
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })
    }*/
}