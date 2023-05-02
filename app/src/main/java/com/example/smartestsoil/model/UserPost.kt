package com.example.smartestsoil.model



import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await
import com.google.firebase.Timestamp
import java.util.Date

data class UserPost(
    var media: String = "",
    var body: String = "",
    var date: Date? = null,
    var likes: Int = 0,
    var user: String = ""
    //var comments List<comment> = emptyList()
)

data class comment(
    val commenter: String = "",
    val commentBody: String = "",
    val time: String = ""
)

class PostsFireStorePagingSource(
    private val db :FirebaseFirestore,

    ) : PagingSource<QuerySnapshot, UserPost>() {

    override suspend fun load(params: LoadParams<QuerySnapshot>): LoadResult<QuerySnapshot, UserPost> {

        return try {

            val currentPage = params.key ?: db.collection("posts")
                .orderBy("date")
                .limit(params.loadSize.toLong())
                .get()
                .await()

            val lastDocumentSnapshot = currentPage.documents.lastOrNull()
            val nextPage = if (lastDocumentSnapshot != null) {
                db.collection("posts")
                    .orderBy("date")
                    .startAfter(lastDocumentSnapshot)
                    .limit(params.loadSize.toLong())
                    .get()
                    .await()
            } else {
                null
            }

            val postsList = currentPage.documents.map { documentSnapshot ->
                val media = documentSnapshot.getString("media") ?: ""
                val body = documentSnapshot.getString("body") ?: ""
                val date = documentSnapshot.getTimestamp("date")?.toDate() // Convert Timestamp to Date
                val likes = documentSnapshot.getLong("likes")?.toInt() ?: 0 // Convert Long to Int
                val user = documentSnapshot.getString("user") ?: ""
                UserPost(media, body, date, likes, user)
            }



            LoadResult.Page(
                data = postsList,
                prevKey = null,
                nextKey = nextPage
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }

    }


    override fun getRefreshKey(state: PagingState<QuerySnapshot, UserPost>): QuerySnapshot? {
        // Implementation of the getRefreshKey function
        return null
    }
}
