import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";

type queryParamsType = Map<string, any>|string|Object

export abstract class CommonService {
  protected abstract httpClient(): HttpClient;
  protected abstract baseUrl(): string;

  httpGet(path: string, queryParams?: queryParamsType, headerProvider?: ()=>HttpHeaders): Observable<Object> {
    const fullPath = this.concatePath(this.baseUrl(), this.concateQueryString(path, queryParams));
    const headers = this.buildHttpHeaders(headerProvider);
    return this.httpClient().get(fullPath, {headers: headers, withCredentials: false});
  }

  httpPut<T>(path: string, obj: T, queryParams?: queryParamsType, headerProvider?: ()=>HttpHeaders): Observable<Object> {
    const fullPath = this.concatePath(this.baseUrl(), this.concateQueryString(path, queryParams));
    const headers = this.buildHttpHeaders(headerProvider);
    return this.httpClient().put(fullPath, obj, {headers: headers, withCredentials: false});
  }

  httpPost<T>(path: string, obj: T, queryParams?: queryParamsType, headerProvider?: ()=>HttpHeaders): Observable<Object> {
    const fullPath = this.concatePath(this.baseUrl(), this.concateQueryString(path, queryParams));
    const headers = this.buildHttpHeaders(headerProvider);
    return this.httpClient().post(fullPath, obj, {headers: headers, withCredentials: false});
  }

  private buildHttpHeaders(headerProvider?: ()=>HttpHeaders) : HttpHeaders {
    let ret: HttpHeaders = null
    if (headerProvider) {
      ret = headerProvider();
    }
    if (!ret) {
      ret = new HttpHeaders();
      ret.set('Content-Type', 'application/json');
      // ret.append('Access-Control-Allow-Credentials', 'false');
    }
    return ret;
  }

  private concatePath(path1: string, path2: string) {
    if (path1.endsWith("/")) {
      if (path2.startsWith("/")) {
        return path1 + path2.substring(1);
      } else {
        return path1 + path2;
      }
    } else {
      if (path2.startsWith("/")) {
        return path1 + path2;
      } else {
        return path1 + "/" + path2;
      }
    }
  }

  concateQueryString(path: string, queryParams?: queryParamsType): string {
    let queryStr = '';
    if (queryParams) {
      if (queryParams instanceof Map) {
        queryParams.forEach( (v, k) => {
          if (v instanceof Array) {
            v.forEach(iv => queryStr = this.addOneQueryParam(queryStr, k, iv));
          } else {
            queryStr = this.addOneQueryParam(queryStr, k, v);
          }
        });
      } else if (typeof queryParams === 'string') {    // can be an object
        queryStr = queryParams.startsWith("&") ? queryParams : ("&" + queryParams);
      } else {    // can be an object
        for (const k of Object.keys(queryParams)) {
          const v = queryParams[k];
          if (v instanceof Array) {
            v.forEach(iv => queryStr = this.addOneQueryParam(queryStr, k, iv));
          } else {
            queryStr = this.addOneQueryParam(queryStr, k, v);
          }
        }
      }
    }
    if (queryStr && queryStr.length > 1) {
      return "${path}?${queryStr.substring(1)}";
    } else {
      return path;
    }
  }

  private addOneQueryParam(prev: string, k: string, v: string) {
    return prev + '&' + k + "=" + v;
  }
}
