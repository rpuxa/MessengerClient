package ru.rpuxa.messenger.model

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay
import ru.rpuxa.messenger.RxBus
import ru.rpuxa.messenger.model.db.CurrentUserDao
import ru.rpuxa.messenger.model.server.LongOperationsServer
import ru.rpuxa.messenger.model.server.answers.ActionType
import ru.rpuxa.messenger.view.App
import java.io.IOException
import javax.inject.Inject

class ActionsWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {

    @Inject
    lateinit var server: LongOperationsServer

    @Inject
    lateinit var currentUserDao: CurrentUserDao

    @Inject
    lateinit var bus: RxBus

    init {
        App.component.inject(this)
    }

    override suspend fun doWork(): Result {
        Log.d(TAG, "start")
        val currentUser = currentUserDao.get()
        val token = currentUser.token
        var lastActionId = currentUser.lastActionId
        while (!isStopped) {
            try {
                Log.d(TAG, "waiting")
                val result = server.getActions(token, lastActionId)
                Log.d(TAG, "received")
                result.actions.forEach {
                    when (it.type) {
                        ActionType.MESSAGE.id -> {
                            Log.d(TAG, "msg")
                            bus.send(RxBus.NewMessage)
                        }
                    }
                }

                lastActionId = result.actions.maxBy { it.id }?.id ?: continue
                currentUser.lastActionId = lastActionId
                currentUserDao.update(currentUser)
            } catch (e: IOException) {
                e.printStackTrace()
                delay(2000)
            } catch (e: CancellationException) {
                Log.d(TAG, "cancelled")
            }
        }
        Log.d(TAG, "finished")
        return Result.success()
    }

    companion object {
        private const val TAG = "actionsworker"

        fun start(context: Context) {
            val instance = WorkManager.getInstance(context)

            instance.cancelAllWorkByTag(TAG)

            val request = OneTimeWorkRequestBuilder<ActionsWorker>()
                .addTag(TAG)
                .build()

            instance.enqueue(request)
        }


        fun stop(context: Context) {
            WorkManager.getInstance(context).cancelAllWorkByTag(TAG)
        }
    }
}