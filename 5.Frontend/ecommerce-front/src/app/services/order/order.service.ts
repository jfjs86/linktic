import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { OrderRequestModel } from 'src/app/models/order/order-request.model';
import { OrderResponseModel } from 'src/app/models/order/order-response.model';
import { environment } from 'src/environments/environment'
import { BehaviorSubject  } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class OrderService {

  private dataSubjectResponse = new BehaviorSubject<OrderResponseModel>(new OrderResponseModel());
  dataResponse$= this.dataSubjectResponse.asObservable();

  constructor(private http:HttpClient) { }

  saveOrder(order: OrderRequestModel){
    const httpOptions = {
      headers: new HttpHeaders({
          'Content-Type': 'application/json',
          'Accept': '*/*',
          'Access-Control-Allow-Origin': '*',
          'Access-Control-Allow-Credentials': 'true',
          'Access-Control-Allow-Headers': 'Content-Type',
          'Access-Control-Allow-Methods': 'GET,PUT,POST,DELETE'

      })
    };
    const orderBody:string = JSON.stringify(order);
    console.log('orderBody',orderBody);
    return this.http.post<OrderResponseModel>(environment.apiOrderUrl+'/save-order', orderBody, httpOptions);
  }

  getAllOrders(){
    return this.http.get<OrderResponseModel[]>(environment.apiOrderUrl+'/get-orders');
  }

  getActiveOrder(){
    return this.http.get<OrderResponseModel>(environment.apiOrderUrl+'/get-active-order');
  }

  paidOrder(order: OrderRequestModel){

    const httpOptions = {
      headers: new HttpHeaders({
          'Content-Type': 'application/json',
          'Accept': '*/*',
          'Access-Control-Allow-Origin': '*',
          'Access-Control-Allow-Credentials': 'true',
          'Access-Control-Allow-Headers': 'Content-Type',
          'Access-Control-Allow-Methods': 'GET,PUT,POST,DELETE'

      })
    };
    const orderBody:string = JSON.stringify(order);
    console.log('orderBody',orderBody);
    return this.http.put<OrderResponseModel>(environment.apiOrderUrl+'/paid-order', orderBody, httpOptions);

  }

  shareOrder(data:any){
    console.log('shareOrder: '+data);
    this.dataSubjectResponse.next(data);
  }

}
