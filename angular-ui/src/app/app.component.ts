import {Component, OnInit} from '@angular/core';
import {ShoppingCart} from "./common/common-model";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {
  title = 'angular-ui';

  shoppingCart: ShoppingCart;
  counter: number = 0;

  ngOnInit(): void {
    this.shoppingCart = new ShoppingCart();
  }

  onPurchased(purchased: boolean) {
    if (purchased) {
      this.counter ++;
    }
  }

}
