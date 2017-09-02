import { Injectable } from '@angular/core';
import { Headers, Http, RequestOptions, Response } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/catch';

@Injectable()
export class BackendService {

  constructor(private http: Http) {
  }

  public createAccount(account: any): Observable<any> {
    return this.http
      .post(`${API_URL}/accounts`, account, this.buildRequestOptions())
      .map((res: Response) => res.status)
      .catch((error: any) => Observable.throw(error.json().error));
  }

  private buildRequestOptions(): RequestOptions {
    let headers = new Headers({'Content-Type': 'application/json'});
    return new RequestOptions({headers});
  }
}
