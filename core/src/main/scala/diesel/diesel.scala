package diesel

import scala.meta._

/**
  * Annotation used for expanding a trait parameterised with a type that takes
  * one type application into a DSL.
  *
  * If you wish to put concrete methods into the resulting companion object, write them
  * in a separate concrete object. The Tagless-Final expansions will be prepended to the
  * body of the companion object that you write. If you don't write a companion object,
  * one will be created.
  *
  * Example:
  *
  * {{{
  * scala> import diesel._
  *
  * // Wrapper is only for the sake of sbt-doctest and is unnecessary in real-life usage
  * scala> object Wrapper {
  *      | // Declare our DSL
  *      | @diesel
  *      | trait Maths[G[_]] {
  *      |   def int(i: Int): G[Int]
  *      |   def add(l: G[Int], r: G[Int]): G[Int]
  *      | } }
  * scala> import Wrapper._
  *
  * // Write an interpreter
  * scala> type Id[A] = A
  * scala> val interpreter = new Maths.Algebra[Id] {
  *      |   def int(i: Int)                 = i
  *      |   def add(l: Id[Int], r: Id[Int]) = l + r
  *      | }
  *
  * // Now we can use our DSL
  * scala> import Maths._
  *
  * scala> int(3)(interpreter)
  * res0: Int = 3
  *
  * scala> add(int(3), int(10))(interpreter)
  * res1: Int = 13
  * }}}
  */
class diesel extends scala.annotation.StaticAnnotation {

  inline def apply(defn: Any): Any = meta {
    val r = diesel.internal.MacroImpl.expand(defn)
//    println(r.syntax)
    r
  }

}
