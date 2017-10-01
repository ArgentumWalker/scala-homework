package ru.spbau.svidchenko.hw04

import scala.collection.generic.CanBuildFrom
import scala.collection.mutable

object Multiset {
  def apply[V](seq: V*): Multiset[V] = {
    val result = new Multiset[V]
    for (value: V <- seq) {
      result += value
    }
    result
  }

  def unapplySeq[V](multiset: Multiset[V]): Option[Seq[V]] = {
    Some(multiset.iterator.toSeq)
  }
}

class Multiset[V] extends mutable.Iterable[V] {
  private val value2values: mutable.HashMap[V, mutable.ArrayBuffer[V]] = new mutable.HashMap[V, mutable.ArrayBuffer[V]]

  def apply(value: V): Boolean = contains(value)

  def map[B](f: (V) => B): Multiset[B] = {
    val multiset = new Multiset[B]
    for (v <- this) {
      multiset += f(v)
    }
    multiset
  }

  def flatMap[B](f: (V) => Multiset[B]): Multiset[B] = {
    var result: Multiset[B] = new Multiset[B]
    for (v <- this) {
      result = result | f(v)
    }
    result
  }

  def &(multiset: Multiset[V]): Multiset[V] = {
    val result: Multiset[V] = new Multiset[V]
    for (vav <- value2values) {
      if (multiset.value2values.contains(vav._1)) {
        val multisetValues = multiset.value2values(vav._1)
        if (multisetValues.size < vav._2.size) {
          result.value2values += ((vav._1, vav._2.clone()))
        } else {
          result.value2values += ((vav._1, multisetValues.clone()))
        }
      }
    }
    result
  }

  def |(multiset: Multiset[V]): Multiset[V] = {
    val result: Multiset[V] = new Multiset[V]
    for (vav <- value2values) {
      result.value2values.getOrElseUpdate(vav._1, new mutable.ArrayBuffer[V]) ++= vav._2
    }
    for (vav <- multiset.value2values) {
      result.value2values.getOrElseUpdate(vav._1, new mutable.ArrayBuffer[V]) ++= vav._2
    }
    result
  }

  def +=(elem: V): Multiset.this.type = {
    value2values.getOrElseUpdate(elem, new mutable.ArrayBuffer[V]) += elem
    this
  }

  def find(elem: V): Option[V] = {
    if (value2values.contains(elem)) {
      return Some(value2values(elem)(0))
    }
    None
  }

  def contains(elem: V): Boolean = {
    value2values.contains(elem)
  }

  override def iterator: Iterator[V] = {
    new MultisetIterator
  }

 class MultisetIterator extends Iterator[V] {
   private val mapIterator: Iterator[(V, mutable.ArrayBuffer[V])] = value2values.iterator
   private var arrayIterator: Iterator[V] = null

   override def hasNext: Boolean = {
     mapIterator.hasNext || (arrayIterator != null && arrayIterator.hasNext)
   }

   override def next(): V = {
     if (arrayIterator != null && arrayIterator.hasNext) {
       return arrayIterator.next()
     }
     if (mapIterator.hasNext) {
       arrayIterator = mapIterator.next()._2.iterator
       return arrayIterator.next()
     }
     throw new IllegalStateException()
   }
 }
}
