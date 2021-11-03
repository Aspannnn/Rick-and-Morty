package kz.aspan.rickandmorty.data.paging

import android.net.Uri
import androidx.paging.PagingSource
import androidx.paging.PagingState
import kz.aspan.rickandmorty.data.remote.RickAndMortyApi
import kz.aspan.rickandmorty.domain.model.character.Character
import retrofit2.HttpException
import java.io.IOException

class CharacterFilterPagingDataSource(
    private val api: RickAndMortyApi,
    private val name: String,
    private val status: String
) :
    PagingSource<Int, Character>() {


    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Character> {
        val pageNumber = params.key ?: 1
        return try {
            val response = api.getCharacterByName(pageNumber, name, status)
            val pagedResponse = response.body()
            val data = pagedResponse?.listOfCharacter

            var nextPageNumber: Int? = null
            if (pagedResponse?.info?.next != null) {
                val uri = Uri.parse(pagedResponse.info.next)
                val nextPageQuery = uri.getQueryParameter("page")
                nextPageNumber = nextPageQuery?.toInt()
            }

            LoadResult.Page(
                data = data.orEmpty(),
                prevKey = if (pageNumber == 1) null else pageNumber - 1,
                nextKey = nextPageNumber
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Character>): Int = 1
}