package domain

import domain.models.ShortCode

trait ShortCodeGenerator {
  def generate(): ShortCode 
}
