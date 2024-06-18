import { ProductRequestModel } from '../product/product-request.model';

export class OrderResponseModel{

    orderId : number | undefined;
    products : Array<ProductRequestModel> | undefined;
    totalPrice : number | undefined;
    active : boolean = false;
}