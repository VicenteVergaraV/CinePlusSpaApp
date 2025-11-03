package com.cineplusapp.cineplusspaapp.data.sync

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.cineplusapp.cineplusspaapp.data.remote.ApiService
import com.cineplusapp.cineplusspaapp.repository.CartRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first

@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters,
    private val cartRepo: CartRepository,
    private val api: ApiService
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        return try {
            val pending = cartRepo.pending().first()

            var hadRetryableError = false

            for (item in pending) {
                try {
                    // TODO: envía al backend. Ejemplo:
                    // api.syncCartItem(SyncCartItemDto.from(item))
                    cartRepo.markSynced(item.id) // <- id: Long en la entidad
                } catch (e: retrofit2.HttpException) {
                    when (e.code()) {
                        400, 404 -> {
                            // No recuperable. O marcas synced para sacarlo de la cola…
                            // cartRepo.markSynced(item.id)
                            // …o lo dejas sin tocar para inspección manual
                        }
                        401, 500, 502, 503 -> {
                            // Recuperables
                            hadRetryableError = true
                        }
                        else -> hadRetryableError = true
                    }
                } catch (e: Exception) {
                    // Networking/transient
                    hadRetryableError = true
                }
            }

            if (hadRetryableError) Result.retry() else Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}

