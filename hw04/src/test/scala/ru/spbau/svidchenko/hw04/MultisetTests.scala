package ru.spbau.svidchenko.hw04

import scala.collection.immutable.Set
import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

class MultisetTests extends org.scalatest.FunSuite {

  test("add test") {
    val multiset: Multiset[Long] = new Multiset[Long]
    multiset += 1
    assert(multiset.contains(1))
    multiset += 2
    multiset += 2
    assert(multiset.contains(2))
  }

  test("intersect test") {
    val multiset1: Multiset[Long] = new Multiset[Long]
    val multiset2: Multiset[Long] = new Multiset[Long]
    multiset1 += 1
    multiset1 += 2
    multiset2 += 3
    multiset2 += 1
    val intersection = multiset1 & multiset2
    assert(intersection.contains(1))
    assert(!intersection.contains(2))
    assert(!intersection.contains(3))
  }

  test("union test") {
    val multiset1: Multiset[Long] = new Multiset[Long]
    val multiset2: Multiset[Long] = new Multiset[Long]
    multiset1 += 1
    multiset1 += 2
    multiset2 += 3
    multiset2 += 1
    val intersection = multiset1 | multiset2
    assert(intersection.contains(1))
    assert(intersection.contains(2))
    assert(intersection.contains(3))
  }

  test("iteration test") {
    val multiset: Multiset[Long] = new Multiset[Long]
    multiset += 1
    multiset += 2
    multiset += 3
    var count: Long = 0
    for (something <- multiset) {
      count += something
    }
    assertResult(6)(count)
  }

  test("find test") {
    val multiset: Multiset[Long] = new Multiset[Long]
    multiset += 1
    assertResult(Some(1))(multiset.find(1))
    assertResult(None)(multiset.find(2))
  }

  test("methods test") {
    val multiset: Multiset[Long] = new Multiset[Long]
    multiset += 1
    val multiset2: Multiset[Long] = multiset.map[Long](l => l + 1)
    assert(multiset2.contains(2))
    assert(!multiset2.contains(1))
    val multiset3: Multiset[Long] = multiset.flatMap((l: Long) => Multiset(l + 1, l + 2, l + 3))
    assert(multiset3.contains(2))
    assert(multiset3.contains(3))
    assert(multiset3.contains(4))
    assert(!multiset3.contains(1))
    assert(multiset3(2))
  }

  test("pattern-matching") {
    val multiset: Multiset[Long] = new Multiset[Long]
    multiset += 1
    multiset match {
      case Multiset(1) => //OK
      case _ => fail()
    }
    multiset += 2
    multiset match {
      case Multiset(2, 1) => //OK
      case _ => fail()
    }
    multiset += 1
    multiset match {
      case Multiset(2, 1, 1) => //OK
      case _ => fail()
    }
  }
}
