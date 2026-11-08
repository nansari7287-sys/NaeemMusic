package com.maxrave.simpmusic.service.rss

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

/**
 * Legacy blog-feed worker retained as a no-op for compatibility with any existing
 * WorkManager registration. Naeem Music no longer polls the upstream developer blog.
 */
class RssFeedNotifyWork(
    context: Context,
    params: WorkerParameters,
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result = Result.success()
}
