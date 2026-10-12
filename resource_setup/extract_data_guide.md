# Safe Data Migration: From Local Folders Back to Docker Volumes

If you want to remove the local `resource_setup` folder and let Docker manage the data internally again (without any data loss), you MUST migrate the data into **Docker Named Volumes**. 

**DO NOT** bake your database directly into a Docker image. Databases are meant to change, and if you bake it into an image, any new data you add later will be written to temporary anonymous volumes which are easily wiped out by accident, leading to catastrophic data loss.

Follow these steps exactly to migrate your data safely back to Docker-managed volumes with 100% certainty of no data loss.

---

## Step 1: Stop Your Containers
You must stop the containers so no new data is written while we copy it.
```bash
docker compose down
```

## Step 2: Create the Docker Named Volumes
Create the internal Docker volumes that will hold your data permanently.
```bash
docker volume create nexus_postgres_data
docker volume create nexus_minio_data
```

## Step 3: Copy Data into the Docker Volumes
We will run a temporary Ubuntu container to strictly copy the files from your local folders into the new Docker internal volumes, preserving all permissions and files.

**Copy Postgres Data:**
```bash
docker run --rm \
  -v $(pwd)/resource_setup/postgres-data:/source \
  -v nexus_postgres_data:/target \
  ubuntu cp -a /source/. /target/
```

**Copy MinIO Data:**
```bash
docker run --rm \
  -v $(pwd)/resource_setup/minio-data:/source \
  -v nexus_minio_data:/target \
  ubuntu cp -a /source/. /target/
```

## Step 4: Update `docker-compose.yml`
Now, tell your `docker-compose.yml` to use these internal volumes instead of the local folder.

**1. At the bottom of `docker-compose.yml`, update the `volumes` section to register the new volumes:**
```yaml
volumes:
  nexus_postgres_data:
    external: true
  nexus_minio_data:
    external: true
  redis-data:
# ... keep the rest ...
```

**2. Update the `postgres` service:**
Change the `volumes` section from the local path to the named volume:
```yaml
  postgres:
    # ...
    volumes:
      - nexus_postgres_data:/var/lib/postgresql/data
```

**3. Update the `minio` service:**
Change the `volumes` section from the local path to the named volume:
```yaml
  minio:
    # ...
    volumes:
      - nexus_minio_data:/data
```

## Step 5: Start the Containers and Verify
Start your application:
```bash
docker compose up -d
```
Log into your app and verify that **all your database records and MinIO files are still there.** 

## Step 6: Safe Cleanup
**ONLY AFTER** you have fully verified that the app is working perfectly and the data is all present, you can safely delete the local folders:
```bash
sudo rm -rf ./resource_setup/postgres-data
sudo rm -rf ./resource_setup/minio-data
rm ./resource_setup/postgres_latest_backup.sql
```
