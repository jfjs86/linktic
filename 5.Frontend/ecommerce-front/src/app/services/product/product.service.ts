import { Injectable } from '@angular/core';
import { HttpClient,HttpHeaders } from '@angular/common/http';
import { ProductListModel } from './../../models/product/product-list.model'
import { environment } from 'src/environments/environment';
import { BehaviorSubject  } from 'rxjs';
import { ProductResponseModel } from 'src/app/models/product/product-response.model';

@Injectable({
  providedIn: 'root'
})
export class ProductService {

  private dataSubject = new BehaviorSubject<ProductResponseModel>(new ProductResponseModel());
  data$= this.dataSubject.asObservable();

  constructor(private http:HttpClient) {}

  getProducts(){
    return this.http.get<ProductListModel>(environment.apiProductUrl+'/get-products');
  }

  shareProduct(data:ProductResponseModel){
    this.dataSubject.next(data);
  }
}
