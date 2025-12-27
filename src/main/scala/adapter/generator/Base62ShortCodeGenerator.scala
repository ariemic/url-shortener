package adapter.generator

import domain.ShortCodeGenerator
import domain.models.{OriginalUrl, ShortCode}

import java.util.concurrent.atomic.AtomicLong

class Base62ShortCodeGenerator extends ShortCodeGenerator {
  private val counter = new AtomicLong(0)

  override def generate(url: Option[OriginalUrl] = None): ShortCode = {
    val id = counter.incrementAndGet()
    ShortCode(Base62Encoder.encode(id))  
  }
}
