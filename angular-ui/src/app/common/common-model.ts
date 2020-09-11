export class Product {
  productId: number;
  productName: string;
  quantity: number;
  buyQuantity: number = 1;
  unitPrice: number
}

export class DbSnapshot {
  atTime?: Date;
  accountId: number;
  accountName: string;
  balance: number
  products: Array<Product>;

  isChanged(dbSnapshot?: DbSnapshot): boolean {
    if (dbSnapshot) {
      return this.balance != dbSnapshot.balance || this.isDiff(this.products, dbSnapshot.products);
    } else {
      return true;
    }
  }

  private isDiff(products1: Array<Product>, products2: Array<Product>) {
    const map1 = new Map<number, Product>();
    if (products1) {
      products1.forEach(v => map1.set(v.productId, v));
    }
    if (products2) {
      products2.forEach(v2 => {
        const v1 = map1.get(v2.productId);
        if (v1) {
          if (v1.quantity != v2.quantity) {
            return true;
          }
        } else {
          return true;
        }
      })
      return false;
    } else {
      return map1.size == 0;
    }
  }
}

export class Order {
  accountId: number = 1;
  items: Array<OrderItem>;

  static fromShoppingCart(cart: ShoppingCart): Order {
    const ret = new Order();
    ret.items = cart.allProducts().map(p => OrderItem.fromProduct(p));
    return ret;
  }
}

export class OrderItem {
  productId: number;
  orderQuantity: number;
  orderUnitPrice: number;

  static fromProduct(product: Product): OrderItem {
    const ret = new OrderItem();
    ret.productId = product.productId;
    ret.orderQuantity = product.quantity;
    ret.orderUnitPrice = product.unitPrice;
    return ret;
  }
}

export class ShoppingCart {
  cart: Map<number, Product>;

  reset() {
    if (this.cart) {
      this.cart.clear();
    } else {
      this.cart = new Map();
    }
  }

  buyProduct(p: Product) {
    this.cart = this.cart ? this.cart : new Map();
    let old = this.cart.get(p.productId);
    if (old) {
      if (!p.buyQuantity || p.buyQuantity == 0) {
        old.quantity += 1;
      } else {
        old.quantity += p.buyQuantity;
      }
    } else {
      old = Object.assign({}, p);
      if (!p.buyQuantity || p.buyQuantity == 0) {
        old.quantity = 1;
      } else {
        old.quantity = p.buyQuantity;
      }
      this.cart.set(old.productId, old);
    }
    if (old.quantity <= 0) {
      this.remove(old);     // remove from cart
    }
  }

  allProducts(): Array<Product> {
    if (this.cart) {
      const ret = new Array<Product>();
      for (let v of this.cart.values()) {
        ret.push(v);
      }
      return ret;
    } else {
      return new Array<Product>()
    }
  }

  remove(product: Product) {
    if (this.cart) {
      this.cart.delete(product.productId);
    }
  }

  totalAmount() {
    let ret = 0;
    if (this.cart) {
      for (let v of this.cart.values()) {
        ret += v.quantity * v.unitPrice;
      }
      return ret;
    } else {
      return 0;
    }
  }

  totalQuantity() {
    let ret = 0;
    if (this.cart) {
      for (let v of this.cart.values()) {
        ret += v.quantity;
      }
      return ret;
    } else {
      return 0;
    }
  }
}
