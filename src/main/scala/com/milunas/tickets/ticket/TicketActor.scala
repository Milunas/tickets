package com.milunas.tickets.ticket

import akka.actor.Props
import com.milunas.tickets.ticket.actor.TicketActor

object TicketActor {
  def props(movie: String) = Props(new TicketActor(movie))
}
