package com.example.smartestsoil.model

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await

data class UserPlant(
    var plantid:String,
    var imageUrl: String,
    var plantName:String,
    var pairedSensor:String



    )
class PlantsFirestorePagingSource(
    private val db: FirebaseFirestore
) : PagingSource<QuerySnapshot, UserPlant>() {

    override suspend fun load(params: LoadParams<QuerySnapshot>): LoadResult<QuerySnapshot, UserPlant> {
        // Implementation of the load function
        return try {
            val currentPage = params.key ?: db.collection("plants")
                .orderBy("plantid")
                .limit(params.loadSize.toLong())
                .get()
                .await()

            val lastDocumentSnapshot = currentPage.documents.lastOrNull()
            val nextPage = if (lastDocumentSnapshot != null) {
                db.collection("plants")
                    .orderBy("plantid")
                    .startAfter(lastDocumentSnapshot)
                    .limit(params.loadSize.toLong())
                    .get()
                    .await()
            } else {
                null
            }

            val plantsList = currentPage.documents.map { documentSnapshot ->
                val plantid = documentSnapshot.getString("plantid") ?: ""
                val imageUrl = documentSnapshot.getString("imageUrl") ?: ""
                val plantName = documentSnapshot.getString("plantName") ?: ""
                val pairedSensor = documentSnapshot.getString("pairedSensor") ?: ""

                UserPlant(plantid,imageUrl, plantName,pairedSensor)
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
