import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { BackendService } from '../../services/backend.service';
import { createEncryptionKey, CryptoService } from '../../services/crypto.service';
import { SessionService } from '../../services/session.service';
import { createErrorHandler } from '../../ui-components/functions';
import 'rxjs/add/operator/finally';

@Component({
  selector: 'login',
  providers: [],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {

  public loginForm: FormGroup;
  public buttonClicked: boolean = false;
  public notificationKey: string = null;
  public isSuccess: boolean = false;

  constructor(private backendService: BackendService, private cryptoService: CryptoService,
              private formBuilder: FormBuilder, private sessionService: SessionService) {
    this.loginForm = formBuilder.group({
      username: ['', [Validators.required, Validators.minLength(3)]],
      password: ['', [Validators.required, Validators.minLength(8)]],
      otp: ['', [Validators.required]]
    });
  }

  public login(value) {
    if (!this.loginForm.valid) {
      return;
    }
    this.buttonClicked = true;
    const errFn = createErrorHandler(this);
    this.backendService.getSalt(value.username, value.otp)
      .subscribe((response) => {
        const loginData = this.cryptoService.getLoginData(value.username, value.password, response.salt);
        this.backendService
          .login(loginData, value.otp)
          .subscribe((tokenResponse) => setTimeout(() => this.onSuccess(tokenResponse.token, value.password, response.salt), 100), errFn);
      }, errFn);
  }

  private onSuccess(token: string, password: string, salt: string) {
    this.buttonClicked = false;
    this.notificationKey = 'login.successful';
    this.isSuccess = true;
    window.scrollTo(0, 0);
    const encKey = createEncryptionKey(password, salt);
    this.sessionService.storeSession(token, encKey.toString('hex'));
  }

  get username() {
    return this.loginForm.get('username');
  }

  get password() {
    return this.loginForm.get('password');
  }

  get otp() {
    return this.loginForm.get('otp');
  }
}
