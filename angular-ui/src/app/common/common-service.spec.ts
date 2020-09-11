import {CommonService} from "./common-service";
import {HttpClient} from "@angular/common/http";

fdescribe("common-service_common-funcs", function() {
  var commonService: CommonService;
  var baseUrl: string;

  beforeEach(function () {
    baseUrl = "http://localhost:9001";
    commonService = new class extends CommonService {
      baseUrl(): string {
        return baseUrl;
      }

      httpClient(): HttpClient {
        return null;
      }
    }
  });

  fit("should return simple path", function (){
    expect(commonService.concateQueryString("/api/accounts", null)).toBe("/api/accounts")
  });

  fit("should return path and query params", function (){
    expect(commonService.concateQueryString("/api/accounts", {"firstName": "George", "lastName": "Zhou"}))
      .toBe("/api/accounts?firstName=George&lastName=Zhou")
  });
})
