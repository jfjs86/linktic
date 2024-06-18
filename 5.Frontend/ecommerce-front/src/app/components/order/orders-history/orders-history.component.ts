import { Component, OnInit } from '@angular/core';
import { OrderRequestModel } from 'src/app/models/order/order-request.model';
import { OrderResponseModel } from 'src/app/models/order/order-response.model';
import { OrderService } from 'src/app/services/order/order.service';

@Component({
  selector: 'app-orders-history',
  templateUrl: './orders-history.component.html',
  styleUrls: ['./orders-history.component.css']
})
export class OrdersHistoryComponent implements OnInit{

  orderResponse : Array<OrderResponseModel> = new Array()

  constructor(private orderService : OrderService){}

  ngOnInit(): void {
    this.getAllOrders();
  }

  
  getAllOrders(){
    this.orderService.getAllOrders().subscribe(response => {
      this.orderResponse = response;
      console.log(this.orderResponse);
    });
  }

}
