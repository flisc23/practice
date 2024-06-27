package testing

    val shareNameMapping = mapOf(
        "CENTICODE" to "DMS_CENTICODE$",
        "CENTITECH" to "DMS_CENTICODE$",
        "CONISTICS" to "DMS_CONISTICS$",
        "HOLDING"   to "DMS_HOLDING$",
        "INTACTON"  to "DMS_INTACTON$",
        "POSITAL"   to "DMS_POSITAL$",
        "VITECTOR"  to "DMS_VITECTOR$",
        "STATISTICALDATA" to "StatisticalData$",
        "ARCHIVE" to "archive$"
    )
    val driveLetterMapping = mapOf(
        "G" to "HOLDING$",
        "I" to "INTACTON$",
        "K" to "CENTI$",
        "N" to "NOBACKUP$",
        "O" to "POSITAL$",
        "R" to "FRABA_RO$",
        "V" to "VITECTOR$",
    )
    val DMS_PATH_REGEX = Regex("\\\\fraba.local\\\\DMS\\\\(.*?)\\\\(.*)")
    val DRIVE_BY_LETTER_REGEX = Regex("([A-Z]):\\\\(.*)")



    fun getShareNameAndPathFromDrivePath(path: String): Pair<String, String> {
        if (DMS_PATH_REGEX.matches(path)) {
            val matchResult = DMS_PATH_REGEX.find(path) ?: throw IllegalArgumentException("Path format not recognized")
            val (shareName, remainingPath) = matchResult.destructured

            val mappedShareName = shareNameMapping[shareName.uppercase()]
                ?: throw IllegalArgumentException("Share name not recognized: $shareName")

            return Pair(mappedShareName, "\\$remainingPath")
        } else if (DRIVE_BY_LETTER_REGEX.matches(path)) {
            val matchResult =
                DRIVE_BY_LETTER_REGEX.find(path) ?: throw IllegalArgumentException("Path format not recognized")
            val (driveLetter, remainingPath) = matchResult.destructured

            val shareName = driveLetterMapping[driveLetter]
                ?: throw IllegalArgumentException("Drive letter not recognized: $driveLetter")

            return Pair(shareName, "\\$remainingPath")
        } else {
            throw IllegalArgumentException("Don't have access to the current path: " + path)
        }
    }

fun main() {
    getShareNameAndPathFromDrivePath("\\fraba.local\\DMS\\POSITAL\\Project\\POS_AS-17-D-180")
}