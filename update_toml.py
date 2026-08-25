import re

with open("gradle/libs.versions.toml", "r") as f:
    content = f.read()

versions = """
kotlinxDatetime = "0.6.1"
ktor = "3.0.0"
coil3 = "3.0.4"
lifecycleKmp = "2.8.2"
kotlinxSerialization = "1.7.3"
"""

libraries = """
kotlinx-datetime = { group = "org.jetbrains.kotlinx", name = "kotlinx-datetime", version.ref = "kotlinxDatetime" }
ktor-client-core = { group = "io.ktor", name = "ktor-client-core", version.ref = "ktor" }
ktor-client-okhttp = { group = "io.ktor", name = "ktor-client-okhttp", version.ref = "ktor" }
ktor-client-darwin = { group = "io.ktor", name = "ktor-client-darwin", version.ref = "ktor" }
ktor-client-content-negotiation = { group = "io.ktor", name = "ktor-client-content-negotiation", version.ref = "ktor" }
ktor-serialization-kotlinx-json = { group = "io.ktor", name = "ktor-serialization-kotlinx-json", version.ref = "ktor" }
coil3-compose = { group = "io.coil-kt.coil3", name = "coil-compose", version.ref = "coil3" }
coil3-network-ktor3 = { group = "io.coil-kt.coil3", name = "coil-network-ktor3", version.ref = "coil3" }
lifecycle-viewmodel-kmp = { group = "org.jetbrains.androidx.lifecycle", name = "lifecycle-viewmodel-compose", version.ref = "lifecycleKmp" }
kotlinx-serialization-json = { group = "org.jetbrains.kotlinx", name = "kotlinx-serialization-json", version.ref = "kotlinxSerialization" }
"""

plugins = """
kotlin-serialization = { id = "org.jetbrains.kotlin.plugin.serialization", version.ref = "kotlin" }
"""

content = content.replace("[versions]", "[versions]\n" + versions.strip())
content = content.replace("[libraries]", "[libraries]\n" + libraries.strip())
content = content.replace("[plugins]", "[plugins]\n" + plugins.strip())

with open("gradle/libs.versions.toml", "w") as f:
    f.write(content)
