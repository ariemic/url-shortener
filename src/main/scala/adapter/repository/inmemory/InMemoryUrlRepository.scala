package adapter.repository.inmemory
import domain.UrlRepository
import domain.models.{MappedUrl, OriginalUrl, ShortCode}

class InMemoryUrlRepository extends UrlRepository{
  //it's better to store whole MappedUrl as it contains originalUrl and I can add later other
  //fields for analytics
  private var storage: Map[ShortCode, MappedUrl] = Map.empty


  //it's not good for concurrent calls use Ref from cats-effect later
  override def save(mappedUrl: MappedUrl): Unit =
    storage += (mappedUrl.shortCode -> mappedUrl)

  override def findByShortCode(code: ShortCode): Option[MappedUrl] = storage.get(code)

  override def findByOriginalUrl(originalUrl: OriginalUrl): Option[MappedUrl] = {
   storage.values.find(_.originalUrl == originalUrl)
  }

}
