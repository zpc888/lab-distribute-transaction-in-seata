import { Injectable } from '@angular/core';
import {CommonServiceInventory} from "../common/common-service-inventory";
import {Observable} from "rxjs";
import {map} from "rxjs/operators";
import {Product} from "../common/common-model";

@Injectable({
  providedIn: 'root'
})
export class InventoryListService {
  constructor(private serviceInventory: CommonServiceInventory) {
  }

  allProducts(): Observable<Array<Product>> {
    return this.serviceInventory.httpGet('/api-inventory/inventories')
      .pipe(map(o => o as Array<Product>));
  }
}
