package com.ruslanshugaipov.chatapp

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.chat2desk.chat2desk_sdk.Chat2Desk
import com.chat2desk.chat2desk_sdk.HttpClient
import com.chat2desk.chat2desk_sdk.IChat2Desk
import com.chat2desk.chat2desk_sdk.LogLevel
import com.chat2desk.chat2desk_sdk.Settings
import com.chat2desk.chat2desk_sdk.create
import com.ruslanshugaipov.chatapp.ui.components.AppBar
import com.ruslanshugaipov.chatapp.ui.components.Chat
import com.ruslanshugaipov.chatapp.ui.theme.ChatAppTheme
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ChatAppTheme {
                val chat2Desk = initChat2Desk(applicationContext)
                ChatScreen(
                    c2d = chat2Desk,
                    client = null
                )
            }
        }
    }
}

private fun initChat2Desk(
    appContext: Context,
): IChat2Desk {
    val settings = Settings(
        authToken = BuildConfig.WIDGET_TOKEN,
        baseHost = BuildConfig.BASE_HOST,
        wsHost = BuildConfig.WS_HOST,
        storageHost = BuildConfig.STORAGE_HOST
    )
    settings.withLog = BuildConfig.DEBUG
    settings.logLevel = LogLevel.ALL

    settings.socketClient = HttpClient()
        .newBuilder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    return Chat2Desk.create(settings, appContext)
}

@Composable
fun ChatScreen(
    c2d: IChat2Desk,
    client: String?
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val error = c2d.error.collectAsState(null)

    LaunchedEffect(error.value) {
        error.value?.let {
            it.message?.let { message ->
                snackbarHostState.showSnackbar(
                    message
                )
                Log.e("App", message, it)
            }
        }
    }
    Scaffold(
        topBar = {
            AppBar(
                modifier = Modifier.systemBarsPadding(),
                chat2Desk = c2d,
                client = client,
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .navigationBarsPadding()
        ) {
            Chat(
                chat2desk = c2d,
                client = client,
            )
        }
    }
}
