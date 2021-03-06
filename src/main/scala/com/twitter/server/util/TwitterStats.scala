package com.twitter.server.util

import com.twitter.finagle.stats.StatsReceiver
import com.twitter.concurrent.Scheduler

object TwitterStats {

  def register(statsReceiver: StatsReceiver): Unit = {
    val scheduler = statsReceiver.scope("scheduler")

    // Productivity is a very rough estimate of time spent not
    // blocking. It measures the proportion of a thread's execution
    // time spent on the CPU. This cannot take into account issues
    // like CPU scheduling effects.
    scheduler.provideGauge("productivity") {
      val cpu = Scheduler.cpuTime
      val wall = Scheduler.wallTime
      if (wall.toFloat <= 0F) 0F
      else cpu.toFloat / wall.toFloat
    }

    scheduler.provideGauge("dispatches") {
      Scheduler.numDispatches.toFloat
    }
  }
}
