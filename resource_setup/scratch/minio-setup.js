const Minio = require('minio');
const fs = require('fs');

const minioClient = new Minio.Client({
  endPoint: 'localhost',
  port: 9000,
  useSSL: false,
  accessKey: 'minioadmin',
  secretKey: 'minioadmin'
});

const bucketName = 'nexus-media';
const imagePath = '/home/aadarsh/Documents/NexusEngine/image1.jpeg';

async function setup() {
  try {
    const exists = await minioClient.bucketExists(bucketName);
    if (!exists) {
      await minioClient.makeBucket(bucketName, 'us-east-1');
      console.log(`Bucket ${bucketName} created successfully.`);
    } else {
      console.log(`Bucket ${bucketName} already exists.`);
    }

    const policy = {
      Version: '2012-10-17',
      Statement: [
        {
          Effect: 'Allow',
          Principal: { AWS: ['*'] },
          Action: ['s3:GetObject'],
          Resource: [`arn:aws:s3:::${bucketName}/public/*`]
        }
      ]
    };
    await minioClient.setBucketPolicy(bucketName, JSON.stringify(policy));
    console.log(`Bucket policy set to public read for /public/*`);

    if (fs.existsSync(imagePath)) {
      const uploads = [
        'public/marketing/banners/electronics-sale.webp',
        'public/marketing/banners/fashion-week.webp',
        'public/catalog/brands/1-apple/logo.webp',
        'public/catalog/brands/2-samsung/logo.webp',
        'public/catalog/products/1-iphone-17/main.webp',
        'public/catalog/products/2-galaxy-s25/main.webp',
        'public/catalog/skus/APP-IPH-1-BLK-128/main.webp',
        'public/catalog/skus/SAM-GAL-2-PHA-256/main.webp',
        'public/users/avatars/default/avatar.webp'
      ];
      
      for (const dest of uploads) {
        await minioClient.fPutObject(bucketName, dest, imagePath, {
          'Content-Type': 'image/jpeg'
        });
        console.log(`Uploaded to ${dest}`);
      }
    } else {
      console.log(`Image not found at ${imagePath}`);
    }
  } catch (err) {
    console.error(err);
  }
}

setup();
