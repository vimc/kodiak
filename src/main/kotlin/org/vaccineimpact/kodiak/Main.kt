package org.vaccineimpact.kodiak

fun main(args: Array<String>) {

    val config = Config()
    println("Available targets: ${config.targets.map{ it.id }}")

    val allowedModes = arrayOf("backup", "restore")

    if (args.isEmpty() || !allowedModes.contains(args[0])) {
        println("Please provide a command-line argument of either 'backup' or 'restore'")
        return
    }

    println("TODO: ${args[0]}")
}