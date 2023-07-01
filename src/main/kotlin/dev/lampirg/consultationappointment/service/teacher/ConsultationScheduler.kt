package dev.lampirg.consultationappointment.service.teacher

import dev.lampirg.consultationappointment.data.schedule.PatternSchedule
import dev.lampirg.consultationappointment.data.schedule.PatternScheduleRepository
import dev.lampirg.consultationappointment.data.teacher.DatePeriod
import dev.lampirg.consultationappointment.data.teacher.Teacher
import dev.lampirg.consultationappointment.web.fetch.ConsultationPattern
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.TaskScheduler
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.*
import java.util.*
import java.util.concurrent.ScheduledFuture
import java.util.function.Consumer

@Service
class ConsultationScheduler @Autowired constructor(
    patternScheduleRepository: PatternScheduleRepository,
    consultationMaker: ConsultationMaker,
    scheduler: TaskScheduler
) {
    private class Pair(var start: ScheduledFuture<*>) {
        var end: ScheduledFuture<*>? = null

        val all: List<ScheduledFuture<*>?>
            get() = listOf(start, end)
    }

    private val patternScheduleRepository: PatternScheduleRepository
    private val consultationMaker: ConsultationMaker
    private val scheduler: TaskScheduler
    private val patternsMap: MutableMap<Int?, MutableList<ConsultationPattern>> =
        HashMap<Int?, MutableList<ConsultationPattern>>()
    private val schedulesMap: MutableMap<ConsultationPattern, Pair> = HashMap<ConsultationPattern, Pair>()
    private val patternScheduleMap: MutableMap<ConsultationPattern, PatternSchedule> =
        HashMap<ConsultationPattern, PatternSchedule>()

    init {
        this.patternScheduleRepository = patternScheduleRepository
        this.consultationMaker = consultationMaker
        this.scheduler = scheduler
    }

    @Transactional
    fun savePattern(pattern: ConsultationPattern) {
        val schedule = PatternSchedule(pattern)
        patternScheduleRepository.save(schedule)
        patternScheduleMap[pattern] = schedule
    }

    fun addPattern(pattern: ConsultationPattern) {
        while (pattern.consultationInfo.date.isBefore(LocalDate.now()))
            pattern.consultationInfo.date = pattern.consultationInfo.date.plusDays(7)
        patternsMap.putIfAbsent(pattern.teacher!!.id, ArrayList<ConsultationPattern>())
        val instant = LocalDateTime.of(
            pattern.consultationInfo.date,
            pattern.consultationInfo.endTime
        ).atZone(ZoneId.of("Europe/Moscow")).toInstant()
        val future = scheduler.scheduleAtFixedRate({
            pattern.consultationInfo.date = pattern.consultationInfo.date.plusDays(7)
            val datePeriod = DatePeriod(
                pattern.consultationInfo.classroom,
                LocalDateTime.of(
                    pattern.consultationInfo.date,
                    pattern.consultationInfo.startTime
                ),
                LocalDateTime.of(
                    pattern.consultationInfo.date,
                    pattern.consultationInfo.endTime
                )
            )
            consultationMaker.createConsultation(pattern.teacher!!, datePeriod)
        }, instant, Duration.ofDays(7))
        patternsMap[pattern.teacher!!.id]!!.add(pattern)
        schedulesMap[pattern] = Pair(future)
        scheduleRemove(pattern)
    }

    fun getTeacherPatterns(teacher: Teacher): Optional<List<ConsultationPattern>> {
        return Optional.ofNullable<List<ConsultationPattern>>(patternsMap[teacher.id])
    }

    @Transactional
    fun removePattern(pattern: ConsultationPattern) {
        schedulesMap[pattern]!!.all.forEach(Consumer { future: ScheduledFuture<*>? ->
            future!!.cancel(
                false
            )
        })
        schedulesMap.remove(pattern)
        patternsMap[pattern.teacher!!.id]!!.remove(pattern)
        patternScheduleRepository.delete(patternScheduleMap[pattern]!!)
        patternScheduleMap.remove(pattern)
    }

    private fun scheduleRemove(pattern: ConsultationPattern) {
        val instant = LocalDateTime.of(pattern.until, LocalTime.MAX)
            .atZone(ZoneId.of("Europe/Moscow")).toInstant()
        scheduler.schedule({ removePattern(pattern) }, instant)
    }
}