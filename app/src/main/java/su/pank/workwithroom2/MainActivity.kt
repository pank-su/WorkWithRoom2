package su.pank.workwithroom2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import androidx.room.Room
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import su.pank.workwithroom.db.AppDataBase
import su.pank.workwithroom.enities.User
import su.pank.workwithroom.ui.theme.WorkWithRoomTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WorkWithRoomTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var users = remember {
                        mutableStateListOf<User>()
                    }




                    LaunchedEffect(null) {
                        CoroutineScope(Dispatchers.IO).launch {
                            val db = Room.databaseBuilder(
                                applicationContext,
                                AppDataBase::class.java, "database-name"
                            ).build()

                            val loadedUsers = db.userDao().getAll()
                            withContext(Dispatchers.Main) {
                                users.clear()
                                users.addAll(loadedUsers)
                            }
                        }
                    }


                    var userAddDialogVis by remember { mutableStateOf(false) }

                    Scaffold(floatingActionButton = {
                        FloatingActionButton({
                            userAddDialogVis = true
                        }) {
                            Icon(
                                Icons.Default.Add,
                                null
                            )
                        }
                    }) { innerPadding ->
                        LazyColumn(Modifier.padding(innerPadding)) {
                            items(users) { user ->
                                Card {
                                    Text("${user.first_name} ${user.last_name}")
                                }
                            }
                        }
                    }

                    if (userAddDialogVis) {
                        Dialog({ userAddDialogVis = false }) {
                            var lastName by remember { mutableStateOf("") }
                            var firstName by remember { mutableStateOf("") }

                            Column {
                                Text(text = "Добавить пользователя")
                                TextField(
                                    lastName,
                                    { newValue -> lastName = newValue },
                                    label = { Text("Фамилия") })
                                TextField(
                                    firstName,
                                    { newValue -> firstName = newValue },
                                    label = { Text("Имя") })
                                Button(onClick = {
                                    CoroutineScope(Dispatchers.IO).launch {
                                        val db = Room.databaseBuilder(
                                            applicationContext,
                                            AppDataBase::class.java, "database-name"
                                        ).build()
                                        println(users.size)
                                        val newId = if (users.isEmpty()) 0 else users.size
                                        db.userDao().addUser(User(newId, lastName, firstName))
                                        users.add(User(newId, lastName, firstName))
                                    }

                                }) {
                                    Text(text = "Добавить")
                                }
                            }
                        }
                    }

                }
            }
        }
    }
}
