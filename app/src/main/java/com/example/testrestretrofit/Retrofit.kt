package com.example.testrestretrofit

import android.util.Log
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager


class Retrofit {

    public fun getUsers(): MutableList<User>{

        val okHttpClient: OkHttpClient = UnsafeOkHttpClient.getUnsafeOkHttpClient()

        var UserList: MutableList<User> = mutableListOf()

        val api= Retrofit.Builder()
            .baseUrl("https://10.0.2.2")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)

        api.getUser().enqueue(object : Callback<List<User>> {
            override fun onResponse(
                call: Call<List<User>>,
                response: Response<List<User>>
            ) {
                if(response.isSuccessful){
                    response.body()?.let{
                        for(comm in it){
                            Log.i("Retro", comm.name)
                            UserList.add(comm)
                        }
                    }
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                Log.i("Retro", "ERRR")

            }
        })
        return UserList
    }

    public fun postUser(user: User){

        val okHttpClient: OkHttpClient = UnsafeOkHttpClient.getUnsafeOkHttpClient()

        var UserList: MutableList<User> = mutableListOf()

        val api= Retrofit.Builder()
            .baseUrl("https://10.0.2.2")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)

        val requestModel = user

        val call = api.createUser(requestModel)
        call.enqueue(object : Callback<MyResponse> {
            override fun onResponse(call: Call<MyResponse>, response: Response<MyResponse>) {
                if (response.isSuccessful) {
                    val responseModel = response.body()
                    // Обработка успешного ответа
                    if (responseModel != null) {
                        // Делаем что-то с данными из responseModel
                    }
                } else {
                    // Обработка неуспешного ответа
                }
            }

            override fun onFailure(call: Call<MyResponse>, t: Throwable) {
                // Обработка ошибки
            }
        })
    }

    object UnsafeOkHttpClient {
        fun getUnsafeOkHttpClient(): OkHttpClient {
            try {
// Create a trust manager that does not validate certificate chains
                val trustAllCerts: Array<TrustManager> = arrayOf(object : X509TrustManager {
                    override fun checkClientTrusted(chain: Array<java.security.cert.X509Certificate>, authType: String) {
                    }
                    override fun checkServerTrusted(chain: Array<java.security.cert.X509Certificate>, authType: String) {
                    }

                    override fun getAcceptedIssuers(): Array<java.security.cert.X509Certificate> {
                        return arrayOf()
                    }
                })

                // Install the all-trusting trust manager
                val sslContext: SSLContext = SSLContext.getInstance("SSL")
                sslContext.init(null, trustAllCerts, java.security.SecureRandom())

                // Create an ssl socket factory with our all-trusting manager
                val sslSocketFactory: SSLSocketFactory = sslContext.socketFactory

                val builder: OkHttpClient.Builder = OkHttpClient.Builder()
                builder.sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
                builder.hostnameVerifier { _, _ -> true }

                val okHttpClient: OkHttpClient = builder.build()
                return okHttpClient
            } catch (e: Exception) {
                throw RuntimeException(e)
            }
        }
    }


}









