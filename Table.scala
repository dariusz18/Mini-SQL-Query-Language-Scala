
type Row = Map[String, String]
type Tabular = List[Row]

case class Table (tableName: String, tableData: Tabular) {

  // TODO 1.0
  def header: List[String] = {
    tableData match {
      case Nil => List()
      case _ => tableData.head.keys.toList //List(Nume, Prenume, Varsta)
    }
  }
  def data: Tabular = tableData
  def name: String = tableName


  // TODO 1.1
  override def toString: String = {
    val header2 = header.mkString(",")
    val data2 = data.map(rand => rand.values.mkString(",")).mkString("\n") //fiecare valoare o facem string si o prelucram
    header2 + "\n" + data2
  }

  // TODO 1.3
  def insert(row: Row): Table = {
    if (data.contains(row)) {
      Table(tableName, data)
    } else {
      val table = data ++ List(row) //adaugam linia noua daca nu exista deja
      Table(tableName, table)
    }
  }

  // TODO 1.4
  def delete(row: Row): Table = {
    if (data.contains(row)) {
      Table(tableName, List())
    }
    val rand = data.filter(rand => rand != row) //filtrare randuri
    Table(tableName, rand)
  }

  // TODO 1.5
  def sort(column: String, ascending: Boolean = true): Table = {
    if (column.isEmpty) {
      Table(tableName, data)
    } else if (data.isEmpty) {
      Table(tableName, List())
    } else {
      val table = ascending match {
        case true => data.sortBy(rand => rand.get(column)) //sortam randurile dupa column
        case false => data.sortBy(rand => rand.get(column)).reverse
      }
      Table(tableName, table)
    }
  }

  // TODO 1.6
  def select(columns: List[String]): Table = {
    if (columns.isEmpty) return Table("My_table", List())
    if (data.isEmpty) return Table("My_table", List())

    val table = data.map { rand => //parcurgem fiecare rand
      rand.filter { elem => //filtram fiecare pereche "Nume" -> "Popescu"
        elem match {
          case (cheie, valoare) => columns.contains(cheie)
        }
      }
    }
    Table("My_table", table)
  }

  // TODO 1.7
  // Construiti headerele noi concatenand numele tabelei la headere
  def cartesianProduct(otherTable: Table): Table = ???

  // TODO 1.8
  def join(other: Table)(col1: String, col2: String): Table = ???

  // TODO 2.3
  def filter(f: FilterCond): Table = {
    val table = data.filter(rand => f.eval(rand).get) //List[Row]
    Table(tableName, table)
  }

  // TODO 2.4
  def update(f: FilterCond, updates: Map[String, String]): Table = {
    val table = data.map { rand =>
      if (f.eval(rand).get) rand ++ updates //actualizeaza valorile
      else rand
    }
    Table(tableName, table)
  }

  // TODO 3.5
  // Implement indexing
  //folosesc apply, deoarece permite sintaxa tables(index)
  def apply(i: Int): Row = {
    val rand = tableData(i)
    rand
  }
}

object Table {
  // TODO 1.2
  def fromCSV(csv: String): Table = {
    val linii = csv.split("\n").toList
    linii match {
      case Nil => Table("My_table", List())
      case header :: data =>
        val header2 = header.split(",").toList
        val data2 = data.map { rand =>
          val valori = rand.split(",").toList
          header2.zip(valori).toMap
        }
        Table("My_table", data2)
    }
  }

  // TODO 1.9
  def apply(name: String, s: String): Table = {
    val linii = s.split("\n").toList
    linii match {
      case Nil => Table(name, List())
      case header :: data =>
        val header2 = header.split(",").toList
        val data2 = data.map { rand =>
          val valori = rand.split(",").toList
          header2.zip(valori).toMap
        }
        Table(name, data2)
    }
  }
}