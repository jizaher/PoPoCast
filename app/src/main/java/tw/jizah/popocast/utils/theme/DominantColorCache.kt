package tw.jizah.popocast.utils.theme

import androidx.collection.LruCache

object DominantColorCache {
    private const val CACHE_SIZE = 10
    private val LRU_CACHE: LruCache<String, DominantColors> = LruCache(CACHE_SIZE)

    fun setColors(key: String, dominantColors: DominantColors) {
        LRU_CACHE.put(key, dominantColors)
    }

    fun getColors(key: String): DominantColors? = LRU_CACHE[key]
}