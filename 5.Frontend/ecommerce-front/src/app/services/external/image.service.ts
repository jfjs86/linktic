import { Injectable } from '@angular/core';
import * as AWS from 'aws-sdk';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ImageService {

  private bucketName = 'test-images-ecommerce'; // Reemplaza con el nombre de tu bucket
  private s3: AWS.S3;

  constructor() { 
    AWS.config.update({
      region: 'us-east-1', 
      credentials: new AWS.Credentials(environment.accessImageId, environment.accessImagekey)
    });

    this.s3 = new AWS.S3();
  }

  getImageUrl(key: string): string {
    const params = {
      Bucket: this.bucketName,
      Key: key,
      Expires: 60
    };

    return this.s3.getSignedUrl('getObject', params);
  }

  getPresignedUrl(key: string |undefined): Promise<string> {
    const params = {
      Bucket: this.bucketName,
      Key: key,
      Expires: 3600
    };

    return new Promise((resolve, reject) => {
      this.s3.getSignedUrl('getObject', params, (err, url) => {
        if (err) {
          console.error('Error al obtener la URL firmada:', err);
          reject(err);
        } else {
          console.log('Validando url');
          resolve(url);
        }
      });
    });
  }
}
