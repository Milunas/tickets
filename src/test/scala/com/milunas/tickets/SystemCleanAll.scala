package com.milunas.tickets

import akka.testkit.TestKit
import org.scalatest.{BeforeAndAfterAll, Suite}

trait SystemCleanAll extends BeforeAndAfterAll {
  this: TestKit with Suite =>
  override protected def afterAll(): Unit = {
    super.afterAll()
    system.terminate()
  }
}
