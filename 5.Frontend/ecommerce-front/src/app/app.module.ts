import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { ProductComponent } from './components/product/product-component/product.component';
import { HttpClientModule } from '@angular/common/http';
import { CreateOrderComponent } from './components/order/create-order/create-order.component';
import { ModalComponent } from './components/modal/modal.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { AddProductComponent } from './components/product/add-product/add-product.component';
import { FormsModule } from '@angular/forms';
import { OrderDetailsComponent } from './components/order/order-details/order-details.component';
import { OrdersHistoryComponent } from './components/order/orders-history/orders-history.component';
import { BooleanPipe } from './boolean.pipe';

@NgModule({
  declarations: [
    AppComponent,
    ProductComponent,
    CreateOrderComponent,
    ModalComponent,    
    AddProductComponent, OrderDetailsComponent, OrdersHistoryComponent, BooleanPipe
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    NgbModule,
    FormsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
