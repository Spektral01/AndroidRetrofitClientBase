package com.example.testrestretrofit

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.awaitResponse
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.random.Random

class composeMain {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun seeUser() {


        val api = Retrofit()
        var UserList: List<User> = mutableListOf()
        val apiUser = retrofit2.Retrofit.Builder()
            .baseUrl("https://10.0.2.2")
            .client(Retrofit.UnsafeOkHttpClient.getUnsafeOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            var name by remember { mutableStateOf("") }
            var age by remember { mutableStateOf("") }
            var id by remember { mutableStateOf("") }
            var elements by remember {
                mutableStateOf(UserList)
            }
/*            TextField(
                value = id,
                onValueChange = { id = it },
                label = { Text("Id") }
            )*/
            TextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") }
            )
            TextField(
                value = age,
                onValueChange = { age = it },
                label = { Text("Age") }
            )

            Button(onClick = {
                if(name.isNotEmpty() && age.isNotEmpty()) {
                    var user = User(name, age.toInt())
                    api.postUser(user)
                }
            }) {
                Text("Save")
            }
            Button(onClick = {
                UserList = api.getUsers()
                GlobalScope.launch(Dispatchers.IO) {
                    val response = apiUser.getUser().awaitResponse()
                    if(response.isSuccessful){
                        UserList = response.body()!!
                        elements = UserList
                    }
                }

            }) {
                Text("Read")
            }
            Button(onClick = {
                GlobalScope.launch(Dispatchers.IO) {
                    val response = apiUser.deleteUser().awaitResponse()
                    if(response.isSuccessful){
                        elements = emptyList()
                    }
                }
            }) {
                Text("Delete All")
            }


            LazyColumn(

            ) {
                itemsIndexed(elements) { index, item ->
                    printUserColumn(item)
                }
            }


        }
    }

    @Composable
    fun showUsers(UserList: List<User>){

    }

    @Composable
    fun printUserColumn(item: User){
        Row() {
            Text(
                text = item.name,
                modifier = Modifier
                    .padding(end = 5.dp))
            Text(text = item.age.toString())
        }
    }
}