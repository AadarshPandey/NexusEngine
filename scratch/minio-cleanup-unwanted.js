const Minio = require('minio');

const minioClient = new Minio.Client({
  endPoint: 'localhost',
  port: 9000,
  useSSL: false,
  accessKey: 'minioadmin',
  secretKey: 'minioadmin'
});

const bucketName = 'nexus-media';

async function cleanup() {
  try {
    const objectsList = [];
    const stream = minioClient.listObjectsV2(bucketName, '', true);
    
    stream.on('data', function(obj) {
      if (obj.name.includes('mobile') || obj.name.includes('dark')) {
        objectsList.push(obj.name);
      }
    });
    
    stream.on('end', async function() {
      if (objectsList.length > 0) {
        await minioClient.removeObjects(bucketName, objectsList);
        console.log('Removed objects:');
        objectsList.forEach(obj => console.log(' - ' + obj));
      } else {
        console.log('No unwanted mobile or dark images found.');
      }
    });
    
    stream.on('error', function(err) {
      console.error(err);
    });
  } catch (err) {
    console.error(err);
  }
}

cleanup();
