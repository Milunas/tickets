package com.milunas.tickets.ticket.actor

import akka.actor.Actor
import com.milunas.tickets.cinema.domain.Movie
import com.milunas.tickets.ticket.domain.{Ticket, Tickets}
import com.milunas.tickets.ticket.message.{AddTickets, BuyTickets, GetMovie}

class TicketActor (movie: String) extends Actor {

  var tickets = Vector.empty[Ticket]

  override def receive: Receive = {

    case AddTickets(newTickets) => tickets ++= newTickets

    case BuyTickets(numberOfTickets) => buyTickets(numberOfTickets)

    case GetMovie => sender() ! Some(Movie(movie, tickets.size))
  }

  private def buyTickets(numberOfTickets: Int): Unit = {
    val entries = tickets.take(numberOfTickets)
    if (entries.size >= numberOfTickets) {
      sender() ! Tickets(movie, entries)
      tickets = tickets.drop(numberOfTickets)
    } else {
      sender() ! Tickets(movie)
    }
  }

}
