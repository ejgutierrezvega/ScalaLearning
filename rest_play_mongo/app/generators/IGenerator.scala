package generators

import java.util.UUID

trait IGenerator {
  def random: UUID
}
