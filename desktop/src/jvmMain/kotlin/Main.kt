import nl.vanparerensoftwaredevelopment.saltedpassmanager.common.desktopMain
import utils.withJUniqueLock

private const val UNIQUE_APPLICATION_ID = "nl.vanparerensoftwaredevelopment.saltthepassmanager"

fun main(vararg args: String) {
    withJUniqueLock(
        id = UNIQUE_APPLICATION_ID
    ) { openMessages ->
        desktopMain(args.toList(), openMessages)
    }
}