case class Database(tables: List[Table]) {
  // TODO 3.0
  override def toString: String = {
    val tabele = tables.map(table => table.toString).mkString("\n")
    tabele
  }

  // TODO 3.1
  def insert(tableName: String): Database = {
    val verificare = tables.map { tabela =>
      if (tabela.name == tableName) {
        Database(tables)
      }
    }
    //cream un table cu o lista goala, cerinta cerand numele
    val table = Table(tableName, List())
    val tabele = tables :+ table
    Database(tabele)
  }

  // TODO 3.2
  def update(tableName: String, newTable: Table): Database = {
    val new_tables = tables.map(tabela => if (tabela.name == tableName) newTable else tabela)
    Database(new_tables)
  }

  // TODO 3.3
  def delete(tableName: String): Database = {
    val tabele = tables.filter(tabela => if (tabela.name != tableName) true else false)
    Database(tabele)
  }

  // TODO 3.4
  def selectTables(tableNames: List[String]): Option[Database] = {
    //filtrare dupa nume
    val tabele = tables.filter(tabela => if (tableNames.contains(tabela.name)) true else false)
    Some(Database(tabele))
  }

  // TODO 3.5
  // Implement indexing here
  //folosesc apply, deoarece permite sintaxa tables(index)
   def apply(i: Int): Table = {
    val tabel = tables(i)
    tabel
  }
}