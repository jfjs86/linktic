import { ProductRequestModel } from 'src/app/models/product/product-request.model'

export class OrderRequestModel{
    orderId : number | undefined;
    totalPrice : number | undefined;
    products : Array<ProductRequestModel> | undefined;
}