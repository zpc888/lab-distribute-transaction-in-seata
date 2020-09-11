import { Injectable } from '@angular/core';
import {CommonServiceOrder} from "../common/common-service-order";
import {Observable} from "rxjs";
import {DbSnapshot, Order, ShoppingCart} from "../common/common-model";
import {map} from "rxjs/operators";

@Injectable({
  providedIn: 'root'
})
export class DbToolService {

  constructor(private serviceOrder: CommonServiceOrder) { }

  dbSnapshot(): Observable<DbSnapshot> {
    return this.serviceOrder.httpGet('/api-order/account-balance-and-inventories/1')
      .pipe(map(o => { const ret = new DbSnapshot(); Object.assign(ret, o); return ret }));
  }

  purchase(order: Order) {
    return this.serviceOrder.httpPost('/api-order/orders', order);
  }
}
