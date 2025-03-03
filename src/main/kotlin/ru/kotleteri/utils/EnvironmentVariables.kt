package ru.kotleteri.utils

// application
val SECRET = System.getenv("SECRET")?.toString() ?: "081499836611D227B0B5F608C2B62E76FDE6946A4994E7BF338549BB20D19B40"
val SERVER_PORT = System.getenv("SERVER_PORT")?.toInt() ?: 8080

//postgres
val POSTGRES_URL = System.getenv("POSTGRES_URL") ?: "jdbc:postgresql://localhost:5432/postgres"
val POSTGRES_PASSWORD = System.getenv("POSTGRES_PASSWORD") ?: "postgres"
val POSTGRES_USER = System.getenv("POSTGRES_USER") ?: "postgres"

// redis
val REDIS_HOST = System.getenv("REDIS_HOST") ?: "localhost"
val REDIS_PORT = System.getenv("REDIS_PORT")?.toInt() ?: 6379

//s3
val S3_URL = System.getenv("MINIO_ENDPOINT") ?: "http://localhost:9000"
val S3_ACCESS_KEY = System.getenv("MINIO_ROOT_USER") ?: "minioadmin"
val S3_SECRET_KEY = System.getenv("MINIO_ROOT_PASSWORD") ?: "minioadmin"
val S3_BUCKET = System.getenv("BUCKET") ?: "prod"