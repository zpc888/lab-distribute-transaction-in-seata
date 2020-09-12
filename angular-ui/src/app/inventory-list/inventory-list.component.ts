import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {InventoryListService} from "./inventory-list.service";
import {Observable} from "rxjs";
import {Order, Product, ShoppingCart} from "../common/common-model"
import {CommonServiceOrder} from "../common/common-service-order";
import {DbToolService} from "../db-tool/db-tool.service";

@Component({
  selector: 'app-inventory-list',
  templateUrl: './inventory-list.component.html',
  styleUrls: ['./inventory-list.component.scss']
})
export class InventoryListComponent implements OnInit {
  inventoryList$: Observable<Array<Product>>;
  errorInfo: string;

  @Input()
  shoppingCart: ShoppingCart;
  @Output()
  purchased = new EventEmitter<boolean>();

  displayedCols: Array<keyof Product|"amount"|"del"> = ["productName", "quantity", "unitPrice", "amount", "del"]

  constructor(private inventoryListService: InventoryListService
      , private serviceOrder: DbToolService) { }

  ngOnInit(): void {
    this.inventoryList$ = this.inventoryListService.allProducts();
  }

  buy(prod: Product) {
    this.shoppingCart.buyProduct(prod);
  }

  removeFromShoppingCart(product: Product) {
    this.shoppingCart.remove(product);
  }

  removeAllFromShoppingCart() {
    this.shoppingCart.reset();
  }

  purchase() {
    this.errorInfo = null;
    const order = Order.fromShoppingCart(this.shoppingCart);
    // this.serviceOrder.purchase(order);
    this.serviceOrder.purchase(order).subscribe(x => {this.purchased.emit(true)}
        , error => {
          if (error.error && error.error.trace) {
            this.errorInfo = error.error.trace
          } else {
            this.errorInfo = JSON.stringify(error)
          }
      });
    this.removeAllFromShoppingCart();
  }
}
