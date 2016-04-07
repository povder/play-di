package services

import play.api.cache.{CacheApi, EhCacheComponents}

trait CacheComponent {
  def defaultCacheApi: CacheApi
}

trait EhCacheComponent extends CacheComponent with EhCacheComponents {
}
