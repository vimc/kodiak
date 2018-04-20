package org.vaccineimpact.kodiak

fun main(args: Array<String>) {
    val allowedModes = arrayOf("backup", "restore")

    if (args.isEmpty() || !allowedModes.contains(args[0])) {
        println("Please provide a command-line argument of either 'backup' or 'restore'")
        return
    }

    println("TODO: ${args[0]}")
}