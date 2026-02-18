import scala.language.implicitConversions

trait FilterCond {
  def eval(r: Row): Option[Boolean]

  // TODO 2.2
  def ===(other: FilterCond): FilterCond = Equal(this, other)
  def &&(other: FilterCond): FilterCond = And(this, other)
  def ||(other: FilterCond): FilterCond = Or(this, other)
  def unary_! : FilterCond = Not(this)
}

case class Field(colName: String, predicate: String => Boolean) extends FilterCond {
  override def eval(r: Row): Option[Boolean] = {
    val valoare = r.get(colName).mkString(",")
    if (predicate(valoare)) Some(true)
    else Some(false)
  }
}

case class Compound(op: (Boolean, Boolean) => Boolean, conditions: List[FilterCond]) extends FilterCond {
  override def eval(r: Row): Option[Boolean] = {
    val conditii = conditions.map(conditie => conditie.eval(r)) //apelez eval de mai sus si am o lista de Option(boolean)
    val lista = conditii.map(optiune => optiune.get) //lista de Boolean
    //folosim foldLeft, plecam cu acc = lista.head initial
    val adevar = lista.foldLeft(lista.head) { (acc, x) =>
      op(acc, x)
    }
    Some(adevar)
  }
}

case class Not(f: FilterCond) extends FilterCond {
  override def eval(r: Row): Option[Boolean] = {
    if (f.eval(r).get) Some(false)
    else Some(true)
  }
}

//instantiere clasa Compound
def And(f1: FilterCond, f2: FilterCond): FilterCond = {
  val boolean = new Compound((x, y) => x && y, List(f1, f2))
  boolean
}

def Or(f1: FilterCond, f2: FilterCond): FilterCond = {
  val boolean = new Compound((x, y) => x || y, List(f1, f2))
  boolean
}
def Equal(f1: FilterCond, f2: FilterCond): FilterCond = {
  val boolean = new Compound((x, y) => x == y, List(f1, f2))
  boolean
}

case class Any(fs: List[FilterCond]) extends FilterCond {
  override def eval(r: Row): Option[Boolean] = {
    if (fs.isEmpty) return Some(false)
    val boolean = fs.map { filter =>
      if (filter.eval(r).get) true
      else false
    }
    if (boolean.contains(true)) Some(true)
    else Some(false)
  }
}

case class All(fs: List[FilterCond]) extends FilterCond {
  override def eval(r: Row): Option[Boolean] = {
    if (fs.isEmpty) return Some(false)
    val boolean = fs.map { filter =>
      if (filter.eval(r).get) true
      else false
    }
    if (boolean.contains(false)) Some(false)
    else Some(true)
  }
}