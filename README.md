# Mini SQL Query Language in Scala

Implementation of a **functional SQL-inspired query language** in Scala, built around immutable `Table` and `Database` data structures. Supports row-level filtering with a composable filter condition algebra, table operations (insert, delete, sort, select, join), and predefined SQL-like queries. Developed as a Functional Programming course assignment.

## Overview

Tables are represented as `List[Map[String, String]]` (a list of rows, each row being a column→value map). A `Database` is a list of named `Table` objects. All operations return new immutable instances — no mutation is performed.

## Data Model

```scala
type Row     = Map[String, String]   // column name → value
type Tabular = List[Row]

case class Table(tableName: String, tableData: Tabular)
case class Database(tables: List[Table])
```

## Table Operations

| Method | Description |
|---|---|
| `fromCSV(csv)` | Parses a CSV string into a `Table` |
| `toString` | Serializes a `Table` back to CSV |
| `insert(row)` | Inserts a row if not already present |
| `delete(row)` | Removes all rows equal to the given row |
| `sort(column, ascending)` | Sorts rows by a column; ascending is optional (default `true`) |
| `select(columns)` | Returns a new table with only the specified columns |
| `filter(f)` | Keeps only rows satisfying a `FilterCond` |
| `update(f, updates)` | Updates all rows matching `f` with new column values |
| `apply(i)` | Index-based row access: `table(i)` |
| `cartesianProduct(other)` | Cartesian product of two tables *(not implemented)* |
| `join(other)(col1, col2)` | Full outer join on specified columns *(not implemented)* |

## Filter Condition Algebra (`FilterCond`)

Filters are composable expressions that evaluate to `Option[Boolean]` per row:

```scala
trait FilterCond { def eval(r: Row): Option[Boolean] }
```

| Constructor | Description |
|---|---|
| `Field(colName, predicate)` | True if `predicate(row(colName))` is satisfied |
| `Compound(op, conditions)` | Folds a list of conditions with a binary boolean operator |
| `Not(f)` | Logical negation |
| `And(f1, f2)` | Logical conjunction |
| `Or(f1, f2)` | Logical disjunction |
| `Equal(f1, f2)` | Equality of two filter results |
| `Any(fs)` | True if at least one condition in the list holds |
| `All(fs)` | True if all conditions in the list hold |

Operator syntax is supported via `FilterCond` trait extensions:

```scala
Field("age", _.toInt > 18) && Field("city", _ == "Bucharest")
!Field("status", _ == "inactive") || Field("role", _ == "admin")
```

## Database Operations

| Method | Description |
|---|---|
| `insert(tableName)` | Adds a new empty table if the name doesn't already exist |
| `update(tableName, newTable)` | Replaces an existing table by name |
| `delete(tableName)` | Removes a table by name |
| `selectTables(names)` | Returns an `Option[Database]` with only the named tables |
| `apply(i)` | Index-based table access: `db(i)` |

## Predefined Queries (`Queries.scala`)

```scala
// Customers older than ageLimit living in one of the given cities, sorted by id
query_1(db, ageLimit, cities): Option[Table]

// Orders after a date, excluding a specific employee, returning order_id + cost sorted by cost desc
query_2(db, date, employeeID): Option[Table]

// Orders with cost above minCost, returning order_id + employee_id + cost sorted by employee_id
query_3(db, minCost): Option[Table]
```

All queries are written as one-liners chaining `selectTables`, `filter`, `select`, and `sort`.

## Building & Testing

```bash
sbt compile
sbt test
```

Tests use [ScalaTest](https://www.scalatest.org/) and [Scalactic](https://www.scalactic.org/).

## Notes

- `cartesianProduct` and `join` are declared but not implemented (`???`)
- `query_3` does not return `CustomerName` (cross-table join was not implemented)
- All data is immutable; every operation returns a new object
# Mini-SQL-Query-Language-Scala
