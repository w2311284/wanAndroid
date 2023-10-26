package com.tong.wanandroid

import androidx.paging.PagingSource
import androidx.paging.PagingState
import java.lang.Exception

class BasePagingSource<V: Any>(
    private val pageStart: Int = 1,
    private val load: suspend (Int, Int) -> List<V>
) : PagingSource<Int, V>() {
    override fun getRefreshKey(state: PagingState<Int, V>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1) ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, V> {
        val currentPage = params.key ?: 1
        return try {
            val data = load( currentPage, params.loadSize)
            val prevKey = if (currentPage == pageStart) null else currentPage - 1
            val nextKey = if (data.isNotEmpty()) currentPage + 1 else null
            LoadResult.Page(
                data = data,
                prevKey = prevKey,
                nextKey = nextKey
            )
        } catch (e: Exception){
            LoadResult.Error(e)
        }

    }
}