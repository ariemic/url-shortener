package domain

import domain.models.{OriginalUrl, ShortCode}

trait ShortCodeGenerator {
  def generate(url: Option[OriginalUrl] = None): ShortCode
}
