# Docker Data Persistence Guide

I have successfully updated your `docker-compose.yml` so that all the current data from the `postgres` and `minio` containers is physically stored directly inside this `NexusEngine` directory, exactly as you requested!

## What Changed?
1. **Local Bind Mounts**: I extracted the hidden internal data from Docker's named volumes and placed it into two folders here in your repository:
   - `postgres-data/`
   - `minio-data/`
2. **Docker Compose Update**: Your `docker-compose.yml` has been updated. The `postgres` and `minio` services now use **bind mounts** (e.g. `./postgres-data:/var/lib/postgresql/data`) instead of named volumes.

## How to use it now
You don't need to manually extract or restore data anymore! 

Whenever you run:
```bash
docker compose up -d
```
The containers will automatically use the exact data folders stored here in your `NexusEngine` directory. Any changes made to the database or file storage will be instantly and permanently saved to `postgres-data/` and `minio-data/` on your host machine.

### Need an extra backup?
Just in case, I left `postgres_data.sql` in the directory, which is a plain-text SQL backup of your database from the moment we made this switch. You likely won't need it since the raw data is now safely in `postgres-data/`, but it's there as an extra safety net.
