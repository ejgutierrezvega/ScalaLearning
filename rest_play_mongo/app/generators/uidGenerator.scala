package generators

import java.util.UUID

class uidGenerator extends IGenerator{
  override def random: UUID = UUID.randomUUID()
}
