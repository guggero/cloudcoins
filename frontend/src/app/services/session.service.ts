import { Injectable } from '@angular/core';

@Injectable()
export class SessionService {

  private tokenKey: string = 'loginToken';
  private encKey: string = 'encKey';

  public storeSession(token: string, key: string): void {
    window.sessionStorage.setItem(this.tokenKey, token);
    window.sessionStorage.setItem(this.encKey, key);
  }

  public getToken(): string {
    return window.sessionStorage.getItem(this.tokenKey);
  }

  public getKey(): string {
    return window.sessionStorage.getItem(this.encKey);
  }

  public isLoggedIn(): boolean {
    return this.getToken() !== null;
  }

  public logout(): void {
    window.sessionStorage.removeItem(this.tokenKey);
    window.sessionStorage.removeItem(this.encKey);
  }
}
