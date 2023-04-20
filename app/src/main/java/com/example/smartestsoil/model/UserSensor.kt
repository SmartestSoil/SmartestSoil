package com.example.smartestsoil.model

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

data class UserSensor(
    var userId: String = "",
    var imageUrl: String = "",
    var sensorName:String = "",
    var notes: List<Note> = emptyList()
)

data class Note(
    val title: String = "",
    val note: String = "",
    val time: String = ""
)

class SensorsFirestorePagingSource(
    private val db: FirebaseFirestore
) : PagingSource<QuerySnapshot, UserSensor>() {

    override suspend fun load(params: LoadParams<QuerySnapshot>): LoadResult<QuerySnapshot, UserSensor> {
        // Implementation of the load function
        return try {
            val currentPage = params.key ?: db.collection("sensors")
                .orderBy("sensorName")
                .limit(params.loadSize.toLong())
                .get()
                .await()

            val lastDocumentSnapshot = currentPage.documents.lastOrNull()
            val nextPage = if (lastDocumentSnapshot != null) {
                db.collection("sensors")
                    .orderBy("sensorName")
                    .startAfter(lastDocumentSnapshot)
                    .limit(params.loadSize.toLong())
                    .get()
                    .await()
            } else {
                null
            }

            val sensorsList = currentPage.documents.map { documentSnapshot ->
                val imageUrl = documentSnapshot.getString("imageUrl") ?: ""
                val sensorName = documentSnapshot.getString("sensorName") ?: ""
                UserSensor(imageUrl, sensorName)
            }

            LoadResult.Page(
                data = sensorsList,
                prevKey = null,
                nextKey = nextPage
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<QuerySnapshot, UserSensor>): QuerySnapshot? {
        // Implementation of the getRefreshKey function
        return null
    }
}
