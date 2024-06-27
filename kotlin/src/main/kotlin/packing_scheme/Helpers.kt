package packing_scheme

class Helpers {

}
fun main() {
    val target = 11
    val numbers = setOf(3, 5, 10)
    val tolerance = 2
    val result = findClosestSum(target, numbers, tolerance)
    println("Best sum: ${result.first}")
    println("Numbers used: ${result.second}")
}

fun findClosestSum(target: Int, numbers: Set<Int>, tolerance: Int): Pair<Int, List<Int>> {
    val memo = mutableMapOf<Pair<Int, Int>, Pair<Int, List<Int>>?>()
    val result = findClosestSumHelper(target, numbers.toList(), 0, memo, tolerance)
    return result ?: Pair(0, emptyList())
}

fun findClosestSumHelper(
    target: Int,
    numbers: List<Int>,
    index: Int,
    memo: MutableMap<Pair<Int, Int>, Pair<Int, List<Int>>?>,
    tolerance: Int
): Pair<Int, List<Int>>? {
    if (index >= numbers.size) return null
//    if (target in (target - tolerance)..(target + tolerance)) return Pair(target, emptyList())

    val key = Pair(target, index)
    if (memo.containsKey(key)) return memo[key]

    val includeResult = findClosestSumHelper(target - numbers[index], numbers, index + 1, memo, tolerance)
    val excludeResult = findClosestSumHelper(target, numbers, index + 1, memo, tolerance)

    val bestSum = when {
        includeResult != null && (includeResult.first + numbers[index] in (target - tolerance)..(target + tolerance)) ->
            Pair(includeResult.first + numbers[index], includeResult.second + numbers[index])
        excludeResult != null && (excludeResult.first in (target - tolerance)..(target + tolerance)) ->
            excludeResult
        includeResult != null ->
            Pair(includeResult.first + numbers[index], includeResult.second + numbers[index])
        else -> excludeResult
    }

    memo[key] = bestSum
    return bestSum
}
