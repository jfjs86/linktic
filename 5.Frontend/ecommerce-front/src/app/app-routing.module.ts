import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ProductComponent } from './components/product/product-component/product.component';
import { CreateOrderComponent } from './components/order/create-order/create-order.component';
import { OrderDetailsComponent } from './components/order/order-details/order-details.component';
import { OrdersHistoryComponent } from './components/order/orders-history/orders-history.component';

const routes: Routes = [
  {path: 'products', component: ProductComponent},
  {path: 'orders', component: OrderDetailsComponent},
  {path: 'orders-history', component: OrdersHistoryComponent},
  {path: '', redirectTo: '/products', pathMatch: 'full'}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
