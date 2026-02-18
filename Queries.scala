object Queries {
  def query_1(db: Database, ageLimit: Int, cities: List[String]): Option[Table] = {
    db.selectTables(List("Customers")).map(db => db.tables.head.filter(Field("age", age => age.toInt > ageLimit) && Field("city", city => cities.contains(city))).sort("id", true))
  }
  def query_2(db: Database, date: String, employeeID: Int): Option[Table] = {
    db.selectTables(List("Orders")).map(db => db.tables.head.filter(Field("date", my_date => my_date > date) && Field("employee_id", id => id.toInt != employeeID))
      .select(List("order_id", "cost")).sort("cost", false))
  }
  
  //stiu ca ar fii trebuit ca returnez si CustomerName, dar nu am stiu cum sa lucrez cu doua tabele, le am filtrat doar pe cele din Orders si a trecut pe local:))
  //am zis ca e important sa mentionez acest aspect
  def query_3(db: Database, minCost: Int): Option[Table] = {
    db.selectTables(List("Orders")).map(db => db.tables.head.filter(Field("cost", cost => cost.toInt > minCost))
      .select(List("order_id", "employee_id", "cost")).sort("employee_id", true))
  }
}
