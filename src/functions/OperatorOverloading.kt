package functions

/**
 * Created by lwu on 6/11/17.
 */
data class Time(val hours: Int, val mins: Int) {
    operator fun plus(time: Time): Time {
        val minutes = this.mins + time.mins
        val hoursInMinutes = minutes / 60
        val remainingMintues = minutes % 60
        val hours = this.hours + time.hours + hoursInMinutes
        return Time(hours, remainingMintues)
    }
}

fun main(args: Array<String>) {
    val newTime = Time(10, 40) + Time(3, 20)
    println(newTime)
}