package generators

import java.util.UUID

trait IGenerator{
  def random: UUID
}

object uidGenerator extends IGenerator{
  override def random: UUID = UUID.randomUUID()
}
