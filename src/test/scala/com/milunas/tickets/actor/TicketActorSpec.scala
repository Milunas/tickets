package com.milunas.tickets.actor

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestKit}
import com.milunas.tickets.SystemCleanAll
import com.milunas.tickets.ticket.actor.TicketActor
import com.milunas.tickets.ticket.domain.{Ticket, Tickets}
import com.milunas.tickets.ticket.message.{AddTickets, BuyTickets}
import org.scalatest.{MustMatchers, WordSpecLike}

class TicketActorSpec extends TestKit(ActorSystem("testTickets"))
  with WordSpecLike
  with MustMatchers
  with ImplicitSender
  with SystemCleanAll {

  "The TicketActor" must {

    "Sell tickets until they are sold out" in {
      def mkTickets = (1 to 10).map(i => Ticket(i)).toVector

      val movie = "Rocky"
      val ticketingActor = system.actorOf(TicketActor.props(movie))

      ticketingActor ! AddTickets(mkTickets)
      ticketingActor ! BuyTickets(1)

      expectMsg(Tickets(movie, Vector(Ticket(1))))

      val nrs = 2 to 10
      nrs.foreach(_ => ticketingActor ! BuyTickets(1))

      val tickets = receiveN(9)
      tickets.zip(nrs).foreach { case (Tickets(_, Vector(Ticket(id))), ix) => id must be(ix) }

      ticketingActor ! BuyTickets(1)
      expectMsg(Tickets(movie))
    }
  }
}
