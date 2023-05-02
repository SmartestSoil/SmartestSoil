package com.example.smartestsoil.model


import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

data class UserPlant(
    var imageUrl: String,
    var pairedSensor:String,
    var plantId:String,
    var plantName:String,
    var userId: String,
    var notes: List<Note> = emptyList()
)

data class Note(
    val title: String = "",
    val note: String = "",
    val time: String = ""
)

class PlantsFireStorePagingSource(
    private val db :FirebaseFirestore,
    private val userId: String

) : PagingSource<QuerySnapshot, UserPlant>() {

    override suspend fun load(params: LoadParams<QuerySnapshot>): LoadResult<QuerySnapshot, UserPlant> {

        return try {

            val currentPage = params.key ?: db.collection("plants")
                .whereEqualTo("userId", userId)
                .orderBy("plantName")
                .limit(params.loadSize.toLong())
                .get()
                .await()

            val lastDocumentSnapshot = currentPage.documents.lastOrNull()
            val nextPage = if (lastDocumentSnapshot != null) {
                db.collection("plants")
                    .whereEqualTo("userId", userId)
                    .orderBy("plantName")
                    .startAfter(lastDocumentSnapshot)
                    .limit(params.loadSize.toLong())
                    .get()
                    .await()
            } else {
                null
            }

            val plantsList =
                currentPage.documents.map { documentSnapshot ->

                    val imageUrl = documentSnapshot.getString("imageUrl") ?: ""
                    val pairedSensor = documentSnapshot.getString("pairedSensor") ?: ""
                    val plantId = documentSnapshot.getString("plantId") ?: ""
                    val plantName = documentSnapshot.getString("plantName") ?: ""
                    val userId = documentSnapshot.getString("userId") ?: ""
                    UserPlant(imageUrl, pairedSensor, plantId, plantName, userId)
                }



            LoadResult.Page(
                data = plantsList,
                prevKey = null,
                nextKey = nextPage
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }

    }


    override fun getRefreshKey(state: PagingState<QuerySnapshot, UserPlant>): QuerySnapshot? {
        // Implementation of the getRefreshKey function
        return null
    }
}
