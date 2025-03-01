#!/bin/sh

# Wait for MinIO to fully start
sleep 10

# Set up MinIO client
mc alias set myminio http://minio:9000 $MINIO_ROOT_USER $MINIO_ROOT_PASSWORD

# Create the bucket if it doesn't exist
mc mb myminio/$BUCKET || true
