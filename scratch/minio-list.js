const Minio = require('minio');

const minioClient = new Minio.Client({
  endPoint: 'localhost',
  port: 9000,
  useSSL: false,
  accessKey: 'minioadmin',
  secretKey: 'minioadmin'
});

const bucketName = 'nexus-media';

async function list() {
  try {
    const stream = minioClient.listObjectsV2(bucketName, '', true);
    stream.on('data', function(obj) {
      console.log(obj.name);
    });
    stream.on('error', function(err) {
      console.error(err);
    });
  } catch (err) {
    console.error(err);
  }
}

list();
