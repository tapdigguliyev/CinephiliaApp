package az.abb.tap.cinephilia.utility

import androidx.paging.Pager
import androidx.paging.PagingConfig
import az.abb.tap.cinephilia.base.BaseMediaPagingSource
import az.abb.tap.cinephilia.utility.Constants.NETWORK_PAGE_SIZE

fun <V: Any> createPager(
    pageSize: Int = NETWORK_PAGE_SIZE,
    enablePlaceHolders: Boolean = false,
    block: suspend (Int) -> List<V>
): Pager<Int, V> = Pager(
    config = PagingConfig(pageSize = pageSize, enablePlaceholders = enablePlaceHolders),
    pagingSourceFactory = { BaseMediaPagingSource(block) }
)