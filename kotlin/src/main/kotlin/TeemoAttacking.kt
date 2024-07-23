@OptIn(ExperimentalStdlibApi::class)
fun findPoisonedDuration(timeSeries: IntArray, duration: Int): Int {
    /**
     * AFL 7/22/24 TODO : correct version but slow
    val seconds: MutableSet<Int> = mutableSetOf()
    timeSeries.forEach {
    seconds.addAll(it..it + duration - 1)
    }
    total = seconds.size
     **/
    var total = (timeSeries[timeSeries.size - 1]..timeSeries[timeSeries.size - 1] + duration - 1).count()
    for (i in 0..<(timeSeries.size - 1)) {
        val currentRange = timeSeries[i]..timeSeries[i] + duration - 1
        if (i < timeSeries.size - 1 && timeSeries[i + 1] in currentRange) {
            total += (currentRange.first..timeSeries[i + 1]).count()-1
            continue
        } else {
            total += currentRange.count()
        }
    }

    return total
}
fun wordSubsets(words1: Array<String>, words2: Array<String>): List<String> {
    var res: MutableList<String> = mutableListOf()
    words1.forEach { word ->
        run {
            if (words2.all { word.contains(it) }) {
                res.add(word)
            }
        }
    }
    return res
}

fun main() {
//
//    println(findPoisonedDuration(intArrayOf(1, 4), 2))
//    println(findPoisonedDuration(intArrayOf(1, 2), 2))
//    println(findPoisonedDuration(intArrayOf(1, 2, 6), 2))

//https://leetcode.com/problems/word-subsets/
    println(wordSubsets(arrayOf("amazon","apple","facebook","google","leetcode"), arrayOf("e","o")))
    println(wordSubsets(arrayOf("amazon","apple","facebook","google","leetcode"), arrayOf("l","e")))
    println(wordSubsets(arrayOf("amazon","apple","facebook","google","leetcode"), arrayOf("lo","eo")))
}
