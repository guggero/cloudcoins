import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { BackendService } from '../../services/backend.service';
import { createEncryptionKey, CryptoService } from '../../services/crypto.service';
import { SessionService } from '../../services/session.service';
import { createErrorHandler } from '../../ui-components/functions';

@Component({
  selector: 'login',
  providers: [],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {

  public loginForm: FormGroup;
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
    const errFn = createErrorHandler(this);
    this.backendService.getSalt(value.username, value.otp)
      .subscribe((salt) => {
        const loginData = this.cryptoService.getLoginData(value.username, value.password, salt);
        this.backendService.login(loginData, value.otp)
          .subscribe((token) => this.onSuccess(token, value.password, salt), errFn);
      }, errFn);
  }

  private onSuccess(token: string, password: string, salt: string) {
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
