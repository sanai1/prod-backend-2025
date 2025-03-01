package ru.kotleteri.utils

// application
val SECRET = System.getenv("SECRET")?.toString() ?: "081499836611D227B0B5F608C2B62E76FDE6946A4994E7BF338549BB20D19B40"
val SERVER_PORT = System.getenv("SERVER_PORT")?.toInt() ?: 8080

//postgres
val POSTGRES_URL = System.getenv("POSTGRES_URL").toString()
val POSTGRES_PASSWORD = System.getenv("POSTGRES_PASSWORD").toString()
val POSTGRES_USER = System.getenv("POSTGRES_USER").toString()

// redis
val REDIS_HOST = System.getenv("REDIS_HOST").toString()
val REDIS_PORT = System.getenv("REDIS_PORT")?.toInt() ?: 6379