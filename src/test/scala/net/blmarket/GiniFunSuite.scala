package net.blmarket

import org.scalatest.FunSuite
import net.blmarket.rforest.Gini

/**
 * Tests Gini class basic features
 * Created by blmarket on 2014. 2. 15..
 */
class GiniFunSuite extends FunSuite {
  test("nodeScore works") {
    assert(Gini.nodeScore(5, 5) > 0.49)
    assert(Gini.nodeScore(0, 5) < 0.01)
    assert(Gini.nodeScore(3, 5) > 0.45)
  }
}
