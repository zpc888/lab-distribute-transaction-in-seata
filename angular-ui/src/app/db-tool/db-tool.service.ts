import { Injectable } from '@angular/core';
import {CommonServiceOrder} from "../common/common-service-order";
import {Observable} from "rxjs";
import {AccountBalanceErrorRange, DbSnapshot, marshTo, Order, ShoppingCart} from "../common/common-model";
import {map} from "rxjs/operators";

@Injectable({
  providedIn: 'root'
})
export class DbToolService {
  private accountId = 1;

  constructor(private serviceOrder: CommonServiceOrder) { }

  dbSnapshot(): Observable<DbSnapshot> {
    return this.serviceOrder.httpGet("/api-order/account-balance-and-inventories/" + this.accountId)
      .pipe(map(o => { const ret = new DbSnapshot(); Object.assign(ret, o); return ret }));
  }

  getAccountBalanceErrorRange(): Observable<AccountBalanceErrorRange> {
    return this.serviceOrder.httpGet('/api-order/accountBalanceErrorRange').pipe(
      map( o => marshTo(o, AccountBalanceErrorRange))
    );
  }

  purchase(order: Order) {
    return this.serviceOrder.httpPost('/api-order/orders', order);
  }

  dbReset() {
    return this.serviceOrder.httpPost('/api-order/resetDB/' + this.accountId, null);
  }
}
