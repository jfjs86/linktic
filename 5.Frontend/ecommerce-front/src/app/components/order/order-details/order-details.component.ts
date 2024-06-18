import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { OrderResponseModel } from 'src/app/models/order/order-response.model';
import { ProductRequestModel } from 'src/app/models/product/product-request.model';
import { ImageService } from 'src/app/services/external/image.service';
import { OrderService } from 'src/app/services/order/order.service';

@Component({
  selector: 'app-order-details',
  templateUrl: './order-details.component.html',
  styleUrls: ['./order-details.component.css']
})
export class OrderDetailsComponent implements OnInit{

  orderResponse:OrderResponseModel = new OrderResponseModel();

  constructor(
    private orderService: OrderService,
    private imageService: ImageService,
    private router: Router,
    private route: ActivatedRoute,
    ){}

  ngOnInit(): void {
    this.getActiveOrder();
  }

  

  getActiveOrder(){
    this.orderService.getActiveOrder().subscribe(data=>{
      this.orderResponse = data;

      if(this.orderResponse && this.orderResponse.products){
        this.orderResponse.products.forEach(product =>{
          this.imageService.getPresignedUrl(product.productImageUrl)
            .then(url => product.productImageUrl = url)
            .catch(err => console.error('Error al obtener URL firmada:', err));
        });
      }

    });
  }

  getIndexProducts(product: ProductRequestModel){
    let index = -1
    if (this.orderResponse && this.orderResponse.products) {
      index = this.orderResponse.products.findIndex(productResponse => product.productId === productResponse.productId);
    }
    return index;
  }

  removeProduct(product: ProductRequestModel): void {

    if (this.orderResponse && this.orderResponse.products) {
      const index = this.getIndexProducts(product);
      if (index !== -1) {        
        this.orderResponse.totalPrice = (this.orderResponse.totalPrice??0) - (this.orderResponse.products.at(index)?.productTotalPrice??0);
        this.orderResponse.products.splice(index, 1);
        this.sendOrder()
      }
    }       
      
  }

  calculateAmount(product: ProductRequestModel){
    if (this.orderResponse && this.orderResponse.products) {
      const index = this.getIndexProducts(product);

      if (index !== -1) {        
        const totalPrice = (product.productPrice??0) * (product.productQuantity??0);
        this.orderResponse.products[index].productTotalPrice = totalPrice;
        
        let newTotalPrice = 0;
        this.orderResponse.products.forEach(prod => {
          newTotalPrice += prod.productTotalPrice ?? 0;
        });
        
        this.orderResponse.totalPrice = newTotalPrice;
      
      }
    }
  }
  

  sendOrder(){
    console.log("new Response: ",this.orderResponse)
    if(this.orderResponse){
      this.orderService.saveOrder(this.orderResponse).subscribe(response =>{
        this.orderResponse = response
      });
    }
  }

  paidOrder(){
    if(this.orderResponse){
      this.orderService.paidOrder(this.orderResponse).subscribe(response =>{
        this.orderResponse = response;
        this.reloadPage();
      }); 
    }      
  }

  reloadPage(){
    this.router.routeReuseStrategy.shouldReuseRoute = () =>false;
    this.router.onSameUrlNavigation = 'reload';
    this.router.navigate(['./'],{
      relativeTo : this.route
    });

  }

  
}
