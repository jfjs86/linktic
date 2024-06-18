import { Component,Input, OnDestroy, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { ProductRequestModel } from 'src/app/models/product/product-request.model';
import { ProductResponseModel } from 'src/app/models/product/product-response.model';
import { ProductService } from 'src/app/services/product/product.service';
import { OrderService } from '../../../services/order/order.service';
import { OrderRequestModel } from 'src/app/models/order/order-request.model';
import { OrderResponseModel } from '../../../models/order/order-response.model';

@Component({
  selector: 'app-add-product',
  templateUrl: './add-product.component.html',
  styleUrls: ['./add-product.component.css']
})
export class AddProductComponent implements OnInit, OnDestroy{

  product: ProductResponseModel = new ProductResponseModel();
  private subscriptionProduct: Subscription = new Subscription();
  private subscriptionOrder: Subscription = new Subscription();
  productRequest : ProductRequestModel = new ProductRequestModel();
  quantity : number | undefined;
  totalPrice : number | undefined;
  orderTotalPrice : number | undefined;
  orderRequest : OrderRequestModel = new OrderRequestModel();
  orderResponse : OrderResponseModel = new OrderResponseModel();

  

  constructor(private productService : ProductService, private orderService:OrderService){
    
  }

  ngOnInit(): void {
    this.subscriptionProduct = this.productService.data$?.subscribe(data => {
      console.log("Product", data);
      this.product = data;
      this.quantity = 1;
      this.totalPrice = 0;
      this.orderTotalPrice =0;
      this.mappingProductRequest()
    });

    this.subscriptionOrder = this.orderService.dataResponse$.subscribe( data =>{
      this.orderRequest = data;
      console.log("Order", data);
    });

  }

  ngOnDestroy(): void {
    this.subscriptionProduct.unsubscribe();
  }

  mappingProductRequest(){
    this.productRequest.productId = this.product.productId;
    this.productRequest.productName = this.product.productName;
    this.productRequest.productDescription = this.product.productDescription;
    this.productRequest.productPrice = this.product.productPrice;
    this.productRequest.productQuantity = 1;
    this.productRequest.productTotalPrice = this.productRequest.productPrice;

  }

  calculateAmount(){
    this.quantity = this.productRequest.productQuantity;
    this.totalPrice = (this.productRequest.productPrice??0) * (this.quantity ?? 0);
    this.productRequest.productQuantity = this.quantity;
    this.productRequest.productTotalPrice = this.totalPrice;
  }

 
  addProduct(){
    
    let existProduct = false;

    this.orderRequest.products?.forEach(product => {
      if (product.productId == this.productRequest.productId) {
        product.productQuantity = this.productRequest.productQuantity;
        product.productTotalPrice = this.productRequest.productTotalPrice;
        existProduct = true;
      }
    });

    if (!existProduct) {
      this.orderRequest.products?.push(this.productRequest);
    }
    
    this.getOrderTotalPrice();

    console.log('Agregando producto', this.orderRequest);
    this.sendOrderRequest();

  }

  sendOrderRequest(){
    if(this.orderRequest !=null){
      console.log('orderRequest:', this.orderRequest);
      this.orderService.saveOrder(this.orderRequest).subscribe(
        response =>{
          console.log('response: ',response);
          this.orderService.shareOrder(response);
        }
      );
    }
  }

  getOrderTotalPrice(){
    this.orderTotalPrice = 0;
    this.orderRequest.products?.forEach(product => {
      this.orderTotalPrice = (this.orderTotalPrice??0)+(product.productTotalPrice??0);
    });
    this.orderRequest.totalPrice = this.orderTotalPrice;
  }

}
