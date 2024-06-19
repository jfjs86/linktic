import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ProductService } from 'src/app/services/product/product.service';
import { ProductListModel } from 'src/app/models/product/product-list.model';
import { ProductResponseModel } from 'src/app/models/product/product-response.model'
import { OrderRequestModel } from 'src/app/models/order/order-request.model';
import { OrderService } from 'src/app/services/order/order.service';
import { OrderResponseModel } from 'src/app/models/order/order-response.model';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ModalComponent } from 'src/app/components/modal/modal.component'
import { AddProductComponent } from 'src/app/components/product/add-product/add-product.component'
import { Subscription } from 'rxjs';


@Component({
  selector: 'app-product-component',
  templateUrl: './product.component.html',
  styleUrls: ['./product.component.css']
})
export class ProductComponent implements OnInit, OnDestroy {

  products :ProductListModel | undefined; 
  orderResponse : OrderResponseModel | undefined;
  orderRequest: OrderRequestModel | undefined;
  private subscriptionOrder: Subscription = new Subscription();
  closeModal : boolean = false;


  constructor(
    private productService: ProductService,
    private orderService : OrderService,
    private modalService: NgbModal
  ) {}  

  ngOnInit() {
    
    if(!this.products){
      this.loadProducts();
    }

    this.subscribeOrder();
    
    if(!this.orderResponse){
      console.log("Buscando orderResponse en la base de datos");
      this.loadExistOrders();
    }
    
  }

  ngOnDestroy(): void {
    this.subscriptionOrder.unsubscribe();
  }

  subscribeOrder(){
    this.subscriptionOrder = this.orderService.dataResponse$.subscribe( data =>{
      if(data && (data.orderId??0) >0){
        this.orderResponse = data;
        console.log('orderResponse observer', this.orderResponse);
        this.closeModal = true;
        this.closeModalMethod();
      }
    });

  }

  loadProducts() {
    this.productService.getProducts().subscribe(
      (data: any) => {
        this.products = data;       
      },
      error => {
        console.error('Error al obtener lista de productos:', error);
      }
    );
  }

  loadExistOrders(){
    this.orderService.getActiveOrder().subscribe(
      data=>{
        if(data){
          this.orderResponse= data;
        }else{
          this.generateOrderRequest();
        }
        console.log("back: ",data);
      },
      error => {
        this.generateOrderRequest();
      }
    );     
  }

  generateOrderRequest(){

    this.orderResponse = new OrderResponseModel();
    this.orderResponse.orderId = 0;
    this.orderResponse.products = [];
    this.orderResponse.totalPrice = 0.0;

  }

  addProductModal(product: ProductResponseModel){    
      this.productService.shareProduct(product);
      console.log('addProductModal: '+ this.orderResponse);
      this.orderService.shareOrder(this.orderResponse);
      const modalRef = this.modalService.open(ModalComponent);
      modalRef.componentInstance.title = 'Agregar nuevo producto';
      modalRef.componentInstance.content = AddProductComponent;    
  }
  
  closeModalMethod(){
    if(this.closeModal){
      this.modalService.dismissAll();
    }
  }

}
