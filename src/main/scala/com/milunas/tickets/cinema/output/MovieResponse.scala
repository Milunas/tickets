package com.milunas.tickets.cinema.output

import com.milunas.tickets.cinema.domain.Movie

object MovieResponse {
  sealed trait MovieResponse
  case class MovieCreated(movie: Movie) extends MovieResponse
}