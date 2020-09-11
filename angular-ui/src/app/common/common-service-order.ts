import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {CommonService} from "./common-service";

@Injectable({
  providedIn: "root"
})
export class CommonServiceOrder extends CommonService {
  constructor(private _httpClient: HttpClient) {
    super()
  }

  baseUrl(): string {
    return "http://localhost:9011";
  }

  protected httpClient(): HttpClient {
    return this._httpClient;
  }
}
